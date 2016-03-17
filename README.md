# iRODS jargon authcache extension

This package is an IRODS jargon extension for caching and reusing the authenticated [`IRODSAccount`](https://github.com/DICE-UNC/jargon/blob/master/jargon-core/src/main/java/org/irods/jargon/core/connection/IRODSAccount.java) object in the [Apache Java Caching System (JCS)](https://commons.apache.org/proper/commons-jcs/).

The extension is achieved by wrapping around the [`IRODSAccessObjectFactoryImpl` class](https://github.com/DICE-UNC/jargon/blob/master/jargon-core/src/main/java/org/irods/jargon/core/pub/IRODSAccessObjectFactoryImpl.java), and intersecting the `authenticateIRODSAccount` method for

- retrieving authenticated IRODSAccount from JCS before the original `authenticateIRODSAccount` method is called, and
- storing authenticated IRODSAccount into JCS after the original `authenticateIRODSAccount` method is successful.

## Why the extension?

In the development of the DI-RDM system, we integrated the one-time password mechanism with iRODS using the PAM authentication.  The end-user is required to sign-in via their own IdP to a (web-based) CMS system from which their valid one-time password can be retrieved for accessing data in the repository. By this approach, we ensure users to be validated via trusted identity providers, before they can start accessing the data.

This approach works fine with `icommands` given that once the user is authenticated once, a token is stored in `$HOME/.irods/.irodsA`.  During the lifetime of the token, it will be reused for sub-sequent `icommands` without the need to re-authenticate the user again.

However, for web-based services such as `irods-webdav` and `irods-rest`, the jargon library underneath will authenticate the same set of username and password for every interactions with iCAT, resulting in the unauthorised error due to the reuse of the same (event-based) one-time password.

The purpose of this jargon extension is to mimic the behavior of `icommands`.  That is when a set of username/password is authenticated once, they are cached and reused for sub-sequent interactions between jargon and iRODS.

### Why JCS?

Given the `IRODSAccount` object is _serialisable_, one can simply use various Java mechanism to store it.  However, we opt for a flexible and extensible caching framework like JCS because of the following reasons:

1. manageability of the cache: JCS offers a simple WebApp for administrators to browse (and even revoke) any cached tokens.
1. shareability of the cache:  JCS offers auxiliariy plugins for storing and distributing cached items to multiple server/services, allowing us to cluster a group of servers for load-balancing.
1. configurable lifetime of the cache: JCS offers a configurable approach to define the token's lifetime

## How to use it?

The extension is packed as a single `jar` file called `jargon-authcache-<version>.jar`.  Simply drop this file into the library of your project, and use the class `nl.ru.rdm.authcache.IRODSAccessObjectFactoryJCS` instead of `org.irods.jargon.core.pub.IRODSAccessObjectFactoryImpl` to initialise the [`IRODSAccessObjectFactory`](https://github.com/DICE-UNC/jargon/blob/master/jargon-core/src/main/java/org/irods/jargon/core/pub/IRODSAccessObjectFactory.java) object.

If you build `irods-rest` packages, you could follow the example step below:

### build the extension

```bash

$ git clone https://github.com/donders-research-data-management/rdm-jargon-authcache.git

$ cd rdm-jargon-authcache

$ mvn package -e -Dmaven.test.skip=true

$ ls -l target/jargon-authcache*.jar

$ cd ..

```

### checkout irods-rest package

```bash

$ git clone https://github.com/DICE-UNC/irods-rest.git

$ cd irods-rest

$ mkdir -p src/main/webapp/WEB-INF/lib/

$ cp ../rdm-jargon-authcache/target/jargon-authcache*.jar src/main/webapp/WEB-INF/lib

```

### configure irods-rest package

Firstly, add the following dependency in the `pom.xml` of `irods-rest`

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-jcs-core</artifactId>
    <version>2.0-beta-1</version>
</dependency>
```

Modify `src/main/resources/jargon-beans.xml` to replace the class of `IRODSAccessObjectFactory`.  This is done by replacing

```xml
<beans:bean id="irodsAccessObjectFactory" class="org.irods.jargon.core.pub.IRODSAccessObjectFactoryImpl">
    <beans:constructor-arg ref="irodsSession" />
</beans:bean>
```

with

```xml
<beans:bean id="irodsAccessObjectFactory" class="nl.ru.rdm.authcache.IRODSAccessObjectFactoryJCS">
    <beans:constructor-arg ref="irodsSession" />
</beans:bean>
```

_Optionally_ add your own `cache.ccf` file in `src/main/resources` to control the behavior of the JCS. Setup and configure the region called `irods_account`, as it's the region used for storing the tokens.

The following example shows you how to use JCS remote cache for storing and sharing tokens:

```
# DEFAULT CACHE REGION

jcs.default=
jcs.default.cacheattributes=org.apache.commons.jcs.engine.CompositeCacheAttributes
jcs.default.cacheattributes.MaxObjects=1000
jcs.default.cacheattributes.MemoryCacheName=org.apache.commons.jcs.engine.memory.lru.LRUMemoryCache

# REGION irods_account using "Remote" auxiliary
jcs.region.irods_account=Remote
jcs.region.irods_account.cacheattributes.MaxObjects=100
jcs.region.irods_account.cacheattributes.UseMemoryShrinker=true
jcs.region.irods_account.cacheattributes.MaxMemoryIdleTime=3600
jcs.region.irods_account.cacheattributes.ShrinkerInterval=60
jcs.region.irods_account.elementattributes=org.apache.commons.jcs.engine.ElementAttributes
jcs.region.irods_account.elementattributes.MaxLife=7200
jcs.region.irods_account.elementattributes.IdleTime=1800
jcs.region.irods_account.elementattributes.IsEternal=false
jcs.region.irods_account.elementattributes.IsRemote=true

# Remote RMI Cache setup
jcs.auxiliary.Remote=org.apache.commons.jcs.auxiliary.remote.RemoteCacheFactory
jcs.auxiliary.Remote.attributes=org.apache.commons.jcs.auxiliary.remote.RemoteCacheAttributes
jcs.auxiliary.Remote.attributes.FailoverServers=localhost:1101
jcs.auxiliary.Remote.attributes.RemoveUponRemotePut=true
jcs.auxiliary.Remote.attributes.GetOnly=false
```
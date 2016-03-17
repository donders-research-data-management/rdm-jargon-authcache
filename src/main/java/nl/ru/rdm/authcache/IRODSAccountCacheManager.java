/**
 * The class for managing authenticated IRODSAccount's cached in Java Cache System (JCS) 
 */
package nl.ru.rdm.authcache;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.irods.jargon.core.connection.IRODSAccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IRODSAccountCacheManager {

    private static IRODSAccountCacheManager instance;
    private static CacheAccess<String, IRODSAccount> irodsAccountCache;
	private static final Logger log = LoggerFactory.getLogger(IRODSAccountCacheManager.class);

    private IRODSAccountCacheManager() throws IRODSAccountCacheManagerError {

        try {
            irodsAccountCache = JCS.getInstance("irods_account");
        } catch (Exception e) {
            throw new IRODSAccountCacheManagerError("irods_account cache initialization failed.", e);
        }
    }

    public static IRODSAccountCacheManager getInstance() throws IRODSAccountCacheManagerError {

        synchronized (IRODSAccountCacheManager.class) {

            if (instance == null) {
                instance = new IRODSAccountCacheManager();
            }
        }

        return instance;
    }

    public IRODSAccount getIRODSAccount(String username, String password)
        throws IRODSAccountCacheManagerError {
        return getIRODSAccount(getAuthId(username,password));
    }

    public IRODSAccount getIRODSAccount(String authId)
        throws IRODSAccountCacheManagerError {
        log.debug("retrieving iRODS account cached for authId: {}", authId);
        return irodsAccountCache.get(authId);
    }

    public IRODSAccount putIRODSAccount(String username, String password, IRODSAccount account)
        throws IRODSAccountCacheManagerError {

        String authId;

        try {
            authId = getAuthId(username,password);
            irodsAccountCache.put(authId, account);
            log.debug("irods account cached for authId: {}", authId);
        } catch (Exception e) {
            throw new IRODSAccountCacheManagerError("cache put failed.", e);
        }

        // return account from cache
        return getIRODSAccount(authId);
    }

    public static String getAuthId(String username, String password)
        throws IRODSAccountCacheManagerError {

        String authId;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String data = username + ":" + password;
            md.update(data.getBytes("UTF-8"));
            authId = username + ":" + new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new IRODSAccountCacheManagerError("authId generation failed.", e);
        }

        return authId;
    }
}

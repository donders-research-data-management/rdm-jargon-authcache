/**
 * General exception concerning IRODSAccountCacheManager errors
 */
package nl.ru.rdm.authcache;

public class IRODSAccountCacheManagerError extends Exception {

    public IRODSAccountCacheManagerError(String message) {
        super(message);
    }

    public IRODSAccountCacheManagerError(String message, Throwable throwable) {
        super(message, throwable);
    }
}

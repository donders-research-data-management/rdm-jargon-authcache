package nl.ru.rdm.authcache;

import org.irods.jargon.core.connection.*;
import org.irods.jargon.core.connection.auth.AuthResponse;

import org.irods.jargon.core.packinstr.TransferOptions;

import org.irods.jargon.core.exception.AuthenticationException;
import org.irods.jargon.core.exception.JargonException;

import org.irods.jargon.core.pub.*;
import org.irods.jargon.core.pub.io.IRODSFileFactory;
import org.irods.jargon.core.transfer.TransferControlBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IRODSAccessObjectFactoryJCS implements IRODSAccessObjectFactory {

    private IRODSAccessObjectFactoryImpl factory;

    private static final Logger log = LoggerFactory.getLogger(IRODSAccessObjectFactoryJCS.class);

    /**
	 * Construct an instance with the given <code>IRODSSession<code>
	 *
	 * @param irodsSession
	 */
	public IRODSAccessObjectFactoryJCS(final IRODSSession irodsSession) {
        factory = new IRODSAccessObjectFactoryImpl(irodsSession);
	}

	/**
	 * Default constructor which does not initialize the
	 * <code>IRODSSession</code>. It is up to the developer to inject the
	 * <code>IRODSsession</code> or an exception will result.
	 */
	public IRODSAccessObjectFactoryJCS() {
		factory = new IRODSAccessObjectFactoryImpl();
	}


	/**
	 * Creates an instance of this access object factory.
	 *
	 * @param irodsSession
	 *            {@link org.irods.jargon.core.connection.IRODSSession} that is
	 *            capable of creating connections to iRODS on demand.
	 * @return
	 * @throws JargonException
	 */
	public static IRODSAccessObjectFactory instance(final IRODSSession irodsSession) throws JargonException {
		if (irodsSession == null) {
			log.error("null irods session");
			throw new IllegalArgumentException("IRODSSession cannot be null");
		}
		log.debug("creating access object factory");
		return new IRODSAccessObjectFactoryJCS(irodsSession);
	}

	@Override
	public UserAO getUserAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getUserAO(irodsAccount);
	}

	@Override
	public EnvironmentalInfoAO getEnvironmentalInfoAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getEnvironmentalInfoAO(irodsAccount);
	}

	@Override
	public IRODSGenQueryExecutor getIRODSGenQueryExecutor(IRODSAccount irodsAccount) throws JargonException {
		return factory.getIRODSGenQueryExecutor(irodsAccount);
	}

	@Override
	public ZoneAO getZoneAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getZoneAO(irodsAccount);
	}

	@Override
	public ResourceAO getResourceAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getResourceAO(irodsAccount);
	}

	@Override
	public IRODSFileSystemAO getIRODSFileSystemAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getIRODSFileSystemAO(irodsAccount);
	}

	@Override
	public IRODSFileFactory getIRODSFileFactory(IRODSAccount irodsAccount) throws JargonException {
		return factory.getIRODSFileFactory(irodsAccount);
	}

	@Override
	public UserGroupAO getUserGroupAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getUserGroupAO(irodsAccount);
	}

	@Override
	public void closeSession() throws JargonException {
		factory.closeSession();
	}

	@Override
	public CollectionAO getCollectionAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getCollectionAO(irodsAccount);
	}

	@Override
	public DataObjectAO getDataObjectAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getDataObjectAO(irodsAccount);
	}

	@Override
	public CollectionAndDataObjectListAndSearchAO getCollectionAndDataObjectListAndSearchAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getCollectionAndDataObjectListAndSearchAO(irodsAccount);
	}

	@Override
	public RuleProcessingAO getRuleProcessingAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getRuleProcessingAO(irodsAccount);
	}

	@Override
	public DataTransferOperations getDataTransferOperations(IRODSAccount irodsAccount) throws JargonException {
		return factory.getDataTransferOperations(irodsAccount);
	}

	@Override
	public RemoteExecutionOfCommandsAO getRemoteExecutionOfCommandsAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getRemoteExecutionOfCommandsAO(irodsAccount);
	}

	@Override
	public void closeSession(IRODSAccount irodsAccount) throws JargonException {
		factory.closeSession(irodsAccount);
	}

	@Override
	public void setIrodsSession(IRODSSession irodsSession) {
		factory.setIrodsSession(irodsSession);
	}

	@Override
	public IRODSSession getIrodsSession() {
		return factory.getIrodsSession();
	}

	@Override
	public BulkFileOperationsAO getBulkFileOperationsAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getBulkFileOperationsAO(irodsAccount);
	}

	@Override
	public QuotaAO getQuotaAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getQuotaAO(irodsAccount);
	}

	@Override
	public SimpleQueryExecutorAO getSimpleQueryExecutorAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getSimpleQueryExecutorAO(irodsAccount);
	}

	@Override
	public void closeSessionAndEatExceptions() {
		factory.closeSessionAndEatExceptions();
	}

	@Override
	public void closeSessionAndEatExceptions(IRODSAccount irodsAccount) {
		factory.closeSessionAndEatExceptions(irodsAccount);
	}

	@Override
	public Stream2StreamAO getStream2StreamAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getStream2StreamAO(irodsAccount);
	}

	@Override
	public JargonProperties getJargonProperties() throws JargonException {
		return factory.getJargonProperties();
	}

	@Override
	public DataObjectAuditAO getDataObjectAuditAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getDataObjectAuditAO(irodsAccount);
	}

	@Override
	public CollectionAuditAO getCollectionAuditAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getCollectionAuditAO(irodsAccount);
	}

	@Override
	public MountedCollectionAO getMountedCollectionAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getMountedCollectionAO(irodsAccount);
	}

	@Override
	public IRODSRegistrationOfFilesAO getIRODSRegistrationOfFilesAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getIRODSRegistrationOfFilesAO(irodsAccount);
	}

	@Override
	public TransferControlBlock buildDefaultTransferControlBlockBasedOnJargonProperties() throws JargonException {
		return factory.buildDefaultTransferControlBlockBasedOnJargonProperties();
	}

	@Override
	public TransferOptions buildTransferOptionsBasedOnJargonProperties() throws JargonException {
		return factory.buildTransferOptionsBasedOnJargonProperties();
	}

	@Override
	public ProtocolExtensionPoint getProtocolExtensionPoint(IRODSAccount irodsAccount) throws JargonException {
		return factory.getProtocolExtensionPoint(irodsAccount);
	}

	@Override
	public IRODSServerProperties getIRODSServerProperties(IRODSAccount irodsAccount) throws JargonException {
		return factory.getIRODSServerProperties(irodsAccount);
	}

	@Override
	public ResourceGroupAO getResourceGroupAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getResourceGroupAO(irodsAccount);
	}

	@Override
	public SpecificQueryAO getSpecificQueryAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getSpecificQueryAO(irodsAccount);
	}

	/*
         * (non-Javadoc)
         *
         * @see
         * org.irods.jargon.core.pub.IRODSAccessObjectFactory#authenticateIRODSAccount
         * (org.irods.jargon.core.connection.IRODSAccount)
         */
	@Override
    public AuthResponse authenticateIRODSAccount(final IRODSAccount irodsAccount)
			throws AuthenticationException, JargonException {

		AuthResponse response = null;
		IRODSAccountCacheManager cacheManager = null;
		IRODSAccount myIrodsAccount = null;

        /* retrieve authenticated IRODSAccount from the cache manager */
		try {
			cacheManager = IRODSAccountCacheManager.getInstance();
			myIrodsAccount = cacheManager.getIRODSAccount(irodsAccount.getUserName(), irodsAccount.getPassword());
			log.debug("reusing authenticated IRODSAccount for user: " + irodsAccount.getUserName());
		} catch (IRODSAccountCacheManagerError error) {
			log.debug("authenticating with new IRODSAccount for user: " + irodsAccount.getUserName());
		}

		if ( myIrodsAccount == null ) myIrodsAccount = irodsAccount;
		response = factory.authenticateIRODSAccount(myIrodsAccount);

        // cache and (optionally) refresh authenticated IRODSAccount in the cache manager
		if ( cacheManager == null ) throw new AuthenticationException("cannot initiate IRODSAccountCacheManager");

		try {
			log.debug("caching authenticated IRODSAccount for user: " + irodsAccount.getUserName());
			cacheManager.putIRODSAccount(irodsAccount.getUserName(),
					irodsAccount.getPassword(),
                    response.getAuthenticatedIRODSAccount());
		} catch (IRODSAccountCacheManagerError error) {
			throw new AuthenticationException(error);
		}

		return response;
    }

	@Override
	public boolean isUsingDynamicServerPropertiesCache() {
		return factory.isUsingDynamicServerPropertiesCache();
	}

	@Override
	public DiscoveredServerPropertiesCache getDiscoveredServerPropertiesCache() {
		return factory.getDiscoveredServerPropertiesCache();
	}

	@Override
	public DataObjectChecksumUtilitiesAO getDataObjectChecksumUtilitiesAO(IRODSAccount irodsAccount) throws JargonException {
		return factory.getDataObjectChecksumUtilitiesAO(irodsAccount);
	}

}
package nl.ru.rdm.authcache;

import org.irods.jargon.core.connection.AuthScheme;
import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.connection.IRODSSession;
import org.irods.jargon.core.connection.IRODSSimpleProtocolManager;
import org.irods.jargon.core.connection.auth.AuthResponse;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by hclee on 3/16/16.
 */
public class IRODSAccessObjectFactoryJCSTest {

    private String userName;
    private String userHOTP;

    @Before
    public void initialize() {
        userName = System.getProperty("rdm.irods.username");
        userHOTP = System.getProperty("rdm.irods.userhotp");
    }

    @Test
    public void testAuthenticateIRODSAccount() throws Exception {

        // initialising IRODS session
        IRODSSession irodsSession = IRODSSession.instance(IRODSSimpleProtocolManager.instance());

        // initialising IRODSAccessObjectFactoryJCS
        IRODSAccessObjectFactory factory = IRODSAccessObjectFactoryJCS.instance(irodsSession);

        // creating IRODSAccount for authentication
        IRODSAccount irodsAccount = IRODSAccount.instance("irods-icat.uci.ru.nl", 1247, userName, userHOTP,
                "/rdmtst/di", "rdmtst", "demoResc", AuthScheme.PAM);

        // authenticating IRODSAccount and checking the user name of the authenticated IRODSAccount
        AuthResponse response1 = factory.authenticateIRODSAccount(irodsAccount);
        assertEquals(irodsAccount.getUserName(), response1.getAuthenticatedIRODSAccount().getUserName());

        // authenticating again the same IRODSAccount and checking the user name of the authenticated IRODSAccount
        AuthResponse response2 = factory.authenticateIRODSAccount(irodsAccount);
        assertEquals(response1.getAuthenticatedIRODSAccount().getPassword(), response2.getAuthenticatedIRODSAccount().getPassword());

         // retrieving authenticated IRODSAccount from JCS and checking against the user name
        IRODSAccount cachedIrodsAccount = IRODSAccountCacheManager.getInstance().getIRODSAccount(irodsAccount.getUserName(), irodsAccount.getPassword());
        assertEquals(irodsAccount.getUserName(), cachedIrodsAccount.getUserName());

        // closing IRODSSession
        irodsSession.closeSession(response2.getAuthenticatedIRODSAccount());
    }
}

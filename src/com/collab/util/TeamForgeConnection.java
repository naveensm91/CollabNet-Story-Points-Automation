package com.collab.util;


import java.rmi.RemoteException;
import org.apache.log4j.Logger;

import com.collabnet.ce.soap50.webservices.cemain.ICollabNetSoap;
import com.collabnet.ce.soap50.webservices.ClientSoapStubFactory;



/**
 * Example class encapsulating a TeamForge connection.
 * This class covers up the details of logging in and out
 * of a TeamForge server.
 *
 */
public class TeamForgeConnection
{
   private static final Logger
      log = Logger.getLogger(TeamForgeConnection.class);


   /* the TeamForge interface */
   private ICollabNetSoap m_sfSoap;

   /* the username used to login to TeamForge */
   private String m_userName;



   /**
    * a simple constructor built around the URL of the TeamForge server
    * <p>
    * @param serverUrl The fully qualified URL of the TeamForge server
    *                  instance
    */
   public TeamForgeConnection(String serverUrl)
	{

		m_sfSoap = (ICollabNetSoap) ClientSoapStubFactory.getSoapStub(
					                       ICollabNetSoap.class, serverUrl);
	}



   /**
    * login to the TeamForge instance using the supplied username and password.
    * Upon successful login, the session ID is returned to the calling object.
    * <p>
    * @param  userName a valid TeamForge user name
    * @param  password the unencrypted password for the TeamForge user
    * <p>
    * @return the session id established by the login() call
    * <p>
    * @throws RemoteException if any errors are returned by the login call, the
    *                         exception is returned to the calling program
    */
   public String login(String userName, String password)
		throws RemoteException
	{
      String sessionId;

      m_userName = userName;

      try
      {
         /*
            the login call takes a username and password and returns a session
            identifer to be passed into subsequent API calls.

            The userName and password are the same values used to login to
            TeamForge via the web interface
          */
         sessionId = m_sfSoap.login(userName, password);
      }
      catch (RemoteException e)
      {
         log.error("unable to connect to TeamForge", e);
         throw new RemoteException("unable to connect to TeamForge",e);
      }

      log.debug("successfully connected to TeamForge");

      return (sessionId);
   }


   /**
    * logoff closes the TeamForge connection specified by sessionId and
    * releases any resources held by the client API and the server
    * <p>
    * @param sessionId the identifier for the TeamForge session.  This ID
    *                  should have been previously returned by the login()
    *                  TeamForge call.
    * @throws RemoteException any errors returned by the logoff() call are
    *                         packaged and thrown to the calling object
    */
   public void logoff(String sessionId)
		throws RemoteException
	{
      try
      {
         m_sfSoap.logoff(m_userName, sessionId);
      }
      catch (RemoteException e)
      {
         log.error("unable to disconnect from TeamForge", e);
         throw new RemoteException("unable to disconnect from TeamForge",e);
      }

      log.debug("successfully disconnected to TeamForge");
   }

}

package com.collab.util44;


import java.rmi.RemoteException;
import org.apache.log4j.Logger;

import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;



/**
 * Example class encapsulating a SourceForge connection.
 * This class covers up the details of logging in and out
 * of a SourceForge server.
 *
 */
public class SourceForgeConnection
{
   private static final Logger
      log = Logger.getLogger(SourceForgeConnection.class);


   /* the SourceForge interface */
   private ISourceForgeSoap m_sfSoap;

   /* the username used to login to SourceForge */
   private String m_userName;



   /**
    * a simple constructor built around the URL of the SourceForge server
    * <p>
    * @param serverUrl The fully qualified URL of the SourceForge server
    *                  instance
    */
   public SourceForgeConnection(String serverUrl)
	{

		m_sfSoap = (ISourceForgeSoap) ClientSoapStubFactory.getSoapStub(
					                       ISourceForgeSoap.class, serverUrl);
	}



   /**
    * login to the SourceForge instance using the supplied username and password.
    * Upon successful login, the session ID is returned to the calling object.
    * <p>
    * @param  userName a valid SourceForge user name
    * @param  password the unencrypted password for the SourceForge user
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
            SourceForge via the web interface
          */
         sessionId = m_sfSoap.login(userName, password);
      }
      catch (RemoteException e)
      {
         log.error("unable to connect to SourceForge", e);
         throw new RemoteException("unable to connect to SourceForge",e);
      }

      log.debug("successfully connected to SourceForge");

      return (sessionId);
   }


   /**
    * logoff closes the SourceForge connection specified by sessionId and
    * releases any resources held by the client API and the server
    * <p>
    * @param sessionId the identifier for the SourceForge session.  This ID
    *                  should have been previously returned by the login()
    *                  SourceForge call.
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
         log.error("unable to disconnect from SourceForge", e);
         throw new RemoteException("unable to disconnect from SourceForge",e);
      }

      log.debug("successfully disconnected to SourceForge");
   }

}

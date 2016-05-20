package com.collab.examples;


import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import com.collab.util.TeamForgeConnection;




public class Example1
{
   private static final Logger
      log = Logger.getLogger(Example1.class);


  /**
   * Example 1 is the prototypical "Hello World" example which connects to a
   * TeamForge server using the TeamForgeConnection utility object.
   * <p>
   * The program expects the following arguments:
   * <p>
   *    teamforge-url: the URL of the TeamForge server
   * <p>
   *    username:        a valid TeamForge user
   * <p>
   *    password:        the password of the TeamForge user
   * <p>
   * @param args command line arguments passed into main().
   * <p>
   * @throws RemoteException any errors logging in or out of TeamForge will
   *                         be thrown from this method
   */
   public static void main(String args[])
		throws RemoteException
	{
      //BasicConfigurator.configure();

      log.info("running Example1");

      String sfUrl;
      String sfUser;
      String sfPass;

      if (args.length != 3)
      {
         log.error("Usage: teamforge-url username password");
      }
      else
      {
         sfUrl  = args[0];
         sfUser = args[1];
         sfPass = args[2];

         TeamForgeConnection sfc = new TeamForgeConnection(sfUrl);

         String sessionId = sfc.login(sfUser,sfPass);

         log.info("Hello World! successfully connected to TeamForge");

         sfc.logoff(sessionId);

         log.debug("successfully disconnected from TeamForge");
      }

      log.debug("exiting Example1");
   }
}
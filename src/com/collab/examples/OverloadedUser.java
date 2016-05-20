package com.collab.examples;


import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;

import com.collab.util.TeamForgeConnection;
import com.collab.util.TrackerUtil;
import com.collab.util.DocumentUtil;
import com.collab.util.TaskUtil;
import com.collabnet.ce.soap50.webservices.tracker.ArtifactSoapRow;


/**
 * Example5 illustrates some of the method calls for working with tasks.
 * The program loops through and displays the user who is the most overloaded,
 * in other words, the user who is assigned the most hours worth of tasks.
 *
 */
public class OverloadedUser
{
   private static final Logger
      log = Logger.getLogger(OverloadedUser.class);


  /**
   * Example 5 looks for the most overloaded user.
   * <p>
   * The program expects the following arguments:
   * <p>
   *    teamforge-url:  the URL of the TeamForge server
   * <p>
   *    username:         a valid TeamForge user
   * <p>
   *    password:         the password of the TeamForge user
   * <p>
   *    project-id:       The ID of the project containing the tasks
   * <p>
   * @param args command line arguments passed into main().
   * <p>
   * @throws RemoteException any errors logging in or out of TeamForge will
   *                         be thrown from this method
   */
   public static void main(String args[])
		throws RemoteException,
             Exception
   {

      log.info("running Example5");

      String sfUrl;
      String sfUser;
      String sfPass;
      String projectId;

      if (args.length != 4)
      {
         log.error("Usage: teamforge-url username password tracker-id");
      }
      else
      {
         sfUrl       = args[0];
         sfUser      = args[1];
         sfPass      = args[2];
         projectId   = args[3];

         /*
            login to TeamForge
          */
         TeamForgeConnection sfc = new TeamForgeConnection(sfUrl);

         String sessionId = sfc.login(sfUser,sfPass);

         log.info("successfully connected to TeamForge");

         /*
            retrieve all artifacts in trackerId
          */
         TaskUtil task = new TaskUtil(sfUrl, sessionId);

         log.info("looking for the most overloaded user");

         String user = task.overloadedUser(projectId);

         log.info("the most overloaded user is  " + user);

         sfc.logoff(sessionId);

         log.info("successfully disconnected from TeamForge");
      }

      log.debug("exiting Example5");
   }
}

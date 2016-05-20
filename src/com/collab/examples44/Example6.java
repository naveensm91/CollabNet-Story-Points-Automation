package com.collab.examples44;


import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;

import com.collab.util44.*;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapRow;


/**
 * Example6 illustrates some of the method calls for working with SCM
 * checkins.  The output is a table detailing all the project members
 * and their checkin counts.
 * <p>
 */
public class Example6
{
   private static final Logger
      log = Logger.getLogger(Example6.class);


  /**
   * Example 6 finds the user with the most commits in the project.
   * <p>
   * The program expects the following arguments:
   * <p>
   *    teamforge-url:  the URL of the SourceForge server
   * <p>
   *    username:         a valid SourceForge user
   * <p>
   *    password:         the password of the SourceForge user
   * <p>
   *    project-id:       The ID of the project containing the commits
   * <p>
   * @param args command line arguments passed into main().
   * <p>
   * @throws java.rmi.RemoteException any errors logging in or out of SourceForge will
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
            login to SourceForge
          */
         SourceForgeConnection sfc = new SourceForgeConnection(sfUrl);

         String sessionId = sfc.login(sfUser,sfPass);

         log.info("successfully connected to SourceForge");

         /*
            retrieve all artifacts in trackerId
          */
         ScmUtil scm = new ScmUtil(sfUrl, sessionId);

         log.info("generating table of user checkins");

         scm.reportCommitCounts(projectId);

         sfc.logoff(sessionId);

         log.info("successfully disconnected from SourceForge");
      }

      log.debug("exiting Example1");
   }
}

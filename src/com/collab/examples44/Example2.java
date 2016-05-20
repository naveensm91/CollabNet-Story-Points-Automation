package com.collab.examples44;


import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;

import com.collab.util44.SourceForgeConnection;
import com.collab.util44.ProjectUtil;
import com.vasoftware.sf.soap44.webservices.sfmain.ProjectSoapRow;


/**
 * Example2 shows how one might go about retrieving a list of projects
 * in which a given user (passed in on the command line) is a member.
 * <p>
 * This example illustrates how a developer might retrieve and then
 * loop through a list of projects.  It further shows how information
 * on a given user might be retrieved within the context of a given
 * project.
 * <p>
 */
public class Example2
{
   private static final Logger
      log = Logger.getLogger(Example2.class);


  /**
   * Example 2 prints out a list of projects of which the user passed
   * on the command line is a member.
   * <p>
   * The program expects the following arguments:
   * <p>
   *    teamforge-url: the URL of the SourceForge server
   * <p>
   *    username:        a valid SourceForge user
   * <p>
   *    password:        the password of the SourceForge user
   * <p>
   *    username:        the login account for which we will print
   *                     out all projects in which the user has
   *                     administrative permissions
   * <p>
   * @param args command line arguments passed into main().
   * <p>
   * @throws java.rmi.RemoteException any errors logging in or out of SourceForge will
   *                         be thrown from this method
   */
   public static void main(String args[])
		throws RemoteException
	{

      log.info("running Example2");

      String sfUrl;
      String sfUser;
      String sfPass;
      String sfFindMe;

      if (args.length != 4)
      {
         log.error("Usage: teamforge-url username password user-search-term");
      }
      else
      {
         sfUrl    = args[0];
         sfUser   = args[1];
         sfPass   = args[2];
         sfFindMe = args[3];

         SourceForgeConnection sfc = new SourceForgeConnection(sfUrl);

         String sessionId = sfc.login(sfUser,sfPass);

         log.info("successfully connected to SourceForge");

         ProjectUtil projs = new ProjectUtil(sfUrl, sessionId);

         log.info("retrieving all projects of which " + sfFindMe + " is a member");

         List<ProjectSoapRow> mbrProjects = projs.getProjectList(sfFindMe);

         log.info("Project list");
         log.info("------------");

         for (ProjectSoapRow i : mbrProjects)
            log.info(i.getTitle() + "  " + i.getId());

         sfc.logoff(sessionId);

         log.info("successfully disconnected from SourceForge");
      }

      log.debug("exiting Example1");
   }
}

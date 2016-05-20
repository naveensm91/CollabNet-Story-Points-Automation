package com.collab.examples;


import java.rmi.RemoteException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.collab.util.TeamForgeConnection;
import com.collab.util.GroupUtil;
import com.collab.util.RoleUtil;



public class Example7
{
   private static final Logger
      log = Logger.getLogger(Example7.class);


  /**
   * Example 7 finds the logged-in users Groups and Roles.
   * <p>
   * The program expects the following arguments:
   * <p>
   *    teamforge-url: the URL of the TeamForge server
   * <p>
   *    username:        a valid TeamForge user
   * <p>
   *    password:        the password of the TeamForge user
   * <p>
   *    project:         the project to report roles from.
   * <p>
   * @param args command line arguments passed into main().
   * <p>
   * @throws RemoteException any errors logging in or out of TeamForge will
   *                         be thrown from this method
   */
   public static void main(String args[])
		throws RemoteException
	{

      log.info("running Example7");

      String sfUrl;
      String sfUser;
      String sfPass;
      String sfProject;

      if (args.length != 4)
      {
         log.error("Usage: teamforge-url username password project");
      }
      else
      {
         sfUrl  = args[0];
         sfUser = args[1];
         sfPass = args[2];
         sfProject = args[3];

         TeamForgeConnection sfc = new TeamForgeConnection(sfUrl);

         String sessionId = sfc.login(sfUser,sfPass);

         log.info("successfully connected to TeamForge");

         log.info("Group memberships for " + sfUser);

         GroupUtil gu = new GroupUtil(sfUrl, sessionId);
         Collection<String> groups = gu.getGroups(sfUser);
         if (groups.isEmpty()) {
             log.info("None Found!");
         } else {
             log.info("=======================");
             for (String group: groups) {
                 log.info(group);
             }
         }
         log.info("");

         log.info("Roles for " + sfUser + " in Project " + sfProject); 
         RoleUtil ru = new RoleUtil(sfUrl, sessionId);
         Collection<String> roles = ru.getRoles(sfProject, sfUser);
         if (roles.isEmpty()) {
             log.info("No roles found!");
         } else {
             log.info("=======================");
             for (String role: roles) {
                 log.info(role);
             }
         }
         log.info("");

         sfc.logoff(sessionId);

         log.debug("successfully disconnected from TeamForge");
      }

      log.debug("exiting Example7");
   }
}

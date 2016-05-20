package com.collab.examples;


import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import com.collab.util.TeamForgeConnection;
import com.collab.util.DocumentUtil;


/**
 * Example4 illustrates some of the method calls for working with documents
 * in TeamForge.  This sample program creates a new document within TeamForge
 * and then uploads the content of that document.
 *
 */
public class Example4
{
   private static final Logger
      log = Logger.getLogger(Example4.class);


  /**
   * Example 4 ceates a document in the TeamForge document manager.
   * <p>
   * The program expects the following arguments:
   * <p>
   *    teamforge-url:  the URL of the TeamForge server
   * <p>
   *    username:         a valid TeamForge user
   * <p>
   *    password:         the password of the TeamForge user
   * <p>
   *    project-id:       The ID of the project which will recieve the
   *                      document.
   * <p>
   *    sf-document-path: The path within the TeamForge document manager
   *                      in which we will upload the document.  For example,
   *                      /designDocs/architecture/systemDiagram.ppt
   * <p>
   *    flat-file-doc:    The document file on disk that we will upload
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

      log.info("running Example4");

      String sfUrl;
      String sfUser;
      String sfPass;
      String projectId;
      String docPath;
      String docFilePath;

      if (args.length != 6)
      {
         log.error("Usage: teamforge-url username password tracker-id");
      }
      else
      {
         sfUrl       = args[0];
         sfUser      = args[1];
         sfPass      = args[2];
         projectId   = args[3];
         docPath     = args[4];
         docFilePath = args[5];

         /*
            login to TeamForge
          */
         TeamForgeConnection sfc = new TeamForgeConnection(sfUrl);

         String sessionId = sfc.login(sfUser,sfPass);

         log.info("successfully connected to TeamForge");

         /*
            retrieve all artifacts in trackerId
          */
         DocumentUtil doc = new DocumentUtil(sfUrl, sessionId);

         log.info("loading a document at the path " + docPath);

         doc.uploadFile(projectId, docPath, docFilePath);

         log.info("successfully uploaded document: " + docFilePath);

         sfc.logoff(sessionId);

         log.info("successfully disconnected from TeamForge");
      }

      log.debug("exiting Example4");
   }
}

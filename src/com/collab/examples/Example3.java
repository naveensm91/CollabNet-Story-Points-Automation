package com.collab.examples;


import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;

import com.collab.util.TeamForgeConnection;
import com.collab.util.TrackerUtil;
import com.collabnet.ce.soap50.webservices.tracker.ArtifactSoapRow;


/**
 * Example3 shows basic techniques for manipulating tracker items.  Given
 * a tracker ID, this program puts all open items "into the parking lot" by
 * changing the priority to level 5.
 * <p>
 * The basic technique here is to retrieve a tracker artifact that is of
 * interest for some reason, editing it via it's getter/setter methods and
 * then saving it back to the server.
 *
 */
public class Example3
{
   private static final Logger
      log = Logger.getLogger(Example2.class);


  /**
   * Example 3 moves all tracker ids out to the parking lot, setting them
   * to a low priority of 5.
   * <p>
   * The program expects the following arguments:
   * <p>
   *    teamforge-url: the URL of the TeamForge server
   * <p>
   *    username:        a valid TeamForge user
   * <p>
   *    password:        the password of the TeamForge user
   * <p>
   *    tracker-id:      The ID of the tracker to process
   * <p>
   * @param args command line arguments passed into main().
   * <p>
   * @throws RemoteException any errors logging in or out of TeamForge will
   *                         be thrown from this method
   */
   public static void main(String args[])
		throws RemoteException
	{

      log.info("running Example3");

      String sfUrl;
      String sfUser;
      String sfPass;
      String trackerId;

      if (args.length != 4)
      {
         log.error("Usage: teamforge-url username password tracker-id");
      }
      else
      {
         sfUrl     = args[0];
         sfUser    = args[1];
         sfPass    = args[2];
         trackerId = args[3];

         /*
            login to TeamForge
          */
         TeamForgeConnection sfc = new TeamForgeConnection(sfUrl);

         String sessionId = sfc.login(sfUser,sfPass);

         log.info("successfully connected to TeamForge");

         /*
            retrieve all artifacts in trackerId
          */
         TrackerUtil tracker = new TrackerUtil(sfUrl, sessionId);

         log.info("resetting the priority of all artifacts in tracker " + trackerId);

         List<ArtifactSoapRow> arts = tracker.getArtifactList(trackerId);

         log.info("Processing artifact");
         log.info("-------------------");

         /*
            loop through the artifacts putting them out into the parking lot
          */
         for (ArtifactSoapRow i : arts)
         {
            log.info(i.getId());
            i.setPriority(5);
            tracker.setArtifactData(i);
         }

         sfc.logoff(sessionId);

         log.info("successfully disconnected from TeamForge");
      }

      log.debug("exiting Example3");
   }
}

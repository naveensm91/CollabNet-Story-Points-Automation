package com.collab.util;


import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.collabnet.ce.soap50.webservices.ClientSoapStubFactory;
import com.collabnet.ce.soap50.webservices.tracker.ITrackerAppSoap;
import com.collabnet.ce.soap50.webservices.tracker.ArtifactSoapRow;
import com.collabnet.ce.soap50.webservices.tracker.ArtifactSoapList;
import com.collabnet.ce.soap50.webservices.tracker.ArtifactSoapDO;


/**
 * This utility class operates on Trackers and the elements within them
 * (artifacts)
 * <p>
 *
 */
public class TrackerUtil
{
   private static final Logger
      log = Logger.getLogger(TrackerUtil.class);


   /* the TeamForge tracker interface */
   private ITrackerAppSoap m_trackerSoap;

   /* the session id returned from a previous call to login() */
   private String m_sessionId;



   /**
    * a simple constructor built around the URL of the TeamForge server
    * <p>
    * @param serverUrl The fully qualified URL of the TeamForge server
    *                  instance
    * @param sessionId A session identifier returned from a prior call
    *                  to login()
    */
   public TrackerUtil(String serverUrl, String sessionId)
   {

      m_sessionId = sessionId;

      m_trackerSoap = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
                                          ITrackerAppSoap.class, serverUrl);
   }


   /**
    * sets the priority data element associated with the soap row passed
    * in on the command line.
    * <p>
    * @param asr The artifact retrieved by a previous call to getArtifact()
    * @throws RemoteException if anything goes wrong within the TeamForge
    *                         calls, wrap a remote exception around it and
    *                         re-throw
    */
   public void setArtifactData(ArtifactSoapRow asr)
      throws RemoteException
   {
      try
      {
         /*
            re-retrieve the artifact.  This is good practice to minimize
            errors resulting from artifacts that have been modified while
            we hold them inside this jvm
          */
         ArtifactSoapDO ado =
            m_trackerSoap.getArtifactData(m_sessionId, asr.getId());

         /* set the priority from the object given as an argument */
         ado.setPriority(asr.getPriority());
         
         m_trackerSoap.setArtifactData(m_sessionId, ado, "set by TrackerUtil",
                                       null, null, null);
      }
      catch (RemoteException e)
      {
         log.error("unable to set artifact " + asr.getId(), e);
         throw new RemoteException("unable to set artifact " + asr.getId(), e);         
      }
   }


   /**
    * returns a type-safe list built from the artifacts within the trackerId,
    * given as an argument
    * <p>
    * @param trackerId the containing tracker the contents of which we're
    *                  going to read and return
    * <p>
    * @throws RemoteException if any errors occur in a TeamForge API, we wrap
    *                         this in an exception and throw it on up the call
    *                         stack
    * <p>
    * @return a list containing all artifacts within the trackerId given on
    *         the command line
    */
   public List<ArtifactSoapRow> getArtifactList(String trackerId)
      throws RemoteException
   {
      ArtifactSoapList asl;
      try
      {
         /* return a list of artifacts from the tracker argument */
         asl = m_trackerSoap.getArtifactList(m_sessionId, trackerId, null);
      }
      catch (RemoteException e)
      {
         log.error("unable to retrieve artifacts", e);
         throw new RemoteException("unable to retrieve artifacts", e);
      }

      log.debug("successfully retrieved artifacts");

      ArtifactSoapRow[] artfRows = asl.getDataRows();

      /* put our artifacts in a type-safe list */
      List<ArtifactSoapRow> artifacts = new ArrayList<ArtifactSoapRow>();      
      artifacts.addAll(Arrays.asList(artfRows));

      return (artifacts);
   }
}
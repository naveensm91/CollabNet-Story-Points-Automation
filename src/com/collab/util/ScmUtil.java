package com.collab.util;


import java.rmi.RemoteException;


import org.apache.log4j.Logger;

import com.collabnet.ce.soap50.webservices.ClientSoapStubFactory;
import com.collabnet.ce.soap50.webservices.scm.*;
import com.collabnet.ce.soap50.webservices.cemain.ICollabNetSoap;
import com.collabnet.ce.soap50.webservices.cemain.ProjectMemberSoapList;
import com.collabnet.ce.soap50.webservices.cemain.ProjectMemberSoapRow;



/**
 * This example class works with  TeamForge documents including file
 * upload, etc.
 * <p>
 *
 */
public class ScmUtil
{
   private static final Logger
      log = Logger.getLogger(ScmUtil.class);

   /* the task storage interface */
   private IScmAppSoap m_scmSoap;

   /* the main teamforge interface */
   private ICollabNetSoap m_sfSoap;

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
   public ScmUtil(String serverUrl, String sessionId)
   {

      m_sessionId = sessionId;

      m_scmSoap = (IScmAppSoap) ClientSoapStubFactory.getSoapStub(
                                      IScmAppSoap.class, serverUrl);
      m_sfSoap = (ICollabNetSoap) ClientSoapStubFactory.getSoapStub(
                                      ICollabNetSoap.class, serverUrl);
   }




   public void reportCommitCounts(String projectId)
      throws RemoteException
   {
      ProjectMemberSoapList pmsl;
      RepositorySoapList repList;
      try
      {
         /* retrieve the members of the project */
         pmsl = m_sfSoap.getProjectMemberList(m_sessionId, projectId);
         /* retrieve the repositorie(s) in the project */
         repList = m_scmSoap.getRepositoryList(m_sessionId, projectId);
      }
      catch (Exception e)
      {
         log.error("unable to retrieve membership for project " + projectId);
         throw new RemoteException("unable to retrieve membership for project " + projectId);
      }

      ProjectMemberSoapRow[] pmsr = pmsl.getDataRows();
      RepositorySoapRow[] rsr = repList.getDataRows();


      log.info("Checkins By User");
      log.info("------------------------");

      for ( ProjectMemberSoapRow i : pmsr)
      {
         int checkins = 0;

         for (RepositorySoapRow j : rsr)
         {
            checkins += checkinCount(j, i.getUserName());
         }
         String format = "%1$-20s| %2$-5s\n";

         String s = String.format(format, i.getFullName(), checkins);
         log.info(s);
      }

   }


   private int checkinCount(RepositorySoapRow repo, String name)
      throws RemoteException
   {
      CommitSoapList csl;
      try
      {
         csl = m_scmSoap.getCommitList(m_sessionId, repo.getId(), null);
      }
      catch (RemoteException e)
      {
         log.error("unable to retrieve commits from repository " + repo.getId());
         throw new RemoteException("unable to retrieve commits from repository",e);
      }

      CommitSoapRow[] csr = csl.getDataRows();

      int commitCount = 0;

      for ( CommitSoapRow i : csr)
      {
         if (i.getCreatedBy().equals(name))
            commitCount++;
      }

      return (commitCount);
   }

}
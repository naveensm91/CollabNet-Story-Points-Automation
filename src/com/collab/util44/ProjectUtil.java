package com.collab.util44;


import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.vasoftware.sf.soap44.webservices.sfmain.*;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;


/**
 * This example class retrieves various data associated with SourceForge
 * projects including membership, etc.
 * <p>
 *
 */
public class ProjectUtil
{
   private static final Logger
      log = Logger.getLogger(ProjectUtil.class);


   /* the SourceForge interface */
   private ISourceForgeSoap m_sfSoap;

   /* the session id returned from a previous call to login() */
   private String m_sessionId;



   /**
    * a simple constructor built around the URL of the SourceForge server
    * <p>
    * @param serverUrl The fully qualified URL of the SourceForge server
    *                  instance
    * @param sessionId A session identifier returned from a prior call
    *                  to login()
    */
   public ProjectUtil(String serverUrl, String sessionId)
   {

      m_sessionId = sessionId;

      m_sfSoap = (ISourceForgeSoap) ClientSoapStubFactory.getSoapStub(
                                      ISourceForgeSoap.class, serverUrl);
   }



   /**
    * Return a list of projects of which the argument user is a member.
    * <p>
    * @param userName A SourceForge user account used to filter the project list
    * @return a list of ProjectSoapRow objects of which userName is a member
    * @throws RemoteException Any errors returned from the SourceForge api are
    *                         wrapped and re-thrown to the caller
    */
   public List<ProjectSoapRow> getProjectList(String userName)
      throws RemoteException
   {
      ProjectSoapList psl;
      try
      {
         /* return a list of projects visible to the user logged in via m_sessionId */
         psl = m_sfSoap.getProjectList(m_sessionId);
      }
      catch (RemoteException e)
      {
         log.error("unable to retrieve project list", e);
         throw new RemoteException("unable to retrieve project list", e);
      }

      log.debug("successfully retrieved project list");

      ProjectSoapRow[] projRows = psl.getDataRows();

      /*
         loop through and add projects to the return list if our user is a
         member
       */
      List<ProjectSoapRow> projects = new ArrayList<ProjectSoapRow>();
      for (ProjectSoapRow i : projRows)
         if (isMember(i.getId(), userName))
            projects.add(i);

      return (projects);
   }



   /**
    * isMember returns true if the user argument is a member of the project specified by the
    * project id argument.
    * <a>
    * @param projectId the id of the project in question
    * @param userName the name of the user that we're examining for project membership
    * @return true, if the user is a member of the project, false otherwise
    * @throws RemoteException if any errors occur during SourceForge calls that exception
    *                         is wrapped and re-thrown to the calling program
    */
   private boolean isMember(String projectId, String userName)
      throws RemoteException
   {
      log.debug("checking to see whether user " + userName + " is a member of project " + projectId);
        

      boolean rval = false;

      ProjectMemberSoapList mbrList;
      try
      {
         /* retrieve from SourceForge a list of members of projectId */
         mbrList = m_sfSoap.getProjectMemberList(m_sessionId,
                                                 projectId);
      }
      catch (RemoteException e)
      {
         log.error("unable to determine project membership");
         throw new RemoteException("unable to determine project membership", e);
      }

      ProjectMemberSoapRow[] mbrRow = mbrList.getDataRows();

      /*
         loop through the project membership.  If our user is found, set the return
         value to true and break out of the loop
       */
      for (ProjectMemberSoapRow i: mbrRow)
      {
         if (i.getUserName().equals(userName))
         {
            rval = true;
            break;
         }
      }

      log.debug("successfully determined project membership for project: " +
                projectId);

      return (rval);
   }
}

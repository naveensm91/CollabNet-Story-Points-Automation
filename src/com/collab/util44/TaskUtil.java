package com.collab.util44;


import java.rmi.RemoteException;


import org.apache.log4j.Logger;

import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.sfmain.ProjectMemberSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.ProjectMemberSoapRow;
import com.vasoftware.sf.soap44.webservices.taskmgr.ITaskAppSoap;
import com.vasoftware.sf.soap44.webservices.taskmgr.TaskSoapList;
import com.vasoftware.sf.soap44.webservices.taskmgr.TaskSoapRow;
import com.vasoftware.sf.soap44.webservices.docman.IDocumentAppSoap;



/**
 * This example class works with  SourceForge documents including file
 * upload, etc.
 * <p>
 *
 */
public class TaskUtil
{
   private static final Logger
      log = Logger.getLogger(TaskUtil.class);

   /* the task storage interface */
   private ITaskAppSoap m_taskSoap;

   /* the main sourceforge interface */
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
   public TaskUtil(String serverUrl, String sessionId)
   {

      m_sessionId = sessionId;

      m_taskSoap = (ITaskAppSoap) ClientSoapStubFactory.getSoapStub(
                                      ITaskAppSoap.class, serverUrl);
      m_sfSoap = (ISourceForgeSoap) ClientSoapStubFactory.getSoapStub(
                                      ISourceForgeSoap.class, serverUrl);
   }




   public String overloadedUser(String projectId)
      throws RemoteException
   {
      ProjectMemberSoapList pmsl;
      TaskSoapList tsl;
      try
      {
         pmsl = m_sfSoap.getProjectMemberList(m_sessionId, projectId);
         tsl = m_taskSoap.getTaskList(m_sessionId, projectId, null);
      }
      catch (Exception e)
      {
         log.error("unable to retrieve membership for project " + projectId);
         throw new RemoteException("unable to retrieve membership for project " + projectId);
      }

      ProjectMemberSoapRow[] pmsr = pmsl.getDataRows();
      TaskSoapRow[] tasks = tsl.getDataRows();

      int maxHours = -1;
      String maxUser = "";

      for ( ProjectMemberSoapRow i : pmsr)
      {
         int hours = 0;

         for (TaskSoapRow j : tasks)
         {
            if (i.getUserName().equals(j.getAssignedToUsername()))
               hours += j.getEstimatedHours();
         }

         if (hours > maxHours)
         {
            maxHours = hours;
            maxUser = i.getUserName();
         }
      }

      return (maxUser);
   }



}

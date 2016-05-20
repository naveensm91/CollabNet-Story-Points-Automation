package com.collab.util;


import java.rmi.RemoteException;


import org.apache.log4j.Logger;

import com.collabnet.ce.soap50.webservices.ClientSoapStubFactory;
import com.collabnet.ce.soap50.webservices.cemain.ICollabNetSoap;
import com.collabnet.ce.soap50.webservices.cemain.ProjectMemberSoapList;
import com.collabnet.ce.soap50.webservices.cemain.ProjectMemberSoapRow;
import com.collabnet.ce.soap50.webservices.taskmgr.ITaskAppSoap;
import com.collabnet.ce.soap50.webservices.taskmgr.TaskSoapList;
import com.collabnet.ce.soap50.webservices.taskmgr.TaskSoapRow;
import com.collabnet.ce.soap50.webservices.docman.IDocumentAppSoap;



/**
 * This example class works with  TeamForge documents including file
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
   public TaskUtil(String serverUrl, String sessionId)
   {

      m_sessionId = sessionId;

      m_taskSoap = (ITaskAppSoap) ClientSoapStubFactory.getSoapStub(
                                      ITaskAppSoap.class, serverUrl);
      m_sfSoap = (ICollabNetSoap) ClientSoapStubFactory.getSoapStub(
                                      ICollabNetSoap.class, serverUrl);
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
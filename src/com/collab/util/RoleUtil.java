package com.collab.util;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.collabnet.ce.soap50.webservices.cemain.ICollabNetSoap;
import com.collabnet.ce.soap50.webservices.rbac.IRbacAppSoap;
import com.collabnet.ce.soap50.webservices.rbac.RoleSoapList;
import com.collabnet.ce.soap50.webservices.ClientSoapStubFactory;


/**
 * This example class retrieves data about Role membership.
 * <p>
 *
 */
public class RoleUtil
{
   private static final Logger
      log = Logger.getLogger(RoleUtil.class);


   /* the TeamForge interface */
   private ICollabNetSoap m_sfSoap;

   /* the Role-Based Access interface */
   private IRbacAppSoap m_sfRbac;

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
   public RoleUtil(String serverUrl, String sessionId)
   {

      m_sessionId = sessionId;

      m_sfSoap = (ICollabNetSoap) ClientSoapStubFactory.
          getSoapStub(ICollabNetSoap.class, serverUrl);

      m_sfRbac = (IRbacAppSoap) ClientSoapStubFactory.
          getSoapStub(IRbacAppSoap.class, serverUrl);
   }



   /**
    * Return a list of roles for the user in the project.  Will only
    * work if the username given is the logged-in user or if the logged-in
    * user is a superuser.
    * <p>
    * @param username
    * <p>
    * @return a collection of role names.
    * @throws RemoteException Any errors returned from the TeamForge api are
    *                         wrapped and re-thrown to the caller
    */
   public Collection<String> getRoles(String projectId, String username)
      throws RemoteException
   {
       Collection<String> roles = new ArrayList<String>();
       RoleSoapList rsList = m_sfRbac.getUserRoleList(m_sessionId, 
                                                      projectId, username);
       for (int i = 0; i < rsList.getDataRows().length; i++) {
           roles.add(rsList.getDataRows()[i].getTitle());
       }
       return roles;
   }
}

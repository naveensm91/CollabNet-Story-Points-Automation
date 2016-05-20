package com.collab.util;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.collabnet.ce.soap50.webservices.cemain.ICollabNetSoap;
import com.collabnet.ce.soap50.webservices.cemain.GroupSoapList;
import com.collabnet.ce.soap50.webservices.cemain.GroupSoapRow;
import com.collabnet.ce.soap50.webservices.ClientSoapStubFactory;


/**
 * This example class retrieves data about Group membership.
 * <p>
 *
 */
public class GroupUtil
{
   private static final Logger
      log = Logger.getLogger(GroupUtil.class);


   /* the TeamForge interface */
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
   public GroupUtil(String serverUrl, String sessionId)
   {

      m_sessionId = sessionId;

      m_sfSoap = (ICollabNetSoap) ClientSoapStubFactory.
          getSoapStub(ICollabNetSoap.class, serverUrl);
   }



   /**
    * Return a list of groups of which the user is a member.  Will only
    * work if the username given is the logged-in user or if the logged-in
    * user is a superuser.
    * <p>
    * @param username
    * <p>
    * @return a collection of group names.
    * @throws RemoteException Any errors returned from the TeamForge api are
    *                         wrapped and re-thrown to the caller
    */
   public Collection<String> getGroups(String username)
      throws RemoteException
   {
       Collection<String> groups = new ArrayList<String>();
       GroupSoapList gList = m_sfSoap.getUserGroupList(m_sessionId, 
                                                       username);
       for (GroupSoapRow row: gList.getDataRows()) {
           groups.add(row.getFullName());
       }
       return groups;
   }
}

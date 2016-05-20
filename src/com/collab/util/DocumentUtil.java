package com.collab.util;


import java.rmi.RemoteException;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.File;

import org.apache.log4j.Logger;

import com.collabnet.ce.soap50.webservices.ClientSoapStubFactory;
import com.collabnet.ce.soap50.webservices.filestorage.IFileStorageAppSoap;
import com.collabnet.ce.soap50.webservices.docman.IDocumentAppSoap;
import com.collabnet.ce.soap50.webservices.docman.DocumentFolderSoapList;
import com.collabnet.ce.soap50.webservices.docman.DocumentFolderSoapRow;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;


/**
 * This example class works with  TeamForge documents including file
 * upload, etc.
 * <p>
 *
 */
public class DocumentUtil
{
   private static final Logger
      log = Logger.getLogger(DocumentUtil.class);


   /* the TeamForge document interface */
   private IDocumentAppSoap m_docSoap;

   /* the file storage interface */
   private IFileStorageAppSoap m_filestorageSoap;

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
   public DocumentUtil(String serverUrl, String sessionId)
   {
      m_sessionId = sessionId;

      m_filestorageSoap = (IFileStorageAppSoap) ClientSoapStubFactory.getSoapStub(
                                      IFileStorageAppSoap.class, serverUrl);

      m_docSoap = (IDocumentAppSoap) ClientSoapStubFactory.getSoapStub(
                                      IDocumentAppSoap.class, serverUrl);
   }




   public void uploadFile(String projectId, String docPath, String docFilePath)
      throws Exception
   {
      String docFolderId = getDocFolderId(projectId, docPath);

      File myFile = new File(docFilePath);
      if (!myFile.canRead())
      {
         log.error("unable to read local file " + docFilePath);
         throw new Exception("unable to read local file " + docFilePath);
      }
      DataSource ds = new FileDataSource(myFile);
      DataHandler dh = new DataHandler(ds);

      String uploadId;
      try
      {
         uploadId = m_filestorageSoap.uploadFile(m_sessionId, dh);
         m_docSoap.createDocument(m_sessionId, docFolderId,
                                  "a sample document upload",
                                  "the description goes here",
                                  "version one",
                                  "draft",
                                  false,
                                  "sample.pdf",
                                  "aplication/pdf",
                                  uploadId,
                                  null,
                                  null);
      }
      catch (RemoteException e)
      {
         log.error("unable to upload file " + docFilePath);
         throw new RemoteException("unable to upload local file " + docFilePath);
      }

      log.debug("successfully uploaded file");


   }


   private String getDocFolderId(String projectId, String docPath)
      throws RemoteException
   {
      DocumentFolderSoapList folders;

      try
      {
         folders = m_docSoap.getDocumentFolderList(m_sessionId, projectId, true);
      }
      catch (RemoteException e)
      {
         log.error("unable to retrieve list of folders in project " + projectId);
         throw new RemoteException("unable to retrieve list of folders in project " + projectId, e);
      }

      DocumentFolderSoapRow[] folderRow = folders.getDataRows();


      /*
         define and build a map of ids -> folder data
       */
      Map<String, DocumentFolderSoapRow>
         idMap = new LinkedHashMap<String, DocumentFolderSoapRow>();
      for (DocumentFolderSoapRow i : folderRow)
         idMap.put(i.getId(), i);

      /*
         loop through the folders, building a list keyed by the full doc
         path and whose value is the tracker id which will later be used
         to upload the document.
       */
      Map<String, String>
         pathMap = new HashMap<String, String>();

      String returnFolderId = null;

      for (DocumentFolderSoapRow i : idMap.values())
      {

         if (i.getParentFolderId().startsWith("DocumentApp"))
         {
            String value = "/" + i.getTitle();
            pathMap.put(i.getId(), value);
         }
         else
         {
            String value = i.getTitle();
            for (DocumentFolderSoapRow j = idMap.get(i.getParentFolderId());
                 !j.getParentFolderId().startsWith("DocumentApp");
                 j = idMap.get(j.getParentFolderId()))
               value = j.getTitle() + "/" + value;

            value = "/" + value;
            pathMap.put(i.getId(), value);
         }
      }

      log.debug("directories");
      log.debug("-----------");
      for (String i : pathMap.values())
         log.debug(i);


      /* loop through the values and find the appropriate key and return it */
      for ( String key : pathMap.keySet() )
      {
         String value = pathMap.get(key);
         if (value.equals(docPath))
         {
            returnFolderId = key;
            break;
         }
      }

      return (returnFolderId);
   }

}

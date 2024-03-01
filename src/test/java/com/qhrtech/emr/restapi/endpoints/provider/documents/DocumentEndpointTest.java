
package com.qhrtech.emr.restapi.endpoints.provider.documents;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.qhrtech.emr.accdocs.api.docs.AccDocManager;
import com.qhrtech.emr.accdocs.model.docs.AccDoc;
import com.qhrtech.emr.accuro.api.docs.DocsAccblobManager;
import com.qhrtech.emr.accuro.api.docs.DocumentManager;
import com.qhrtech.emr.accuro.api.docs.FolderTypeManager;
import com.qhrtech.emr.accuro.api.locks.ProtectionLockManager;
import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.model.docs.DocsAccblob;
import com.qhrtech.emr.accuro.model.docs.Document;
import com.qhrtech.emr.accuro.model.docs.DocumentReview;
import com.qhrtech.emr.accuro.model.docs.FileType;
import com.qhrtech.emr.accuro.model.docs.FolderType;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.locks.ProtectionLock;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.DocumentDto;
import com.qhrtech.emr.restapi.models.dto.DocumentReviewDto;
import com.qhrtech.emr.restapi.models.dto.FileTypeDto;
import com.qhrtech.emr.restapi.models.service.AcronConfiguration;
import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.services.AcronDetailsService;
import com.qhrtech.emr.restapi.services.BlobStorageService;
import com.qhrtech.emr.restapi.services.ModuleService;
import com.qhrtech.emr.restapi.services.exceptions.MD5Exception;
import com.qhrtech.emr.restapi.util.CustomMD5;
import com.qhrtech.emr.restapi.util.DateFormatter;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class DocumentEndpointTest extends AbstractEndpointTest<DocumentEndpoint> {

  public static final String PDF = ".pdf";
  private static String[] nonReferralExn = {".pdf", ".jpg", ".jpeg"};
  ApiSecurityContext context;
  private DocumentManager documentManager;
  private AccDocManager accDocManager;
  private AccuroPreferenceManager preferenceManager;
  private FolderTypeManager folderManager;
  private AuditLogUser user;
  private FolderType folder;
  private String fileName = TestUtilities.nextString(5);

  private DocsAccblobManager docsAccblobManager;

  private ProtectionLockManager lockManager;

  @Mock
  private AcronDetailsService discoveryDetailsService;

  private ModuleService moduleService;
  private BlobStorageService blobStorageService;

  private CloudStorageAccount cloudStorageAccount;
  private AcronConfiguration acronConfiguration;
  private DocsAccblob docsAccblob;

  public DocumentEndpointTest() {
    super(new DocumentEndpoint(), DocumentEndpoint.class);
    documentManager = mock(DocumentManager.class);
    accDocManager = mock(AccDocManager.class);
    preferenceManager = mock(AccuroPreferenceManager.class);
    folderManager = mock(FolderTypeManager.class);
    docsAccblobManager = mock(DocsAccblobManager.class);
    moduleService = mock(ModuleService.class);
    blobStorageService = mock(BlobStorageService.class);

    lockManager = mock(ProtectionLockManager.class);
    folder = new FolderType();
    folder.setName(TestUtilities.nextString(10));

    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));

    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(user);

  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> map = new HashMap<>();
    map.put(DocumentManager.class, documentManager);
    map.put(AccDocManager.class, accDocManager);
    map.put(AccuroPreferenceManager.class, preferenceManager);
    map.put(FolderTypeManager.class, folderManager);
    map.put(DocsAccblobManager.class, docsAccblobManager);
    map.put(ProtectionLockManager.class, lockManager);
    return map;
  }

  @Test
  public void createDocumentTest() throws Exception {

    String randomFilename = nonReferralExn[TestUtilities.nextInt(nonReferralExn.length)];
    DocumentDto docDto = getDocument(randomFilename);

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + randomFilename), bytes);

    File file = new File(fileName + randomFilename);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      List<File> files = new ArrayList<>();
      files.add(file);

      DocumentDto documentDto = getEncryptedFileName(docDto, fileBytes);
      Document document = getDocumentMapping(documentDto);
      Set<DocumentReview> documentReview = getDocReviewMapping(documentDto);
      AccDoc accDoc = getAccDocMapping(documentDto, bytes);

      Mockito.when(folderManager.getFolderById(documentDto.getFolderId())).thenReturn(folder);

      Mockito.when(lockManager.getLockByItemId(any(), any())).thenReturn(null);
      Mockito.when(lockManager.createLock(any())).thenReturn(UUID.randomUUID());

      Integer documentId = document.getDocumentId();
      Mockito.when(documentManager
          .createDocument(document, documentReview, accDoc,
              accDocManager.getAccDocConnection(), getSecurityContext().getUser()))
          .thenReturn(documentId);

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");
      documentDto.setFileName(null);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(documentDto);

      Set<Integer> actual = toCollection(
          given().multiPart("details", json)
              .multiPart("document", file)
              .queryParam("type", "REFERRALs")
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(200)
              .extract().as(Integer[].class),
          HashSet::new);
      Assert.assertTrue(actual.contains(documentId));
      verify(documentManager).createDocument(document, documentReview,
          accDoc, accDocManager.getAccDocConnection(), getSecurityContext().getUser());
    } finally {
      file.delete();
    }
  }

  @Test
  public void testCreateDocumentWithAzureBlob() throws Exception {

    String randomFilename = nonReferralExn[TestUtilities.nextInt(nonReferralExn.length)];
    DocumentDto docDto = getDocument(randomFilename);

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + randomFilename), bytes);

    File file = new File(fileName + randomFilename);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      List<File> files = new ArrayList<>();
      files.add(file);

      DocumentDto documentDto = getEncryptedFileName(docDto, fileBytes);

      Mockito.when(folderManager.getFolderById(documentDto.getFolderId())).thenReturn(folder);

      Mockito.when(lockManager.getLockByItemId(any(), any())).thenReturn(null);
      Mockito.when(lockManager.createLock(any())).thenReturn(UUID.randomUUID());

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");

      setUpBlobStorageContent();

      when(discoveryDetailsService.getCloudStorageAccount(anyString()))
          .thenReturn(acronConfiguration.getCloudStorageAccount());

      Document document = getDocumentMapping(documentDto);
      when(docsAccblobManager.getByFolderIdAndFileName(document.getFolderId(),
          document.getFileName())).thenReturn(docsAccblob);

      Set<DocumentReview> documentReview = getDocReviewMapping(documentDto);

      Integer documentId = document.getDocumentId();

      when(moduleService.isAccBlobEnabled()).thenReturn(true);
      when(documentManager
          .createDocument(eq(document), eq(documentReview), any(BlobUploadStub.class)))
              .thenReturn(documentId);

      documentDto.setFileName(null);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(documentDto);

      Set<Integer> actual = toCollection(
          given().multiPart("details", json)
              .multiPart("document", file)
              .queryParam("type", "REFERRALs")
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(200)
              .extract().as(Integer[].class),
          HashSet::new);
      Assert.assertTrue(actual.contains(documentId));
      verify(documentManager).createDocument(eq(document), eq(documentReview),
          any(BlobUploadStub.class));
    } finally {
      file.delete();
    }
  }


  @Test
  public void createDocumentTestWithMoreThanPermittedAttachments() throws Exception {

    String fileExtension = PDF;
    DocumentDto docDto = getDocument(fileExtension);

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      DocumentDto documentDto = getEncryptedFileName(docDto, fileBytes);
      Document document = getDocumentMapping(documentDto);
      Set<DocumentReview> documentReview = getDocReviewMapping(documentDto);
      AccDoc accDoc = getAccDocMapping(documentDto, bytes);

      Mockito.when(folderManager.getFolderById(documentDto.getFolderId())).thenReturn(folder);

      Mockito.when(lockManager.getLockByItemId(any(), any())).thenReturn(null);
      Mockito.when(lockManager.createLock(any())).thenReturn(UUID.randomUUID());

      Integer documentId = document.getDocumentId();
      Mockito.when(documentManager
          .createDocument(document, documentReview, accDoc,
              accDocManager.getAccDocConnection(), getSecurityContext().getUser()))
          .thenReturn(documentId);

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(documentDto);

      Object actual =
          // passing more than 5 documents
          given()
              .multiPart("details", json)
              .multiPart("document", file)
              .multiPart("document", file)
              .multiPart("document", file)
              .multiPart("document", file)
              .multiPart("document", file)
              .multiPart("document", file)
              .queryParam("type", "REFERRALs")
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(400)
              .extract().as(Object.class);
      assertTrue(actual.toString().contains("Maximum 5 files can be uploaded."));
      verify(documentManager, never()).createDocument(document, documentReview,
          accDoc, accDocManager.getAccDocConnection(), getSecurityContext().getUser());
    } finally {
      file.delete();
    }
  }

  @Test
  public void createDocumentTestWithMultipleAttachments() throws Exception {

    List<String> types = Arrays.asList("REFERRAL", TestUtilities.nextString(10));
    String type = TestUtilities.nextElement(types);

    DocumentDto documentDto = getDocument("." + getExtension(type));

    Set<Integer> expected = new HashSet<>();
    File file1 = generateFile(expected, documentDto, getExtension(type));
    File file2 = generateFile(expected, documentDto, getExtension(type));
    File file3 = generateFile(expected, documentDto, getExtension(type));
    File file4 = generateFile(expected, documentDto, getExtension(type));
    File file5 = generateFile(expected, documentDto, getExtension(type));

    try {
      Mockito.when(folderManager.getFolderById(documentDto.getFolderId())).thenReturn(folder);

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");

      Mockito.when(lockManager.getLockByItemId(any(), any())).thenReturn(null);
      Mockito.when(lockManager.createLock(any())).thenReturn(UUID.randomUUID());

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      documentDto.setFileName(null);
      String json = ow.writeValueAsString(documentDto);

      // passing more 3 documents
      Set<Integer> actual = toCollection(
          given()
              .multiPart("details", json)
              .multiPart("document", file1)
              .multiPart("document", file2)
              .multiPart("document", file3)
              .multiPart("document", file4)
              .multiPart("document", file5)
              .queryParam("type", type)
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(200)
              .extract().as(Integer[].class),
          HashSet::new);

      assertEquals(expected, actual);

      verify(documentManager, times(5))
          .createDocument(
              any(),
              any(),
              any(),
              any(),
              any());
    } finally {
      file1.delete();
      file2.delete();
      file3.delete();
      file4.delete();
      file5.delete();
    }
  }

  @Test
  public void createDocumentTestWithWrongFileExtension() throws Exception {

    String fileExtension = "." + TestUtilities.nextString(10);
    DocumentDto docDto = getDocument(fileExtension);

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      List<File> files = new ArrayList<>();
      files.add(file);

      DocumentDto documentDto = getEncryptedFileName(docDto, fileBytes);
      Document document = getDocumentMapping(documentDto);
      Set<DocumentReview> documentReview = getDocReviewMapping(documentDto);
      AccDoc accDoc = getAccDocMapping(documentDto, bytes);

      Mockito.when(folderManager.getFolderById(documentDto.getDocumentId())).thenReturn(folder);

      Mockito.when(lockManager.getLockByItemId(any(), any())).thenReturn(null);
      Mockito.when(lockManager.createLock(any())).thenReturn(UUID.randomUUID());

      Integer documentId = document.getDocumentId();
      Mockito.when(documentManager
          .createDocument(document, documentReview, accDoc,
              accDocManager.getAccDocConnection(), getSecurityContext().getUser()))
          .thenReturn(documentId);

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");

      documentDto.setFileName(null);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(documentDto);
      Object actual =
          given().multiPart("details", json)
              .multiPart("document", file)
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(400)
              .extract().as(Object.class);
      assertTrue(actual.toString().contains("Valid file extensions are: PDF, JPEG or JPG."));
      verify(documentManager, never()).createDocument(document, documentReview,
          accDoc, accDocManager.getAccDocConnection(), getSecurityContext().getUser());
    } finally {
      file.delete();
    }
  }


  @Test
  public void createDocumentTestWithMissingFileExtension() throws Exception {

    String fileExtension = "";
    DocumentDto docDto = getDocument(fileExtension);

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);

    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      List<File> files = new ArrayList<>();
      files.add(file);

      DocumentDto documentDto = getEncryptedFileName(docDto, fileBytes);
      Document document = getDocumentMapping(documentDto);
      Set<DocumentReview> documentReview = getDocReviewMapping(documentDto);
      AccDoc accDoc = getAccDocMapping(documentDto, bytes);

      Mockito.when(folderManager.getFolderById(documentDto.getDocumentId())).thenReturn(folder);

      Integer documentId = document.getDocumentId();
      Mockito.when(documentManager
          .createDocument(document, documentReview, accDoc,
              accDocManager.getAccDocConnection(), getSecurityContext().getUser()))
          .thenReturn(documentId);

      Mockito.when(lockManager.getLockByItemId(any(), any())).thenReturn(null);
      Mockito.when(lockManager.createLock(any())).thenReturn(UUID.randomUUID());

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");

      documentDto.setFileName(null);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(documentDto);
      Object actual =
          given().multiPart("details", json)
              .multiPart("document", file)
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(400)
              .extract().as(Object.class);
      assertTrue(actual.toString().contains("Missing file Extension."));
      verify(documentManager, never()).createDocument(document, documentReview,
          accDoc, accDocManager.getAccDocConnection(), getSecurityContext().getUser());
    } finally {
      file.delete();
    }
  }

  @Test
  public void createDocumentWithDataAccessException() throws Exception {
    String fileExtension = "";

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);

    try {
      Mockito.when(lockManager.getLockByItemId(any(), any())).thenReturn(null);
      Mockito.when(lockManager.createLock(any())).thenReturn(UUID.randomUUID());

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(getFixture(DocumentDto.class));

      doThrow(new DatabaseInteractionException("Test Error")).when(preferenceManager)
          .getSystemPreference("ShowDocumentsSubType");
      Object actual =
          given().multiPart("details", json)
              .multiPart("document", file)
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(400)
              .extract().as(Object.class);
      assertTrue(actual.toString().contains("Error processing request. Reason: "));
    } finally {
      file.delete();
    }
  }

  @Test
  public void createDocumentWhenSubtypeModuleOff() throws Exception {
    String fileExtension = "";

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);
    try {
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(getFixture(DocumentDto.class));

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("false");

      Object actual =
          given().multiPart("details", json)
              .multiPart("document", file)
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(401)
              .extract().as(Object.class);
      assertTrue(actual.toString().contains("Preference not enabled to view Sub Folder."));
    } finally {
      file.delete();
    }
  }

  @Test
  public void createDocumentWhenDocumentFromOff() throws Exception {
    String fileExtension = "";

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);

    try {
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(getFixture(DocumentDto.class));

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("false");

      Object actual =
          given().multiPart("details", json)
              .multiPart("document", file)
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(401)
              .extract().as(Object.class);
      assertTrue(actual.toString().contains("Preference not enabled to view From field."));
    } finally {
      file.delete();
    }
  }

  @Test
  public void createReferralDocumentTestWithWrongFileExtension() throws Exception {

    String fileExtension = "." + TestUtilities.nextString(10);
    DocumentDto docDto = getDocument(fileExtension);

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      List<File> files = new ArrayList<>();
      files.add(file);

      DocumentDto documentDto = getEncryptedFileName(docDto, fileBytes);
      Document document = getDocumentMapping(documentDto);
      Set<DocumentReview> documentReview = getDocReviewMapping(documentDto);
      AccDoc accDoc = getAccDocMapping(documentDto, bytes);

      Mockito.when(folderManager.getFolderById(documentDto.getDocumentId())).thenReturn(folder);

      Integer documentId = document.getDocumentId();
      Mockito.when(documentManager
          .createDocument(document, documentReview, accDoc,
              accDocManager.getAccDocConnection(), getSecurityContext().getUser()))
          .thenReturn(documentId);

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");

      documentDto.setFileName(null);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(documentDto);

      Object actual =
          given().multiPart("details", json)
              .multiPart("document", file)
              .queryParam("type", "REFERRAL")
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(400)
              .extract().as(Object.class);
      assertTrue(actual.toString().contains("Valid file extension is: PDF."));
      verify(documentManager, never()).createDocument(document, documentReview,
          accDoc, accDocManager.getAccDocConnection(), getSecurityContext().getUser());
    } finally {
      file.delete();
    }
  }

  @Test
  public void createReferralDocumentTest() throws Exception {

    String fileExtension = ".pdf";
    DocumentDto docDto = getDocument(fileExtension);

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      List<File> files = new ArrayList<>();
      files.add(file);

      DocumentDto documentDto = getEncryptedFileName(docDto, fileBytes);
      Document document = getDocumentMapping(documentDto);
      Set<DocumentReview> documentReview = getDocReviewMapping(documentDto);
      AccDoc accDoc = getAccDocMapping(documentDto, bytes);

      Mockito.when(folderManager.getFolderById(documentDto.getDocumentId())).thenReturn(folder);

      Integer documentId = document.getDocumentId();
      Mockito.when(documentManager
          .createDocument(document, documentReview, accDoc,
              accDocManager.getAccDocConnection(), getSecurityContext().getUser()))
          .thenReturn(documentId);

      Mockito.when(lockManager.getLockByItemId(any(), any())).thenReturn(null);
      Mockito.when(lockManager.createLock(any())).thenReturn(UUID.randomUUID());

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(documentDto);

      Set<Integer> actual = toCollection(
          given().multiPart("details", json)
              .multiPart("document", file)
              .queryParam("type", "REFERRAL")
              .header("Content-Type", "multipart/form-data")
              .when()
              .post(getBaseUrl() + "/v1/provider-portal/documents")
              .then()
              .assertThat().statusCode(200)
              .extract().as(Integer[].class),
          HashSet::new);
      Assert.assertTrue(actual.contains(documentId));
      verify(documentManager).createDocument(document, documentReview,
          accDoc, accDocManager.getAccDocConnection(), getSecurityContext().getUser());
    } finally {
      file.delete();
    }
  }

  @Test
  public void createDocumentLockConflictTest() throws Exception {

    String fileExtension = ".pdf";
    DocumentDto docDto = getDocument(fileExtension);

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      List<File> files = new ArrayList<>();
      files.add(file);

      DocumentDto documentDto = getEncryptedFileName(docDto, fileBytes);
      Mockito.when(folderManager.getFolderById(documentDto.getDocumentId())).thenReturn(folder);
      Mockito.when(lockManager.getLockByItemId(any(), any()))
          .thenReturn(mock(ProtectionLock.class));

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");

      // To shorten wait time
      Field field = DocumentEndpoint.class.getDeclaredField("WAIT_LOCK_INTERVAL");
      field.setAccessible(true);
      field.setInt(null, 1);

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(documentDto);

      given().multiPart("details", json)
          .multiPart("document", file)
          .queryParam("type", "REFERRAL")
          .header("Content-Type", "multipart/form-data")
          .when()
          .post(getBaseUrl() + "/v1/provider-portal/documents")
          .then()
          .assertThat().statusCode(409);
    } finally {
      file.delete();
    }
  }

  @Test
  public void createDocumentCreateLockConflictTest() throws Exception {

    String fileExtension = ".pdf";
    DocumentDto docDto = getDocument(fileExtension);

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      List<File> files = new ArrayList<>();
      files.add(file);

      DocumentDto documentDto = getEncryptedFileName(docDto, fileBytes);

      Mockito.when(folderManager.getFolderById(documentDto.getDocumentId())).thenReturn(folder);
      Mockito.when(lockManager.getLockByItemId(any(), any())).thenReturn(null);
      Mockito.when(lockManager.createLock(any())).thenReturn(null);

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");

      // To shorten wait time
      Field field = DocumentEndpoint.class.getDeclaredField("WAIT_LOCK_INTERVAL");
      field.setAccessible(true);
      field.setInt(null, 1);

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(documentDto);

      given().multiPart("details", json)
          .multiPart("document", file)
          .queryParam("type", "REFERRAL")
          .header("Content-Type", "multipart/form-data")
          .when()
          .post(getBaseUrl() + "/v1/provider-portal/documents")
          .then()
          .assertThat().statusCode(409);
    } finally {
      file.delete();
    }
  }

  @Test
  public void createDocumentExistingLockConflictTest() throws Exception {

    String fileExtension = ".pdf";
    DocumentDto docDto = getDocument(fileExtension);

    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName + fileExtension), bytes);

    File file = new File(fileName + fileExtension);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      List<File> files = new ArrayList<>();
      files.add(file);

      DocumentDto documentDto = getEncryptedFileName(docDto, fileBytes);

      when(lockManager.createLock(any())).thenThrow(new ResourceConflictException(""));

      Mockito.when(folderManager.getFolderById(documentDto.getDocumentId())).thenReturn(folder);
      Mockito.when(lockManager.getLockByItemId(any(), any())).thenReturn(null);

      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsFrom"))
          .thenReturn("true");
      Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
          .thenReturn("true");

      // To shorten wait time
      Field field = DocumentEndpoint.class.getDeclaredField("WAIT_LOCK_INTERVAL");
      field.setAccessible(true);
      field.setInt(null, 1);

      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(documentDto);

      given().multiPart("details", json)
          .multiPart("document", file)
          .queryParam("type", "REFERRAL")
          .header("Content-Type", "multipart/form-data")
          .when()
          .post(getBaseUrl() + "/v1/provider-portal/documents")
          .then()
          .assertThat().statusCode(409);
    } finally {
      file.delete();
    }
  }

  private DocumentDto getDocument(String fileExtension) {
    DocumentDto documentDto = new DocumentDto();
    DocumentReviewDto documentReviewDto = new DocumentReviewDto();
    LocalDateTime date = LocalDate.now().toLocalDateTime(LocalTime.MIDNIGHT);
    documentReviewDto.setReviewDate(date);
    documentReviewDto.setProviderId(TestUtilities.nextId());
    documentDto.setDocumentDate(date);
    documentDto.setReceivedDate(date);
    documentDto.setDateCreated(date);
    documentDto.setDocumentId(3);
    documentDto.setPriority(1);
    documentDto.setFileName(TestUtilities.nextString(10) + fileExtension);
    documentDto.setFolderId(1);
    documentDto.setReviews(Collections.singleton(documentReviewDto));
    documentDto.setFromName(TestUtilities.nextString(10));
    documentDto.setPatientId(TestUtilities.nextInt());

    return documentDto;
  }

  private DocumentDto getEncryptedFileName(DocumentDto documentDto, byte[] fileBytes)
      throws MD5Exception {

    CustomMD5 fileCrypt = new CustomMD5("md5");
    fileCrypt.readByteArray(fileBytes);
    String fileName = documentDto.getFileName();
    int periodLocation = fileName.lastIndexOf(".");
    String extension;
    if (periodLocation == -1) {
      extension = "";
    } else {
      extension = fileName.substring(periodLocation);

    }
    fileName = fileCrypt.toString() + extension;
    documentDto.setFileName(fileName);
    return documentDto;


  }

  private Document getDocumentMapping(DocumentDto documentDto) {

    if (documentDto.getPriority() == null) {
      documentDto.setPriority(1);// 1 is corresponding to normal priority.
    }

    LocalDate localDate = LocalDate.now();
    documentDto.setDocumentDate(localDate.toLocalDateTime(LocalTime.MIDNIGHT));
    if (documentDto.getDateCreated() == null) {
      documentDto.setDateCreated(localDate.toLocalDateTime(LocalTime.MIDNIGHT));
    } else {
      documentDto.setDateCreated(DateFormatter.toDay(documentDto.getDateCreated()));

    }

    if (documentDto.getReceivedDate() == null) {
      documentDto.setReceivedDate(localDate.toLocalDateTime(LocalTime.MIDNIGHT));
    } else {
      documentDto.setReceivedDate(DateFormatter.toDay(documentDto.getReceivedDate()));
    }

    return mapDto(documentDto, Document.class);
  }

  private Set<DocumentReview> getDocReviewMapping(DocumentDto documentDto) {
    Set<DocumentReview> reviews = new HashSet<>();
    if (documentDto.getReviews() != null) {
      documentDto.getReviews().forEach(x -> {
        if (!(x.getProviderId() == 0 && x.getReviewDate() == null)) {
          x.setReviewDate(DateFormatter.toDay(x.getReviewDate()));
          DocumentReview documentReview = mapDto(x, DocumentReview.class);
          documentReview.setReviewed(!(documentReview.getReviewDate() == null));
          reviews.add(documentReview);
        }
      });
    }
    if (reviews.isEmpty()) {
      DocumentReview documentReview = new DocumentReview();
      documentReview.setReviewed(false);
      documentReview.setProviderId(0);
      reviews.add(documentReview);
    }
    return reviews;
  }

  private AccDoc getAccDocMapping(DocumentDto documentDto, byte[] bytes) {
    AccDoc accDoc = new AccDoc();
    accDoc.setContents(bytes);
    accDoc.setFileName(documentDto.getFileName());
    accDoc.setUploadedDate(documentDto.getDocumentDate());
    return accDoc;
  }

  @Test
  public void testGetFileTypes() throws ProtossException {
    Set<FileType> fileTypes =
        getFixtures(FileType.class, HashSet::new, 5);

    Set<FileTypeDto> expected = mapDto(fileTypes, FileTypeDto.class, HashSet::new);

    when(documentManager.getFileTypes()).thenReturn(fileTypes);

    Set<FileTypeDto> actual = toCollection(
        given()
            .when()

            .get(getBaseUrl() + "/v1/provider-portal/documents/file-types")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(FileTypeDto[].class),
        HashSet::new);

    assertEquals(expected, actual);

    verify(documentManager).getFileTypes();
  }

  @Test
  public void testGetDocumentReview() throws ProtossException {
    int documentId = TestUtilities.nextInt();
    Set<DocumentReview> documentReviews = getFixtures(DocumentReview.class, HashSet::new, 3);

    Set<DocumentReviewDto> expected = documentReviews.stream()
        .map(d -> mapDto(d, DocumentReviewDto.class)).collect(Collectors.toSet());

    when(documentManager.getReviewsForDocument(documentId)).thenReturn(documentReviews);
    Set<DocumentReviewDto> actual = toCollection(
        given()
            .pathParams("documentId", documentId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/documents/{documentId}/reviews")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(DocumentReviewDto[].class),
        HashSet::new);

    assertEquals(expected, actual);

    verify(documentManager).getReviewsForDocument(documentId);
  }


  private void setUpBlobStorageContent() {

    cloudStorageAccount = new CloudStorageAccount();
    cloudStorageAccount.setStorageAccountName(TestUtilities.nextString(10));
    cloudStorageAccount.setContainerName(TestUtilities.nextString(10));

    acronConfiguration = new AcronConfiguration();
    acronConfiguration.setId(TestUtilities.nextString(10));
    acronConfiguration.setName(TestUtilities.nextString(20));
    acronConfiguration.setCloudStorageAccount(cloudStorageAccount);

    docsAccblob = getFixture(DocsAccblob.class);
  }

  private File generateFile(Set<Integer> expected, DocumentDto documentDto, String fileExtension)
      throws Exception {
    String fileName = TestUtilities.nextString(10) + "." + fileExtension;
    byte[] bytes = Base64.getDecoder().decode(TestUtilities.nextString(10));
    Files.write(Paths.get(fileName), bytes);

    File file = new File(fileName);
    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());

      documentDto.setFileName(fileName);
      documentDto = getEncryptedFileName(documentDto, fileBytes);
      Document document = getDocumentMapping(documentDto);
      Set<DocumentReview> documentReview = getDocReviewMapping(documentDto);
      AccDoc accDoc = getAccDocMapping(documentDto, bytes);
      Integer documentId = TestUtilities.nextInt();
      expected.add(documentId);

      Mockito.when(documentManager
          .createDocument(document, documentReview, accDoc,
              accDocManager.getAccDocConnection(), getSecurityContext().getUser()))
          .thenReturn(documentId);
    } catch (Exception e) {
      file.delete();
      fail("Failed to generate file");
    }

    return file;
  }

  private String getExtension(String type) {
    List<String> fileExtensions = Arrays.asList("pdf", "jpg", "jpeg");
    String fileExtension = "pdf";
    if (!type.equals("REFERRAL")) {
      fileExtension = TestUtilities.nextElement(fileExtensions);
    }
    return fileExtension;
  }
}

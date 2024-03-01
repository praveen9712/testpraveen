
package com.qhrtech.emr.restapi.endpoints.provider.documents;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accdocs.api.docs.AccDocManager;
import com.qhrtech.emr.accdocs.model.docs.AccDoc;
import com.qhrtech.emr.accuro.api.docs.DocsAccblobManager;
import com.qhrtech.emr.accuro.api.docs.DocumentManager;
import com.qhrtech.emr.accuro.model.docs.DocsAccblob;
import com.qhrtech.emr.accuro.model.docs.Document;
import com.qhrtech.emr.accuro.model.docs.FileType;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.DocumentDto;
import com.qhrtech.emr.restapi.models.dto.PatientFoldersDto;
import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.services.AcronDetailsService;
import com.qhrtech.emr.restapi.services.BlobIdentifier;
import com.qhrtech.emr.restapi.services.BlobIdentifier.BlobIdentifierBuilder;
import com.qhrtech.emr.restapi.services.BlobStorageService;
import com.qhrtech.emr.restapi.services.ModuleService;
import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;
import com.qhrtech.emr.restapi.services.impl.HostedDiscoveryDetailsService;
import com.qhrtech.emr.restapi.util.DateFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class PatientDocumentEndpointTest extends AbstractEndpointTest<PatientDocumentEndpoint> {


  ApiSecurityContext context;
  private DocumentManager documentManager;
  private AccDocManager accDocManager;
  private DocsAccblobManager docsAccblobManager;


  private ModuleService moduleService;

  private BlobStorageService blobStorageService;
  private AcronDetailsService discoveryDetailsService;

  private DocsAccblob docsAccblob;
  private AuditLogUser user;

  public PatientDocumentEndpointTest() {
    super(new PatientDocumentEndpoint(), PatientDocumentEndpoint.class);
    documentManager = mock(DocumentManager.class);
    accDocManager = mock(AccDocManager.class);
    docsAccblobManager = mock(DocsAccblobManager.class);
    moduleService = mock(ModuleService.class);
    blobStorageService = mock(BlobStorageService.class);
    discoveryDetailsService = mock(HostedDiscoveryDetailsService.class);

    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));

    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);

  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));

    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> map = new HashMap<>();
    map.put(DocumentManager.class, documentManager);
    map.put(AccDocManager.class, accDocManager);
    map.put(DocsAccblobManager.class, docsAccblobManager);

    return map;
  }

  @Test
  public void testGetPatientFolders() throws ProtossException {

    // set up scenario
    Integer patientId = TestUtilities.nextInt();
    Set<Document> documents = getFixtures(Document.class, HashSet::new, 5);
    // set up documents
    setDefaultValuesForDocument(documents, patientId);

    // set up deleted documents so this values are filtered
    Set<Document> deletedDocuments = getFixtures(Document.class, HashSet::new, 3);
    deletedDocuments.forEach(doc -> doc.setDeleted(1));

    documents.addAll(deletedDocuments);

    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);

    PatientFoldersDto expected = getFixture(PatientFoldersDto.class);
    Set<String> folders = new HashSet<>();
    Set<String> subType = new HashSet<>();

    documents.forEach(doc -> {
      if (new Integer(0).equals(doc.getDeleted())) {
        folders.add(doc.getPathType());

        if (!StringUtils.isBlank(doc.getSubtype())) {
          subType.add(doc.getSubtype());
        }
      }
    });

    expected.setPatientId(patientId);
    expected.setFolders(folders);
    expected.setSubtypes(subType);

    PatientFoldersDto actual =
        given()
            .pathParam("patientId", patientId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/folders/")
            .then()
            .assertThat().statusCode(200)
            .extract().as(PatientFoldersDto.class);

    verify(documentManager).getDocumentsForPatient(patientId);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetDocuments() throws Exception {
    String folderName = TestUtilities.nextString(10);
    String subType = TestUtilities.nextString(10);
    Integer patientId = TestUtilities.nextId();

    Set<FileType> fileTypes = getFixtures(FileType.class, HashSet::new, 5);
    Map<Integer, FileType> fileTypeMap = fileTypes.stream().collect(
        Collectors.toMap(FileType::getTypeId, t -> t));

    Set<Document> documents = getFixtures(Document.class, HashSet::new, 5);
    for (Document doc : documents) {
      doc.setPathType(folderName);
      doc.setSubtype(subType);
      doc.setPatientId(patientId);
      doc.setDeleted(0);
      doc.setArchived(0);
      doc.setActive(1);
      doc.setDocumentDate(DateFormatter.toDay(doc.getDocumentDate()));
      doc.setDateCreated(DateFormatter.toDay(doc.getDateCreated()));
      doc.setReceivedDate(DateFormatter.toDay(doc.getReceivedDate()));
      doc.setFileType(TestUtilities.nextElement(fileTypeMap.keySet()));
    }

    Set<Document> deletedDocuments = getFixtures(Document.class, HashSet::new, 3);
    deletedDocuments.forEach(doc -> doc.setDeleted(1));

    Set<Document> unexpectedDocuments = getFixtures(Document.class, HashSet::new, 3);
    setDefaultValuesForDocument(unexpectedDocuments, patientId);

    documents.addAll(deletedDocuments);
    documents.addAll(unexpectedDocuments);

    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);
    when(documentManager.getFileTypes()).thenReturn(fileTypes);

    Set<DocumentDto> expected = documents.stream().map(d -> {
      DocumentDto document = mapDto(d, DocumentDto.class);
      if (new Integer(0).equals(d.getDeleted())
          && folderName.equals(d.getPathType())
          && subType.equals(d.getSubtype())) {

        return document;
      }
      return null;
    }).collect(Collectors.toSet());
    expected.remove(null);

    Set<DocumentDto> actual = toCollection(
        given()
            .pathParams("patientId", patientId)
            .queryParams("folder", folderName, "subtype", subType)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/")
            .then()
            .assertThat().statusCode(200)
            .extract().as(DocumentDto[].class),
        HashSet::new);

    assertTrue(actual.containsAll(expected));

    verify(documentManager).getDocumentsForPatient(patientId);
  }

  @Test
  public void testGetDocumentsWithoutFolderName() throws Exception {
    String subType = TestUtilities.nextString(10);
    Integer patientId = TestUtilities.nextId();

    Set<FileType> fileTypes = getFixtures(FileType.class, HashSet::new, 5);
    Map<Integer, FileType> fileTypeMap = fileTypes.stream().collect(
        Collectors.toMap(FileType::getTypeId, t -> t));

    Set<Document> documents = getFixtures(Document.class, HashSet::new, 5);
    for (Document doc : documents) {
      doc.setSubtype(subType);
      doc.setPatientId(patientId);
      doc.setDeleted(0);
      doc.setArchived(0);
      doc.setActive(1);
      doc.setDocumentDate(DateFormatter.toDay(doc.getDocumentDate()));
      doc.setDateCreated(DateFormatter.toDay(doc.getDateCreated()));
      doc.setReceivedDate(DateFormatter.toDay(doc.getReceivedDate()));
      doc.setFileType(TestUtilities.nextElement(fileTypeMap.keySet()));
    }

    Set<Document> deletedDocuments = getFixtures(Document.class, HashSet::new, 3);
    deletedDocuments.forEach(doc -> doc.setDeleted(1));

    Set<Document> unexpectedDocuments = getFixtures(Document.class, HashSet::new, 3);
    setDefaultValuesForDocument(unexpectedDocuments, patientId);

    documents.addAll(deletedDocuments);
    documents.addAll(unexpectedDocuments);

    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);
    when(documentManager.getFileTypes()).thenReturn(fileTypes);

    Set<DocumentDto> expected = documents.stream().map(d -> {
      DocumentDto document = mapDto(d, DocumentDto.class);
      if (new Integer(0).equals(d.getDeleted())
          && subType.equals(d.getSubtype())) {

        return document;
      }
      return null;
    }).collect(Collectors.toSet());
    expected.remove(null);

    Set<DocumentDto> actual = toCollection(
        given()
            .pathParams("patientId", patientId)
            .queryParams("subtype", subType)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/")
            .then()
            .assertThat().statusCode(200)
            .extract().as(DocumentDto[].class),
        HashSet::new);

    assertTrue(actual.containsAll(expected));

    verify(documentManager).getDocumentsForPatient(patientId);
  }

  @Test
  public void testGetDocumentsWithoutSubType() throws Exception {
    String folderName = TestUtilities.nextString(10);
    Integer patientId = TestUtilities.nextId();

    Set<FileType> fileTypes = getFixtures(FileType.class, HashSet::new, 5);
    Map<Integer, FileType> fileTypeMap = fileTypes.stream().collect(
        Collectors.toMap(FileType::getTypeId, t -> t));

    Set<Document> documents = getFixtures(Document.class, HashSet::new, 5);
    for (Document doc : documents) {
      doc.setPathType(folderName);
      doc.setPatientId(patientId);
      doc.setDeleted(0);
      doc.setArchived(0);
      doc.setActive(1);
      doc.setDocumentDate(DateFormatter.toDay(doc.getDocumentDate()));
      doc.setDateCreated(DateFormatter.toDay(doc.getDateCreated()));
      doc.setReceivedDate(DateFormatter.toDay(doc.getReceivedDate()));
      doc.setFileType(TestUtilities.nextElement(fileTypeMap.keySet()));
    }

    Set<Document> deletedDocuments = getFixtures(Document.class, HashSet::new, 3);
    deletedDocuments.forEach(doc -> doc.setDeleted(1));

    Set<Document> unexpectedDocuments = getFixtures(Document.class, HashSet::new, 3);
    setDefaultValuesForDocument(unexpectedDocuments, patientId);

    documents.addAll(deletedDocuments);
    documents.addAll(unexpectedDocuments);

    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);
    when(documentManager.getFileTypes()).thenReturn(fileTypes);

    Set<DocumentDto> expected = documents.stream().map(d -> {
      DocumentDto document = mapDto(d, DocumentDto.class);
      if (new Integer(0).equals(d.getDeleted())
          && folderName.equals(d.getPathType())) {
        return document;
      }
      return null;
    }).collect(Collectors.toSet());
    expected.remove(null);

    Set<DocumentDto> actual = toCollection(
        given()
            .pathParams("patientId", patientId)
            .queryParams("folder", folderName)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/")
            .then()
            .assertThat().statusCode(200)
            .extract().as(DocumentDto[].class),
        HashSet::new);

    assertTrue(actual.containsAll(expected));

    verify(documentManager).getDocumentsForPatient(patientId);
  }

  @Test
  public void testGetEmptyDocuments() throws Exception {
    Integer patientId = TestUtilities.nextId();
    Set<Document> documents = getFixtures(Document.class, HashSet::new, 5);

    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);

    Set<DocumentDto> expected = Collections.emptySet();

    Set<DocumentDto> actual = toCollection(
        given()
            .pathParams("patientId", patientId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/")
            .then()
            .assertThat().statusCode(200)
            .extract().as(DocumentDto[].class),
        HashSet::new);

    assertEquals(expected, actual);
    verify(documentManager).getDocumentsForPatient(patientId);
  }

  @Test
  public void testGetDoc() throws ProtossException {
    Set<FileType> fileTypes = getFixtures(FileType.class, HashSet::new, 5);

    Document document = getFixture(Document.class);
    document.setFileType(TestUtilities.nextElement(fileTypes).getTypeId());

    int documentId = document.getDocumentId();
    int patientId = document.getPatientId();
    String fileName = document.getFileName();
    String pathName = document.getPathName();

    AccDoc accDoc = getFixture(AccDoc.class);

    when(documentManager.getDocumentById(documentId)).thenReturn(document);
    when(documentManager.getFileTypes()).thenReturn(fileTypes);
    when(accDocManager.getAccDocByFileName(fileName, pathName)).thenReturn(accDoc);

    byte[] expected = accDoc.getContents();

    byte[] actual = given()
        .pathParams("patientId", patientId, "documentId", documentId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/{documentId}")
        .then()
        .assertThat().statusCode(200)
        .extract().asByteArray();

    assertTrue(Arrays.equals(expected, actual));

    verify(documentManager).getDocumentById(documentId);
    verify(documentManager).getFileTypes();
    verify(accDocManager).getAccDocByFileName(fileName, pathName);
  }


  @Test
  public void testGetDocWithUnmatchedTypeId() throws ProtossException {
    Document document = getFixture(Document.class);

    int documentId = document.getDocumentId();
    int patientId = document.getPatientId();
    String fileName = document.getFileName();
    String pathName = document.getPathName();

    Set<FileType> fileTypes = getFixtures(FileType.class, HashSet::new, 5);

    AccDoc accDoc = getFixture(AccDoc.class);

    when(documentManager.getDocumentById(documentId)).thenReturn(document);
    when(documentManager.getFileTypes()).thenReturn(fileTypes);
    when(accDocManager.getAccDocByFileName(fileName, pathName)).thenReturn(accDoc);

    byte[] expected = accDoc.getContents();

    byte[] actual = given()
        .pathParams("patientId", patientId, "documentId", documentId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/{documentId}")
        .then()
        .assertThat().statusCode(200)
        .extract().asByteArray();

    assertTrue(Arrays.equals(expected, actual));

    verify(documentManager).getDocumentById(documentId);
    verify(documentManager).getFileTypes();
    verify(accDocManager).getAccDocByFileName(fileName, pathName);
  }

  @Test
  public void testGetDocWithNullTypeId() throws ProtossException {
    Document document = getFixture(Document.class);
    document.setFileType(null);

    int documentId = document.getDocumentId();
    int patientId = document.getPatientId();
    String fileName = document.getFileName();
    String pathName = document.getPathName();

    Set<FileType> fileTypes = getFixtures(FileType.class, HashSet::new, 5);

    AccDoc accDoc = getFixture(AccDoc.class);

    when(documentManager.getDocumentById(documentId)).thenReturn(document);
    when(documentManager.getFileTypes()).thenReturn(fileTypes);
    when(accDocManager.getAccDocByFileName(fileName, pathName)).thenReturn(accDoc);

    byte[] expected = accDoc.getContents();

    byte[] actual = given()
        .pathParams("patientId", patientId, "documentId", documentId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/{documentId}")
        .then()
        .assertThat().statusCode(200)
        .extract().asByteArray();

    assertTrue(Arrays.equals(expected, actual));

    verify(documentManager).getDocumentById(documentId);
    verify(documentManager).getFileTypes();
    verify(accDocManager).getAccDocByFileName(fileName, pathName);
  }

  @Test
  public void testGetDocWithInvalidDocumentId() throws ProtossException {
    int documentId = TestUtilities.nextId();
    int patientId = TestUtilities.nextId();

    given()
        .pathParams("patientId", patientId, "documentId", documentId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/{documentId}")
        .then()
        .assertThat().statusCode(404);

    verify(documentManager).getDocumentById(documentId);
    verify(documentManager, never()).getFileTypes();
  }

  @Test
  public void testGetDocWithInvalidDocumentFileAndPathName() throws ProtossException {
    Document document = getFixture(Document.class);
    int documentId = document.getDocumentId();
    int patientId = document.getPatientId();
    String fileName = document.getFileName();
    String pathName = document.getPathName();

    Set<FileType> fileTypes = getFixtures(FileType.class, HashSet::new, 5);

    when(documentManager.getDocumentById(documentId)).thenReturn(document);
    when(documentManager.getFileTypes()).thenReturn(fileTypes);

    given()
        .pathParams("patientId", patientId, "documentId", documentId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/{documentId}")
        .then()
        .assertThat().statusCode(404);

    verify(documentManager).getDocumentById(documentId);
    verify(documentManager).getFileTypes();
    verify(accDocManager).getAccDocByFileName(fileName, pathName);
  }

  @Test
  public void testGetDocForAzureBlob() throws ProtossException {
    Set<FileType> fileTypes = getFixtures(FileType.class, HashSet::new, 5);

    Document document = getFixture(Document.class);
    document.setFileType(TestUtilities.nextElement(fileTypes).getTypeId());

    int documentId = document.getDocumentId();

    byte[] expected = new byte[30];

    new Random().nextBytes(expected);

    when(documentManager.getDocumentById(documentId)).thenReturn(document);
    when(documentManager.getFileTypes()).thenReturn(fileTypes);

    setUpBlobStorageContent();

    CloudStorageAccount cloudStorageAccount = getFixture(CloudStorageAccount.class);

    when(discoveryDetailsService.getCloudStorageAccount(any(String.class))).thenReturn(
        cloudStorageAccount);

    BlobIdentifier blobIdentifier = new BlobIdentifierBuilder()
        .withContainerName(cloudStorageAccount.getContainerName())
        .withStorageAccount(cloudStorageAccount.getStorageAccountName())
        .withBlobName(docsAccblob.getBlobName())
        .build();

    when(moduleService.isAccBlobEnabled()).thenReturn(true);
    when(docsAccblobManager.getByDocumentId(documentId)).thenReturn(docsAccblob);
    when(blobStorageService.downloadBlob(eq(blobIdentifier))).thenReturn(expected);

    int patientId = document.getPatientId();

    byte[] actual = given()
        .pathParams("patientId", patientId, "documentId", documentId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/{documentId}")
        .then()
        .assertThat().statusCode(200)
        .extract().asByteArray();

    assertTrue(Arrays.equals(expected, actual));

    verify(documentManager).getDocumentById(documentId);
    verify(documentManager).getFileTypes();
    verify(docsAccblobManager).getByDocumentId(documentId);
  }

  @Test
  public void testGetDocForAzureBlobNameNotFound() throws ProtossException {
    Set<FileType> fileTypes = getFixtures(FileType.class, HashSet::new, 5);

    Document document = getFixture(Document.class);
    document.setFileType(TestUtilities.nextElement(fileTypes).getTypeId());

    int documentId = document.getDocumentId();

    when(documentManager.getDocumentById(documentId)).thenReturn(document);
    when(documentManager.getFileTypes()).thenReturn(fileTypes);

    setUpBlobStorageContent();

    when(moduleService.isAccBlobEnabled()).thenReturn(true);

    when(docsAccblobManager.getByDocumentId(documentId)).thenReturn(null);

    int patientId = document.getPatientId();

    given()
        .pathParams("patientId", patientId, "documentId", documentId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/{documentId}")
        .then()
        .assertThat().statusCode(404)
        .extract().asByteArray();

    verify(documentManager).getDocumentById(documentId);
    verify(documentManager).getFileTypes();
    verify(docsAccblobManager).getByDocumentId(documentId);
  }

  @Test
  public void testGetDocForAzureBlobStorageServiceException() throws ProtossException {
    Set<FileType> fileTypes = getFixtures(FileType.class, HashSet::new, 5);

    Document document = getFixture(Document.class);
    document.setFileType(TestUtilities.nextElement(fileTypes).getTypeId());

    int documentId = document.getDocumentId();

    when(documentManager.getDocumentById(documentId)).thenReturn(document);
    when(documentManager.getFileTypes()).thenReturn(fileTypes);

    setUpBlobStorageContent();

    when(moduleService.isAccBlobEnabled()).thenReturn(true);

    when(docsAccblobManager.getByDocumentId(documentId)).thenReturn(docsAccblob);

    when(blobStorageService.downloadBlob(any(BlobIdentifier.class))).thenThrow(
        StorageServiceException.class);

    int patientId = document.getPatientId();

    given()
        .pathParams("patientId", patientId, "documentId", documentId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/documents/{documentId}")
        .then()
        .assertThat().statusCode(500)
        .extract().asByteArray();

    verify(documentManager).getDocumentById(documentId);
    verify(documentManager).getFileTypes();
    verify(docsAccblobManager).getByDocumentId(documentId);
  }

  private void setUpBlobStorageContent() {
    docsAccblob = getFixture(DocsAccblob.class);
  }

  private void setDefaultValuesForDocument(Set<Document> documents, int patientId) {
    documents.forEach(
        doc -> {
          doc.setPatientId(patientId);
          doc.setDeleted(0);
          doc.setArchived(0);
          doc.setActive(1);
        });
  }


}

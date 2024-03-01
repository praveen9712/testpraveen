
package com.qhrtech.emr.restapi.endpoints.patient;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accdocs.api.docs.AccDocManager;
import com.qhrtech.emr.accdocs.model.docs.AccDoc;
import com.qhrtech.emr.accuro.api.docs.DocumentManager;
import com.qhrtech.emr.accuro.model.docs.Document;
import com.qhrtech.emr.accuro.model.docs.FileType;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.DocumentDto;
import com.qhrtech.emr.restapi.models.dto.PatientFoldersDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class DocumentEndpointTest extends AbstractEndpointTest<DocumentEndpoint> {
  private final DocumentManager documentManager;
  private final AccDocManager accDocManager;
  ApiSecurityContext context;
  private AuditLogUser user;


  public DocumentEndpointTest() {
    super(new DocumentEndpoint(), DocumentEndpoint.class);
    documentManager = mock(DocumentManager.class);
    accDocManager = mock(AccDocManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = getFixture(AuditLogUser.class);
    context.setUser(user);
    context.setPatientId(RandomUtils.nextInt());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(DocumentManager.class, documentManager);
    servicesMap.put(AccDocManager.class, accDocManager);
    return servicesMap;
  }

  @Test
  public void testGetPatientFolders() throws Exception {
    // setup random data
    Integer patientId = context.getPatientId();
    PatientFoldersDto expected = new PatientFoldersDto();

    Set<Document> documents =
        getFixtures(Document.class, HashSet::new, 2);
    documents.forEach(x -> {
      x.setPatientId(patientId);
      x.setActive(1);
      x.setArchived(0);
      x.setDeleted(0);
    });
    Set<String> folders = new HashSet<>();
    Set<String> subType = new HashSet<>();
    documents.forEach(doc -> {
      folders.add(doc.getPathType());

      if (!StringUtils.isBlank(doc.getSubtype())) {
        subType.add(doc.getSubtype());
      }
    });
    expected.setPatientId(patientId);
    expected.setFolders(folders);
    expected.setSubtypes(subType);

    // mock dependencies
    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);

    // test
    PatientFoldersDto actual =
        given()
            .when()
            .get(getBaseUrl() + "/v1/patient-portal/documents/folders/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PatientFoldersDto.class);

    // assertions
    assertEquals(expected, actual);
    verify(documentManager).getDocumentsForPatient(patientId);
  }

  @Test
  public void testGetDocuments() throws Exception {
    // setup random data
    Integer patientId = context.getPatientId();
    String folder = RandomStringUtils.randomAlphanumeric(5);
    String subType = RandomStringUtils.randomAlphanumeric(5);
    Set<Document> documents =
        getFixtures(Document.class, HashSet::new, 2);
    documents.forEach(x -> {
      x.setPatientId(patientId);
      x.setActive(1);
      x.setArchived(0);
      x.setDeleted(0);
      x.setPathType(folder);
      x.setSubtype(subType);
    });
    Set<DocumentDto> expected =
        mapDto(documents, DocumentDto.class, HashSet::new);

    // mock dependencies
    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);

    // test
    Set<DocumentDto> actual = toCollection(
        given()
            .queryParam("folder", folder)
            .queryParam("subtype", subType)
            .when()
            .get(getBaseUrl()
                + "/v1/patient-portal/documents/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(DocumentDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(documentManager).getDocumentsForPatient(patientId);
  }

  @Test
  public void testGetDocumentsEmptyDocsFiltered() throws Exception {
    // setup random data
    Integer patientId = context.getPatientId();
    String folder = RandomStringUtils.randomAlphanumeric(5);
    String subType = RandomStringUtils.randomAlphanumeric(5);
    Set<Document> documents =
        getFixtures(Document.class, HashSet::new, 2);

    // mock dependencies
    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);
    Set<DocumentDto> expected = Collections.emptySet();

    // test
    Set<DocumentDto> actual = toCollection(
        given()
            .queryParam("folder", folder)
            .queryParam("subtype", subType)
            .when()
            .get(getBaseUrl()
                + "/v1/patient-portal/documents/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(DocumentDto[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(documentManager).getDocumentsForPatient(patientId);
  }

  @Test
  public void testGetDocumentsNullFolder() throws Exception {
    // setup random data
    Integer patientId = context.getPatientId();
    String subType = RandomStringUtils.randomAlphanumeric(5);
    Set<Document> documents =
        getFixtures(Document.class, HashSet::new, 2);

    // mock dependencies
    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);
    Set<DocumentDto> expected = Collections.emptySet();

    // test
    Set<DocumentDto> actual = toCollection(
        given()
            .queryParam("subtype", subType)
            .when()
            .get(getBaseUrl()
                + "/v1/patient-portal/documents/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(DocumentDto[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(documentManager).getDocumentsForPatient(patientId);
  }

  @Test
  public void testGetDocumentsNullSubType() throws Exception {
    // setup random data
    Integer patientId = context.getPatientId();
    String folder = RandomStringUtils.randomAlphanumeric(5);
    Set<Document> documents =
        getFixtures(Document.class, HashSet::new, 2);

    // mock dependencies
    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);
    Set<DocumentDto> expected = Collections.emptySet();

    // test
    Set<DocumentDto> actual = toCollection(
        given()
            .queryParam("folder", folder)
            .when()
            .get(getBaseUrl()
                + "/v1/patient-portal/documents/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(DocumentDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(documentManager).getDocumentsForPatient(patientId);
  }

  @Test
  public void testGetDocumentsNullSubTypeAndFolder() throws Exception {
    // setup random data
    Integer patientId = context.getPatientId();
    Set<Document> documents =
        getFixtures(Document.class, HashSet::new, 2);

    // mock dependencies
    when(documentManager.getDocumentsForPatient(patientId)).thenReturn(documents);
    Set<DocumentDto> expected = Collections.emptySet();

    // test
    Set<DocumentDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl()
                + "/v1/patient-portal/documents/")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(DocumentDto[].class),
        HashSet::new);

    // assertions
    assertEquals(expected, actual);
    verify(documentManager).getDocumentsForPatient(patientId);
  }

  @Test
  public void testGetDoc() throws Exception {
    // setup random data
    Document document = getFixture(Document.class);
    document.setPatientId(context.getPatientId());
    Set<FileType> fileTypeSet = getFixtures(FileType.class, HashSet::new, 4);
    FileType fileType = getFixture(FileType.class);
    fileType.setTypeId(document.getFileType());
    fileTypeSet.add(fileType);
    AccDoc accDoc = getFixture(AccDoc.class);
    Integer documentId = document.getDocumentId();

    // mock dependencies
    when(documentManager.getDocumentById(documentId)).thenReturn(document);
    when(documentManager.getFileTypes()).thenReturn(fileTypeSet);
    when(accDocManager.getAccDocByFileName(document.getFileName(), document.getPathName()))
        .thenReturn(accDoc);
    // test
    given()
        .pathParam("documentId", documentId)
        .when()
        .get(getBaseUrl()
            + "/v1/patient-portal/documents/{documentId}")
        .then()
        .assertThat()
        .statusCode(200);

    // assertions
    verify(documentManager).getDocumentById(documentId);
    verify(documentManager).getFileTypes();
    verify(accDocManager).getAccDocByFileName(document.getFileName(), document.getPathName());
  }

  @Test
  public void testGetDocNotFoundDocument() throws Exception {
    // setup random data
    Document document = getFixture(Document.class);
    Integer documentId = document.getDocumentId();

    // mock dependencies
    when(documentManager.getDocumentById(documentId)).thenReturn(document);

    // test
    given()
        .pathParam("documentId", documentId)
        .when()
        .get(getBaseUrl()
            + "/v1/patient-portal/documents/{documentId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(documentManager).getDocumentById(documentId);
    verify(documentManager, never()).getFileTypes();
    verify(accDocManager, never()).getAccDocByFileName(document.getFileName(),
        document.getPathName());
  }

  @Test
  public void testGetDocAccDocNotFound() throws Exception {
    // setup random data
    Document document = getFixture(Document.class);
    Integer documentId = document.getDocumentId();
    document.setPatientId(context.getPatientId());

    // mock dependencies
    when(documentManager.getDocumentById(documentId)).thenReturn(document);

    // test
    given()
        .pathParam("documentId", documentId)
        .when()
        .get(getBaseUrl()
            + "/v1/patient-portal/documents/{documentId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(documentManager).getDocumentById(documentId);
    verify(documentManager).getFileTypes();
    verify(accDocManager).getAccDocByFileName(document.getFileName(), document.getPathName());
  }

  @Test
  public void testGetDocAccDocNotFoundWithNullFileType() throws Exception {
    // setup random data
    Document document = getFixture(Document.class);
    Integer documentId = document.getDocumentId();
    document.setPatientId(context.getPatientId());
    document.setFileType(null);

    // mock dependencies
    when(documentManager.getDocumentById(documentId)).thenReturn(document);

    // test
    given()
        .pathParam("documentId", documentId)
        .when()
        .get(getBaseUrl()
            + "/v1/patient-portal/documents/{documentId}")
        .then()
        .assertThat()
        .statusCode(404);

    // assertions
    verify(documentManager).getDocumentById(documentId);
    verify(documentManager).getFileTypes();
    verify(accDocManager).getAccDocByFileName(document.getFileName(), document.getPathName());
  }
}

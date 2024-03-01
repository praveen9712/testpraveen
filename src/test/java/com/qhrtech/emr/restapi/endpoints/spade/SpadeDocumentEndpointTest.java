
package com.qhrtech.emr.restapi.endpoints.spade;

import static io.restassured.RestAssured.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qhrtech.emr.accdocs.model.docs.DocsData;
import com.qhrtech.emr.accdocs.model.docs.DocsMetadata;
import com.qhrtech.emr.accdocs.model.docs.SpadeDocument;
import com.qhrtech.emr.accdocs.model.docs.SpadeFolder;
import com.qhrtech.emr.accuro.api.docs.DocsDataManager;
import com.qhrtech.emr.accuro.api.docs.SpadeDocumentManager;
import com.qhrtech.emr.accuro.api.docs.SpadeFolderManager;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.spade.SpadeDocumentDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SpadeDocumentEndpointTest extends AbstractEndpointTest<SpadeDocumentEndpoint> {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private SpadeDocumentManager documentManager;
  private DocsDataManager dataManager;
  private SpadeFolderManager spadeFolderManager;
  private ApiSecurityContext context;
  private AuditLogUser logUser;


  public SpadeDocumentEndpointTest() {
    super(new SpadeDocumentEndpoint(), SpadeDocumentEndpoint.class);
    documentManager = mock(SpadeDocumentManager.class);
    dataManager = mock(DocsDataManager.class);
    spadeFolderManager = mock(SpadeFolderManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    logUser = getFixture(AuditLogUser.class);
    context.setUser(logUser);
    context.setOauthClientId(TestUtilities.nextString(5));

  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> map = new HashMap<>();
    map.put(SpadeDocumentManager.class, documentManager);
    map.put(DocsDataManager.class, dataManager);
    map.put(SpadeFolderManager.class, spadeFolderManager);
    return map;
  }

  @Test
  public void testCreateSpadeDocument() throws Exception {

    SpadeDocumentDto spadeDocumentDto = getFixture(SpadeDocumentDto.class);
    spadeDocumentDto.setTimeCreated(LocalDateTime.now());
    spadeDocumentDto.setTimeModified(LocalDateTime.now());
    spadeDocumentDto.setHashValue(Hex.encodeHexString(TestUtilities.nextString(10)
        .getBytes()));
    validateDto(spadeDocumentDto);
    DocsMetadata docMetadata = mapDto(spadeDocumentDto, DocsMetadata.class);

    File testFile = temporaryFolder.newFile("test.pdf");
    byte[] bytes = Files.readAllBytes(testFile.toPath());
    when(documentManager.createSpadeDocument(docMetadata, bytes,
        spadeDocumentDto.getFolderId(), logUser))
            .thenReturn(spadeDocumentDto.getDocumentId());

    when(spadeFolderManager.getSpadeFolder(spadeDocumentDto.getFolderId()))
        .thenReturn(getFixture(SpadeFolder.class));

    Integer actual =
        given()
            .multiPart("metadata", spadeDocumentDto, MediaType.APPLICATION_JSON)
            .multiPart("file", testFile)
            .header("Content-Type", "multipart/form-data")
            .when()
            .post(getBaseUrl() + "/v1/spade/documents")
            .then()
            .assertThat().statusCode(200)
            .extract().as(Integer.class);

    Assert.assertEquals(actual, Integer.valueOf(spadeDocumentDto.getDocumentId()));

    verify(documentManager).createSpadeDocument(docMetadata, bytes, spadeDocumentDto.getFolderId(),
        logUser);

  }

  @Test
  public void testCreateSpadeDocumentBadRequest() throws Exception {

    SpadeDocumentDto spadeDocumentDto = getFixture(SpadeDocumentDto.class);
    spadeDocumentDto.setTimeCreated(LocalDateTime.now());
    spadeDocumentDto.setTimeModified(LocalDateTime.now());
    spadeDocumentDto.setHashValue(Hex.encodeHexString(TestUtilities.nextString(10)
        .getBytes()));

    when(spadeFolderManager.getSpadeFolder(spadeDocumentDto.getFolderId()))
        .thenReturn(null);

    File testFile = temporaryFolder.newFile("test.pdf");
    given()
        .multiPart("metadata", spadeDocumentDto, MediaType.APPLICATION_JSON)
        .multiPart("file", testFile)
        .header("Content-Type", "multipart/form-data")
        .when()
        .post(getBaseUrl() + "/v1/spade/documents")
        .then()
        .assertThat().statusCode(400)
        .extract();

    verify(documentManager, never()).createSpadeDocument(any(DocsMetadata.class), any(byte[].class),
        anyInt(), any(AuditLogUser.class));
  }

  @Test
  public void testUpdateSpadeDocument() throws Exception {

    SpadeDocument spadeDocument = getFixture(SpadeDocument.class);
    SpadeDocumentDto spadeDocumentDto = mapDto(spadeDocument, SpadeDocumentDto.class);

    when(documentManager.getSpadeDocument(spadeDocument.getDocumentId()))
        .thenReturn(spadeDocument);

    DocsMetadata docMetadata = mapDto(spadeDocumentDto, DocsMetadata.class);
    validateDto(spadeDocumentDto);

    docMetadata.setMetadataId(spadeDocument.getMetadataId());
    given()
        .body(spadeDocumentDto)
        .header("Content-Type", "application/json")
        .pathParam("id", spadeDocumentDto.getDocumentId())
        .when()
        .put(getBaseUrl() + "/v1/spade/documents/{id}")
        .then()
        .assertThat().statusCode(204);

    verify(documentManager).updateDocsMetadata(docMetadata, logUser);
  }

  @Test
  public void testUpdateSpadeDocumentBadRequest() throws Exception {

    SpadeDocument spadeDocument = getFixture(SpadeDocument.class);
    SpadeDocumentDto spadeDocumentDto = mapDto(spadeDocument, SpadeDocumentDto.class);
    spadeDocumentDto.setDeleted(!spadeDocument.isDeleted());

    when(documentManager.getSpadeDocument(spadeDocument.getDocumentId()))
        .thenReturn(spadeDocument);

    DocsMetadata docMetadata = mapDto(spadeDocumentDto, DocsMetadata.class);
    validateDto(spadeDocumentDto);

    docMetadata.setMetadataId(spadeDocument.getMetadataId());
    given()
        .body(spadeDocumentDto)
        .header("Content-Type", "application/json")
        .pathParam("id", spadeDocumentDto.getDocumentId())
        .when()
        .put(getBaseUrl() + "/v1/spade/documents/{id}")
        .then()
        .assertThat().statusCode(400);

    verify(documentManager, never()).updateDocsMetadata(docMetadata, logUser);
  }

  @Test
  public void testUpdateSpadeDocumentWithWrongId() throws Exception {

    SpadeDocumentDto spadeDocumentDto = getFixture(SpadeDocumentDto.class);
    spadeDocumentDto.setHashValue(Hex.encodeHexString(TestUtilities.nextString(10)
        .getBytes()));
    spadeDocumentDto.setTimeCreated(LocalDateTime.now());
    spadeDocumentDto.setTimeModified(LocalDateTime.now());
    validateDto(spadeDocumentDto);

    given()
        .body(spadeDocumentDto)
        .header("Content-Type", "application/json")
        .pathParam("id", TestUtilities.nextId())
        .when()
        .put(getBaseUrl() + "/v1/spade/documents/{id}")
        .then()
        .assertThat().statusCode(400);

    verify(documentManager, never()).updateDocsMetadata(any(DocsMetadata.class), any());
    verify(dataManager, never()).updateProcessFlag(anyInt(), anyInt(), any());
  }

  @Test
  public void testDeleteSpadeDocument() throws Exception {

    int documentId = TestUtilities.nextInt();
    given()
        .header("Content-Type", "application/json")
        .pathParam("id", documentId)
        .when()
        .delete(getBaseUrl() + "/v1/spade/documents/{id}")
        .then()
        .assertThat().statusCode(204);
    verify(documentManager).deleteSpadeDocument(documentId, logUser);
  }

  @Test
  public void testHardDeleteSpadeDocument() throws Exception {

    int documentId = TestUtilities.nextInt();
    given()
        .header("Content-Type", "application/json")
        .pathParam("id", documentId)
        .when()
        .delete(getBaseUrl() + "/v1/spade/documents/{id}?type=hard")
        .then()
        .assertThat().statusCode(204);
    verify(documentManager).removeSpadeDocument(documentId, logUser);
  }

  @Test
  public void testGetSpadeDocument() throws Exception {
    // test
    SpadeDocument spadeDocument = getFixture(SpadeDocument.class);
    SpadeDocumentDto documentDto = mapDto(spadeDocument, SpadeDocumentDto.class);
    String expectedContent = TestUtilities.nextString(10);

    doReturn(spadeDocument).when(documentManager)
        .getSpadeDocument(documentDto.getDocumentId());

    DocsData docsData = getFixture(DocsData.class);
    docsData.setContents(expectedContent.getBytes());
    doReturn(docsData).when(dataManager)
        .getDocsData(spadeDocument.getDataId());
    String actual = given()
        .pathParam("id", documentDto.getDocumentId())
        .accept("multipart/form-data")
        .when()
        .get(getBaseUrl() + "/v1/spade/documents/{id}")
        .then()
        .assertThat()
        .statusCode(200).extract().asString();

    // splitting the results on the line basis so as to parse the results for json string
    // and byte data.
    // Rest assured doesnt support parsing of Multipart response, So we have to manually
    // parse the results.
    String[] results = actual.split("\n");

    int position = 0;
    String jsonString = "";
    String actualContent = "";

    for (String part : results) {
      if (part.contains(MediaType.APPLICATION_JSON)) {

        jsonString = results[position + 4].trim();

      }
      if (part.contains(spadeDocument.getMimeType())) {

        actualContent = results[position + 4].trim();
      }
      position++;
    }

    ObjectMapper mapper = new ObjectMapper();
    SpadeDocumentDto actualDto = mapper.readValue(jsonString, SpadeDocumentDto.class);

    Assert.assertEquals(actualDto, documentDto);
    Assert.assertEquals(actualContent, expectedContent);
    verify(documentManager).getSpadeDocument(documentDto.getDocumentId());
    verify(dataManager).getDocsData(spadeDocument.getDataId());

  }

  @Test
  public void testGetSpadeDocumentWithWrongId() throws Exception {
    // test
    SpadeDocument spadeDocument = getFixture(SpadeDocument.class);
    SpadeDocumentDto documentDto = mapDto(spadeDocument, SpadeDocumentDto.class);

    doReturn(null).when(documentManager)
        .getSpadeDocument(documentDto.getDocumentId());

    given()
        .pathParam("id", documentDto.getDocumentId())
        .accept("multipart/form-data")
        .when()
        .get(getBaseUrl() + "/v1/spade/documents/{id}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(documentManager).getSpadeDocument(documentDto.getDocumentId());
    verify(dataManager, never()).getDocsData(anyInt());

  }
}

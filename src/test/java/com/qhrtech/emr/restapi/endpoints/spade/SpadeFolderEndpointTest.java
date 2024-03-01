
package com.qhrtech.emr.restapi.endpoints.spade;

import static io.restassured.RestAssured.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accdocs.model.docs.SpadeDocument;
import com.qhrtech.emr.accdocs.model.docs.SpadeFolder;
import com.qhrtech.emr.accuro.api.docs.SpadeDocumentManager;
import com.qhrtech.emr.accuro.api.docs.SpadeFolderManager;
import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.spade.SpadeDocumentDto;
import com.qhrtech.emr.restapi.models.dto.spade.SpadeFolderDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

public class SpadeFolderEndpointTest extends AbstractEndpointTest<SpadeFolderEndpoint> {

  private AccuroPreferenceManager preferenceManager;
  private SpadeFolderManager spadeFolderManager;
  private SpadeDocumentManager spadeDocumentManager;
  private AuditLogUser user;
  private ApiSecurityContext context;

  public SpadeFolderEndpointTest() {
    super(new SpadeFolderEndpoint(), SpadeFolderEndpoint.class);
    preferenceManager = mock(AccuroPreferenceManager.class);
    spadeFolderManager = mock(SpadeFolderManager.class);
    spadeDocumentManager = mock(SpadeDocumentManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = getFixture(AuditLogUser.class);
    context.setUser(user);
    context.setOauthClientId(TestUtilities.nextString(5));
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> map = new HashMap<>();
    map.put(SpadeFolderManager.class, spadeFolderManager);
    map.put(SpadeDocumentManager.class, spadeDocumentManager);
    return map;
  }

  @Test
  public void getSpadeFoldersTest() throws ProtossException {

    List<SpadeFolderDto> spadeFolderDtos = getFixtures(SpadeFolderDto.class, ArrayList::new, 5);
    List<SpadeFolder> spadeFolders;
    spadeFolders = mapDto(spadeFolderDtos, SpadeFolder.class, ArrayList::new);
    when(spadeFolderManager.getAllSpadeFolders()).thenReturn(spadeFolders);

    List<SpadeFolderDto> actual = toCollection(given()
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/spade/folders")
        .then()
        .assertThat().statusCode(200)
        .extract().as(SpadeFolderDto[].class),
        ArrayList::new);

    Assert.assertEquals(spadeFolderDtos, actual);
  }

  @Test
  public void getSpadeFolderTest() throws ProtossException {

    SpadeFolderDto spadeFolderDto = getFixture(SpadeFolderDto.class);
    SpadeFolder spadeFolder;
    spadeFolder = mapDto(spadeFolderDto, SpadeFolder.class);
    when(spadeFolderManager.getSpadeFolder(spadeFolder.getId())).thenReturn(spadeFolder);

    SpadeFolderDto actual = given()
        .when()
        .contentType(ContentType.JSON)
        .with().pathParam("folderId", spadeFolder.getId())
        .get(getBaseUrl() + "/v1/spade/folders/{folderId}")
        .then()
        .assertThat().statusCode(200)
        .extract().as(SpadeFolderDto.class);

    Assert.assertEquals(spadeFolderDto, actual);
  }

  @Test
  public void testGetSpadeFolderNotExists() throws ProtossException {

    int folderId = RandomUtils.nextInt();
    when(spadeFolderManager.getSpadeFolder(folderId)).thenReturn(null);

    given()
        .when()
        .contentType(ContentType.JSON)
        .with().pathParam("folderId", folderId)
        .get(getBaseUrl() + "/v1/spade/folders/{folderId}")
        .then()
        .assertThat().statusCode(404)
        .extract().as(SpadeFolderDto.class);
  }

  @Test
  public void getSpadeDocumentTest() throws ProtossException {

    SpadeFolderDto spadeFolderDto = getFixture(SpadeFolderDto.class);
    SpadeFolder spadeFolder = mapDto(spadeFolderDto, SpadeFolder.class);
    SpadeDocument spadeDocument = getFixture(SpadeDocument.class);

    when(spadeDocumentManager.getSpadeDocumentListByFolder(spadeFolder.getId()))
        .thenReturn(Collections.singleton(spadeDocument));
    when(spadeFolderManager.getSpadeFolder(spadeFolder.getId())).thenReturn(spadeFolder);
    SpadeDocumentDto spadeDocumentDto = mapDto(spadeDocument, SpadeDocumentDto.class);

    Set<SpadeDocumentDto> actual = toCollection(given()
        .when()
        .contentType(ContentType.JSON)
        .with().pathParam("folderId", spadeFolder.getId())
        .get(getBaseUrl() + "/v1/spade/folders/{folderId}/documents")
        .then()
        .assertThat().statusCode(200)
        .extract().as(SpadeDocumentDto[].class), HashSet::new);

    Assert.assertEquals(Collections.singleton(spadeDocumentDto), actual);
    verify(spadeDocumentManager).getSpadeDocumentListByFolder(spadeFolder.getId());
    verify(spadeFolderManager).getSpadeFolder(spadeFolder.getId());
  }

  @Test
  public void getSpadeDocumentWithWrongFolderTest() throws ProtossException {

    int folderId = TestUtilities.nextInt();
    when(spadeFolderManager.getSpadeFolder(folderId)).thenReturn(null);

    given()
        .when()
        .contentType(ContentType.JSON)
        .with().pathParam("folderId", folderId)
        .get(getBaseUrl() + "/v1/spade/folders/{folderId}/documents")
        .then()
        .assertThat().statusCode(404);

    verify(spadeDocumentManager, never()).getSpadeDocumentListByFolder(anyInt());
    verify(spadeFolderManager).getSpadeFolder(folderId);
  }

  @Test
  public void getSpadeDocumentWithNoDocumentTest() throws ProtossException {

    SpadeFolderDto spadeFolderDto = getFixture(SpadeFolderDto.class);
    SpadeFolder spadeFolder = mapDto(spadeFolderDto, SpadeFolder.class);

    when(spadeDocumentManager.getSpadeDocumentListByFolder(spadeFolder.getId()))
        .thenReturn(null);
    when(spadeFolderManager.getSpadeFolder(spadeFolder.getId())).thenReturn(spadeFolder);

    given()
        .when()
        .contentType(ContentType.JSON)
        .with().pathParam("folderId", spadeFolder.getId())
        .get(getBaseUrl() + "/v1/spade/folders/{folderId}/documents")
        .then()
        .assertThat().statusCode(404);
    verify(spadeDocumentManager).getSpadeDocumentListByFolder(spadeFolder.getId());
    verify(spadeFolderManager).getSpadeFolder(spadeFolder.getId());
  }

  @Test
  public void deleteSpadeFolderTest() throws ProtossException {

    SpadeFolderDto spadeFolderDto = getFixture(SpadeFolderDto.class);
    SpadeFolder spadeFolder;
    spadeFolder = mapDto(spadeFolderDto, SpadeFolder.class);
    given()
        .when()
        .contentType(ContentType.JSON)
        .with().pathParam("folderId", spadeFolder.getId())
        .delete(getBaseUrl() + "/v1/spade/folders/{folderId}")
        .then()
        .assertThat().statusCode(204);

    verify(spadeFolderManager).deleteSpadeFolder(spadeFolder.getId(), user);
  }

  @Test
  public void updateSpadeFolderTest() throws ProtossException {

    SpadeFolderDto spadeFolderDto = getFixture(SpadeFolderDto.class);
    SpadeFolder spadeFolder;
    spadeFolder = mapDto(spadeFolderDto, SpadeFolder.class);

    given()
        .pathParam("id", spadeFolderDto.getId())
        .body(spadeFolderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/spade/folders/{id}")
        .then()
        .assertThat().statusCode(204);

    verify(spadeFolderManager).updateSpadeFolder(spadeFolder, user);
  }


  @Test
  public void updateSpadeFolderWithWrongResourceTest() throws ProtossException {

    SpadeFolderDto spadeFolderDto = getFixture(SpadeFolderDto.class);
    SpadeFolder spadeFolder;
    spadeFolder = mapDto(spadeFolderDto, SpadeFolder.class);

    given()
        .pathParam("id", TestUtilities.nextInt())
        .body(spadeFolderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/spade/folders/{id}")
        .then()
        .assertThat().statusCode(400);

    verify(spadeFolderManager, never()).updateSpadeFolder(spadeFolder, user);
  }

  @Test
  public void testUpdateSpadeFolderNotMatch()
      throws ProtossException {

    SpadeFolderDto spadeFolderDto = getFixture(SpadeFolderDto.class);
    int spadeFolderId = spadeFolderDto.getId() + 1;

    given()
        .body(spadeFolderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/spade/folders/" + spadeFolderId)
        .then()
        .assertThat().statusCode(400);

    verify(spadeFolderManager, never()).updateSpadeFolder(any(SpadeFolder.class),
        any(AuditLogUser.class));
  }

  @Test
  public void createSpadeFolderTest() throws ProtossException {

    SpadeFolderDto spadeFolderDto = getFixture(SpadeFolderDto.class);
    SpadeFolder spadeFolder;
    spadeFolder = mapDto(spadeFolderDto, SpadeFolder.class);

    given()
        .body(spadeFolderDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/spade/folders")
        .then()
        .assertThat().statusCode(200);

    verify(spadeFolderManager).createSpadeFolder(spadeFolder, user);
  }
}

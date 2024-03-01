
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.docs.FolderTypeManager;
import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.model.docs.FolderType;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.FolderDto;
import com.qhrtech.emr.restapi.models.dto.SubFolderDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;


public class FolderEndpointTest extends AbstractEndpointTest<FolderEndpoint> {

  private AccuroPreferenceManager preferenceManager;
  private FolderTypeManager folderTypeManager;
  private AuditLogUser user;

  public FolderEndpointTest() {
    super(new FolderEndpoint(), FolderEndpoint.class);
    preferenceManager = mock(AccuroPreferenceManager.class);
    folderTypeManager = mock(FolderTypeManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    ApiSecurityContext context = new ApiSecurityContext();
    context.setUser(user);
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> map = new HashMap<>();
    map.put(AccuroPreferenceManager.class, preferenceManager);
    map.put(FolderTypeManager.class, folderTypeManager);
    return map;

  }

  @Test
  public void getFoldersTest() throws ProtossException {

    FolderType folderType = getFixture(FolderType.class);
    FolderType subType = getFixture(FolderType.class);

    Map<FolderType, Set<FolderType>> folders = new HashMap<>();
    folders.put(folderType, Collections.singleton(subType));

    String folderName = TestUtilities.nextString(10);
    when(folderTypeManager.getAllFolderRelationsByFolderName(folderName)).thenReturn(folders);

    Set<FolderDto> expected = new HashSet<>();

    for (FolderType folder : folders.keySet()) {
      Set<SubFolderDto> folderSubTypes = new HashSet<>();
      FolderDto folderDto = mapDto(folder, FolderDto.class);
      for (FolderType folderSubType : folders.get(folder)) {

        if (folderSubType.getId() != null) {
          SubFolderDto subFolderDto = mapDto(folderSubType, SubFolderDto.class);
          folderSubTypes.add(subFolderDto);
        }
      }

      folderDto.setSubFolders(folderSubTypes);
      expected.add(folderDto);
    }

    Set<FolderDto> actual = toCollection(given()
        .when()
        .contentType(ContentType.JSON)
        .queryParam("folderName", folderName)
        .get(getBaseUrl() + "/v1/provider-portal/folders")
        .then()
        .assertThat().statusCode(200)
        .extract().as(FolderDto[].class),
        HashSet::new);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getFolderByIdTest() throws ProtossException {

    FolderType folderType = getFixture(FolderType.class);
    FolderType subType = getFixture(FolderType.class);

    Map<FolderType, Set<FolderType>> folders = new HashMap<>();
    folders.put(folderType, Collections.singleton(subType));

    when(folderTypeManager.getFolderRelationsByFolderId(folderType.getId()))
        .thenReturn(folders);

    FolderDto expected = new FolderDto();
    for (FolderType folder : folders.keySet()) {
      Set<SubFolderDto> folderSubTypes = new HashSet<>();
      expected = mapDto(folder, FolderDto.class);
      for (FolderType folderSubType : folders.get(folder)) {

        if (folderSubType.getId() != null) {
          SubFolderDto subFolderDto = mapDto(folderSubType, SubFolderDto.class);
          folderSubTypes.add(subFolderDto);
        }
      }
      expected.setSubFolders(folderSubTypes);

    }

    FolderDto actual = given()
        .when()
        .pathParam("folderId", folderType.getId())
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/folders/{folderId}")
        .then()
        .assertThat().statusCode(200)
        .extract().as(FolderDto.class);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getFolderByIdNotFoundTest() throws ProtossException {

    FolderType folderType = getFixture(FolderType.class);

    when(folderTypeManager.getFolderRelationsByFolderId(folderType.getId()))
        .thenReturn(Collections.emptyMap());

    given()
        .when()
        .pathParam("folderId", folderType.getId())
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/folders/{folderId}")
        .then()
        .assertThat().statusCode(404);
  }

  @Test
  public void getSubFoldersTest() throws ProtossException {
    FolderType subType = getFixture(FolderType.class);

    when(folderTypeManager.getSubFolders()).thenReturn(Collections.singleton(subType));
    when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
        .thenReturn("true");
    Set<SubFolderDto> expected = new HashSet<>();
    for (FolderType folder : Collections.singleton(subType)) {
      SubFolderDto folderTypeDto = mapDto(folder, SubFolderDto.class);
      expected.add(folderTypeDto);
    }

    Set<SubFolderDto> actual = toCollection(given()
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/folders/sub-folders")
        .then()
        .assertThat().statusCode(200)
        .extract().as(SubFolderDto[].class),
        HashSet::new);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getSubTypesByIdTest() throws ProtossException {
    FolderType subType = new FolderType();
    subType.setName(TestUtilities.nextString(5));
    subType.setId(TestUtilities.nextInt());

    Mockito.when(folderTypeManager.getSubFolderById(subType.getId())).thenReturn(subType);
    Mockito.when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
        .thenReturn("true");


    SubFolderDto actual = given()
        .when()
        .pathParam("subFolderId", subType.getId())
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/folders/sub-folders/{subFolderId}")
        .then()
        .assertThat().statusCode(200)
        .extract().as(SubFolderDto.class);
    SubFolderDto expected = mapDto(subType, SubFolderDto.class);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getSubFolderByIdNotFoundTest() throws ProtossException {
    FolderType subType = getFixture(FolderType.class);

    when(folderTypeManager.getSubFolderById(subType.getId())).thenReturn(null);

    given()
        .when()
        .pathParam("subFolderId", subType.getId())
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + "/v1/provider-portal/folders/sub-folders/{subFolderId}")
        .then()
        .assertThat().statusCode(404);
  }

  @Test
  public void createFolderTest()
      throws ProtossException {

    FolderDto folderDto = new FolderDto();
    folderDto.setName(TestUtilities.nextString(10));
    Integer expected = TestUtilities.nextInt();

    Set<FolderType> folderSubTypes = new HashSet<>();
    when(folderTypeManager.createFolder(folderDto.getName(),
        folderSubTypes, user)).thenReturn(expected);

    Integer actual = given()
        .body(folderDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/folders")
        .then()
        .assertThat().statusCode(200)
        .extract().as(Integer.class);

    Assert.assertEquals(expected, actual);

  }

  @Test
  public void createFolderWithSubTypesTest()
      throws ProtossException {

    Set<FolderType> protossSubTypes = getFixtures(FolderType.class, HashSet::new, 5);
    Set<SubFolderDto> subFolders = mapDto(protossSubTypes, SubFolderDto.class, HashSet::new);

    FolderDto folderDto = new FolderDto();
    folderDto.setName(TestUtilities.nextString(10));

    folderDto.setSubFolders(subFolders);

    int expected = TestUtilities.nextInt();

    when(folderTypeManager.createFolder(folderDto.getName(),
        protossSubTypes, user)).thenReturn(expected);

    int actual = given()
        .body(folderDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/folders")
        .then()
        .assertThat().statusCode(200)
        .extract().as(Integer.class);

    Assert.assertEquals(expected, actual);

  }

  @Test
  public void updateFolderTest()
      throws ProtossException {

    FolderDto folderDto = new FolderDto();
    folderDto.setName(TestUtilities.nextString(10));
    folderDto.setId(Math.abs(TestUtilities.nextInt()));
    FolderType folder = mapDto(folderDto, FolderType.class);

    Set<FolderType> folderSubTypes = new HashSet<>();

    given()
        .body(folderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/folders/" + folderDto.getId())
        .then()
        .assertThat().statusCode(200);

    verify(folderTypeManager).updateFolderRelations(folder, folderSubTypes, user);

  }

  @Test
  public void updateFolderTestWithNullFolder() throws Exception {
    int id = TestUtilities.nextId();
    given()
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/folders/" + id)
        .then()
        .assertThat().statusCode(400);

    verify(folderTypeManager, never()).updateFolderRelations(
        any(FolderType.class),
        anySetOf(FolderType.class),
        any(AuditLogUser.class));
  }

  @Test
  public void updateFolderTestWithNegativeId() throws Exception {
    FolderDto folderDto = getFixture(FolderDto.class);
    int id = Math.abs(TestUtilities.nextId()) * (-1);
    given()
        .body(folderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/folders/" + id)
        .then()
        .assertThat().statusCode(400);

    verify(folderTypeManager, never()).updateFolderRelations(
        any(FolderType.class),
        anySetOf(FolderType.class),
        any(AuditLogUser.class));
  }

  @Test
  public void updateFolderWithSubTypesTest()
      throws ProtossException {

    FolderDto folderDto = new FolderDto();
    folderDto.setName(TestUtilities.nextString(10));
    folderDto.setId(Math.abs(TestUtilities.nextInt()));

    Set<FolderType> protossSubTypes = getFixtures(FolderType.class, HashSet::new, 5);
    Set<SubFolderDto> subFolders = mapDto(protossSubTypes, SubFolderDto.class, HashSet::new);

    folderDto.setSubFolders(subFolders);
    FolderType folder = mapDto(folderDto, FolderType.class);

    Set<FolderType> folderSubTypes = new HashSet<>();
    if (!(folderDto.getSubFolders() == null)) {
      folderSubTypes =
          folderDto.getSubFolders().stream().map(x -> mapDto(x, FolderType.class))
              .collect(Collectors.toSet());
    }

    given()
        .body(folderDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/folders/" + folderDto.getId())
        .then()
        .assertThat().statusCode(200);

    verify(folderTypeManager).updateFolderRelations(folder, folderSubTypes, user);

  }

  @Test
  public void deleteFolderTest() throws ProtossException {

    int folderId = TestUtilities.nextInt();
    when(folderTypeManager.getFolderById(folderId)).thenReturn(getFixture(FolderType.class));

    given()
        .pathParam("folderId", folderId)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/folders/{folderId}")
        .then()
        .assertThat().statusCode(200);

    verify(folderTypeManager).deleteFolder(folderId, user);


  }

  @Test
  public void createFolderSubTypeTest()
      throws ProtossException {

    SubFolderDto subFolderDto = new SubFolderDto();
    subFolderDto.setName(TestUtilities.nextString(10));
    Integer expected = TestUtilities.nextInt();
    when(folderTypeManager.createSubFolder(subFolderDto.getName(), user))
        .thenReturn(expected);
    when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
        .thenReturn("true");

    Integer actual = given()
        .body(subFolderDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/folders/sub-folders")
        .then()
        .assertThat().statusCode(200)
        .extract().as(Integer.class);

    Assert.assertEquals(actual, expected);

  }

  @Test
  public void createFolderSubTypeWithoutPreferenceEnabledTest()
      throws ProtossException {

    SubFolderDto subFolderDto = new SubFolderDto();
    subFolderDto.setName(TestUtilities.nextString(10));
    Integer expected = TestUtilities.nextInt();
    when(folderTypeManager.createSubFolder(subFolderDto.getName(), user))
        .thenReturn(expected);
    when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
        .thenReturn("false");

    given()
        .body(subFolderDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/folders/sub-folders")
        .then()
        .assertThat().statusCode(401);

    verify(folderTypeManager, never()).createSubFolder(subFolderDto.getName(), user);

  }

  @Test
  public void createFolderSubTypeWithoutFolderNameTest()
      throws ProtossException {

    SubFolderDto subFolderDto = new SubFolderDto();
    subFolderDto.setName(null);

    given()
        .body(subFolderDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/folders/sub-folders")
        .then()
        .assertThat().statusCode(400);

    verify(folderTypeManager, never()).createSubFolder(subFolderDto.getName(), user);

  }

  @Test
  public void deleteFolderSubTypeTest()
      throws ProtossException {

    int subFolderId = TestUtilities.nextInt();
    when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
        .thenReturn("true");

    when(folderTypeManager.getSubFolderById(anyInt())).thenReturn(getFixture(FolderType.class));

    given()
        .pathParam("subFolderId", subFolderId)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/folders/sub-folders/{subFolderId}")
        .then()
        .assertThat().statusCode(200);

    verify(folderTypeManager).deleteSubFolder(subFolderId, user);

  }

  @Test
  public void deleteFolderSubTypeWithPreferenceDisabledTest()
      throws ProtossException {

    int subFolderId = TestUtilities.nextInt();
    when(preferenceManager.getSystemPreference("ShowDocumentsSubType"))
        .thenReturn("false");

    given()
        .pathParam("subFolderId", subFolderId)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/folders/sub-folders/{subFolderId}")
        .then()
        .assertThat().statusCode(401);

    verify(folderTypeManager, never()).deleteSubFolder(subFolderId, user);

  }

}

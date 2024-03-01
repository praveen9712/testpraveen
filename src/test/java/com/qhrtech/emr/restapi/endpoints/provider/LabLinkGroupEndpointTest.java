
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.labs.LabLinkGroupManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.labs.LabLinkGroup;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.LabLinkGroupDto;
import com.qhrtech.emr.restapi.models.dto.LabLinkGroupReadOnlyDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class LabLinkGroupEndpointTest extends AbstractEndpointTest<LabLinkGroupEndpoint> {

  ApiSecurityContext context;
  private LabLinkGroupManager labManagerMock;

  public LabLinkGroupEndpointTest() {
    super(new LabLinkGroupEndpoint(), LabLinkGroupEndpoint.class);

    labManagerMock = mock(LabLinkGroupManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(LabLinkGroupManager.class, labManagerMock);
    return servicesMap;
  }

  @Test
  public void testGetAll() throws ProtossException {
    // set up data
    Set<LabLinkGroup> protossResult = getFixtures(LabLinkGroup.class, HashSet::new, 5);

    Set<LabLinkGroupReadOnlyDto> expected =
        mapDto(protossResult, LabLinkGroupReadOnlyDto.class, HashSet::new);
    // mock
    when(labManagerMock.getAll()).thenReturn(protossResult);

    // test
    Set<LabLinkGroupReadOnlyDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/lab-link-groups")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LabLinkGroupReadOnlyDto[].class),
        HashSet::new);

    // assert
    assertEquals(expected, actual);
    verify(labManagerMock).getAll();

  }

  @Test
  public void testGetByGroupId() throws ProtossException {
    // set up data
    LabLinkGroup protossResult = getFixture(LabLinkGroup.class);
    LabLinkGroupReadOnlyDto expected = mapDto(protossResult, LabLinkGroupReadOnlyDto.class);
    int groupId = protossResult.getLinkGroupId();
    // mock
    when(labManagerMock.getByGroupId(groupId)).thenReturn(protossResult);

    // test
    LabLinkGroupReadOnlyDto actual = given()
        .pathParam("groupId", groupId)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/lab-link-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(LabLinkGroupReadOnlyDto.class);

    // assert
    assertEquals(expected, actual);
    verify(labManagerMock).getByGroupId(groupId);

  }

  @Test
  public void testCreateLabLinkGroup() throws ProtossException {

    LabLinkGroupDto labLinkGroupDto = getLabLinkGroup();
    when(labManagerMock.createResultLinks(
        labLinkGroupDto.getPrimaryResultId(),
        labLinkGroupDto.getLinkedLabResultIds())).thenReturn(labLinkGroupDto.getLinkGroupId());

    int actual = given()
        .body(labLinkGroupDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/lab-link-groups")
        .then()
        .assertThat()
        .statusCode(200)
        .extract().as(Integer.class);

    assertEquals(labLinkGroupDto.getLinkGroupId(), actual);
    verify(labManagerMock).createResultLinks(labLinkGroupDto.getPrimaryResultId(),
        labLinkGroupDto.getLinkedLabResultIds());

  }

  @Test
  public void testCreateLabLinkGroupEmptySet() {

    LabLinkGroupDto labLinkGroupDto = getLabLinkGroup();
    labLinkGroupDto.setLinkedLabResultIds(new HashSet<>());

    given()
        .body(labLinkGroupDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/lab-link-groups")
        .then()
        .assertThat()
        .statusCode(400);

    verifyZeroInteractions(labManagerMock);

  }

  @Test
  public void testUpdateLabLinkGroupEmptySet() {

    LabLinkGroupDto labLinkGroupDto = getLabLinkGroup();
    labLinkGroupDto.setLinkedLabResultIds(new HashSet<>());

    given()
        .body(labLinkGroupDto)
        .pathParam("groupId", labLinkGroupDto.getLinkGroupId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/lab-link-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400);

    verifyZeroInteractions(labManagerMock);

  }

  @Test
  public void testUpdateLabLinkGroup() throws ProtossException {
    LabLinkGroupDto labLinkGroupDto = getLabLinkGroup();
    doNothing().when(labManagerMock).updateResultLinks(labLinkGroupDto.getLinkGroupId(),
        labLinkGroupDto.getPrimaryResultId(), labLinkGroupDto.getLinkedLabResultIds());

    // test
    given()
        .body(labLinkGroupDto)
        .pathParam("groupId", labLinkGroupDto.getLinkGroupId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/lab-link-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(200);

    verify(labManagerMock).updateResultLinks(labLinkGroupDto.getLinkGroupId(),
        labLinkGroupDto.getPrimaryResultId(), labLinkGroupDto.getLinkedLabResultIds());
  }

  @Test
  public void testUpdateLabLinkGroupPathParamMismatch() throws ProtossException {
    LabLinkGroupDto labLinkGroupDto = getLabLinkGroup();

    int randomGroupId = labLinkGroupDto.getLinkGroupId() + RandomUtils.nextInt();
    // test
    given()
        .body(labLinkGroupDto)
        .pathParam("groupId", randomGroupId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/lab-link-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(400);

    verifyZeroInteractions(labManagerMock);
  }



  @Test
  public void testDeleteLabLinkGroup() throws ProtossException {
    int groupId = TestUtilities.nextInt();

    doNothing().when(labManagerMock).deleteResultLinks(groupId);

    given()
        .pathParam("groupId", groupId)
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/lab-link-groups/{groupId}")
        .then()
        .assertThat()
        .statusCode(200);

    verify(labManagerMock).deleteResultLinks(groupId);
  }


  private LabLinkGroupDto getLabLinkGroup() {
    int primaryId = TestUtilities.nextInt();
    Set<Integer> resultIds = getFixtures(Integer.class, HashSet::new, 3);
    resultIds.add(primaryId);
    LabLinkGroupDto dto = new LabLinkGroupDto();
    dto.setPrimaryResultId(primaryId);
    dto.setLinkedLabResultIds(resultIds);
    int groupId = TestUtilities.nextInt();
    dto.setLinkGroupId(groupId);

    return dto;
  }
}

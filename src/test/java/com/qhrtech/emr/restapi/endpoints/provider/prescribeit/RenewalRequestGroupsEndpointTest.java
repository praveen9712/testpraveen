
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestGroupManager;
import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestManager;
import com.qhrtech.emr.accuro.model.prescribeit.RenewalRequest;
import com.qhrtech.emr.accuro.model.prescribeit.RenewalRequestGroup;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.RenewalRequestGroupDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class RenewalRequestGroupsEndpointTest
    extends AbstractEndpointTest<RenewalRequestGroupsEndpoint> {

  private final RenewalRequestGroupManager manager;
  private final RenewalRequestManager requestManager;

  public RenewalRequestGroupsEndpointTest() {
    super(new RenewalRequestGroupsEndpoint(), RenewalRequestGroupsEndpoint.class);
    manager = mock(RenewalRequestGroupManager.class);
    requestManager = mock(RenewalRequestManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(getFixture(AuditLogUser.class));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> services = new HashMap<>();
    services.put(RenewalRequestGroupManager.class, manager);
    services.put(RenewalRequestManager.class, requestManager);
    return services;
  }

  @Test
  public void testGetById() throws Exception {
    RenewalRequestGroup requestGroup = getFixture(RenewalRequestGroup.class);

    doReturn(requestGroup).when(manager).getRenewalRequestGroupById(requestGroup.getId());

    RenewalRequestGroupDto expected = mapDto(requestGroup, RenewalRequestGroupDto.class);

    RenewalRequestGroupDto actual =
        given()
            .pathParam("id", requestGroup.getId())
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/renewal-request-groups/{id}")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(RenewalRequestGroupDto.class);
    expected.setCreatedDateUtc(actual.getCreatedDateUtc());

    assertEquals(expected, actual);
  }

  @Test
  public void testCreateRenewalRequestGroup() throws Exception {
    RenewalRequestGroup requestGroup = getFixture(RenewalRequestGroup.class);

    doReturn(requestGroup.getId()).when(manager)
        .create(any(RenewalRequestGroup.class));

    RenewalRequestGroupDto renewalRequestGroupDto =
        mapDto(requestGroup, RenewalRequestGroupDto.class);
    int expected = requestGroup.getId();

    int actual =
        given()
            .body(renewalRequestGroupDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/renewal-request-groups")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(Integer.class);

    assertEquals(expected, actual);
  }

  @Test
  public void testCreateRenewalRequestGroupWithNoObject() throws Exception {
    RenewalRequestGroup requestGroup = getFixture(RenewalRequestGroup.class);

    RenewalRequestGroupDto renewalRequestGroupDto =
        mapDto(requestGroup, RenewalRequestGroupDto.class);


    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/renewal-request-groups")
        .then()
        .assertThat()
        .statusCode(400);


  }

  @Test
  public void testUpdateRenewalRequestGroup() throws Exception {
    RenewalRequestGroupDto requestGroupDto = getFixture(RenewalRequestGroupDto.class);
    requestGroupDto.setCreatedDateUtc(null);

    RenewalRequestGroup renewalRequestGroup = mapDto(requestGroupDto, RenewalRequestGroup.class);


    given()
        .pathParam("id", requestGroupDto.getId())
        .body(requestGroupDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/renewal-request-groups/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).update(renewalRequestGroup);

  }

  @Test
  public void testUpdateRenewalRequestGroupWithNoObject() throws Exception {
    RenewalRequestGroupDto requestGroupDto = getFixture(RenewalRequestGroupDto.class);

    given()
        .pathParam("id", requestGroupDto.getId())
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/renewal-request-groups/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

  }

  @Test
  public void testUpdateRenewalRequestGroupWithWrongResourceId() throws Exception {
    RenewalRequestGroupDto requestGroupDto = getFixture(RenewalRequestGroupDto.class);
    given()
        .pathParam("id", TestUtilities.nextId())
        .body(requestGroupDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/renewal-request-groups/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

  }

  @Test
  public void testDeleteRenewalRequestGroup() throws Exception {
    RenewalRequestGroupDto requestGroupDto = getFixture(RenewalRequestGroupDto.class);
    requestGroupDto.setCreatedDateUtc(null);

    RenewalRequestGroup renewalRequestGroup = mapDto(requestGroupDto, RenewalRequestGroup.class);

    doReturn(new ArrayList()).when(requestManager)
        .getRenewalRequestsByRenewalRequestGroupId(requestGroupDto.getId());

    given()
        .pathParam("id", requestGroupDto.getId())
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/renewal-request-groups/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).delete(requestGroupDto.getId());

  }

  @Test
  public void testDeleteRenewalRequestGroupInUse() throws Exception {
    RenewalRequestGroupDto requestGroupDto = getFixture(RenewalRequestGroupDto.class);
    requestGroupDto.setCreatedDateUtc(null);

    RenewalRequestGroup renewalRequestGroup = mapDto(requestGroupDto, RenewalRequestGroup.class);

    List<RenewalRequest> existingRequest = new ArrayList<>();
    existingRequest.add(new RenewalRequest());
    doReturn(existingRequest).when(requestManager)
        .getRenewalRequestsByRenewalRequestGroupId(requestGroupDto.getId());

    given()
        .pathParam("id", requestGroupDto.getId())
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + "/v1/provider-portal/renewal-request-groups/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(manager, never()).delete(requestGroupDto.getId());

  }

}

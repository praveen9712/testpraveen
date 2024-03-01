
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestManager;
import com.qhrtech.emr.accuro.model.prescribeit.RenewalRequest;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.RenewalRequestDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class RenewalRequestEndpointTest
    extends AbstractEndpointTest<RenewalRequestEndpoint> {

  private RenewalRequestManager requestManager;
  private AuditLogUser user;

  public RenewalRequestEndpointTest() {
    super(new RenewalRequestEndpoint(), RenewalRequestEndpoint.class);
    requestManager = mock(RenewalRequestManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(RenewalRequestManager.class, requestManager);
    return servicesMap;
  }

  @Test
  public void testGetRenewalRequestById()
      throws Exception {
    RenewalRequestDto expected = getFixture(RenewalRequestDto.class);

    int requestId = expected.getId();
    RenewalRequest protossRequest = mapDto(expected, RenewalRequest.class);

    when(requestManager.getRenewalRequestById(requestId)).thenReturn(protossRequest);

    RenewalRequestDto actual = given()
        .when()
        .pathParam("id", requestId)
        .get(getBaseUrl()
            + "/v1/provider-portal/renewal-requests/{id}")
        .then()
        .assertThat().statusCode(200)
        .extract().as(RenewalRequestDto.class);

    assertEquals(expected, actual);
    verify(requestManager).getRenewalRequestById(requestId);

  }

  @Test
  public void testCreateRequest() throws Exception {
    RenewalRequestDto requestDto = getFixture(RenewalRequestDto.class);

    RenewalRequest protossModel = mapDto(requestDto, RenewalRequest.class);

    doReturn(requestDto.getId()).when(requestManager).create(protossModel);

    int id = given()
        .body(requestDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/renewal-requests")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(id == requestDto.getId());
    verify(requestManager).create(protossModel);
  }

  @Test
  public void testDeleteRenewalRequestById()
      throws Exception {

    int id = TestUtilities.nextId();

    given()
        .when()
        .pathParam("id", id)
        .delete(getBaseUrl()
            + "/v1/provider-portal/renewal-requests/{id}")
        .then()
        .assertThat().statusCode(204);

    verify(requestManager).delete(id);
  }

}

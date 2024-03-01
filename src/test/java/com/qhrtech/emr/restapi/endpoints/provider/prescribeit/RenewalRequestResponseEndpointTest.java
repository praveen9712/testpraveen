
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestResponseManager;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.accuro.model.prescribeit.RenewalRequestResponse;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.RenewalRequestResponseDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class RenewalRequestResponseEndpointTest
    extends AbstractEndpointTest<RenewalRequestResponseEndpoint> {

  private RenewalRequestResponseManager manager;
  private AuditLogUser user;
  private ApiSecurityContext context;

  public RenewalRequestResponseEndpointTest() {
    super(new RenewalRequestResponseEndpoint(), RenewalRequestResponseEndpoint.class);
    manager = mock(RenewalRequestResponseManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    context = new ApiSecurityContext();
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
    servicesMap.put(RenewalRequestResponseManager.class, manager);
    return servicesMap;
  }

  @Test
  public void testGetRenewalResponseById()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException,
      InsufficientRolesException {
    RenewalRequestResponseDto expected = getFixture(RenewalRequestResponseDto.class);

    int responseID = expected.getId();
    RenewalRequestResponse protossResponse = mapDto(expected, RenewalRequestResponse.class);

    when(manager.getById(responseID)).thenReturn(protossResponse);

    RenewalRequestResponseDto actual = given()
        .when()
        .pathParam("responseId", responseID)
        .get(getBaseUrl()
            + "/v1/provider-portal/renewal-request-responses/{responseId}")
        .then()
        .assertThat().statusCode(200)
        .extract().as(RenewalRequestResponseDto.class);

    assertEquals(expected, actual);
    verify(manager).getById(responseID);
  }

  @Test
  public void testGetRenewalResponseByIdNotFound()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException,
      InsufficientRolesException {

    int responseID = TestUtilities.nextInt();

    when(manager.getById(responseID)).thenReturn(null);

    given()
        .pathParam("responseId", responseID)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/renewal-request-responses/{responseId}")
        .then()
        .assertThat()
        .statusCode(404);

    verify(manager).getById(responseID);

  }

  @Test
  public void testCreate() throws SupportingResourceNotFoundException, TimeZoneNotFoundException,
      UnsupportedSchemaVersionException, DatabaseInteractionException, ForbiddenException {

    RenewalRequestResponseDto responseDto = getFixture(RenewalRequestResponseDto.class);

    RenewalRequestResponse protossModel = mapDto(responseDto, RenewalRequestResponse.class);
    protossModel.setCreatedById(user.getUserId());

    doReturn(responseDto.getId()).when(manager).create(protossModel);
    when(manager.create(protossModel)).thenReturn(responseDto.getId());
    int id = given()
        .body(responseDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/renewal-request-responses")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(id == responseDto.getId());
    verify(manager).create(protossModel);
  }

  @Test
  public void testCreateWithNullCreatedById()
      throws SupportingResourceNotFoundException, TimeZoneNotFoundException,
      UnsupportedSchemaVersionException, DatabaseInteractionException, ForbiddenException {
    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));
    RenewalRequestResponseDto responseDto = getFixture(RenewalRequestResponseDto.class);

    RenewalRequestResponse protossModel = mapDto(responseDto, RenewalRequestResponse.class);

    doReturn(responseDto.getId()).when(manager).create(protossModel);
    when(manager.create(protossModel)).thenReturn(responseDto.getId());
    int id = given()
        .body(responseDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/renewal-request-responses")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertTrue(id == responseDto.getId());

    verify(manager).create(protossModel);
  }

  @Test
  public void testCreateWithNullClientUserId()
      throws SupportingResourceNotFoundException, TimeZoneNotFoundException,
      UnsupportedSchemaVersionException, DatabaseInteractionException, ForbiddenException {

    context.setUser(new AuditLogUser(null, TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10)));
    RenewalRequestResponseDto responseDto = getFixture(RenewalRequestResponseDto.class);
    responseDto.setCreatedById(null);

    RenewalRequestResponse protossModel = mapDto(responseDto, RenewalRequestResponse.class);

    doReturn(responseDto.getId()).when(manager).create(protossModel);
    when(manager.create(protossModel)).thenReturn(responseDto.getId());
    given()
        .body(responseDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/renewal-request-responses")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());


    verify(manager, never()).create(protossModel);
  }
}

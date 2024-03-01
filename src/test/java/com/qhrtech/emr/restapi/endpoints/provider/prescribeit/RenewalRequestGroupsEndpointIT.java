
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import com.qhrtech.emr.accuro.api.prescribeit.DefaultRenewalRequestGroupManager;
import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestGroupManager;
import com.qhrtech.emr.accuro.db.prescribeit.RenewalRequestGroupFixture;
import com.qhrtech.emr.accuro.model.prescribeit.RenewalRequestGroup;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.RenewalRequestGroupDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class RenewalRequestGroupsEndpointIT extends
    AbstractEndpointIntegrationTest<RenewalRequestGroupsEndpoint> {

  public static final String commonUrl = "/v1/provider-portal/renewal-request-groups";
  private RenewalRequestGroupManager requestGroupManager;

  public RenewalRequestGroupsEndpointIT() throws IOException {
    super(new RenewalRequestGroupsEndpoint(), RenewalRequestGroupsEndpoint.class);
    requestGroupManager = new DefaultRenewalRequestGroupManager(ds, null, defaultUser());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(RenewalRequestGroupManager.class, requestGroupManager);
  }

  @Test
  public void testGetRenewalRequestGroupById() throws Exception {

    RenewalRequestGroup requestGroup = new RenewalRequestGroup();

    try (Connection conn = getConnection()) {
      RenewalRequestGroupFixture fixture = new RenewalRequestGroupFixture();
      fixture.setUp(conn);
      requestGroup = fixture.get();
    } catch (Exception e) {
      fail("Fail to create new Renewal request group");
    }

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

    RenewalRequestGroupDto expected = mapDto(requestGroup, RenewalRequestGroupDto.class);
    expected.setCreatedDateUtc(actual.getCreatedDateUtc());

    assertEquals(expected, actual);
    requestGroupManager.delete(actual.getId());
  }

  @Test
  public void testCreateRenewalRequestGroup() throws Exception {
    RenewalRequestGroup requestGroup = new RenewalRequestGroup();

    try (Connection conn = getConnection()) {
      RenewalRequestGroupFixture fixture = new RenewalRequestGroupFixture();
      fixture.setUp(conn);
      requestGroup = fixture.get();
    } catch (Exception e) {
      fail("Fail to create new Renewal request group");
    }

    RenewalRequestGroupDto expected = mapDto(requestGroup, RenewalRequestGroupDto.class);
    expected.setMatchedPharmacyContactId(null);

    int id =
        given()
            .body(expected)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/renewal-request-groups")
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(Integer.class);
    RenewalRequestGroup renewalRequestGroupById =
        requestGroupManager.getRenewalRequestGroupById(id);

    RenewalRequestGroupDto actual = mapDto(renewalRequestGroupById, RenewalRequestGroupDto.class);
    expected.setCreatedDateUtc(actual.getCreatedDateUtc());
    expected.setId(id);

    assertEquals(expected, actual);
    requestGroupManager.delete(actual.getId());
  }


  @Test
  public void testUpdateRenewalRequestGroup() throws Exception {
    RenewalRequestGroup requestGroup = new RenewalRequestGroup();

    try (Connection conn = getConnection()) {
      RenewalRequestGroupFixture fixture = new RenewalRequestGroupFixture();
      fixture.setUp(conn);
      requestGroup = fixture.get();
    } catch (Exception e) {
      fail("Fail to create new Renewal request group");
    }

    RenewalRequestGroupDto expected = mapDto(requestGroup, RenewalRequestGroupDto.class);
    expected.setPharmacyAddressLine2(TestUtilities.nextString(10));
    expected.setMatchedPharmacyContactId(null);


    given()
        .pathParam("id", expected.getId())
        .body(expected)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/renewal-request-groups/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());
    RenewalRequestGroup renewalRequestGroupById =
        requestGroupManager.getRenewalRequestGroupById(expected.getId());

    RenewalRequestGroupDto actual = mapDto(renewalRequestGroupById, RenewalRequestGroupDto.class);
    expected.setCreatedDateUtc(actual.getCreatedDateUtc());

    assertEquals(expected, actual);
    requestGroupManager.delete(actual.getId());

  }
}


package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.qhrtech.emr.accuro.api.provider.DefaultProviderManager;
import com.qhrtech.emr.accuro.api.provider.ProviderManager;
import com.qhrtech.emr.accuro.api.security.DefaultProviderPermissionManager;
import com.qhrtech.emr.accuro.api.security.ProviderPermissionManager;
import com.qhrtech.emr.accuro.db.Fixture;
import com.qhrtech.emr.accuro.db.office.OfficeFixture;
import com.qhrtech.emr.accuro.db.provider.ProviderFixture;
import com.qhrtech.emr.accuro.model.demographics.Office;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.provider.Provider;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.ProviderDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class ProviderEndpointIT extends AbstractEndpointIntegrationTest<ProviderEndpoint> {

  private ProviderManager manager;
  private ProviderPermissionManager providerPermissionManager;

  public ProviderEndpointIT() throws IOException {
    super(new ProviderEndpoint(), ProviderEndpoint.class);
    manager = new DefaultProviderManager(getDs(), getSecurityContext().getAuthorizationContext(),
        defaultUser());
    providerPermissionManager = new DefaultProviderPermissionManager(getDs(),
        getSecurityContext().getAuthorizationContext(),
        defaultUser());
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> managerMap = new HashMap<>();
    managerMap.put(ProviderManager.class, manager);
    managerMap.put(ProviderPermissionManager.class, providerPermissionManager);
    return managerMap;
  }

  @Test
  public void testCreate() throws UnsupportedSchemaVersionException, DatabaseInteractionException {

    ProviderDto providerDto = new ProviderDto();

    providerDto.setFirstName(TestUtilities.nextString(18));
    providerDto.setLastName(TestUtilities.nextString(18));
    providerDto.setTypeId(1);

    int providerId = given()
        .body(providerDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/providers")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    Provider actual = manager.getProviderById(providerId);
    assertEquals(providerId, actual.getProviderId());
  }

  @Test
  public void testAddProviderToOffices()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, SQLException {

    ProviderFixture providerFixture = new ProviderFixture();
    providerFixture.setUp(getConnection());

    int providerId = providerFixture.get().getProviderId();
    List<Integer> officeIds = new ArrayList<>();
    Fixture<Office> testOffice1 = new OfficeFixture();
    testOffice1.setUp(getConnection());

    officeIds.add(testOffice1.get().getOfficeId());
    given()
        .body(officeIds)
        .pathParam("providerId", providerId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/providers/{providerId}/offices")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    Set<Provider> actual = manager.getProvidersForOffice(testOffice1.get().getOfficeId());
    assertTrue(actual.contains(manager.getProviderById(providerId)));
  }

  @Test
  public void testDisableProviderVisibility()
      throws UnsupportedSchemaVersionException, SQLException {

    ProviderFixture providerFixture = new ProviderFixture();
    providerFixture.setUp(getConnection());

    int providerId = providerFixture.get().getProviderId();
    given()
        .pathParam("providerId", providerId)
        .when()
        .put(getBaseUrl() + "/v1/providers/{providerId}/disable")
        .then()
        .assertThat()
        .statusCode(204);
  }
}


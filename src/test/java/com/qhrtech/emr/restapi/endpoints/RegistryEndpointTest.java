
package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.person.RelationshipStatusManager;
import com.qhrtech.emr.accuro.api.registry.RegistryEntryManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.registry.RegistryEntry;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.RegistryEntryDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class RegistryEndpointTest
    extends AbstractEndpointTest<RegistryEndpoint> {


  private RegistryEntryManager manager;

  public RegistryEndpointTest() {
    super(new RegistryEndpoint(), RegistryEndpoint.class);
    manager = mock(RegistryEntryManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return Collections.singletonMap(RegistryEntryManager.class, manager);
  }

  @Test
  public void testGetAllEntries() throws ProtossException {
    List<RegistryEntry> protossResults =
        getFixtures(RegistryEntry.class, ArrayList::new, 2);
    List<RegistryEntryDto> expected =
        mapDto(protossResults, RegistryEntryDto.class, ArrayList::new);
    // mock dependencies
    when(manager.getEntries(null, null)).thenReturn(protossResults);

    List<RegistryEntryDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/registry-entries")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(RegistryEntryDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(manager).getEntries(null, null);
  }

  @Test
  public void testGetParticularRegistry() throws ProtossException {
    RegistryEntry protossResult =
        getFixture(RegistryEntry.class);
    List<RegistryEntry> protossResults = Collections.singletonList(protossResult);
    List<RegistryEntryDto> expected =
        mapDto(protossResults, RegistryEntryDto.class, ArrayList::new);
    String registryType = expected.stream().findFirst().get().getType().name();
    // mock dependencies
    when(manager.getEntries(protossResult.getType(), null)).thenReturn(protossResults);

    List<RegistryEntryDto> actual = toCollection(
        given()
            .queryParam("registryType", registryType)
            .when()
            .get(getBaseUrl() + "/v1/registry-entries")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(RegistryEntryDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(manager).getEntries(protossResult.getType(), null);
  }

  @Test
  public void testGetParticularRegistryAndId() throws ProtossException {
    RegistryEntry protossResult =
        getFixture(RegistryEntry.class);
    List<RegistryEntry> protossResults = Collections.singletonList(protossResult);
    List<RegistryEntryDto> expected =
        mapDto(protossResults, RegistryEntryDto.class, ArrayList::new);
    String registryType = expected.stream().findFirst().get().getType().name();
    // mock dependencies
    when(manager.getEntries(protossResult.getType(), protossResult.getId()))
        .thenReturn(protossResults);

    List<RegistryEntryDto> actual = toCollection(
        given()
            .queryParam("registryType", registryType)
            .queryParam("id", String.valueOf(protossResult.getId()))
            .when()
            .get(getBaseUrl() + "/v1/registry-entries")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(RegistryEntryDto[].class),
        ArrayList::new);

    assertEquals(expected, actual);
    verify(manager).getEntries(protossResult.getType(), protossResult.getId());
  }

}

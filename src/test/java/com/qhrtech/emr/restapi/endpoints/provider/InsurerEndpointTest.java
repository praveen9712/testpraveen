
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.demographics.InsurerManager;
import com.qhrtech.emr.accuro.model.demographics.Insurer;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.InsurerDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class InsurerEndpointTest extends AbstractEndpointTest<InsurerEndpoint> {
  private final InsurerManager insurerManager;


  public InsurerEndpointTest() {
    super(new InsurerEndpoint(), InsurerEndpoint.class);
    insurerManager = mock(InsurerManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(InsurerManager.class, insurerManager);
    return servicesMap;
  }

  @Test
  public void testGetInsurers() throws Exception {
    // setup random data
    ArrayList<Integer> insurerIds = new ArrayList<>();
    List<Insurer> protossResults = new ArrayList<>();
    insurerIds.add(TestUtilities.nextInt());
    insurerIds.add(TestUtilities.nextInt());
    // mock dependencies
    when(insurerManager.getInsurersByIds(insurerIds)).thenReturn(protossResults);
    Set<InsurerDto> expected = mapDto(protossResults, InsurerDto.class, HashSet::new);

    // test
    Set<InsurerDto> actual = toCollection(
        given()
            .body(insurerIds)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/provider-portal/insurers")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(InsurerDto[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(insurerManager).getInsurersByIds(insurerIds);
  }

  @Test
  public void testGetInsurersNull() throws Exception {
    // test
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/provider-portal/insurers")
        .then()
        .assertThat()
        .statusCode(400);
    verify(insurerManager, never()).getInsurersByIds(anyList());
  }

  @Test
  public void testGetInsureById() throws Exception {
    // setup random data
    Insurer expected = getFixture(Insurer.class);
    int insurerId = expected.getInsurerId();
    // mock dependencies
    when(insurerManager.getInsurerById(insurerId)).thenReturn(expected);
    InsurerDto expectedDto = mapDto(expected, InsurerDto.class);

    // test
    InsurerDto actualDto =
        given().pathParam("insurerId", insurerId)
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/insurers/{insurerId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(InsurerDto.class);
    Assert.assertEquals(expectedDto, actualDto);
    verify(insurerManager).getInsurerById(insurerId);
  }

  @Test
  public void testGetAllInsurers() throws Exception {
    // setup random data
    Set<Insurer> insurers = getFixtures(Insurer.class, HashSet::new, 5);
    Set<InsurerDto> expected = mapDto(insurers, InsurerDto.class, HashSet::new);

    // mock dependencies
    when(insurerManager.getAllInsurers()).thenReturn(insurers);

    // test
    Set<InsurerDto> actual = toCollection(
        given()
            .when()
            .contentType(ContentType.JSON)
            .get(getBaseUrl() + "/v1/provider-portal/insurers")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(InsurerDto[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(insurerManager).getAllInsurers();
  }
}


package com.qhrtech.emr.restapi.endpoints;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qhrtech.emr.accuro.api.provider.ProviderManager;
import com.qhrtech.emr.accuro.api.security.ProviderPermissionManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.pagination.Envelope;
import com.qhrtech.emr.accuro.model.provider.Provider;
import com.qhrtech.emr.accuro.model.provider.ProviderIdentifier;
import com.qhrtech.emr.accuro.model.provider.ProviderType;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.ProviderDto;
import com.qhrtech.emr.restapi.models.dto.ProviderIdentifierDto;
import com.qhrtech.emr.restapi.models.dto.ProviderTypeDto;
import com.qhrtech.emr.restapi.models.dto.pagination.EnvelopeDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.util.PaginationConstant;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class ProviderEndpointTest extends AbstractEndpointTest<ProviderEndpoint> {

  private static final int DEFAULT_PAGE_SIZE = PaginationConstant.DEFAULT_PAGE_SIZE.getSize();
  private static final int MAX_PAGE_SIZE = PaginationConstant.MAX_PAGE_SIZE.getSize();
  private ProviderManager providerManager;
  private final ProviderPermissionManager providerPermissionManager;

  public ProviderEndpointTest() {
    super(new ProviderEndpoint(), ProviderEndpoint.class);
    providerManager = mock(ProviderManager.class);
    providerPermissionManager = mock(ProviderPermissionManager.class);
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
    managerMap.put(ProviderManager.class, providerManager);
    managerMap.put(ProviderPermissionManager.class, providerPermissionManager);
    return managerMap;
  }

  @Test
  public void getProvidersTest() throws ProtossException {

    // setup random data
    Set<Provider> protossResults =
        getFixtures(Provider.class, HashSet::new, 5);

    when(providerManager.getProviders()).thenReturn(protossResults);

    Set<ProviderDto> expected =
        mapDto(protossResults, ProviderDto.class, HashSet::new);

    // test
    Set<ProviderDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/providers")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(ProviderDto[].class),
        HashSet::new);

    assertEquals(actual, expected);
    verify(providerManager).getProviders();

  }

  @Test
  public void getAllOfficeProvidersTest() throws ProtossException {

    // setup random data
    List<Provider> protossResults =
        getFixtures(Provider.class, ArrayList::new, 5);

    when(providerManager.getProvidersForClient()).thenReturn(protossResults);

    Set<ProviderDto> expected =
        mapDto(protossResults, ProviderDto.class, HashSet::new);

    // test
    Set<ProviderDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/providers/offices")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(ProviderDto[].class),
        HashSet::new);

    assertEquals(actual, expected);
    verify(providerManager).getProvidersForClient();

  }

  @Test
  public void getProvidersForOfficeTest() throws ProtossException {

    // setup random data
    Set<Provider> protossResults =
        getFixtures(Provider.class, HashSet::new, 5);

    int officeId = TestUtilities.nextId();
    protossResults.forEach(p -> {
      p.setDefaultOffice(officeId);
    });

    when(providerManager.getProvidersForOffice(officeId)).thenReturn(protossResults);

    Set<ProviderDto> expected =
        mapDto(protossResults, ProviderDto.class, HashSet::new);

    // test
    Set<ProviderDto> actual = toCollection(
        given().pathParam("officeId", officeId)
            .when()
            .get(getBaseUrl() + "/v1/providers/offices/{officeId}")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(ProviderDto[].class),
        HashSet::new);

    assertEquals(actual, expected);
    verify(providerManager).getProvidersForOffice(officeId);
  }


  @Test
  public void getProviderTypesTest() throws ProtossException {

    Set<ProviderType> providerTypes = getFixtures(ProviderType.class, HashSet::new, 3);

    when(providerManager.getProviderTypes()).thenReturn(providerTypes);

    Set<ProviderTypeDto> expectedProviderTypes =
        mapDto(providerTypes, ProviderTypeDto.class, HashSet::new);

    Set<ProviderTypeDto> actualProviderTypes = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/providers/types")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(ProviderTypeDto[].class),
        HashSet::new);

    assertEquals(actualProviderTypes, expectedProviderTypes);
    verify(providerManager).getProviderTypes();
  }

  @Test
  public void getProviderStatusesTest() throws ProtossException {

    Map<Integer, String> expectedStatus = new HashMap<>();

    expectedStatus.put(TestUtilities.nextInt(), TestUtilities.nextString(10));

    when(providerManager.getProviderStatuses()).thenReturn(expectedStatus);

    Map<Integer, String> actualStatus =
        given()
            .when()
            .get(getBaseUrl() + "/v1/providers/statuses")
            .then()
            .assertThat().statusCode(200).extract().as(expectedStatus.getClass());

    assertEquals(expectedStatus.toString(), actualStatus.toString());
    verify(providerManager).getProviderStatuses();
  }

  @Test
  public void getSpecialtiesTest() throws ProtossException {

    Map<String, String> expectedSpeciality = new HashMap<>();

    IntStream.range(0, 5).forEach(i -> expectedSpeciality.put(TestUtilities.nextString(10),
        TestUtilities.nextString(10)));

    when(providerManager.getSpecialtyCodes()).thenReturn(expectedSpeciality);
    Map<Integer, String> actualSpeciality =
        given()
            .when()
            .get(getBaseUrl() + "/v1/providers/specialties")
            .then()
            .assertThat().statusCode(200).extract().as(expectedSpeciality.getClass());

    assertEquals(expectedSpeciality, actualSpeciality);
    verify(providerManager).getSpecialtyCodes();
  }

  @Test
  public void getProviderById() throws ProtossException {
    Provider protossResult = getFixture(Provider.class);
    int providerId = TestUtilities.nextInt();

    when(providerManager.getProviderById(providerId)).thenReturn(protossResult);

    ProviderDto expected =
        mapDto(protossResult, ProviderDto.class);

    // test
    ProviderDto actual =
        given().pathParams("providerId", providerId)
            .when()
            .get(getBaseUrl() + "/v1/providers/{providerId}")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .as(ProviderDto.class);

    assertEquals(actual, expected);
    verify(providerManager).getProviderById(providerId);
  }

  @Test
  public void createProvider() throws ProtossException {
    ProviderDto providerDto = getFixture(ProviderDto.class);
    int providerId = providerDto.getProviderId();

    Provider provider =
        mapDto(providerDto, Provider.class);

    doReturn(providerId).when(providerManager).createProvider(provider);

    // test
    int actual =
        given()
            .body(providerDto)
            .when()
            .contentType(ContentType.JSON)
            .post(getBaseUrl() + "/v1/providers")
            .then()
            .assertThat()
            .statusCode(Status.CREATED.getStatusCode())
            .extract().as(Integer.class);

    assertEquals(providerId, actual);
    verify(providerManager).createProvider(provider);
  }


  @Test
  public void createProviderWithNullNames() throws ProtossException {
    ProviderDto providerDto = getFixture(ProviderDto.class);
    int providerId = providerDto.getProviderId();
    providerDto.setLastName("");

    Provider provider =
        mapDto(providerDto, Provider.class);

    doReturn(providerId).when(providerManager).createProvider(provider);

    // test

    given()
        .body(providerDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/providers")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

  }

  @Test
  public void createProviderWithNullProvider() {

    // test
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + "/v1/providers")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

  }

  @Test
  public void addProviderToOffices() throws ProtossException {

    List<Integer> officeIds = new ArrayList<>();

    officeIds.add(10);
    officeIds.add(11);

    int providerId = TestUtilities.nextInt();

    // test
    given()
        .pathParam("providerId", providerId)
        .body(officeIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/providers/{providerId}/offices")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(providerManager).addProviderToMultipleOffices(providerId, officeIds);
  }

  @Test
  public void addProviderToOfficesWithEmptyOfficeList() throws ProtossException {

    List<Integer> officeIds = new ArrayList<>();

    officeIds.add(null);
    officeIds.add(null);

    int providerId = TestUtilities.nextInt();

    // test
    given()
        .pathParam("providerId", providerId)
        .body(officeIds)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/providers/{providerId}/offices")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

  }

  @Test
  public void getProviderSpecialtiesTest() throws ProtossException {

    Map<String, String> protossMap = new HashMap<>();
    IntStream.range(0, 5).forEach(i -> protossMap.put(TestUtilities.nextString(5),
        TestUtilities.nextString(10)));
    when(providerManager.getSpecialtyCodes()).thenReturn(protossMap);

    int providerId = TestUtilities.nextInt();
    List<String> protossSpecialites = new ArrayList<>();
    Optional<String> result = protossMap.keySet().stream().findFirst();
    protossSpecialites.add(result.get());
    when(providerManager.getSpecialtyCodesForProvider(providerId)).thenReturn(protossSpecialites);

    Map<String, String> expectedMap = protossMap;
    expectedMap.keySet().retainAll(protossSpecialites);

    Map<String, String> actualMap;
    actualMap =
        given().pathParams("providerId", providerId)
            .when()
            .get(getBaseUrl() + "/v1/providers/{providerId}/specialties")
            .then()
            .assertThat().statusCode(200).extract().as(expectedMap.getClass());

    assertEquals(expectedMap, actualMap);

    verify(providerManager).getSpecialtyCodes();
    verify(providerManager).getSpecialtyCodesForProvider(providerId);

  }

  @Test
  public void getProviderIdentifier() throws ProtossException {

    int providerId = TestUtilities.nextInt();
    List<ProviderIdentifier> protossResults =
        getFixtures(ProviderIdentifier.class, ArrayList::new, 1);
    List<ProviderIdentifierDto> expected =
        mapDto(protossResults, ProviderIdentifierDto.class, ArrayList::new);

    when(providerManager.getProviderIdentifiers(providerId)).thenReturn(protossResults);

    List<ProviderIdentifierDto> actual = toCollection(
        given().pathParams("providerId", providerId)
            .when()
            .get(getBaseUrl() + "/v1/providers/{providerId}/identifiers")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(ProviderIdentifierDto[].class),
        ArrayList::new);

    verify(providerManager).getProviderIdentifiers(providerId);

  }

  @Test
  public void testGetProvider()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    Envelope<Provider> protossEnvelope = getProtossEnvelope();
    String firstName = TestUtilities.nextString(256);
    String lastName = TestUtilities.nextString(256);
    int startingId =
        getProtossEnvelope().getContents().stream()
            .min(Comparator.comparing(Provider::getProviderId))
            .orElseThrow(RuntimeException::new).getProviderId();
    doReturn(protossEnvelope).when(providerManager)
        .searchProviders(firstName, lastName, firstName, startingId - 1, DEFAULT_PAGE_SIZE);

    // test
    EnvelopeDto rawActual = given()
        .when()
        .queryParam("firstName", firstName)
        .queryParam("lastName", lastName)
        .queryParam("globalSearch", firstName)
        .queryParam("pageSize", 25)
        .queryParam("startingId", startingId - 1)
        .get(getBaseUrl() + "/v1/providers/search")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<ProviderDto> actual = refType(rawActual);
    assertEquals(getExpectedEnvelope(protossEnvelope), actual);
    verify(providerManager).searchProviders(firstName, lastName, firstName, startingId - 1,
        DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetProvidersByMaxPageSize()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    Envelope<Provider> protossEnvelope = getProtossEnvelope();
    String firstName = TestUtilities.nextString(256);
    String lastName = TestUtilities.nextString(256);
    int pageSize = TestUtilities.nextInt(200, 1000);
    int startingId =
        getProtossEnvelope().getContents().stream()
            .min(Comparator.comparing(Provider::getProviderId))
            .orElseThrow(RuntimeException::new).getProviderId();
    doReturn(protossEnvelope).when(providerManager)
        .searchProviders(firstName, lastName, null, startingId - 1, MAX_PAGE_SIZE);

    // test
    EnvelopeDto rawActual = given()
        .when()
        .queryParam("firstName", firstName)
        .queryParam("lastName", lastName)
        .queryParam("startingId", startingId - 1)
        .queryParam("pageSize", pageSize)
        .get(getBaseUrl() + "/v1/providers/search")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<ProviderDto> actual = refType(rawActual);
    assertEquals(getExpectedEnvelope(protossEnvelope), actual);
    verify(providerManager).searchProviders(firstName, lastName, null, startingId - 1,
        MAX_PAGE_SIZE);
  }

  @Test
  public void testGetProvidersByPageSizeAndStartingIdNull()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    Envelope<Provider> protossEnvelope = getProtossEnvelope();
    String firstName = TestUtilities.nextString(2);
    String lastName = TestUtilities.nextString(2);

    doReturn(protossEnvelope).when(providerManager)
        .searchProviders(firstName, lastName, null, null, DEFAULT_PAGE_SIZE);

    // test
    EnvelopeDto rawActual = given()
        .when()
        .queryParam("firstName", firstName)
        .queryParam("lastName", lastName)
        .get(getBaseUrl() + "/v1/providers/search")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<ProviderDto> actual = refType(rawActual);
    assertEquals(getExpectedEnvelope(protossEnvelope), actual);
    verify(providerManager).searchProviders(firstName, lastName, null, null,
        DEFAULT_PAGE_SIZE);
  }

  @Test
  public void testGetProvidersByGlobalSearch()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    Envelope<Provider> protossEnvelope = getProtossEnvelope();
    String firstName = TestUtilities.nextString(2);

    doReturn(protossEnvelope).when(providerManager)
        .searchProviders(null, null, firstName, null, DEFAULT_PAGE_SIZE);

    // test
    EnvelopeDto rawActual = given()
        .when()
        .queryParam("globalSearch", firstName)
        .get(getBaseUrl() + "/v1/providers/search")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<ProviderDto> actual = refType(rawActual);
    assertEquals(getExpectedEnvelope(protossEnvelope), actual);
    verify(providerManager).searchProviders(null, null, firstName, null, DEFAULT_PAGE_SIZE);

  }

  @Test
  public void testGetProvidersByGlobalSearchWithStartingId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    Envelope<Provider> protossEnvelope = getProtossEnvelope();
    String firstName = TestUtilities.nextString(2);
    int startingId =
        getProtossEnvelope().getContents().stream()
            .min(Comparator.comparing(Provider::getProviderId))
            .orElseThrow(RuntimeException::new).getProviderId();
    doReturn(protossEnvelope).when(providerManager)
        .searchProviders(null, null, firstName, startingId - 1, DEFAULT_PAGE_SIZE);

    // test
    EnvelopeDto rawActual = given()
        .when()
        .queryParam("globalSearch", firstName)
        .queryParam("startingId", startingId - 1)
        .get(getBaseUrl() + "/v1/providers/search")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode()).extract()
        .as(EnvelopeDto.class);

    // assertions
    EnvelopeDto<ProviderDto> actual = refType(rawActual);
    assertEquals(getExpectedEnvelope(protossEnvelope), actual);
    verify(providerManager).searchProviders(null, null, firstName, startingId - 1,
        DEFAULT_PAGE_SIZE);

  }

  private EnvelopeDto<ProviderDto> refType(EnvelopeDto rawEnvelope) {
    List<ProviderDto> contents = getObjectMapper().convertValue(rawEnvelope.getContents(),
        new TypeReference<>() {});
    EnvelopeDto<ProviderDto> e = (EnvelopeDto<ProviderDto>) rawEnvelope;
    e.setContents(contents);
    return e;
  }

  private Envelope<Provider> getProtossEnvelope() {
    List<Provider> protossResult = getFixtures(Provider.class, ArrayList::new, 10);
    Envelope<Provider> envelope = new Envelope<>();
    long lastId = protossResult.stream().max(Comparator.comparing(Provider::getProviderId))
        .orElseThrow(RuntimeException::new).getProviderId();
    envelope.setContents(protossResult);
    envelope.setCount(protossResult.size());
    envelope.setLastId(lastId);
    envelope.setTotal(protossResult.size());
    return envelope;
  }

  private EnvelopeDto<ProviderDto> getExpectedEnvelope(
      Envelope<Provider> protossEnvelope) {
    List<ProviderDto> providerDtos =
        mapDto(protossEnvelope.getContents(), ProviderDto.class,
            ArrayList::new);

    EnvelopeDto<ProviderDto> expected = new EnvelopeDto<>();

    expected.setContents(providerDtos);
    expected.setTotal(protossEnvelope.getTotal());
    expected.setCount(protossEnvelope.getCount());
    expected.setLastId(protossEnvelope.getLastId());

    return expected;
  }

  @Test
  public void testDisableProviderVisibility() throws Exception {
    Integer providerId = TestUtilities.nextInt(10);

    doNothing().when(providerPermissionManager).disableVisibilityForPhysician(providerId);

    given()
        .pathParam("providerId", providerId)
        .when()
        .put(getBaseUrl() + "/v1/providers/{providerId}/disable")
        .then()
        .assertThat()
        .statusCode(204);

    verify(providerPermissionManager).disableVisibilityForPhysician(providerId);
  }

  @Test
  public void testDisableProviderVisibilityInvalidProviderId() throws Exception {
    Integer providerId = 0 - Math.abs(TestUtilities.nextInt()) - 1;

    given()
        .pathParam("providerId", providerId)
        .when()
        .put(getBaseUrl() + "/v1/providers/{providerId}/disable")
        .then()
        .assertThat()
        .statusCode(400);
  }

}

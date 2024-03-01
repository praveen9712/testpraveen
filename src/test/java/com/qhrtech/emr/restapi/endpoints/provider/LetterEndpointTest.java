
package com.qhrtech.emr.restapi.endpoints.provider;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.letters.LetterManager;
import com.qhrtech.emr.accuro.model.letters.LetterType;
import com.qhrtech.emr.accuro.model.letters.NewPatientLetter;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.LetterTypeDto;
import com.qhrtech.emr.restapi.models.dto.NewPatientLetterDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.services.RtfConversionService;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class LetterEndpointTest extends AbstractEndpointTest<LetterEndpoint> {

  @Mock
  private RtfConversionService rtfService;
  private final LetterManager letterManager;
  private ApiSecurityContext context;
  private AuditLogUser user;

  public LetterEndpointTest() {
    super(new LetterEndpoint(), LetterEndpoint.class);
    letterManager = mock(LetterManager.class);
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    user = getFixture(AuditLogUser.class);
    context.setUser(user);
  }

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    super.setUp();
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {

    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(LetterManager.class, letterManager);
    return servicesMap;
  }

  @Test
  public void testGetActiveLetterTypes() throws Exception {
    // setup random data
    Set<LetterType> protossResults =
        getFixtures(LetterType.class, HashSet::new, 5);
    // mock dependencies
    when(letterManager.getLetterTypes()).thenReturn(protossResults);

    Set<LetterTypeDto> expected =
        mapDto(protossResults, LetterTypeDto.class, HashSet::new);

    // test
    Set<LetterTypeDto> actual = toCollection(
        given()
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/letters/types")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(LetterTypeDto[].class),
        HashSet::new);
    // assertions
    assertEquals(expected, actual);
    verify(letterManager).getLetterTypes();
  }


  @Test
  public void testCreatePatientLetterContent() throws Exception {

    Integer expectedId = RandomUtils.nextInt();
    when(letterManager.createPatientLetter(any(NewPatientLetter.class), any(AuditLogUser.class)))
        .thenReturn(expectedId);
    NewPatientLetterDto patientLetterDto = getFixture(NewPatientLetterDto.class);

    Integer actual = given()
        .body(patientLetterDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/letters")
        .then().assertThat()
        .statusCode(200).extract().as(Integer.class);

    NewPatientLetter patientLetter = mapDto(patientLetterDto, NewPatientLetter.class);
    Assert.assertEquals(expectedId, actual);
    verify(letterManager).createPatientLetter(patientLetter, user);
  }

  @Test
  public void testCreatePatientLetterNewPatientLetterDtoNull() throws Exception {

    Integer expectedId = RandomUtils.nextInt();
    when(letterManager.createPatientLetter(any(NewPatientLetter.class), anyInt()))
        .thenReturn(expectedId);

    given()
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/letters")
        .then().assertThat()
        .statusCode(400);
    verify(letterManager, never()).createPatientLetter(any(NewPatientLetter.class), anyInt());
  }

  @Test
  public void testCreatePatientLetterContentNull() throws Exception {

    Integer expectedId = RandomUtils.nextInt();
    when(letterManager.createPatientLetter(any(NewPatientLetter.class), anyInt()))
        .thenReturn(expectedId);
    NewPatientLetterDto dto = getFixture(NewPatientLetterDto.class);
    dto.setContentText(null);

    given()
        .body(dto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/letters")
        .then().assertThat()
        .statusCode(400);
    verify(letterManager, never()).createPatientLetter(any(NewPatientLetter.class), anyInt());
  }

  @Test
  public void testCreatePatientLetterTitleNull() throws Exception {

    Integer expectedId = RandomUtils.nextInt();
    when(letterManager.createPatientLetter(any(NewPatientLetter.class), anyInt()))
        .thenReturn(expectedId);
    NewPatientLetterDto dto = getFixture(NewPatientLetterDto.class);
    dto.setContentText(TestUtilities.nextString(10));
    dto.setTitle(null);

    given()
        .body(dto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + "/v1/provider-portal/letters")
        .then().assertThat()
        .statusCode(400);
    verify(letterManager, never()).createPatientLetter(any(NewPatientLetter.class), anyInt());
  }

}

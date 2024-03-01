
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.medications.LimitedUseCodeManager;
import com.qhrtech.emr.accuro.api.prescription.AnnotationManager;
import com.qhrtech.emr.accuro.api.prescription.DosageManager;
import com.qhrtech.emr.accuro.api.prescription.InteractionManagementDetailsManager;
import com.qhrtech.emr.accuro.api.prescription.InteractionManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionIndicationManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMacroManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.prescription.StatusHistoryManager;
import com.qhrtech.emr.accuro.api.prescription.WellnetPrescriptionLinkManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMacro;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionFavoriteDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionMedicationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class PrescriptionFavoriteEndpointTest
    extends AbstractEndpointTest<PrescriptionFavoriteEndpoint> {

  private final PrescriptionMacroManager prescriptionMacroManager;
  private final PrescriptionMedicationManager prescriptionMedicationManager;
  private final DosageManager dosageManager;
  private final PrescriptionIndicationManager prescriptionIndicationManager;
  private final AnnotationManager annotationManager;
  private final StatusHistoryManager statusHistoryManager;
  private final InteractionManager interactionManager;
  private final InteractionManagementDetailsManager interactionManagementDetailsManager;
  private final WellnetPrescriptionLinkManager wellnetPrescriptionLinkManager;
  private final LimitedUseCodeManager limitedUseCodeManager;
  private final SystemInformationManager systemInformationManager;

  public PrescriptionFavoriteEndpointTest() {
    super(new PrescriptionFavoriteEndpoint(), PrescriptionFavoriteEndpoint.class);
    prescriptionMacroManager = mock(PrescriptionMacroManager.class);
    prescriptionMedicationManager = mock(PrescriptionMedicationManager.class);
    dosageManager = mock(DosageManager.class);
    prescriptionIndicationManager = mock(PrescriptionIndicationManager.class);
    annotationManager = mock(AnnotationManager.class);
    statusHistoryManager = mock(StatusHistoryManager.class);
    interactionManager = mock(InteractionManager.class);
    interactionManagementDetailsManager = mock(InteractionManagementDetailsManager.class);
    wellnetPrescriptionLinkManager = mock(WellnetPrescriptionLinkManager.class);
    limitedUseCodeManager = mock(LimitedUseCodeManager.class);
    systemInformationManager = mock(SystemInformationManager.class);
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
    servicesMap.put(PrescriptionMacroManager.class, prescriptionMacroManager);
    servicesMap.put(PrescriptionMedicationManager.class, prescriptionMedicationManager);
    servicesMap.put(DosageManager.class, dosageManager);
    servicesMap.put(PrescriptionIndicationManager.class, prescriptionIndicationManager);
    servicesMap.put(AnnotationManager.class, annotationManager);
    servicesMap.put(StatusHistoryManager.class, statusHistoryManager);
    servicesMap.put(InteractionManager.class, interactionManager);
    servicesMap.put(InteractionManagementDetailsManager.class, interactionManagementDetailsManager);
    servicesMap.put(WellnetPrescriptionLinkManager.class, wellnetPrescriptionLinkManager);
    servicesMap.put(LimitedUseCodeManager.class, limitedUseCodeManager);
    servicesMap.put(SystemInformationManager.class, systemInformationManager);
    return servicesMap;
  }

  @Test
  public void testGetAll() throws Exception {
    // generate random data
    List<PrescriptionMacro> macros = getFixtures(PrescriptionMacro.class, ArrayList::new, 5);

    // mock dependency
    doReturn(macros).when(prescriptionMacroManager).getRxMacros();

    // set expected favorite
    List<PrescriptionFavoriteDto> expected =
        mapDto(macros, PrescriptionFavoriteDto.class, ArrayList::new);

    // test
    List<PrescriptionFavoriteDto> actual =
        toCollection(
            when()
                .get(getBaseUrl() + "/v1/provider-portal/prescription-favorites")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PrescriptionFavoriteDto[].class),
            ArrayList::new);

    // assertion
    assertEquals(expected, actual);

    // verify
    verify(prescriptionMacroManager).getRxMacros();
  }

  @Test
  public void testGetAllForEmptyReturn() throws Exception {
    // generate empty list as expected
    List<PrescriptionFavoriteDto> expected = Collections.emptyList();

    // test
    List<PrescriptionFavoriteDto> actual =
        toCollection(
            when()
                .get(getBaseUrl() + "/v1/provider-portal/prescription-favorites")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(PrescriptionFavoriteDto[].class),
            ArrayList::new);

    // assertion
    assertEquals(expected, actual);

    // verify
    verify(prescriptionMacroManager).getRxMacros();
  }

  @Test
  public void testGetById() throws Exception {
    // generate random data
    List<PrescriptionMacro> macros = getFixtures(PrescriptionMacro.class, ArrayList::new, 5);

    // mock dependency
    doReturn(macros).when(prescriptionMacroManager).getRxMacros();

    // set expected favorite
    List<PrescriptionFavoriteDto> favorites =
        mapDto(macros, PrescriptionFavoriteDto.class, ArrayList::new);

    PrescriptionFavoriteDto expected = favorites.get(TestUtilities.nextInt(favorites.size()));

    // test
    PrescriptionFavoriteDto actual =
        given().pathParam("favoriteId", expected.getId())
            .when()
            .get(getBaseUrl() + "/v1/provider-portal/prescription-favorites/{favoriteId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(PrescriptionFavoriteDto.class);

    // assertion
    assertEquals(expected, actual);

    // verify
    verify(prescriptionMacroManager).getRxMacros();
  }

  @Test
  public void testGetByIdNotFoundWithEmptyReturn() throws Exception {
    // test
    given().pathParam("favoriteId", TestUtilities.nextId())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/prescription-favorites/{favoriteId}")
        .then()
        .assertThat()
        .statusCode(404);

    // verify
    verify(prescriptionMacroManager).getRxMacros();
  }

  @Test
  public void testGetByIdNotFoundWithInvalidId() throws Exception {
    // generate random data
    List<PrescriptionMacro> macros = getFixtures(PrescriptionMacro.class, ArrayList::new, 5);

    // mock dependency
    doReturn(macros).when(prescriptionMacroManager).getRxMacros();

    // test
    given().pathParam("favoriteId", TestUtilities.nextId())
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/prescription-favorites/{favoriteId}")
        .then()
        .assertThat()
        .statusCode(404);

    // verify
    verify(prescriptionMacroManager).getRxMacros();
  }

  @Test
  public void testGetPrescriptionsByFavoriteId() throws Exception {
    // setup random data and mock dependency
    int favoriteId = TestUtilities.nextId();

    List<PrescriptionMedication> expectedRxFromProtoss =
        getFixtures(PrescriptionMedication.class, ArrayList::new, TestUtilities.nextInt(3));

    doReturn(expectedRxFromProtoss).when(prescriptionMedicationManager)
        .getByFavouriteId(favoriteId);

    // expected data
    List<PrescriptionMedicationDto> expected =
        mapDto(expectedRxFromProtoss, PrescriptionMedicationDto.class, ArrayList::new);

    // test
    List<PrescriptionMedicationDto> actual = toCollection(
        given()
            .pathParam("favoriteId", favoriteId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/prescription-favorites/{favoriteId}/prescriptions")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PrescriptionMedicationDto[].class),
        ArrayList::new);

    // assertions
    assertEquals(expected, actual);
    verify(prescriptionMedicationManager).getByFavouriteId(favoriteId);
  }

  @Test
  public void testGetPrescriptionByFavoriteId() throws Exception {
    // setup random data and mock dependency
    List<PrescriptionMacro> favorites = getFixtures(PrescriptionMacro.class, ArrayList::new, 3);
    doReturn(favorites).when(prescriptionMacroManager).getRxMacros();
    PrescriptionMacro favorite = favorites.get(TestUtilities.nextInt(3));
    favorite.setPrescriptionIds(new ArrayList<>());
    int favoriteId = favorite.getId();
    PrescriptionMedication rxFromProtoss = getFixture(PrescriptionMedication.class);

    List<PrescriptionMedication> expectedRxFromProtoss =
        getFixtures(PrescriptionMedication.class, ArrayList::new, TestUtilities.nextInt(3));
    expectedRxFromProtoss.add(rxFromProtoss);

    doReturn(expectedRxFromProtoss).when(prescriptionMedicationManager)
        .getByFavouriteId(favoriteId);

    int rxId = rxFromProtoss.getPrescriptionId();
    favorite.getPrescriptionIds().add(rxId);


    PrescriptionMedicationDto expect = mapDto(rxFromProtoss, PrescriptionMedicationDto.class);

    doReturn(rxFromProtoss).when(prescriptionMedicationManager).getById(rxId);

    PrescriptionMedicationDto actual =
        given()
            .pathParam("favoriteId", favoriteId)
            .pathParam("rxId", rxId)
            .when()
            .get(getBaseUrl()
                + "/v1/provider-portal/prescription-favorites/{favoriteId}/prescriptions/{rxId}")
            .then()
            .assertThat()
            .statusCode(200)
            .extract().as(PrescriptionMedicationDto.class);

    assertEquals(expect, actual);
  }

  @Test
  public void testGetPrescriptionByFavoriteIdWhenFavoriteDoesNotExist() {
    int favoriteId = TestUtilities.nextId();
    int rxId = TestUtilities.nextId();
    given()
        .pathParam("favoriteId", favoriteId)
        .pathParam("rxId", rxId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/prescription-favorites/{favoriteId}/prescriptions/{rxId}")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  public void testGetPrescriptionByFavoriteIdWhenProvidesWrongRxId() throws Exception {
    // setup random data and mock dependency
    List<PrescriptionMacro> favorites = getFixtures(PrescriptionMacro.class, ArrayList::new, 3);
    doReturn(favorites).when(prescriptionMacroManager).getRxMacros();
    PrescriptionMacro favorite = favorites.get(TestUtilities.nextInt(3));
    favorite.setPrescriptionIds(new ArrayList<>());
    int favoriteId = favorite.getId();

    int rxId = TestUtilities.nextId();
    given()
        .pathParam("favoriteId", favoriteId)
        .pathParam("rxId", rxId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/prescription-favorites/{favoriteId}/prescriptions/{rxId}")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  public void testGetPrescriptionByFavoriteIdWhenRxIdDoesNotExist() throws Exception {
    // setup random data and mock dependency
    List<PrescriptionMacro> favorites = getFixtures(PrescriptionMacro.class, ArrayList::new, 3);
    doReturn(favorites).when(prescriptionMacroManager).getRxMacros();
    PrescriptionMacro favorite = favorites.get(TestUtilities.nextInt(3));
    favorite.setPrescriptionIds(new ArrayList<>());
    int favoriteId = favorite.getId();

    int rxId = TestUtilities.nextId();
    favorite.getPrescriptionIds().add(rxId);

    given()
        .pathParam("favoriteId", favoriteId)
        .pathParam("rxId", rxId)
        .when()
        .get(getBaseUrl()
            + "/v1/provider-portal/prescription-favorites/{favoriteId}/prescriptions/{rxId}")
        .then()
        .assertThat()
        .statusCode(404);
  }
}

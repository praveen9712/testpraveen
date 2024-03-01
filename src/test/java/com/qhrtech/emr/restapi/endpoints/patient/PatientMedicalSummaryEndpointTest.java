
package com.qhrtech.emr.restapi.endpoints.patient;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.allergy.AllergyReactionManager;
import com.qhrtech.emr.accuro.api.allergy.NoKnownAllergyManager;
import com.qhrtech.emr.accuro.api.allergy.PatientAllergyManager;
import com.qhrtech.emr.accuro.api.codes.CodeSystemManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DiagnosisStatusManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientDiagnosisManager;
import com.qhrtech.emr.accuro.api.medications.AlternativeHealthProductManager;
import com.qhrtech.emr.accuro.api.medications.FormulationManager;
import com.qhrtech.emr.accuro.api.medications.GenericDrugManager;
import com.qhrtech.emr.accuro.api.medications.ManufacturedDrugManager;
import com.qhrtech.emr.accuro.api.medications.NaturalHealthProductManager;
import com.qhrtech.emr.accuro.api.prescription.DosageManager;
import com.qhrtech.emr.accuro.api.prescription.IngredientManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.security.AccuroApiContextManager;
import com.qhrtech.emr.accuro.api.security.UserSecurity;
import com.qhrtech.emr.accuro.db.RandomUtils;
import com.qhrtech.emr.accuro.model.allergy.AllergyReaction;
import com.qhrtech.emr.accuro.model.allergy.NoKnownAllergy;
import com.qhrtech.emr.accuro.model.allergy.PatientAllergy;
import com.qhrtech.emr.accuro.model.codes.CodeSystem;
import com.qhrtech.emr.accuro.model.codes.CodeValue;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.medicalhistory.DiagnosisStatus;
import com.qhrtech.emr.accuro.model.medicalhistory.PatientDiagnosis;
import com.qhrtech.emr.accuro.model.medications.AlternativeHealthProduct;
import com.qhrtech.emr.accuro.model.medications.Formulation;
import com.qhrtech.emr.accuro.model.medications.GenericDrug;
import com.qhrtech.emr.accuro.model.medications.ManufacturedDrug;
import com.qhrtech.emr.accuro.model.medications.MedicationType;
import com.qhrtech.emr.accuro.model.medications.NaturalHealthProduct;
import com.qhrtech.emr.accuro.model.prescription.Dosage;
import com.qhrtech.emr.accuro.model.prescription.Ingredient;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyReactionDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.DiagnosisStatusDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.EyeCode;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.AllergiesDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.AlternativeHealthProductSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.CustomCompoundSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.DiagnosisDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.DrugIdentifier;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.FormulationSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.IngredientSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.ManufacturedDrugSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.MedicationDetails;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.MedicationTypeApi;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.NaturalHealthProductSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.NoKnownAllergySummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.PatientAllergySummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.PatientMedicalSummaryDto;
import com.qhrtech.emr.restapi.models.dto.medicalsummary.PrescriptionDto;
import com.qhrtech.emr.restapi.models.dto.medications.GenericDrugDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.DosageDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.security.exceptions.PrescriptionConversionException;
import com.qhrtech.emr.restapi.services.PrescriptionDetailsService;
import com.qhrtech.emr.restapi.util.EmrUnitsMapping;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class PatientMedicalSummaryEndpointTest
    extends AbstractEndpointTest<PatientMedicalSummaryEndpoint> {

  private static final String TABLE_99P0108 = "99P0108";
  private static final String TABLE_99P0100 = "99P0100";
  private final PrescriptionDetailsService prescriptionDetailsService;
  private PrescriptionMedicationManager medicationManagerMock;
  private DosageManager dosageManagerMock;
  private PatientDiagnosisManager patientDiagnosisManagerMock;
  private DiagnosisStatusManager diagnosisStatusManagerMock;
  private PatientAllergyManager patientAllergyManagerMock;
  private AllergyReactionManager allergyReactionManagerMock;
  private NoKnownAllergyManager noKnownAllergyManagerMock;
  private ManufacturedDrugManager manufacturedDrugManagerMock;
  private GenericDrugManager genericDrugManagerMock;
  private FormulationManager formulationManagerMock;
  private NaturalHealthProductManager naturalHealthProductManagerMock;
  private AlternativeHealthProductManager alternativeHealthProductManagerMock;
  private IngredientManager ingredientManagerMock;
  private AccuroApiContextManager accuroApiContextManagerMock;
  private CodeSystemManager codeSystemManagerMock;
  private AuditLogUser user;


  public PatientMedicalSummaryEndpointTest() {
    super(new PatientMedicalSummaryEndpoint(), PatientMedicalSummaryEndpoint.class);

    medicationManagerMock = mock(PrescriptionMedicationManager.class);
    dosageManagerMock = mock(DosageManager.class);
    patientDiagnosisManagerMock = mock(PatientDiagnosisManager.class);
    diagnosisStatusManagerMock = mock(DiagnosisStatusManager.class);
    patientAllergyManagerMock = mock(PatientAllergyManager.class);
    allergyReactionManagerMock = mock(AllergyReactionManager.class);
    noKnownAllergyManagerMock = mock(NoKnownAllergyManager.class);
    manufacturedDrugManagerMock = mock(ManufacturedDrugManager.class);
    genericDrugManagerMock = mock(GenericDrugManager.class);
    formulationManagerMock = mock(FormulationManager.class);
    naturalHealthProductManagerMock = mock(NaturalHealthProductManager.class);
    alternativeHealthProductManagerMock = mock(AlternativeHealthProductManager.class);
    ingredientManagerMock = mock(IngredientManager.class);
    accuroApiContextManagerMock = mock(AccuroApiContextManager.class);
    prescriptionDetailsService = mock(PrescriptionDetailsService.class);
    codeSystemManagerMock = mock(CodeSystemManager.class);

    user = getFixture(AuditLogUser.class);
  }

  private static String formatFloat(float num) {
    if (num == (long) num) {
      return String.format("%d", (long) num);
    } else {
      return String.format("%s", num);
    }
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> serviceMap = new HashMap<>();
    serviceMap.put(PrescriptionMedicationManager.class, medicationManagerMock);
    serviceMap.put(DosageManager.class, dosageManagerMock);
    serviceMap.put(PatientDiagnosisManager.class, patientDiagnosisManagerMock);
    serviceMap.put(DiagnosisStatusManager.class, diagnosisStatusManagerMock);
    serviceMap.put(PatientAllergyManager.class, patientAllergyManagerMock);
    serviceMap.put(AllergyReactionManager.class, allergyReactionManagerMock);
    serviceMap.put(NoKnownAllergyManager.class, noKnownAllergyManagerMock);
    serviceMap.put(ManufacturedDrugManager.class, manufacturedDrugManagerMock);
    serviceMap.put(GenericDrugManager.class, genericDrugManagerMock);
    serviceMap.put(FormulationManager.class, formulationManagerMock);
    serviceMap.put(NaturalHealthProductManager.class, naturalHealthProductManagerMock);
    serviceMap.put(AlternativeHealthProductManager.class, alternativeHealthProductManagerMock);
    serviceMap.put(IngredientManager.class, ingredientManagerMock);
    serviceMap.put(AccuroApiContextManager.class, accuroApiContextManagerMock);
    serviceMap.put(CodeSystemManager.class, codeSystemManagerMock);

    return serviceMap;
  }

  @Test
  public void testGetPatientMedicalSummaryManufacturedDrug()
      throws Exception {

    int provider1 = TestUtilities.nextInt();
    int provider2 = TestUtilities.nextInt();

    Set<Integer> permissibleProviders = new HashSet<>(Arrays.asList(provider1, provider2));
    UserSecurity userSecurity = getFixture(UserSecurity.class);
    when(prescriptionDetailsService.getPermissionsForMedicalSummary()).thenReturn(userSecurity);

    when(accuroApiContextManagerMock.getPermissibleProviders(user.getUserId(), userSecurity))
        .thenReturn(permissibleProviders);

    MedicationType medTypeDin = MedicationType.DIN;
    int patientId = TestUtilities.nextInt();
    PrescriptionMedication prescriptionMedication1 =
        getPrescriptionMedication(medTypeDin, patientId, provider1);

    MedicationType medTypeAhp = MedicationType.AHP;
    PrescriptionMedication prescriptionMedication2 =
        getPrescriptionMedication(medTypeAhp, patientId, provider2);

    List<PrescriptionMedication> protossPrescription = new ArrayList<>();

    protossPrescription.add(prescriptionMedication1);
    protossPrescription.add(prescriptionMedication2);

    when(medicationManagerMock.getPrescriptionsForPatient(patientId))
        .thenReturn(protossPrescription);

    CodeSystem codeSystem = getFixture(CodeSystem.class);
    when(codeSystemManagerMock.getCodeSystem(TABLE_99P0108)).thenReturn(codeSystem);
    when(codeSystemManagerMock.getCodeSystem(TABLE_99P0100)).thenReturn(codeSystem);

    PatientMedicalSummaryDto expectedDto = new PatientMedicalSummaryDto();

    populatePrescription(expectedDto, protossPrescription);
    populateDiagnosis(expectedDto, patientId);
    populateAllergies(expectedDto, patientId);

    PatientMedicalSummaryDto actual = given()
        .when()
        .pathParam("patientId", patientId)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/medical-summary")
        .then()
        .assertThat().statusCode(200)
        .extract().as(PatientMedicalSummaryDto.class);

    assertEquals(expectedDto, actual);
    // verify()
    verify(medicationManagerMock).getPrescriptionsForPatient(patientId);
    verify(noKnownAllergyManagerMock).getForPatient(patientId);
    verify(patientAllergyManagerMock).getForPatient(patientId);
    verify(diagnosisStatusManagerMock).getAll();
    verify(patientDiagnosisManagerMock).getForPatient(patientId);

  }


  @Test
  public void testGetPatientMedicalSummaryManufacturedDrugWithoutDosage()
      throws Exception {

    int provider1 = TestUtilities.nextInt();
    int provider2 = TestUtilities.nextInt();

    Set<Integer> permissibleProviders = new HashSet<>(Arrays.asList(provider1, provider2));
    UserSecurity userSecurity = getFixture(UserSecurity.class);
    when(prescriptionDetailsService.getPermissionsForMedicalSummary()).thenReturn(userSecurity);

    when(accuroApiContextManagerMock.getPermissibleProviders(user.getUserId(), userSecurity))
        .thenReturn(permissibleProviders);

    MedicationType medTypeDin = MedicationType.DIN;
    int patientId = TestUtilities.nextInt();
    PrescriptionMedication prescriptionMedication1 =
        getPrescriptionMedication(medTypeDin, patientId, provider1);

    MedicationType medTypeAhp = MedicationType.AHP;
    PrescriptionMedication prescriptionMedication2 =
        getPrescriptionMedication(medTypeAhp, patientId, provider2);

    List<PrescriptionMedication> protossPrescription = new ArrayList<>();

    protossPrescription.add(prescriptionMedication1);
    protossPrescription.add(prescriptionMedication2);

    when(medicationManagerMock.getPrescriptionsForPatient(patientId))
        .thenReturn(protossPrescription);

    CodeSystem codeSystem = getFixture(CodeSystem.class);
    when(codeSystemManagerMock.getCodeSystem(TABLE_99P0108)).thenReturn(codeSystem);
    when(codeSystemManagerMock.getCodeSystem(TABLE_99P0100)).thenReturn(codeSystem);

    PatientMedicalSummaryDto expectedDto = new PatientMedicalSummaryDto();

    populatePrescription(expectedDto, protossPrescription);
    when(dosageManagerMock.getForPrescription(anyInt())).thenReturn(new ArrayList<>());
    expectedDto.getPrescriptions().stream().forEach(x -> {
      x.setDosages(null);
      x.setInstructionsDisplayText("SigInstructions: " + x.getSigInstructions());

    });
    populateDiagnosis(expectedDto, patientId);
    populateAllergies(expectedDto, patientId);

    PatientMedicalSummaryDto actual = given()
        .when()
        .pathParam("patientId", patientId)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/medical-summary")
        .then()
        .assertThat().statusCode(200)
        .extract().as(PatientMedicalSummaryDto.class);

    assertEquals(expectedDto, actual);
    // verify()
    verify(medicationManagerMock).getPrescriptionsForPatient(patientId);
    verify(noKnownAllergyManagerMock).getForPatient(patientId);
    verify(patientAllergyManagerMock).getForPatient(patientId);
    verify(diagnosisStatusManagerMock).getAll();
    verify(patientDiagnosisManagerMock).getForPatient(patientId);

  }

  @Test
  public void testGetPatientMedicalSummaryNpnDrug()
      throws Exception {

    int provider1 = TestUtilities.nextInt();
    int provider2 = TestUtilities.nextInt();

    Set<Integer> permissibleProviders = new HashSet<>(Arrays.asList(provider1, provider2));
    UserSecurity userSecurity = getFixture(UserSecurity.class);
    when(prescriptionDetailsService.getPermissionsForMedicalSummary()).thenReturn(userSecurity);

    when(accuroApiContextManagerMock.getPermissibleProviders(user.getUserId(), userSecurity))
        .thenReturn(permissibleProviders);

    MedicationType medTypeDin = MedicationType.NPN;
    int patientId = TestUtilities.nextInt();
    PrescriptionMedication prescriptionMedication1 =
        getPrescriptionMedication(medTypeDin, patientId, provider1);
    MedicationType medTypeAhp = MedicationType.GF;
    PrescriptionMedication prescriptionMedication2 =
        getPrescriptionMedication(medTypeAhp, patientId, provider2);

    List<PrescriptionMedication> protossPrescription = new ArrayList<>();

    protossPrescription.add(prescriptionMedication1);
    protossPrescription.add(prescriptionMedication2);

    when(medicationManagerMock.getPrescriptionsForPatient(patientId))
        .thenReturn(protossPrescription);

    CodeSystem codeSystem = getFixture(CodeSystem.class);
    when(codeSystemManagerMock.getCodeSystem(TABLE_99P0108)).thenReturn(codeSystem);
    when(codeSystemManagerMock.getCodeSystem(TABLE_99P0100)).thenReturn(codeSystem);

    PatientMedicalSummaryDto expectedDto = new PatientMedicalSummaryDto();

    populatePrescription(expectedDto, protossPrescription);
    populateDiagnosis(expectedDto, patientId);
    populateAllergies(expectedDto, patientId);

    PatientMedicalSummaryDto actual = given()
        .when()
        .pathParam("patientId", patientId)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/medical-summary")
        .then()
        .assertThat().statusCode(200)
        .extract().as(PatientMedicalSummaryDto.class);

    assertEquals(expectedDto, actual);
    // verify()
    verify(medicationManagerMock).getPrescriptionsForPatient(patientId);
    verify(noKnownAllergyManagerMock).getForPatient(patientId);
    verify(patientAllergyManagerMock).getForPatient(patientId);
    verify(diagnosisStatusManagerMock).getAll();
    verify(patientDiagnosisManagerMock).getForPatient(patientId);
  }

  @Test
  public void testGetPatientMedicalSummaryCompound()
      throws Exception {

    int provider1 = TestUtilities.nextInt();
    int provider2 = TestUtilities.nextInt();

    Set<Integer> permissibleProviders = new HashSet<>(Arrays.asList(provider1, provider2));
    UserSecurity userSecurity = getFixture(UserSecurity.class);
    when(prescriptionDetailsService.getPermissionsForMedicalSummary()).thenReturn(userSecurity);

    when(accuroApiContextManagerMock.getPermissibleProviders(user.getUserId(), userSecurity))
        .thenReturn(permissibleProviders);

    MedicationType medTypeDin = MedicationType.COMPOUND;
    int patientId = TestUtilities.nextInt();
    PrescriptionMedication prescriptionMedication1 =
        getPrescriptionMedication(medTypeDin, patientId, provider1);
    MedicationType medTypeAhp = MedicationType.GD;
    PrescriptionMedication prescriptionMedication2 =
        getPrescriptionMedication(medTypeAhp, patientId, provider2);

    List<PrescriptionMedication> protossPrescription = new ArrayList<>();

    protossPrescription.add(prescriptionMedication1);
    protossPrescription.add(prescriptionMedication2);

    when(medicationManagerMock.getPrescriptionsForPatient(patientId))
        .thenReturn(protossPrescription);

    CodeSystem codeSystem = getFixture(CodeSystem.class);
    when(codeSystemManagerMock.getCodeSystem(TABLE_99P0108)).thenReturn(codeSystem);
    when(codeSystemManagerMock.getCodeSystem(TABLE_99P0100)).thenReturn(codeSystem);

    PatientMedicalSummaryDto expectedDto = new PatientMedicalSummaryDto();

    populatePrescription(expectedDto, protossPrescription);
    populateDiagnosis(expectedDto, patientId);
    populateAllergies(expectedDto, patientId);

    PatientMedicalSummaryDto actual = given()
        .when()
        .pathParam("patientId", patientId)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/medical-summary")
        .then()
        .assertThat().statusCode(200)
        .extract().as(PatientMedicalSummaryDto.class);

    assertEquals(expectedDto.getAllergies(), actual.getAllergies());
    assertEquals(expectedDto.getDiagnosis(), actual.getDiagnosis());
    assertEquals(expectedDto.getPrescriptions().toString(), actual.getPrescriptions().toString());

    verify(medicationManagerMock).getPrescriptionsForPatient(patientId);
    verify(noKnownAllergyManagerMock).getForPatient(patientId);
    verify(patientAllergyManagerMock).getForPatient(patientId);
    verify(diagnosisStatusManagerMock).getAll();
    verify(patientDiagnosisManagerMock).getForPatient(patientId);
  }

  @Test
  public void testFilterPrescriptionWithEmptyProviders()
      throws Exception {
    UserSecurity userSecurity = getFixture(UserSecurity.class);
    when(prescriptionDetailsService.getPermissionsForMedicalSummary()).thenReturn(userSecurity);
    doThrow(new InsufficientPermissionsException("")).when(accuroApiContextManagerMock)
        .getPermissibleProviders(user.getUserId(), userSecurity);

    int patientId = TestUtilities.nextInt();
    PatientMedicalSummaryDto expectedDto = new PatientMedicalSummaryDto();

    populatePrescription(expectedDto, new ArrayList<>());
    populateDiagnosis(expectedDto, patientId);
    populateAllergies(expectedDto, patientId);

    PatientMedicalSummaryDto actual = given()
        .when()
        .pathParam("patientId", patientId)
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/medical-summary")
        .then()
        .assertThat().statusCode(200)
        .extract().as(PatientMedicalSummaryDto.class);

    assertEquals(expectedDto, actual);
    // verify()
    verify(medicationManagerMock).getPrescriptionsForPatient(patientId);
    verify(noKnownAllergyManagerMock).getForPatient(patientId);
    verify(patientAllergyManagerMock).getForPatient(patientId);
    verify(diagnosisStatusManagerMock).getAll();
    verify(patientDiagnosisManagerMock).getForPatient(patientId);

  }

  private void populateAllergies(PatientMedicalSummaryDto medicalSummaryDto,
      int patientId) throws Exception {
    AllergiesDto allergiesDto = new AllergiesDto();
    populateNoKnownAllergies(allergiesDto, patientId);
    populatePatientAllergies(allergiesDto, patientId);
    medicalSummaryDto.setAllergies(allergiesDto);
  }

  private void populateNoKnownAllergies(AllergiesDto allergiesDto, int patientId)
      throws Exception {

    List<NoKnownAllergy> allergies = getFixtures(NoKnownAllergy.class, ArrayList::new, 2);
    when(noKnownAllergyManagerMock.getForPatient(patientId)).thenReturn(allergies);

    List<NoKnownAllergySummaryDto> noKnownAllergySummaryDtos =
        mapDto(allergies, NoKnownAllergySummaryDto.class, ArrayList::new);

    allergiesDto.setNoKnownAllergies(noKnownAllergySummaryDtos);
  }

  private void populatePatientAllergies(AllergiesDto allergiesDto, int patientId)
      throws Exception {
    List<PatientAllergy> patientAllergies = getFixtures(PatientAllergy.class, ArrayList::new, 2);
    when(patientAllergyManagerMock.getForPatient(patientId)).thenReturn(patientAllergies);

    List<PatientAllergySummaryDto> allergySummaryDtos = new ArrayList<>();
    for (PatientAllergy patientAllergy : patientAllergies) {
      /**
       * The providerId in patientAllergy may have value. Check ModifyAllergy.java at line 1158
       * onwards in accuro-core
       */
      PatientAllergySummaryDto allergySummaryDto =
          mapDto(patientAllergy, PatientAllergySummaryDto.class);

      StringBuilder displayText = new StringBuilder();
      displayText.append("Group: ").append(patientAllergy.getAllergyGroupName()).append(" - ");
      displayText.append("Name: ").append(patientAllergy.getAllergyName()).append(" - ");
      displayText.append("Severity: ").append(patientAllergy.getSeverityCode());

      Set<AllergyReaction> allergyReactions = getFixtures(AllergyReaction.class, HashSet::new, 2);

      when(allergyReactionManagerMock.getByPatientAllergyId(patientAllergy.getId()))
          .thenReturn(allergyReactions);

      if (!allergyReactions.isEmpty()) {
        displayText.append(" - ");
        displayText.append("Reactions: ");
        Set<AllergyReactionDto> reactionDtos =
            mapDto(allergyReactions, AllergyReactionDto.class, HashSet::new);
        Iterator<AllergyReactionDto> reactionIterator = reactionDtos.iterator();
        while (reactionIterator.hasNext()) {
          AllergyReactionDto reactionDto = reactionIterator.next();
          displayText.append(reactionDto.getReactionCode()).append(" - ");
          displayText.append(reactionDto.getDescription());
          if (reactionIterator.hasNext()) {
            displayText.append(", ");
          }
        }
        allergySummaryDto
            .setReactions(reactionDtos);
      }
      allergySummaryDto.setAllergyDisplayText(displayText.toString());
      allergySummaryDto.setDrugIdentifier(getDrugIdentifier(
          patientAllergy.getMedicationType() == null ? null
              : patientAllergy.getMedicationType().toString(),
          patientAllergy.getDrugCode()));
      allergySummaryDtos.add(allergySummaryDto);
    }
    allergiesDto.setPatientAllergies(allergySummaryDtos);
  }

  private void populateDiagnosis(PatientMedicalSummaryDto medicalSummaryDto, int patientId)
      throws Exception {
    Set<DiagnosisStatus> diagnosisStatuses = getFixtures(DiagnosisStatus.class, HashSet::new, 2);
    DiagnosisStatus diagStatus = getFixture(DiagnosisStatus.class);
    int diagStatusId = TestUtilities.nextInt();
    diagStatus.setStatusId(diagStatusId);
    diagnosisStatuses.add(diagStatus);

    PatientDiagnosis patientDiag = getFixture(PatientDiagnosis.class);
    patientDiag.setDiagnosisStatusId(diagStatusId);
    Set<PatientDiagnosis> protossResult = Collections.singleton(patientDiag);
    when(diagnosisStatusManagerMock.getAll()).thenReturn(diagnosisStatuses);
    when(patientDiagnosisManagerMock.getForPatient(patientId)).thenReturn(protossResult);

    Map<Integer, DiagnosisStatus> mappedStatus = new HashMap<>();
    diagnosisStatuses.forEach(status -> mappedStatus.put(status.getStatusId(), status));

    Set<DiagnosisDto> diagnosisDtoSet = new HashSet<>();
    for (PatientDiagnosis patientDiagnosis : protossResult) {
      StringBuilder diagnosisDisplayText = new StringBuilder();
      if (patientDiagnosis.isNegative()) {
        diagnosisDisplayText.append("Negative - ");
      }
      diagnosisDisplayText.append("Status: ");
      DiagnosisDto diagnosisDto = mapDto(patientDiagnosis, DiagnosisDto.class);
      DiagnosisStatus diagnosisStatus = mappedStatus.get(patientDiagnosis.getDiagnosisStatusId());
      if (diagnosisStatuses != null) {
        DiagnosisStatusDto diagnosisStatusDto = mapDto(diagnosisStatus, DiagnosisStatusDto.class);
        diagnosisDto.setDiagnosisStatus(diagnosisStatusDto);
        diagnosisDisplayText.append(diagnosisStatus.getStatusName()).append(" - ");
      }

      diagnosisDisplayText.append(patientDiagnosis.getDescription());
      diagnosisDto.setDiagnosisDisplayText(diagnosisDisplayText.toString());
      diagnosisDtoSet.add(diagnosisDto);

    }
    medicalSummaryDto.setDiagnosis(diagnosisDtoSet);
  }

  private PrescriptionMedication getPrescriptionMedication(MedicationType medType, int patientId,
      int providerId) {

    PrescriptionMedication prescriptionMedication = getFixture(PrescriptionMedication.class);
    prescriptionMedication.setDinSystem(medType);
    prescriptionMedication.setPatientId(patientId);
    prescriptionMedication.setPrescribingPhysician(providerId);
    prescriptionMedication.setMedStatus("Active");

    if (prescriptionMedication.getDosages() == null) {
      prescriptionMedication.setDosages(new HashSet<>());
    }

    return prescriptionMedication;
  }

  private void populatePrescription(
      PatientMedicalSummaryDto medicalSummaryDto,
      List<PrescriptionMedication> filteredMedicationList) throws Exception {

    List<PrescriptionDto> prescriptionDtos = new ArrayList<>();

    for (PrescriptionMedication prescriptionMed : filteredMedicationList) {
      PrescriptionDto prescriptionDto = new PrescriptionDto();
      prescriptionDto.setPrescriptionId(prescriptionMed.getPrescriptionId());
      prescriptionDto.setPrescribingProvider(prescriptionMed.getPrescribingPhysician());
      prescriptionDto.setExternalRx(prescriptionMed.isExternalRx());
      prescriptionDto.setAuthorizingProvider(prescriptionMed.getAuthorizingPhysician());
      prescriptionDto.setDosageForm(prescriptionMed.getDosageForm());
      prescriptionDto.setSigInstructions(prescriptionMed.getSigInstructions());
      prescriptionDto.setRoute(prescriptionMed.getRoute());
      prescriptionDto.setMedStatus(prescriptionMed.getMedStatus());
      prescriptionDto.setDrugUse(prescriptionMed.getDrugType());
      prescriptionDto.setNote(prescriptionMed.getNote());
      AccuroCalendar accuroCalendar = new AccuroCalendar();

      prescriptionDto.setWrittenDate(
          prescriptionMed.getWrittenDate() == null ? null
              : LocalDateTime
                  .fromCalendarFields(accuroCalendar.unwrap(prescriptionMed.getWrittenDate())));
      prescriptionDto.setExpiryDate(prescriptionMed.getExpiryDate());
      prescriptionDto.setEffectiveDate(prescriptionMed.getEffectiveDate());
      prescriptionDto.setCreatedDate(prescriptionMed.getCreatedDate());
      prescriptionDto.setLastModified((prescriptionMed.getLastModified()));

      // set medical top level summary

      MedicationType medicationType = prescriptionMed.getDinSystem();
      int medicationId = prescriptionMed.getMedicationId();

      switch (medicationType) {
        case DIN:
          populateManufacturedDrugDto(prescriptionDto, prescriptionMed.getDin());
          break;
        case GD:
          populateGenericDrugDetails(prescriptionDto, medicationId);
          break;
        case NPN:
          populateNaturalHealthProductDto(prescriptionDto, prescriptionMed.getDin());
          break;
        case AHP:
          populateAlternativeHealthProductDetails(prescriptionDto, medicationId);
          break;
        case GF:
          populateFormulationDto(prescriptionDto, prescriptionMed.getWellnetFormulationId());
          break;
        case COMPOUND:
          populateCustomCompoundDto(prescriptionDto, prescriptionMed.getPrescriptionId(),
              prescriptionMed.getCompoundName(), prescriptionMed.getCompoundDetails());
          break;
        default:
          throw new PrescriptionConversionException("No valid medication type: " + medicationType);
      }
      getDosages(prescriptionDto, prescriptionMed.getPrescriptionId());
      StringBuilder instructions = new StringBuilder();
      if (StringUtils.isBlank(prescriptionDto.getInstructionsDisplayText())) {
        instructions.append("SigInstructions: " + prescriptionDto.getSigInstructions());
      } else {
        instructions.append(prescriptionDto.getInstructionsDisplayText());
        instructions.append(" - SigInstructions: " + prescriptionDto.getSigInstructions());
      }
      prescriptionDto.setInstructionsDisplayText(instructions.toString());

      prescriptionDtos.add(prescriptionDto);
    }
    medicalSummaryDto.setPrescriptions(prescriptionDtos);

  }

  private void populateFormulationDto(PrescriptionDto prescriptionDto, Integer formulationId)
      throws Exception {
    Formulation formulation = getFixture(Formulation.class);
    when(formulationManagerMock.getFormulation(formulationId)).thenReturn(formulation);

    if (formulation != null) {
      FormulationSummaryDto formulationSummaryDto = new FormulationSummaryDto();

      formulationSummaryDto.setIdentifier(
          getDrugIdentifier(MedicationTypeApi.GCN.toString(),
              Integer.toString(formulation.getFormulationId())));
      formulationSummaryDto.setName(formulation.getName());
      formulationSummaryDto.setStrengthDescription(formulation.getStrengthDescription());

      prescriptionDto.setMedicationDisplayText(formulation.getName());
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setFormulation(formulationSummaryDto);
      prescriptionDto.setMedicationDetails(medDetails);
    }
  }

  private void populateNaturalHealthProductDto(
      PrescriptionDto prescriptionDto, String drugId)
      throws Exception {
    NaturalHealthProduct product = getFixture(NaturalHealthProduct.class);
    when(naturalHealthProductManagerMock.getNaturalHealthProduct(drugId)).thenReturn(product);

    if (product != null) {
      NaturalHealthProductSummaryDto productSummaryDto = new NaturalHealthProductSummaryDto();
      productSummaryDto.setName(product.getName());
      productSummaryDto.setIdentifier(
          getDrugIdentifier(MedicationTypeApi.NPN.toString(), product.getLicenceNumber()));

      prescriptionDto.setMedicationDisplayText(product.getName());
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setNaturalHealthProduct(productSummaryDto);
      prescriptionDto.setMedicationDetails(medDetails);
    }

  }

  private void populateAlternativeHealthProductDetails(PrescriptionDto rx, int medicationId)
      throws Exception {
    AlternativeHealthProduct ahp = getFixture(AlternativeHealthProduct.class);
    when(alternativeHealthProductManagerMock.getAlternativeHealthProduct(medicationId))
        .thenReturn(ahp);
    if (ahp != null) {
      AlternativeHealthProductSummaryDto ahpDto =
          mapDto(ahp, AlternativeHealthProductSummaryDto.class);
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setAlternativeHealthProduct(ahpDto);
      rx.setMedicationDetails(medDetails);
      rx.setMedicationDisplayText(ahp.getName() + " - " + ahp.getDescription());
    }
  }

  private void populateCustomCompoundDto(PrescriptionDto prescriptionDto, int rxId,
      String compoundName, String compoundDetails)
      throws Exception {
    Set<Ingredient> compoundIngredients;
    compoundIngredients = new HashSet<>();
    Ingredient ingredient1 = getFixture(Ingredient.class);
    Ingredient ingredient2 = getFixture(Ingredient.class);
    compoundIngredients.add(ingredient1);
    compoundIngredients.add(ingredient2);
    when(ingredientManagerMock.get(rxId)).thenReturn(compoundIngredients);

    Map<Boolean, Set<Ingredient>> ingredients = compoundIngredients.stream()
        .collect(Collectors.groupingBy(Ingredient::isDrugIncluded, Collectors.toSet()));

    Set<IngredientSummaryDto> included =
        mapDto(ingredients.getOrDefault(true, new HashSet<>()), IngredientSummaryDto.class,
            HashSet::new);
    Set<IngredientSummaryDto> excluded =
        mapDto(ingredients.getOrDefault(false, new HashSet<>()), IngredientSummaryDto.class,
            HashSet::new);

    included.stream().forEach(t -> t.setCodeSystem(MedicationTypeApi.HIC.toString()));
    excluded.stream().forEach(t -> t.setCodeSystem(MedicationTypeApi.HIC.toString()));

    CustomCompoundSummaryDto compoundDto = new CustomCompoundSummaryDto();
    compoundDto.setName(compoundName);
    compoundDto.setDescription(compoundDetails);
    compoundDto.setIncludedIngredients(included);
    compoundDto.setExcludedIngredients(excluded);

    prescriptionDto.setMedicationDisplayText(compoundName + " " + compoundDetails);
    MedicationDetails medicationDetails = new MedicationDetails();
    medicationDetails.setCustomCompound(compoundDto);
    prescriptionDto.setMedicationDetails(medicationDetails);
  }

  private void populateGenericDrugDetails(PrescriptionDto rx, int medicationId)
      throws Exception {
    GenericDrug genericDrug = getFixture(GenericDrug.class);
    when(genericDrugManagerMock.getGenericDrug(medicationId)).thenReturn(genericDrug);

    if (genericDrug != null) {
      GenericDrugDto genericDrugDto = mapDto(genericDrug, GenericDrugDto.class);
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setGenericDrug(genericDrugDto);
      rx.setMedicationDetails(medDetails);
    }
  }

  private void populateManufacturedDrugDto(PrescriptionDto prescriptionDto,
      String din) throws Exception {

    ManufacturedDrug manufacturedDrug = getFixture(ManufacturedDrug.class);
    manufacturedDrug.setDrugIdentificationNumber(din);

    when(manufacturedDrugManagerMock.getManufacturedDrug(din)).thenReturn(manufacturedDrug);

    if (manufacturedDrug != null) {
      ManufacturedDrugSummaryDto drugSummaryDto = new ManufacturedDrugSummaryDto();
      drugSummaryDto.setName(manufacturedDrug.getName());
      drugSummaryDto.setManufacturer(manufacturedDrug.getManufacturer());
      // DIN
      drugSummaryDto
          .setIdentifier(getDrugIdentifier(MedicationTypeApi.DIN.toString(),
              manufacturedDrug.getDrugIdentificationNumber()));

      // AIG
      drugSummaryDto.setActiveIngredientIdentifier(
          getDrugIdentifier(MedicationTypeApi.AIG.toString(),
              manufacturedDrug.getActiveIngredientNumber()));

      prescriptionDto.setMedicationDisplayText(manufacturedDrug.getName());
      MedicationDetails medDetails = new MedicationDetails();
      medDetails.setManufacturedDrug(drugSummaryDto);
      prescriptionDto.setMedicationDetails(medDetails);
    }

  }

  private void getDosages(PrescriptionDto prescriptionDto, int prescriptionId)
      throws Exception {

    LocalDateTime localDateTime = RandomUtils.getLocalDateTime();
    Dosage dosage1 = getFixture(Dosage.class);
    dosage1.setDurationUnit("Day");
    dosage1.setDurationAmount((float) RandomUtils.getInt(0, 10));
    dosage1.setDurationUnit("Year");
    dosage1.setDurationAmount((float) Integer.MAX_VALUE);
    dosage1.setStartDate(localDateTime);


    Dosage dosage2 = getFixture(Dosage.class);
    dosage2.setDurationUnit("Week");
    dosage2.setConcurrent(false);
    dosage2.setStartDate(localDateTime.plusDays(RandomUtils.getInt(10, 100)));


    Dosage dosage3 = getFixture(Dosage.class);
    dosage3.setDurationUnit("MO28");
    dosage3.setConcurrent(false);
    Dosage dosage4 = getFixture(Dosage.class);
    dosage4.setDurationUnit("MO30");
    dosage4.setConcurrent(false);
    Dosage dosage5 = getFixture(Dosage.class);
    dosage5.setDurationUnit("Year");
    dosage5.setConcurrent(false);
    Dosage dosage6 = getFixture(Dosage.class);
    dosage6.setDurationUnit("Year");
    dosage6.setConcurrent(false);
    Dosage dosage7 = getFixture(Dosage.class);
    dosage7.setConcurrent(true);

    final List<Dosage> protossDosage =
        Arrays.asList(dosage1, dosage2, dosage3, dosage4, dosage5, dosage6, dosage7);

    when(dosageManagerMock.getForPrescription(prescriptionId)).thenReturn(protossDosage);

    if (!protossDosage.isEmpty()) {
      List<DosageDto> dosageDtos =
          mapDto(protossDosage, DosageDto.class, ArrayList::new);
      prescriptionDto.setDosages(dosageDtos);
      prescriptionDto.setInstructionsDisplayText(generateDosageInstructionDisplayText(dosageDtos));
    }
  }

  private DrugIdentifier getDrugIdentifier(String code, String value) {
    DrugIdentifier drugIdentifier = new DrugIdentifier();
    drugIdentifier.setCodeSystem(code);
    drugIdentifier.setValue(value);
    return drugIdentifier;
  }

  private String generateDosageInstructionDisplayText(List<DosageDto> dosages)
      throws DatabaseInteractionException {
    StringBuilder instruction = new StringBuilder();

    // retrieve measure units and dosage frequency types
    Map<String, String> descriptionByDosageFrequencyType = getDescriptionByMnemonic(TABLE_99P0108);
    Map<String, String> descriptionByMeasureUnit = getDescriptionByMnemonic(TABLE_99P0100);

    instruction.append("Dosages: ");
    boolean notFirstDosage = false;
    LocalDate prevDosageStartDate = null;
    DosageDto prevDosage = null;
    for (DosageDto dosage : dosages) {
      LocalDate curDosageStartDate =
          dosage.getStartDate() == null ? null : dosage.getStartDate().toLocalDate();

      if (notFirstDosage) {
        if (dosage.isConcurrent()) {
          instruction.append("AND ");
        } else {
          instruction.append("THEN ");

          if (curDosageStartDate != null && prevDosageStartDate != null) {
            // calculate end date of previous dosage
            LocalDate prevDosageEndDate = prevDosageStartDate.plusDays(
                getDurationInDays(prevDosage.getDurationAmount(), prevDosage.getDurationUnit()));

            if (curDosageStartDate.isAfter(prevDosageEndDate)) {
              int daysBreak = Days.daysBetween(prevDosageEndDate, curDosageStartDate).getDays();
              instruction.append("NOTHING for ");
              instruction.append(daysBreak).append(" ");
              instruction.append("day(s) ");
              instruction.append("THEN ");
            }
          }
        }
      }

      instruction.append(formatFloat(dosage.getMin()));
      if (dosage.getMax() > 0) {
        instruction.append("-");
        instruction.append(formatFloat(dosage.getMax()));
      }
      instruction.append(" ");

      String measureUnit = descriptionByMeasureUnit.get(dosage.getUnit());
      instruction.append(StringUtils.isBlank(measureUnit) ? dosage.getUnit() : measureUnit);
      instruction.append(" ");

      if (dosage.getEyeCode() != null && !EyeCode.NO_EYES.equals(dosage.getEyeCode())) {
        instruction.append(dosage.getEyeCode().toString());
        instruction.append(" ");
      }

      String dosageFrequencyType = descriptionByDosageFrequencyType.get(dosage.getIntervalTime());
      instruction
          .append(StringUtils.isBlank(dosageFrequencyType) ? dosage.getIntervalTime()
              : dosageFrequencyType);
      instruction.append(" ");

      if (dosage.isProReNata()) {
        instruction.append("As Needed ");
      }

      if (!"STAT".equals(dosage.getIntervalTime())) {
        instruction.append("for ");
        instruction.append(formatFloat(dosage.getDurationAmount())).append(" ");
        EmrUnitsMapping unitsMapping = new EmrUnitsMapping();
        TreeMap<String, String> allDurations = unitsMapping.getAllDurUnits();
        String duration = allDurations.get(dosage.getDurationUnit());
        instruction.append(StringUtils.isBlank(duration) ? dosage.getDurationUnit() : duration);
        instruction.append(" ");
      }

      boolean includeDate = false;
      if (curDosageStartDate != null) {
        boolean notSameDay = !curDosageStartDate.equals(prevDosageStartDate);
        if (!notFirstDosage || (dosage.isConcurrent() && notSameDay)) {
          includeDate = true;
        }
      }

      if (includeDate && curDosageStartDate != null) {
        instruction.append("starting on ");
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MMM-dd");
        instruction.append(curDosageStartDate.toString(fmt));
        instruction.append(" ");
      }

      notFirstDosage = true;
      prevDosage = dosage;
      prevDosageStartDate = curDosageStartDate;
    }
    return instruction.toString().trim();
  }

  private Map<String, String> getDescriptionByMnemonic(String tableId)
      throws DatabaseInteractionException {
    CodeSystemManager codeSystemManager = codeSystemManagerMock;
    CodeSystem codeSystem = codeSystemManager.getCodeSystem(tableId);
    List<CodeValue> codeValues = Stream
        .concat(codeSystem.getBuiltInValues().stream(),
            codeSystem.getCustomValues().stream())
        .distinct()
        .collect(Collectors.toList());
    return codeValues
        .stream()
        .collect(Collectors.toMap(CodeValue::getCode, CodeValue::getDescription));
  }

  // copied from Accuro#MedicationManager#getDurationInDays
  private int getDurationInDays(float duration, String units) {

    int unitDuration = 0;
    if (units.equals("Day")) {
      unitDuration = 1;
    } else if (units.equals("Week")) {
      unitDuration = 7;
    } else if (units.equals("MO28")) {
      unitDuration = 28;
    } else if (units.equals("MO30")) {
      unitDuration = 30;
    } else if (units.equals("Year")) {
      unitDuration = 365;
    }

    return (int) ((double) duration * (double) unitDuration);
  }
}


package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.accuro.api.medications.DefaultLimitedUseCodeManager;
import com.qhrtech.emr.accuro.api.medications.LimitedUseCodeManager;
import com.qhrtech.emr.accuro.api.prescription.AnnotationManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultAnnotationManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultDosageManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultInteractionManagementDetailsManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultInteractionManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultPrescriptionIndicationManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultPrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultStatusHistoryManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultWellnetPrescriptionLinkManager;
import com.qhrtech.emr.accuro.api.prescription.DosageManager;
import com.qhrtech.emr.accuro.api.prescription.InteractionManagementDetailsManager;
import com.qhrtech.emr.accuro.api.prescription.InteractionManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionIndicationManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.prescription.StatusHistoryManager;
import com.qhrtech.emr.accuro.api.prescription.WellnetPrescriptionLinkManager;
import com.qhrtech.emr.accuro.api.sysinfo.DefaultSystemInformationManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.db.prescription.PrescriptionMedicationFixture;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.restapi.endpoints.provider.prescribeit.ConversationEndpoint;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.CleanPrescriptionRecords;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescriptions.StatusHistoryDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.services.PrescriptionDetailsService;
import com.qhrtech.emr.restapi.services.impl.DefaultPrescriptionDetailsService;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class PatientPrescriptionEndpointIT extends
    AbstractEndpointIntegrationTest<PatientPrescriptionEndpoint> {

  private final PrescriptionMedicationManager prescriptionMedicationManager;
  private final DosageManager dosageManager;
  private final PrescriptionIndicationManager prescriptionIndicationManager;
  private final AnnotationManager annotationManager;
  private final StatusHistoryManager statusHistoryManager;
  private final InteractionManager interactionManager;
  private final InteractionManagementDetailsManager interactionManagementDetailsManager;
  private final WellnetPrescriptionLinkManager wellnetPrescriptionLinkManager;
  private final LimitedUseCodeManager limitedUseCodeManager;
  private final PrescriptionDetailsService prescriptionDetailsService;
  private final SystemInformationManager systemInformationManager;

  public PatientPrescriptionEndpointIT() throws IOException {
    super(new PatientPrescriptionEndpoint(), PatientPrescriptionEndpoint.class);
    prescriptionMedicationManager =
        new DefaultPrescriptionMedicationManager(ds, null, defaultUser());
    dosageManager = new DefaultDosageManager(ds, null, defaultUser());
    prescriptionIndicationManager =
        new DefaultPrescriptionIndicationManager(ds, null, defaultUser());
    annotationManager = new DefaultAnnotationManager(ds, null, defaultUser());
    statusHistoryManager = new DefaultStatusHistoryManager(ds, null, defaultUser());
    interactionManager = new DefaultInteractionManager(ds, null, defaultUser());
    interactionManagementDetailsManager =
        new DefaultInteractionManagementDetailsManager(ds, null, defaultUser());
    wellnetPrescriptionLinkManager =
        new DefaultWellnetPrescriptionLinkManager(ds, null, defaultUser());
    limitedUseCodeManager = new DefaultLimitedUseCodeManager(ds);
    prescriptionDetailsService = new DefaultPrescriptionDetailsService();
    systemInformationManager = new DefaultSystemInformationManager(ds);

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
  public void testCreateStatusHistory()
      throws SQLException, UnsupportedSchemaVersionException, IOException {
    PrescriptionMedicationFixture medicationFixture = new PrescriptionMedicationFixture();

    medicationFixture.setUp(getConnection());

    PrescriptionMedication medication = medicationFixture.get();

    StatusHistoryDto expected = getFixture(StatusHistoryDto.class);
    expected.setEffectiveDate(TestUtilities.nextLocalDateTime(false));
    expected.setEndDate(TestUtilities.nextLocalDateTime(false));
    expected.setPrescriptionId(medication.getPrescriptionId());

    int patientId = medication.getPatientId();
    int prescriptionId = medication.getPrescriptionId();

    given()
        .pathParams("patientId", patientId)
        .pathParam("prescriptionId", prescriptionId)
        .contentType(ContentType.JSON)
        .body(expected)
        .when()
        .post(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/prescriptions/"
            + "{prescriptionId}/order-statuses")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode());

    cleanMedicationRecord(medication);

  }

  @Test
  public void testGetForPatient()
      throws SQLException, UnsupportedSchemaVersionException, IOException {
    PrescriptionMedicationFixture medicationFixture = new PrescriptionMedicationFixture();

    medicationFixture.setUp(getConnection());
    boolean includeCcddMapping = TestUtilities.nextBoolean();
    PrescriptionMedication medication = medicationFixture.get();
    given().pathParam("patientId", medication.getPatientId())
        .queryParam("includeCcddMapping", includeCcddMapping)
        .when()
        .get(getBaseUrl() + "/v1/provider-portal/patients/{patientId}/prescriptions/")
        .then()
        .assertThat()
        .statusCode(200);

    cleanMedicationRecord(medication);
  }

  private void cleanMedicationRecord(PrescriptionMedication medication)
      throws SQLException, IOException {

    CleanPrescriptionRecords cleanPrescriptionRecords =
        new CleanPrescriptionRecords(new ConversationEndpoint(), ConversationEndpoint.class);
    cleanPrescriptionRecords.cleanMedicationRecord(medication);
  }
}

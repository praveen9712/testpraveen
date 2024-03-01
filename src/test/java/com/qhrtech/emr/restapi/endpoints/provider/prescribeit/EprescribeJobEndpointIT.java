
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobHistoryManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobPrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobHistoryManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobPrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationMessageManager;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeJobFixture;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeJobHistoryFixture;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeJobPrescriptionMedicationFixture;
import com.qhrtech.emr.accuro.db.eprescribe.EprescribeJobTypeFixture;
import com.qhrtech.emr.accuro.db.prescription.PrescriptionMedicationFixture;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJob;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobHistory;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobPrescriptionMedication;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobType;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointIntegrationTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobHistoryDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobPrescriptionMedicationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class EprescribeJobEndpointIT
    extends AbstractEndpointIntegrationTest<EprescribeJobEndpoint> {

  private final String endpointUrl = "/v1/provider-portal/erx-jobs";
  private EprescribeJobManager jobManager;
  private EprescribeJobHistoryManager jobHistoryManager;
  private EprescribeJobPrescriptionMedicationManager erxJobMedicationManager;
  private ConversationMessageManager conversationMessageManager;

  public EprescribeJobEndpointIT() throws IOException {
    super(new EprescribeJobEndpoint(), EprescribeJobEndpoint.class);
    jobManager = new DefaultEprescribeJobManager(ds);
    jobHistoryManager = new DefaultEprescribeJobHistoryManager(ds);
    erxJobMedicationManager = new DefaultEprescribeJobPrescriptionMedicationManager(ds);
    conversationMessageManager = new DefaultConversationMessageManager(ds);
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
    servicesMap.put(EprescribeJobManager.class, jobManager);
    servicesMap.put(EprescribeJobHistoryManager.class, jobHistoryManager);
    servicesMap.put(EprescribeJobPrescriptionMedicationManager.class, erxJobMedicationManager);
    servicesMap.put(ConversationMessageManager.class, conversationMessageManager);
    return servicesMap;
  }

  @Test
  public void testCreateJob()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      BusinessLogicException {
    EprescribeJob erxJob = new EprescribeJob();
    erxJob.setJobType(getAndSetUpJobType());

    erxJob.setJobUuid(UUID.randomUUID().toString());
    erxJob.setQueuedAt(TestUtilities.nextLocalDateTime(true));

    EprescribeJobDto expected = mapDto(erxJob, EprescribeJobDto.class);

    int jobId = given()
        .body(expected)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);
    expected.setEprescribeJobId(jobId);
    EprescribeJob getById = jobManager.getEprescribeJobById(jobId);
    EprescribeJobDto actual = mapDto(getById, EprescribeJobDto.class);

    assertEquals(expected, actual);
    jobManager.deleteEprescribeJob(jobId);

  }

  @Test
  public void testUpdateJob()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      BusinessLogicException {
    EprescribeJob erxJob = setUpAndGetErxJob();
    erxJob.setJobUuid(UUID.randomUUID().toString());

    EprescribeJobDto expected = mapDto(erxJob, EprescribeJobDto.class);

    given()
        .pathParam("jobId", expected.getEprescribeJobId())
        .body(expected)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    EprescribeJob getById = jobManager.getEprescribeJobById(expected.getEprescribeJobId());

    EprescribeJobDto actual = mapDto(getById, EprescribeJobDto.class);

    assertEquals(expected, actual);
    jobManager.deleteEprescribeJob(expected.getEprescribeJobId());

  }

  @Test
  public void testGetJobById()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      BusinessLogicException {

    EprescribeJob erxJob = setUpAndGetErxJob();

    EprescribeJobDto actual = given()
        .pathParam("jobId", erxJob.getePrescribeJobId())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobDto.class);

    EprescribeJobDto expected = mapDto(erxJob, EprescribeJobDto.class);

    assertEquals(expected, actual);
    jobManager.deleteEprescribeJob(erxJob.getePrescribeJobId());

  }

  @Test
  public void testGetJobByIdNotFound() {
    given()
        .pathParam("jobId", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

  }

  @Test
  public void testGetJobByUuid()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      BusinessLogicException {

    EprescribeJob erxJob = setUpAndGetErxJob();

    EprescribeJobDto actual = given()
        .queryParam("jobUuid", erxJob.getJobUuid())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobDto.class);

    EprescribeJobDto expected = mapDto(erxJob, EprescribeJobDto.class);

    assertEquals(expected, actual);
    jobManager.deleteEprescribeJob(erxJob.getePrescribeJobId());

  }

  @Test
  public void testGetJobByMessageHeaderId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException,
      BusinessLogicException {

    EprescribeJob erxJob = setUpAndGetErxJob();

    EprescribeJobDto actual = given()
        .queryParam("messageHeaderId", erxJob.getMessageHeaderId())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobDto.class);

    EprescribeJobDto expected = mapDto(erxJob, EprescribeJobDto.class);

    assertEquals(expected, actual);
    jobManager.deleteEprescribeJob(erxJob.getePrescribeJobId());

  }

  @Test
  public void testGetJobByUuidNotFound() {
    given()
        .queryParam("jobUuid", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

  }

  @Test
  public void testDeleteJob() {
    EprescribeJob erxJob = setUpAndGetErxJob();

    given()
        .pathParam("jobId", erxJob.getePrescribeJobId())
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    try {
      jobManager.getEprescribeJobById(erxJob.getePrescribeJobId());
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }

  }

  @Test
  public void testDeleteJobNotFound() {

    given()
        .pathParam("jobId", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void testGetHistoryById() {
    EprescribeJobHistory jobHistory = setUpAndGetHistory();

    int jobId = jobHistory.getePrescribeJobId();
    int id = jobHistory.getePrescribeJobHistoryId();
    EprescribeJobHistoryDto actual = given()
        .pathParam("jobId", jobId)
        .pathParam("id", id)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl + "/{jobId}/histories/{id}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobHistoryDto.class);

    EprescribeJobHistoryDto expected = mapDto(jobHistory, EprescribeJobHistoryDto.class);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetHistoryByIdNotFound() {

    given()
        .pathParam("jobId", TestUtilities.nextInt())
        .pathParam("id", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl + "/{jobId}/histories/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

  }

  @Test
  public void testGetJobHistories() {
    EprescribeJobHistory jobHistory = setUpAndGetHistory();
    int jobId = jobHistory.getePrescribeJobId();
    List<EprescribeJobHistoryDto> expected = new ArrayList<>();
    expected.add(mapDto(jobHistory, EprescribeJobHistoryDto.class));

    List<EprescribeJobHistoryDto> actual = toCollection(given()
        .pathParam("jobId", jobId)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl + "/{jobId}/histories")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobHistoryDto[].class), ArrayList::new);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetJobHistoriesNoResult() {
    List<EprescribeJobHistoryDto> actual = toCollection(given()
        .pathParam("jobId", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl + "/{jobId}/histories")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobHistoryDto[].class), ArrayList::new);

    assertTrue(actual.size() == 0);
  }

  @Test
  public void testCreateHistory()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    EprescribeJob erxJob = setUpAndGetErxJob();
    EprescribeJobHistory history = new EprescribeJobHistory();
    history.setePrescribeJobId(erxJob.getePrescribeJobId());
    history.setTimestamp(TestUtilities.nextLocalDateTime(true));
    history.setAppErrorCode(TestUtilities.nextString(10));
    history.setStatus(TestUtilities.nextString(10));

    EprescribeJobHistoryDto expected = mapDto(history, EprescribeJobHistoryDto.class);
    int id = given()
        .body(expected)
        .pathParam("jobId", history.getePrescribeJobId())
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl + "/{jobId}/histories")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    expected.setEprescribeJobHistoryId(id);

    EprescribeJobHistory getById = jobHistoryManager.getEprescribeJobHistoryById(id);
    EprescribeJobHistoryDto actual = mapDto(getById, EprescribeJobHistoryDto.class);

    assertEquals(expected, actual);

  }

  @Test
  public void testUpdateHistoryPrescriptionLink() {


    int medicationId = 0;
    int jobId = setUpAndGetErxJob().getePrescribeJobId();
    try (Connection conn = getConnection()) {
      PrescriptionMedicationFixture medicationFixture = new PrescriptionMedicationFixture();
      medicationFixture.setUp(conn);
      medicationId = medicationFixture.get().getPrescriptionId();

    } catch (Exception ex) {
      fail("Error creating Prescription Medication / Eprescription job: " + ex.getMessage());
    }

    given().pathParam("jobId", jobId)
        .pathParam("id", medicationId)
        .when()
        .put(getBaseUrl() + endpointUrl + "/{jobId}/prescriptions/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

  }


  @Test
  public void testGetPrescriptionIdsByJobId() {

    try (Connection conn = getConnection()) {
      EprescribeJobPrescriptionMedicationFixture fixture =
          new EprescribeJobPrescriptionMedicationFixture();

      fixture.setUp(conn);
      EprescribeJobPrescriptionMedication jobPrescriptionMedication = fixture.get();

      List<EprescribeJobPrescriptionMedicationDto> expected =
          mapDto(Collections.singletonList(jobPrescriptionMedication),
              EprescribeJobPrescriptionMedicationDto.class, ArrayList::new);

      List<EprescribeJobPrescriptionMedicationDto> actual = toCollection(given()
          .pathParam("jobId", jobPrescriptionMedication.getePrescribeJobId())
          .when()
          .get(getBaseUrl() + endpointUrl + "/{jobId}/prescriptions")
          .then()
          .assertThat()
          .statusCode(Status.OK.getStatusCode())
          .extract().as(EprescribeJobPrescriptionMedicationDto[].class), ArrayList::new);

      assertEquals(expected, actual);

    } catch (Exception ex) {
      fail("Error creating Eprescription job prescription record: " + ex.getMessage());
    }

  }


  private EprescribeJobHistory setUpAndGetHistory() {
    EprescribeJobHistory jobHistory = new EprescribeJobHistory();

    try (Connection conn = getConnection()) {

      EprescribeJobHistoryFixture fixture = new EprescribeJobHistoryFixture();
      fixture.setUp(conn);
      jobHistory = fixture.get();
    } catch (Exception ex) {
      fail("Failed to create Eprescribe job history: " + ex.getMessage());
    }
    return jobHistory;
  }


  private EprescribeJobType getAndSetUpJobType() {
    EprescribeJobType jobType = new EprescribeJobType();

    try (Connection conn = getConnection()) {

      EprescribeJobTypeFixture fixture = new EprescribeJobTypeFixture();
      fixture.setUp(conn);
      jobType = fixture.get();

    } catch (Exception e) {
      fail("Failed to create eprescribe job type: " + e.getMessage());
    }

    return jobType;
  }

  private EprescribeJob setUpAndGetErxJob() {
    EprescribeJob erxJob = new EprescribeJob();
    try (Connection conn = getConnection()) {

      EprescribeJobFixture fixture = new EprescribeJobFixture();
      fixture.setUp(conn);
      erxJob = fixture.get();

    } catch (Exception ex) {
      fail("Error creating Erx job: " + ex.getMessage());
    }
    return erxJob;
  }

}

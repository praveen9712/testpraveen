
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobHistoryManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobPrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJob;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobHistory;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobPrescriptionMedication;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessage;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobHistoryDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobPrescriptionMedicationDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class EprescribeJobEndpointTest extends AbstractEndpointTest<EprescribeJobEndpoint> {

  private final String endpointUrl = "/v1/provider-portal/erx-jobs";
  EprescribeJobManager manager;
  private EprescribeJobHistoryManager jobHistoryManager;
  private EprescribeJobPrescriptionMedicationManager erxJobMedicationManager;
  private ConversationMessageManager conversationMessageManager;

  public EprescribeJobEndpointTest() {
    super(new EprescribeJobEndpoint(), EprescribeJobEndpoint.class);
    manager = mock(EprescribeJobManager.class);
    jobHistoryManager = mock(EprescribeJobHistoryManager.class);
    erxJobMedicationManager = mock(EprescribeJobPrescriptionMedicationManager.class);
    conversationMessageManager = mock(ConversationMessageManager.class);
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
    servicesMap.put(EprescribeJobManager.class, manager);
    servicesMap.put(EprescribeJobHistoryManager.class, jobHistoryManager);
    servicesMap.put(EprescribeJobPrescriptionMedicationManager.class, erxJobMedicationManager);
    servicesMap.put(ConversationMessageManager.class, conversationMessageManager);
    return servicesMap;
  }

  @Test
  public void testCreate()
      throws SupportingResourceNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ResourceConflictException {
    EprescribeJobDto jobDto = getFixture(EprescribeJobDto.class);
    jobDto.setJobUuid(UUID.randomUUID().toString());
    jobDto.setQueuedAt(TestUtilities.nextLocalDateTime(false));
    jobDto.setProcessedDate(TestUtilities.nextLocalDateTime(false));
    EprescribeJob job = mapDto(jobDto, EprescribeJob.class);
    int expected = job.getePrescribeJobId();

    doReturn(expected).when(manager).createEprescribeJob(job);

    ConversationMessage message = getFixture(ConversationMessage.class);
    message.setConversationId(jobDto.getConversationId());
    message.setMessageId(jobDto.getConversationMessageId());

    when(conversationMessageManager.getMessageById(jobDto.getConversationMessageId()))
        .thenReturn(message);

    int actual = given()
        .body(jobDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    verify(manager).createEprescribeJob(job);
    assertEquals(expected, actual);
  }

  @Test
  public void testCreateInvalidUuid() {
    EprescribeJobDto jobDto = getFixture(EprescribeJobDto.class);

    given()
        .body(jobDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);
  }

  @Test
  public void testCreateWithEmptyBody()
      throws SupportingResourceNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ResourceConflictException {
    given()
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(manager, never()).createEprescribeJob(any());
  }

  @Test
  public void testCreateWithoutJobTypeId()
      throws Exception {
    EprescribeJobDto jobDto = getFixture(EprescribeJobDto.class);
    jobDto.setJobUuid(UUID.randomUUID().toString());
    jobDto.setQueuedAt(TestUtilities.nextLocalDateTime(false));
    jobDto.setProcessedDate(TestUtilities.nextLocalDateTime(false));
    jobDto.getJobType().setEprescribeJobTypeId(0);
    given()
        .body(jobDto)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(manager, never()).createEprescribeJob(any());
  }

  @Test
  public void testUpdate()
      throws SupportingResourceNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ResourceConflictException, NoDataFoundException {
    EprescribeJobDto jobDto = getFixture(EprescribeJobDto.class);
    jobDto.setJobUuid(UUID.randomUUID().toString());
    jobDto.setQueuedAt(TestUtilities.nextLocalDateTime(false));
    jobDto.setProcessedDate(TestUtilities.nextLocalDateTime(false));

    ConversationMessage message = getFixture(ConversationMessage.class);
    message.setConversationId(jobDto.getConversationId());
    message.setMessageId(jobDto.getConversationMessageId());

    when(conversationMessageManager.getMessageById(jobDto.getConversationMessageId()))
        .thenReturn(message);
    EprescribeJob job = mapDto(jobDto, EprescribeJob.class);

    given()
        .pathParam("jobId", job.getePrescribeJobId())
        .body(jobDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).updateEprescribeJob(job);
  }

  @Test
  public void testUpdateDiffConversationId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    EprescribeJobDto jobDto = getFixture(EprescribeJobDto.class);
    jobDto.setJobUuid(UUID.randomUUID().toString());
    jobDto.setQueuedAt(TestUtilities.nextLocalDateTime(false));
    jobDto.setProcessedDate(TestUtilities.nextLocalDateTime(false));

    ConversationMessage message = getFixture(ConversationMessage.class);

    when(conversationMessageManager.getMessageById(jobDto.getConversationMessageId()))
        .thenReturn(message);
    EprescribeJob job = mapDto(jobDto, EprescribeJob.class);

    given()
        .pathParam("jobId", job.getePrescribeJobId())
        .body(jobDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);
    verify(conversationMessageManager).getMessageById(jobDto.getConversationMessageId());
  }

  @Test
  public void testUpdateDiffPathParamAndDtoId() {
    EprescribeJobDto jobDto = getFixture(EprescribeJobDto.class);
    jobDto.setJobUuid(UUID.randomUUID().toString());
    jobDto.setQueuedAt(TestUtilities.nextLocalDateTime(false));
    jobDto.setProcessedDate(TestUtilities.nextLocalDateTime(false));

    given()
        .pathParam("jobId", RandomUtils.nextInt())
        .body(jobDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);
    verifyNoInteractions(conversationMessageManager);
  }

  @Test
  public void testUpdateMessageNotFound()
      throws UnsupportedSchemaVersionException,
      DatabaseInteractionException {
    EprescribeJobDto jobDto = getFixture(EprescribeJobDto.class);
    jobDto.setJobUuid(UUID.randomUUID().toString());
    jobDto.setQueuedAt(TestUtilities.nextLocalDateTime(false));
    jobDto.setProcessedDate(TestUtilities.nextLocalDateTime(false));

    when(conversationMessageManager.getMessageById(jobDto.getConversationMessageId()))
        .thenReturn(null);
    EprescribeJob job = mapDto(jobDto, EprescribeJob.class);

    given()
        .pathParam("jobId", job.getePrescribeJobId())
        .body(jobDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);

    verify(conversationMessageManager).getMessageById(jobDto.getConversationMessageId());
  }

  @Test
  public void testUpdateWithoutJobTypeId() {
    EprescribeJobDto jobDto = getFixture(EprescribeJobDto.class);
    jobDto.setJobUuid(UUID.randomUUID().toString());
    jobDto.setQueuedAt(TestUtilities.nextLocalDateTime(false));
    jobDto.setProcessedDate(TestUtilities.nextLocalDateTime(false));
    jobDto.getJobType().setEprescribeJobTypeId(0);

    given()
        .pathParam("jobId", jobDto.getEprescribeJobId())
        .body(jobDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);
  }

  @Test
  public void testUpdateWithDifferentMessageId()
      throws Exception {
    EprescribeJobDto jobDto = getFixture(EprescribeJobDto.class);
    jobDto.getJobType().setEprescribeJobTypeId(0);
    EprescribeJob job = mapDto(jobDto, EprescribeJob.class);

    given()
        .pathParam("jobId", job.getePrescribeJobId())
        .body(jobDto)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(manager, never()).updateEprescribeJob(any());
  }

  @Test
  public void testUpdateWithEmptyBody()
      throws SupportingResourceNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ResourceConflictException, NoDataFoundException {
    int jobId = TestUtilities.nextInt();

    given()
        .pathParam("jobId", jobId)
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(manager, never()).updateEprescribeJob(any());
  }

  @Test
  public void testUpdateWhenPathIdIsDifferentWithJobId()
      throws SupportingResourceNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ResourceConflictException, NoDataFoundException {
    int pathId = TestUtilities.nextInt();

    given()
        .pathParam("jobId", pathId)
        .body(getFixture(EprescribeJobDto.class))
        .when()
        .contentType(ContentType.JSON)
        .put(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(manager, never()).updateEprescribeJob(any());
  }

  @Test
  public void testDelete()
      throws DatabaseInteractionException, NoDataFoundException, UnsupportedSchemaVersionException,
      BusinessLogicException {
    int id = TestUtilities.nextInt();

    given()
        .pathParam("jobId", id)
        .when()
        .contentType(ContentType.JSON)
        .delete(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(manager).deleteEprescribeJob(id);
  }

  @Test
  public void testGetById()
      throws DatabaseInteractionException, NoDataFoundException, UnsupportedSchemaVersionException {
    EprescribeJobDto expected = getFixture(EprescribeJobDto.class);
    EprescribeJob eprescribeJob = mapDto(expected, EprescribeJob.class);

    doReturn(eprescribeJob).when(manager).getEprescribeJobById(eprescribeJob.getePrescribeJobId());

    EprescribeJobDto actual = given()
        .pathParam("jobId", eprescribeJob.getePrescribeJobId())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl + "/{jobId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobDto.class);

    assertEquals(expected, actual);
    verify(manager).getEprescribeJobById(eprescribeJob.getePrescribeJobId());
  }

  @Test
  public void testGetByUuId()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    EprescribeJobDto expected = getFixture(EprescribeJobDto.class);
    EprescribeJob eprescribeJob = mapDto(expected, EprescribeJob.class);

    doReturn(eprescribeJob).when(manager).getEprescribeJobByUuid(eprescribeJob.getJobUuid());

    EprescribeJobDto actual = given()
        .queryParam("jobUuid", eprescribeJob.getJobUuid())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobDto.class);

    assertEquals(expected, actual);
    verify(manager).getEprescribeJobByUuid(eprescribeJob.getJobUuid());
  }

  @Test
  public void testGetByMessageHeaderId()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    EprescribeJobDto expected = getFixture(EprescribeJobDto.class);
    EprescribeJob eprescribeJob = mapDto(expected, EprescribeJob.class);

    doReturn(eprescribeJob).when(manager)
        .getEprescribeJobByMessageHeaderId(eprescribeJob.getMessageHeaderId());

    EprescribeJobDto actual = given()
        .queryParam("messageHeaderId", eprescribeJob.getMessageHeaderId())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobDto.class);

    assertEquals(expected, actual);
    verify(manager).getEprescribeJobByMessageHeaderId(eprescribeJob.getMessageHeaderId());
  }

  @Test
  public void testGetByBothQueryParams() {

    EprescribeJob eprescribeJob = getFixture(EprescribeJob.class);

    given()
        .queryParam("messageHeaderId", eprescribeJob.getMessageHeaderId())
        .queryParam("jobUuid", eprescribeJob.getJobUuid())
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);
  }

  @Test
  public void testGetByNoQueryParams() {

    given()
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(manager);
  }

  @Test
  public void testCreateHistory()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      BusinessLogicException {
    EprescribeJobHistoryDto jobHistoryDto = getFixture(EprescribeJobHistoryDto.class);
    jobHistoryDto.setTimestamp(TestUtilities.nextLocalDateTime(false));
    int jobId = jobHistoryDto.getEprescribeJobId();
    int expected = jobHistoryDto.getEprescribeJobHistoryId();

    EprescribeJobHistory protossModel = mapDto(jobHistoryDto, EprescribeJobHistory.class);

    when(jobHistoryManager.createEprescriptionJobHistory(protossModel)).thenReturn(expected);

    int actual = given()
        .body(jobHistoryDto)
        .pathParam("jobId", jobId)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl + "/{jobId}/histories")
        .then()
        .assertThat()
        .statusCode(Status.CREATED.getStatusCode())
        .extract().as(Integer.class);

    assertEquals(expected, actual);
    verify(jobHistoryManager).createEprescriptionJobHistory(protossModel);
  }

  @Test
  public void testCreateIdNotMatched() {
    EprescribeJobHistoryDto jobHistoryDto = getFixture(EprescribeJobHistoryDto.class);
    jobHistoryDto.setTimestamp(TestUtilities.nextLocalDateTime(false));
    int jobId = TestUtilities.nextInt();

    given()
        .body(jobHistoryDto)
        .pathParam("jobId", jobId)
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl + "/{jobId}/histories")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(jobHistoryManager);
  }

  @Test
  public void testCreateNullBody() {

    given()
        .pathParam("jobId", TestUtilities.nextInt())
        .when()
        .contentType(ContentType.JSON)
        .post(getBaseUrl() + endpointUrl + "/{jobId}/histories")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verifyNoInteractions(jobHistoryManager);
  }

  @Test
  public void testGetHistoryById()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    EprescribeJobHistoryDto expected = getFixture(EprescribeJobHistoryDto.class);
    EprescribeJobHistory protossModel = mapDto(expected, EprescribeJobHistory.class);
    int jobId = expected.getEprescribeJobId();
    int id = expected.getEprescribeJobHistoryId();
    when(jobHistoryManager.getEprescribeJobHistoryById(id)).thenReturn(protossModel);

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

    assertEquals(expected, actual);
    verify(jobHistoryManager).getEprescribeJobHistoryById(id);

  }

  @Test
  public void testGetByIdNotFound()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {
    EprescribeJobHistoryDto expected = getFixture(EprescribeJobHistoryDto.class);
    EprescribeJobHistory protossModel = mapDto(expected, EprescribeJobHistory.class);
    int jobId = TestUtilities.nextInt();
    int id = expected.getEprescribeJobHistoryId();
    when(jobHistoryManager.getEprescribeJobHistoryById(id)).thenReturn(protossModel);

    given()
        .pathParam("jobId", jobId)
        .pathParam("id", id)
        .when()
        .contentType(ContentType.JSON)
        .get(getBaseUrl() + endpointUrl + "/{jobId}/histories/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(jobHistoryManager).getEprescribeJobHistoryById(id);

  }

  @Test
  public void testGetByJobId()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException, NoDataFoundException {

    List<EprescribeJobHistoryDto> expected =
        getFixtures(EprescribeJobHistoryDto.class, ArrayList::new, 10);

    int jobId = TestUtilities.nextInt();
    List<EprescribeJobHistory> eprescribeJobHistory =
        mapDto(expected, EprescribeJobHistory.class, ArrayList::new);
    when(jobHistoryManager.getAllJobHistory(jobId)).thenReturn(eprescribeJobHistory);
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
    verify(jobHistoryManager).getAllJobHistory(jobId);

  }

  @Test
  public void testUpdateLink()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      SupportingResourceNotFoundException, NoDataFoundException {
    int jobId = TestUtilities.nextInt();
    int id = TestUtilities.nextInt();

    EprescribeJobPrescriptionMedication jobPrescriptionMedication =
        new EprescribeJobPrescriptionMedication();
    jobPrescriptionMedication.setePrescribeJobId(jobId);
    jobPrescriptionMedication.setPrescriptionMedicationId(id);

    when(erxJobMedicationManager.getEprescribeJobPrescriptionMedication(jobId, id))
        .thenReturn(null);

    doNothing().when(erxJobMedicationManager)
        .createEprescribeJobPrescriptionMedication(jobPrescriptionMedication);

    given().pathParam("jobId", jobId)
        .pathParam("id", id)
        .when()
        .put(getBaseUrl() + endpointUrl + "/{jobId}/prescriptions/{id}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(erxJobMedicationManager)
        .createEprescribeJobPrescriptionMedication(jobPrescriptionMedication);


  }

  @Test
  public void testUpdateLinkBadRequest()
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      SupportingResourceNotFoundException, NoDataFoundException {
    int jobId = TestUtilities.nextInt();
    int id = TestUtilities.nextInt();

    EprescribeJobPrescriptionMedication jobPrescriptionMedication =
        new EprescribeJobPrescriptionMedication();
    jobPrescriptionMedication.setePrescribeJobId(jobId);
    jobPrescriptionMedication.setPrescriptionMedicationId(id);

    when(erxJobMedicationManager.getEprescribeJobPrescriptionMedication(jobId, id))
        .thenReturn(new EprescribeJobPrescriptionMedication());

    given().pathParam("jobId", jobId)
        .pathParam("id", id)
        .when()
        .put(getBaseUrl() + endpointUrl + "/{jobId}/prescriptions/{id}")
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(erxJobMedicationManager, never())
        .createEprescribeJobPrescriptionMedication(jobPrescriptionMedication);
  }

  @Test
  public void testGetPrescriptionIdsByJobId()
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    List<EprescribeJobPrescriptionMedicationDto> expected =
        getFixtures(EprescribeJobPrescriptionMedicationDto.class, ArrayList::new, 10);
    List<EprescribeJobPrescriptionMedication> protossResult =
        mapDto(expected, EprescribeJobPrescriptionMedication.class, ArrayList::new);

    int jobId = TestUtilities.nextInt();
    doReturn(protossResult).when(erxJobMedicationManager)
        .getEprescribeJobPrescriptionMedicationByJobId(jobId);

    List<EprescribeJobPrescriptionMedicationDto> actual = toCollection(given()
        .pathParam("jobId", jobId)
        .when()
        .get(getBaseUrl() + endpointUrl + "/{jobId}/prescriptions")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract().as(EprescribeJobPrescriptionMedicationDto[].class), ArrayList::new);

    assertEquals(expected, actual);
    verify(erxJobMedicationManager).getEprescribeJobPrescriptionMedicationByJobId(jobId);
  }

}


package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobHistoryManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobPrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageManager;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJob;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobHistory;
import com.qhrtech.emr.accuro.model.eprescribe.EprescribeJobPrescriptionMedication;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.synapse.ConversationMessage;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobHistoryDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.EprescribeJobPrescriptionMedicationDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermissions;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Endpoints for Eprescribe job.
 *
 * @RequestHeader Authorization Provider level authorization grant or client credentials with first
 *                party QHR scope
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/erx-jobs")
@Facet("provider-portal")
@Tag(name = "Eprescribe job Endpoints",
    description = "Exposes eprescribe job endpoints")
public class EprescribeJobEndpoint extends AbstractEndpoint {

  /**
   * Creates a new e-prescribe job. Mandatory fields are: queuedAt, jobUuid and
   * ePrescribeJobTypeId(pre-existing value) in JobType object. We are not creating ePrescribe
   * JobType here, we are using pre-existing value to create the Job.
   *
   * @param jobDto JSON representation of the e-prescribe job
   * @return the e-prescribe job id
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @throws ResourceConflictException If the message header id is not unique
   * @throws SupportingResourceNotFoundException if the job type does not exist or is provided as
   *         null
   * @HTTP 201 created
   * @HTTP 400 invalid request entity
   */
  @POST
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.create' ) ")
  @Operation(
      summary = "Creates a new e-prescribe job",
      description = "Creates a new e-prescribe job. Mandatory fields are: queuedAt, jobUuid"
          + " and ePrescribeJobTypeId(pre-existing value) in JobType object. "
          + " We are not creating ePrescribe JobType"
          + " here, we are using pre-existing value to create the Job.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "invalid request entity"),
          @ApiResponse(
              responseCode = "201",
              description = "created",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns a new e-prescribe job id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response create(
      @RequestBody(description = "New Eprescribe job") @Valid EprescribeJobDto jobDto)
      throws SupportingResourceNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ResourceConflictException {
    if (jobDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Eprescribe information is missing.");
    }
    if (jobDto.getJobType().getEprescribeJobTypeId() < 1) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Eprescribe job type Id is required field and its value cannot be less than zero.");
    }

    // to check the the message Id belongs to the conversation Id given
    checkValidMessageId(jobDto);
    EprescribeJobManager manager = getImpl(EprescribeJobManager.class);

    int id = manager.createEprescribeJob(mapDto(jobDto, EprescribeJob.class));
    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Updates e-precribe job.The fields belonging to JobType object are read-only cannot be updated
   * through this request.
   *
   * @param jobId e-prescribe job id
   * @param jobDto JSON representation of the e-prescribe job
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws NoDataFoundException If the resource for the operation doesn't exist.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @throws ResourceConflictException If the message header id is not unique
   * @throws SupportingResourceNotFoundException if the job type does not exist or is provided as
   *         null
   * @HTTP 204 no content after update
   * @HTTP 400 invalid request entity
   */
  @PUT
  @Path("/{jobId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.update' ) ")
  @Operation(
      summary = "Updates e-prescribe job",
      description = "Updates e-prescribe job. The fields belonging to JobType object are "
          + "read-only cannot be updated through this request",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "invalid request entity"),
          @ApiResponse(
              responseCode = "204",
              description = "No content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public void update(@Parameter(description = "Eprescribe job id") @PathParam("jobId") int jobId,
      @RequestBody @Valid EprescribeJobDto jobDto)
      throws SupportingResourceNotFoundException, UnsupportedSchemaVersionException,
      DatabaseInteractionException, ResourceConflictException, NoDataFoundException {
    if (jobDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Eprescribe job information is missing.");
    }
    if (jobDto.getEprescribeJobId() != jobId) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid eprescribe job id");
    }
    if (jobDto.getJobType().getEprescribeJobTypeId() < 1) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Eprescribe job type Id is required field and its value cannot be less than zero.");
    }
    // to check the the message Id belongs to the conversation Id given
    checkValidMessageId(jobDto);

    EprescribeJobManager manager = getImpl(EprescribeJobManager.class);
    manager.updateEprescribeJob(mapDto(jobDto, EprescribeJob.class));
  }

  /**
   * Deletes e-prescribe job.
   *
   * @param jobId e-prescribe job id
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws NoDataFoundException If the resource for the operation doesn't exist.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   * @HTTP 204 no content after delete
   */
  @DELETE
  @Path("/{jobId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.delete' ) ")
  @Operation(
      summary = "Deletes e-prescribe job",
      description = "Deletes e-prescribe job.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public void delete(@Parameter(description = "Eprescribe job id") @PathParam("jobId") int jobId)
      throws DatabaseInteractionException, NoDataFoundException, UnsupportedSchemaVersionException,
      BusinessLogicException {
    EprescribeJobManager manager = getImpl(EprescribeJobManager.class);
    manager.deleteEprescribeJob(jobId);
  }

  /**
   * Retrieves e-prescribe job associated with the given job id.
   *
   * @param jobId e-prescribe job id
   * @return The e-prescribe job
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws NoDataFoundException If any required resource for the operation doesn't exist.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   */
  @GET
  @Path("/{jobId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.read' ) ")
  @Operation(
      summary = "Retrieves e-prescribe job",
      description = "Retrieves e-prescribe job.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "success",
              content = @Content(schema = @Schema(implementation = EprescribeJobDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public EprescribeJobDto getById(
      @Parameter(description = "Eprescribe job id") @PathParam("jobId") int jobId)
      throws DatabaseInteractionException, NoDataFoundException, UnsupportedSchemaVersionException {
    EprescribeJobManager manager = getImpl(EprescribeJobManager.class);
    return mapDto(manager.getEprescribeJobById(jobId), EprescribeJobDto.class);
  }

  /**
   * Retrieves e-prescribe job associated with the given job uuid or message header uuid.
   *
   * @param jobUuid e-prescribe job uuid. Required. The result is returned on basis of exact match.
   * @param messageHeaderId e-prescribe message header id. The result is returned on basis of exact
   *        match.
   * @return The e-prescribe job
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.read' ) ")
  @Operation(
      summary = "Retrieves e-prescribe job",
      description = "Retrieves e-prescribe job. Either of JobUuid or message header id is required."
          + " The result is returned on basis of exact match.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "success",
              content = @Content(schema = @Schema(implementation = EprescribeJobDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant or client credentials "
                  + " with first party QHR scope",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "jobUuid",
              description = "Eprescribe job uuid.",
              in = ParameterIn.QUERY),
          @Parameter(
              name = "messageHeaderId",
              description = "Eprescribe Message Header id.",
              in = ParameterIn.QUERY)})
  public EprescribeJobDto getByUuid(@Parameter(hidden = true) @QueryParam("jobUuid") String jobUuid,
      @Parameter(hidden = true) @QueryParam("messageHeaderId") String messageHeaderId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {

    if (StringUtils.isBlank(jobUuid) && StringUtils.isBlank(messageHeaderId)) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "One of job uuid or message header id is required.");
    }

    if (StringUtils.isNotBlank(jobUuid) && StringUtils.isNotBlank(messageHeaderId)) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Only one of job uuid or message header id should be provided.");
    }

    EprescribeJobManager manager = getImpl(EprescribeJobManager.class);

    EprescribeJob protossResult = null;

    if (StringUtils.isNotBlank(jobUuid)) {
      protossResult = manager.getEprescribeJobByUuid(jobUuid);
    }

    if (StringUtils.isNotBlank(messageHeaderId)) {
      protossResult = manager.getEprescribeJobByMessageHeaderId(messageHeaderId);
    }

    return mapDto(protossResult, EprescribeJobDto.class);
  }

  /**
   * Gets All the Eprescribe Job Histories by Job ID.
   *
   * @param id Eprescribe Job id
   * @return List of {@link EprescribeJobHistoryDto} object
   * @throws ProtossException If there is any exception occured.
   */
  @GET
  @Path("/{id}/histories")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.read' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly)
  @Operation(
      summary = "Retrieves Eprescribe Job Histories by Job id",
      description = "Retrieves Eprescribe Job Histories by Job id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = EprescribeJobHistoryDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public List<EprescribeJobHistoryDto> getHistoriesByJobId(
      @Parameter(description = "Eprescribe job id") @PathParam("id") int id)
      throws ProtossException {

    EprescribeJobHistoryManager manager = getImpl(EprescribeJobHistoryManager.class);
    return mapDto(manager.getAllJobHistory(id), EprescribeJobHistoryDto.class, ArrayList::new);
  }

  /**
   * Gets Eprescribe Job History by ID.
   *
   * @param id Eprescribe Job History id
   * @return {@link EprescribeJobHistoryDto} object
   * @throws ProtossException If there is any exception occured.
   */
  @GET
  @Path("/{jobId}/histories/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.read' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly)
  @Operation(
      summary = "Retrieves Eprescribe Job Histories by id",
      description = "Retrieves Eprescribe Job Histories by id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = EprescribeJobHistoryDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public EprescribeJobHistoryDto getHistoryById(
      @Parameter(description = "Eprescribe job id") @PathParam("jobId") int jobId,
      @Parameter(description = "Eprescribe job history id") @PathParam("id") int id)
      throws ProtossException {

    EprescribeJobHistoryManager manager = getImpl(EprescribeJobHistoryManager.class);
    EprescribeJobHistory eprescribeJobHistoryById = manager.getEprescribeJobHistoryById(id);
    if (eprescribeJobHistoryById == null
        || eprescribeJobHistoryById.getePrescribeJobId() != jobId) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Eprescribe job history not found with job id: " + jobId);
    }

    EprescribeJobHistoryDto jobHistoryDto =
        mapDto(eprescribeJobHistoryById, EprescribeJobHistoryDto.class);
    return jobHistoryDto;
  }

  /**
   * Creates Eprescribe Job History.
   *
   * @param jobId Eprescribe Job id
   * @return ID of the newly created record.
   * @throws ProtossException If there is any exception occured.
   */
  @POST
  @Path("/{jobId}/histories")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.create' ) ")
  @ProviderPermissions(
      providerPermissions = {
          @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full),
          @ProviderPermission(type = AccessType.Prescription, level = AccessLevel.Full)})
  @Operation(
      summary = "Creates Eprescribe Job History",
      description = "Creates Eprescribe Job History",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Invalid data"),
          @ApiResponse(
              responseCode = "403",
              description = "If no sufficient roles or features"),
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(implementation = Integer.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response createHistory(@Valid EprescribeJobHistoryDto jobHistoryDto,
      @Parameter(description = "Eprescribe job id") @PathParam("jobId") int jobId)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException,
      BusinessLogicException {

    if (jobHistoryDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Eprescribe Job History cannot be null.");
    }

    if (jobHistoryDto.getEprescribeJobId() != jobId) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Eprescribe job id in body does not match the id in the url.");
    }
    EprescribeJobHistoryManager manager = getImpl(EprescribeJobHistoryManager.class);
    int id =
        manager.createEprescriptionJobHistory(mapDto(jobHistoryDto, EprescribeJobHistory.class));

    return Response.status(Status.CREATED).entity(id).build();
  }

  /**
   * Updates Eprescribe Job prescription medication, creates a link between job and prescription
   * medication.
   *
   * @param jobId Eprescribe Job id
   * @param id Prescription Medication Id
   * @return 204 response if success
   * @throws ProtossException If there is any exception occured.
   */
  @PUT
  @Path("/{jobId}/prescriptions/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.update' ) ")
  @ProviderPermissions(
      providerPermissions = {
          @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full),
          @ProviderPermission(type = AccessType.Prescription, level = AccessLevel.Full)})
  @Operation(
      summary = "Updates Eprescribe Job prescription medication",
      description = "Updates Eprescribe Job prescription medication,  "
          + "creates a link between jobs and prescription medications",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success. No content.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public Response updateHistory(
      @Parameter(description = "Eprescribe job id") @PathParam("jobId") int jobId,
      @Parameter(description = "Prescription Medication id") @PathParam("id") int id)
      throws ProtossException {

    EprescribeJobPrescriptionMedicationManager manager =
        getImpl(EprescribeJobPrescriptionMedicationManager.class);

    // Check duplication
    EprescribeJobPrescriptionMedication existing =
        manager.getEprescribeJobPrescriptionMedication(jobId, id);
    if (existing != null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          String.format(
              "Link of the eprescribe job id(%d) to the prescription id(%d) already exists.",
              jobId, id));
    }

    EprescribeJobPrescriptionMedication jobPrescriptionMedication =
        new EprescribeJobPrescriptionMedication();
    jobPrescriptionMedication.setePrescribeJobId(jobId);
    jobPrescriptionMedication.setPrescriptionMedicationId(id);

    manager.createEprescribeJobPrescriptionMedication(jobPrescriptionMedication);
    return Response.status(Status.NO_CONTENT).build();
  }

  /**
   * Retrieves list of {@link EprescribeJobPrescriptionMedicationDto} associated with the given job
   * id.
   *
   * @param jobId e-prescribe job id
   * @return The list of {@link EprescribeJobPrescriptionMedicationDto}
   * @throws DatabaseInteractionException If an error occurs while interacting with the data source.
   * @throws UnsupportedSchemaVersionException If a schema version is not supported for the
   *         operation.
   */
  @GET
  @Path("/{jobId}/prescriptions")
  @PreAuthorize("#oauth2.hasAnyScope( 'user/provider.RxJob.read' ) ")
  @Operation(
      summary = "Retrieves prescription ids associated with e-prescribe job id",
      description = "Retrieves prescription ids associated with e-prescribe job id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(
                          implementation = EprescribeJobPrescriptionMedicationDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant or client credentials "
          + " with first party QHR scope",
      in = ParameterIn.HEADER,
      required = true)
  public List<EprescribeJobPrescriptionMedicationDto> getPrescriptionIdsForJobId(
      @Parameter(description = "eprescribe job id") @PathParam("jobId") int jobId)
      throws DatabaseInteractionException, UnsupportedSchemaVersionException {
    EprescribeJobPrescriptionMedicationManager manager =
        getImpl(EprescribeJobPrescriptionMedicationManager.class);
    return mapDto(manager.getEprescribeJobPrescriptionMedicationByJobId(jobId),
        EprescribeJobPrescriptionMedicationDto.class, ArrayList::new);
  }

  private void checkValidMessageId(EprescribeJobDto jobDto)
      throws UnsupportedSchemaVersionException, DatabaseInteractionException {
    if (null != jobDto.getConversationId() && null != jobDto.getConversationMessageId()) {
      ConversationMessageManager manager = getImpl(ConversationMessageManager.class);
      ConversationMessage message = manager.getMessageById(jobDto.getConversationMessageId());
      if (null != message) {
        if (message.getConversationId() != jobDto.getConversationId()) {
          throw Error.webApplicationException(Status.BAD_REQUEST,
              "Conversation message id do not belong to the provided conversation id.");
        }
      } else {
        throw Error.webApplicationException(Status.BAD_REQUEST,
            "Conversation message not found with id: " + jobDto.getConversationMessageId());
      }
    }
  }
}

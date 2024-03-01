
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.patient.PatientStatusManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.patient.PatientStatus;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.PatientStatusDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/provider-portal/patient-statuses")
@Facet("provider-portal")
@Tag(name = "Patient status Endpoints", description = "Exposes patient status endpoints")
public class PatientStatusEndpoint extends AbstractEndpoint {

  /**
   * Retrieves patient status associated with the status id.
   *
   * @param id Patient status ID
   * @return Patient status associated with the status id
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the patient status does not exist in the database.
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient status",
      description = "Retrieves patient status associated with the status id.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Patient status not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientStatusDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public PatientStatusDto getById(
      @Parameter(description = "Patient status id") @PathParam("id") Integer id)
      throws ProtossException {
    PatientStatusManager manager = getImpl(PatientStatusManager.class);
    PatientStatus patientStatus = manager.getByStatusId(id);
    return mapDto(patientStatus, PatientStatusDto.class);
  }

  /**
   * Retrieves all patient statuses. Results order by {@link PatientStatusDto#getStatusOrder()}.
   *
   * @return The list of {@link PatientStatusDto patient status}.
   * @throws DataAccessException If there is a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DEMOGRAPHICS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves patient statuses",
      description = "Retrieve all patient statuses."
          + " Results are order by status order",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PatientStatusDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<PatientStatusDto> getAll()
      throws ProtossException {
    PatientStatusManager manager = getImpl(PatientStatusManager.class);
    List<PatientStatus> patientStatuses = manager.getAll();
    return mapDto(patientStatuses, PatientStatusDto.class, ArrayList::new);
  }

  /**
   * Create a new patient status.
   * <p>
   * The field status order is set to '0' if negative/NULL value is passed and set to last in order
   * if the given order is bigger than max order. And all other statuses orders are changed
   * accordingly. Status core is set to true by default as it is in accuro. And colour is set to
   * default '0' - black.
   * </p>
   *
   * @param patientStatusDto JSON representation of the patient status fields
   * @return Response with the patient status ID.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 on field validations.
   */
  @Operation(
      summary = "Creates a new patient status",
      description = "Creates a new patient status."
          + " The field status order is set to '0' if negative/NULL"
          + " value is passed and set to last in order if"
          + " the given order is bigger"
          + " than max order. And all other statuses "
          + " orders are changed accordingly."
          + " Status core is set to true by default as"
          + " it is in accuro. And colour is set to default '0' - black.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "On fields validation"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "integer",
                      example = "1001",
                      description = "Returns patient status id")))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Hidden
  @Facet("internal")
  @Consumes(MediaType.APPLICATION_JSON)
  @POST
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')"
      + "and #accuro.hasAccess('PATIENT_DEMOGRAPHICS', 'READ_WRITE')")
  public Response createPatientStatus(
      @RequestBody(description = "The new patient status") @Valid PatientStatusDto patientStatusDto)
      throws ProtossException {
    if (patientStatusDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient status information is missing");
    }
    PatientStatusManager manager = getImpl(PatientStatusManager.class);
    PatientStatus patientStatus = mapDto(patientStatusDto, PatientStatus.class);

    // set defaults for create
    patientStatus.setCoreStatus(true);
    patientStatus.setStatusColor(0);

    int id = manager.create(patientStatus);
    return Response.status(Status.CREATED).entity(id).build();

  }

  /**
   * Update patient status.
   *
   * <p>
   * The field status order is set to '0' if negative/NULL value is passed and set to last in order
   * if the given order is bigger than max order. And all other statuses orders are changed
   * accordingly. Status color and core status will be set to '0' - black and false respectively if
   * not provided/NULL.
   * </p>
   *
   * @param patientStatusDto JSON representation of the patient status fields
   * @return Response.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 on field validations.
   */
  @Operation(
      summary = "Updates patient status",
      description = "Updates patient status."
          + " The field status order is set to '0' if negative/NULL"
          + " value is passes and set to last in order if"
          + " the given order is bigger than max order. And all other statuses "
          + " orders are changed accordingly. Status color and core status will be set to "
          + "'0' - black and false respectively if not given/NULL.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If resource not found"),
          @ApiResponse(
              responseCode = "400",
              description = "On fields validation"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @PUT
  @Path("/{id}")
  @Hidden
  @Facet("internal")
  @Consumes(MediaType.APPLICATION_JSON)
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')"
      + "and #accuro.hasAccess('PATIENT_DEMOGRAPHICS', 'READ_WRITE')")
  public Response updatePatientStatus(
      @Parameter(description = "Patient status id") @PathParam("id") Integer id,
      @RequestBody(description = "Patient status") @Valid PatientStatusDto patientStatusDto)
      throws ProtossException {
    if (patientStatusDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Patient status information is missing");
    }
    if (id != patientStatusDto.getStatusId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Id in the body doesn't match with the Id in the url");
    }
    PatientStatusManager manager = getImpl(PatientStatusManager.class);

    PatientStatus statusUpdated = mapDto(patientStatusDto, PatientStatus.class);

    manager.update(statusUpdated);

    return Response.status(Status.NO_CONTENT).build();

  }

  /**
   * Delete patient status.
   *
   * @param id status id
   * @return 201 Response.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If resource not found.
   */
  @Operation(
      summary = "Deletes patient status",
      description = "Deletes patient status.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If resource not found"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.PATH,
      required = true)
  @DELETE
  @Path("/{id}")
  @Hidden
  @Facet("internal")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')"
      + "and #accuro.hasAccess('PATIENT_DEMOGRAPHICS', 'READ_WRITE')")
  public Response deletePatientStatus(
      @Parameter(description = "Patient status id") @PathParam("id") Integer id)
      throws ProtossException {

    PatientStatusManager manager = getImpl(PatientStatusManager.class);

    manager.delete(id);

    return Response.status(Status.NO_CONTENT).build();
  }

}

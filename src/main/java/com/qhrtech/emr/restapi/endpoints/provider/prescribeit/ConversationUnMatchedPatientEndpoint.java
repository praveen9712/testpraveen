
package com.qhrtech.emr.restapi.endpoints.provider.prescribeit;

import com.qhrtech.emr.accuro.api.synapse.ConversationUnmatchedPatientManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.synapse.ConversationUnmatchedPatient;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ConversationUnmatchedPatientDto;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


/**
 * This <code>ConversationUnMatchedPatientsEndpoint</code> collection is designed to expose the
 * conversation unmatched patients endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 * @HTTP 403 Access denied
 * @see com.qhrtech.emr.accuro.model.synapse.ConversationUnmatchedPatient
 */
@Component
@Path("/v1/provider-portal/unmatched-patients")
@Facet("provider-portal")
@Tag(name = "Conversation unmatched patients Endpoints",
    description = "Conversation unmatched patients Endpoints")
public class ConversationUnMatchedPatientEndpoint extends AbstractEndpoint {

  /**
   * Retrieves Conversation unmatched patient associated with the id.
   *
   * @param id record id
   *
   * @throws ProtossException If there has been a database error.
   * @return Conversation unmatched patient data transfer object.
   */
  @GET
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) ")
  @Operation(
      summary = "Retrieves conversation unmatched patient",
      description = "Retrieves Conversation unmatched patient associated "
          + "with the unmatched patient id",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = ConversationUnmatchedPatientDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public ConversationUnmatchedPatientDto getById(@Parameter(
      description = "UnMatched patient record id") @PathParam("id") int id)
      throws ProtossException {

    ConversationUnmatchedPatientManager manager =
        getImpl(ConversationUnmatchedPatientManager.class);

    return mapDto(manager.getUnmatchedPatientById(id),
        ConversationUnmatchedPatientDto.class);

  }

  /**
   * Create Conversation unmatched patient.
   *
   * @param unmatchedPatientDto Conversation unmatched Patient data transfer object
   *
   * @throws ProtossException If there has been a database error.
   * @return Id of Conversation unmatched patient created.
   */
  @POST
  @Path("/")
  @PreAuthorize("#oauth2.hasAnyScope('PATIENT_DEMOGRAPHICS_WRITE') ")
  @Operation(
      summary = "Creates conversation unmatched patient",
      description = "Creates Conversation unmatched patient. Since there is no strict requirement "
          + "on the validation of the patient demographics fields, we are doing the basic "
          + "validations on the field length. "
          + "Also, validations for the modules check would be added in the future once we get the "
          + "clarity regarding what all modules are required to access this feature. ",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(type = "Integer", example = "1"))),
          @ApiResponse(
              responseCode = "409",
              description = "Resource already exists."),
          @ApiResponse(
              responseCode = "400",
              description = "If Accuro patient not found.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Response createUnMatchedPatient(
      @RequestBody(
          description = "Patient") @Valid ConversationUnmatchedPatientDto unmatchedPatientDto)
      throws ProtossException {

    ConversationUnmatchedPatientManager manager =
        getImpl(ConversationUnmatchedPatientManager.class);

    int unmatchedPatientId = manager
        .createUnmatchedPatient(mapDto(unmatchedPatientDto, ConversationUnmatchedPatient.class));

    return Response.status(Status.CREATED).entity(unmatchedPatientId).build();

  }


}

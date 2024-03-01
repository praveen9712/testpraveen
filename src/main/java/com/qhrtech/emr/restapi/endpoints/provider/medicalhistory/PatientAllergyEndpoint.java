
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.allergy.AllergyCommentManager;
import com.qhrtech.emr.accuro.api.allergy.AllergyReactionManager;
import com.qhrtech.emr.accuro.api.allergy.PatientAllergyManager;
import com.qhrtech.emr.accuro.model.allergy.AllergyComment;
import com.qhrtech.emr.accuro.model.allergy.AllergyReaction;
import com.qhrtech.emr.accuro.model.allergy.PatientAllergy;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyCommentDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyReactionDto;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyType;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.PatientAllergyDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>PatientAllergyEndpoint</code> is designed to retrieve information relating to patient
 * allergies.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "PatientAllergy Endpoints", description = "Exposes patient allergy endpoints")
public class PatientAllergyEndpoint extends AbstractEndpoint {

  /**
   * Gets all allergies for a given patient.
   * <p>
   * The fields patientReaction, reactionDescription do not represent the actual allergy reactions.
   * The allergy reactions of an allergy can be retrieved through its own endpoint.
   * </p>
   *
   * @param patientId Patient ID
   * @return A List of {@link PatientAllergyDto}s.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{patientId}/allergies/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the patient allergies",
      description = "Retrieves all the allergies for the given patient. "
          + "Results are ascending ordered by id. The fields, patientReaction"
          + " and reactionDescription, do not represent the actual allergy reactions. "
          + "The allergy reactions of the allergy can be retrieved through its own endpoint.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PatientAllergyDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<PatientAllergyDto> getAllPatientAllergies(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Allergy type") @QueryParam("allergyType") AllergyType allergyType)
      throws ProtossException {

    PatientAllergyManager patientAllergyManager = getImpl(PatientAllergyManager.class);
    List<PatientAllergy> patientAllergies = patientAllergyManager.getForPatient(patientId);
    if (allergyType != null) {
      patientAllergies = patientAllergies.stream()
          .filter(a -> a.getAllergyType() != null)
          .filter(a -> a.getAllergyType().toString().equals(allergyType.toString()))
          .collect(Collectors.toList());
    }
    return mapDto(patientAllergies, PatientAllergyDto.class, ArrayList::new);
  }

  /**
   * Gets a single patient allergy for a patient.
   * <p>
   * The fields patientReaction, reactionDescription do not represent the actual allergy reactions.
   * The allergy reactions of an allergy can be retrieved through its own endpoint.
   * </p>
   *
   * @param patientId Patient ID
   * @param patientAllergyId Allergy ID
   * @return A Patient Allergy
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the requested resource is not found
   */
  @GET
  @Path("/{patientId}/allergies/{patientAllergyId}/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the allergy with the given patient and allergy ids",
      description = "Retrieves the allergy with the given patient and allergy ids."
          + "The fields, patientReaction and reactionDescription, do not represent the actual "
          + "allergy reactions. The allergy reactions of the allergy can be retrieved through its "
          + "own endpoint.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If the requested resource is not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = PatientAllergyDto.class)))})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public PatientAllergyDto getPatientAllergy(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Allergy id") @PathParam("patientAllergyId") int patientAllergyId)
      throws ProtossException {

    PatientAllergyManager patientAllergyManager = getImpl(PatientAllergyManager.class);

    PatientAllergy patientAllergy =
        patientAllergyManager.getById(patientAllergyId);

    if (patientAllergy == null || patientAllergy.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    return mapDto(patientAllergy, PatientAllergyDto.class);
  }

  /**
   * Gets all comments associated to a patient allergy.
   *
   * @param patientId Patient ID
   * @param patientAllergyId Patient allergy ID
   * @return A {@link List} of {@link AllergyCommentDto}s ascending order by created date.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the patient allergy id does not exist or not associated to the patient id.
   */
  @GET
  @Path("/{patientId}/allergies/{patientAllergyId}/comments")
  @PreAuthorize("#oauth2.hasAnyScope('EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves allergy comments",
      description = "Retrieves all comments associated to the patient allergy.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "The patient allergy doesn't exist or not associated to "
                  + "the patient id"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = AllergyCommentDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public List<AllergyCommentDto> getAllergyComments(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Allergy id") @PathParam("patientAllergyId") int patientAllergyId)
      throws ProtossException {

    PatientAllergyManager patientAllergyManager = getImpl(PatientAllergyManager.class);
    PatientAllergy patientAllergy = patientAllergyManager.getById(patientAllergyId);

    if (patientAllergy == null || patientAllergy.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource not found");
    }
    AllergyCommentManager allergyCommentManager = getImpl(AllergyCommentManager.class);
    List<AllergyComment> allergyComments =
        allergyCommentManager.getAllergyCommentsByPatientAllergyId(patientAllergyId);

    List<AllergyCommentDto> allergyComment =
        mapDto(allergyComments, AllergyCommentDto.class, ArrayList::new);
    return allergyComment;
  }

  /**
   * <p>
   * Gets all reactions associated to a patient allergy.
   * </p>
   *
   * @param patientId Patient ID
   * @param patientAllergyId patient Allergy ID
   * @return A {@link Set} of {@link AllergyReactionDto}s.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("{patientId}/allergies/{patientAllergyId}/reactions")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves reactions for the allergy",
      description = "Retrieves all reactions associated to the patient allergy.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "The patient does not have the allergy"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = AllergyCommentDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<AllergyReactionDto> getReactionsForPatientAllergy(
      @Parameter(description = "Patient id") @PathParam("patientId") int patientId,
      @Parameter(description = "Allergy id") @PathParam("patientAllergyId") int patientAllergyId)
      throws ProtossException {

    PatientAllergyManager patientAllergyManager = getImpl(PatientAllergyManager.class);
    AllergyReactionManager allergyReactionManager = getImpl(AllergyReactionManager.class);

    PatientAllergy patientAllergy = patientAllergyManager.getById(patientAllergyId);
    if (patientAllergy == null || patientAllergy.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource not found");
    }
    Set<AllergyReaction> allergyReactions =
        allergyReactionManager.getByPatientAllergyId(patientAllergyId);

    return mapDto(allergyReactions, AllergyReactionDto.class, HashSet::new);
  }
}

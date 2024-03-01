
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.allergy.NoKnownAllergyManager;
import com.qhrtech.emr.accuro.model.allergy.NoKnownAllergy;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.AllergyType;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.NoKnownAllergyDto;
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
import java.util.Collections;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code NoKnownAllergyEndpoint} is designed to retrieve no-known allergy information for a
 * patient.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("/v1/provider-portal/patients")
@Facet("provider-portal")
@Tag(name = "NoKnownAllergy Endpoints",
    description = "Exposes no known allergy endpoints.")
public class NoKnownAllergyEndpoint extends AbstractEndpoint {

  /**
   * Get all no-known allergies for a given patient.
   * <p>
   * For each patient there can be only 1 no known allergy for each allergy type.
   * </p>
   *
   * @param patientId The Patient id.
   * @param allergyType Optionally filter by allergy type
   * @return A List of {@link NoKnownAllergyDto NoKnownAllergyDtos}.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 For an Invalid allergy type.
   */
  @GET
  @Path("/{patientId}/no-known-allergies/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves no known allergies",
      description = "Retrieves a set of no known allergies for the patient. There "
          + "can be only 1 no known allergy for each allergy type."
          + " Results are ascending ordered by id.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "For the invalid allergy type."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = NoKnownAllergyDto.class))))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Parameter(
      name = "patientId",
      description = "The patient id",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer"))
  @Parameter(
      name = "allergyType",
      description = "The allergy type",
      in = ParameterIn.QUERY,
      schema = @Schema(allowableValues = {"DRUG_ALLERGY", "NON_DRUG_ALLERGY", "DRUG_INTOLERANCE",
          "NON_DRUG_INTOLERANCE"}))
  public List<NoKnownAllergyDto> getForPatient(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @QueryParam("allergyType") AllergyType allergyType)
      throws ProtossException {
    NoKnownAllergyManager noKnownAllergyManager = getImpl(NoKnownAllergyManager.class);
    List<NoKnownAllergy> allergies = noKnownAllergyManager.getForPatient(patientId);
    if (allergyType != null) {
      for (NoKnownAllergy allergy : allergies) {
        if (allergy.getAllergyType().toString().equals(allergyType.name())) {
          return mapDto(Collections.singleton(allergy), NoKnownAllergyDto.class, ArrayList::new);
        }
      }
      return Collections.emptyList();
    }
    return mapDto(allergies, NoKnownAllergyDto.class, ArrayList::new);
  }

  /**
   * Get a single {@link NoKnownAllergyDto} by no-known allergy id for a patient.
   *
   * @param patientId Patient id.
   * @param allergyId No-known allergy id.
   * @return A {@link NoKnownAllergyDto}.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the records does not exist on the server or not found.
   */
  @GET
  @Path("/{patientId}/no-known-allergies/{allergyId}/")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves the no known allergy",
      description = "Retrieves a single no known allergy by the given id for the patient.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "If the records does not exist on the server or not found."),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = NoKnownAllergyDto.class)))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Parameter(
      name = "patientId",
      description = "The patient id",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer"))
  @Parameter(
      name = "allergyId",
      description = "The no known allergy id",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer"))
  public NoKnownAllergyDto getByNoKnownAllergyId(
      @Parameter(hidden = true) @PathParam("patientId") int patientId,
      @Parameter(hidden = true) @PathParam("allergyId") int allergyId)
      throws ProtossException {
    NoKnownAllergyManager noKnownAllergyManager = getImpl(NoKnownAllergyManager.class);
    NoKnownAllergy noKnownAllergy = noKnownAllergyManager.getById(allergyId);

    if (noKnownAllergy == null || noKnownAllergy.getPatientId() != patientId) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    return mapDto(noKnownAllergy, NoKnownAllergyDto.class);
  }
}

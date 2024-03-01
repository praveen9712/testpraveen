
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.letters.LetterManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.letters.NewPatientLetter;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.LetterTypeDto;
import com.qhrtech.emr.restapi.models.dto.NewPatientLetterDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.services.RtfConversionService;
import com.qhrtech.emr.restapi.services.exceptions.RtfConversionException;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>LetterEndpoint</code> collection is designed to expose the Letter DTO and related
 * public endpoints. Requires provider level authorization.
 *
 * @RequestHeader Authorization Patient authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @see com.qhrtech.emr.accuro.api.letters.LetterManager
 */
@Component
@Path("/v1/provider-portal/letters")
@Facet("provider-portal")
@Tag(name = "Letter Endpoints", description = "Exposes letter endpoints")
public class LetterEndpoint extends AbstractEndpoint {

  @Autowired
  private RtfConversionService rtfService;

  /**
   * Retrieves active Letter Types.
   *
   * @return Available Letter Types.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/types")
  @PreAuthorize("#oauth2.hasAnyScope( 'CLINICAL_NOTES_READ', 'CLINICAL_NOTES_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_CLINICAL_NOTES', 'READ_ONLY' ) ")
  @Operation(
      summary = "Retrieves letter types",
      description = "Retrieves all letter types.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = LetterTypeDto.class))))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Set<LetterTypeDto> getActiveLetterTypes() throws ProtossException {
    LetterManager letterInterface = getImpl(LetterManager.class);
    return mapDto(letterInterface.getLetterTypes(), LetterTypeDto.class, HashSet::new);
  }

  /**
   * Saves a new PatientLetter for a Patient. If you provide Rich Text formatted data, please be
   * aware that currently features like paragraph attributes, images and tables are not supported.
   * The RTF content must follow the Rich Text Format (RTF) Specification by Microsoft Corporation.
   *
   * @param newPatientLetterDto NewPatientLetterDto entity
   * @return The letter ID generated
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws RtfConversionException If the conversion from RTF to plain text or bytes fails.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Letter content can not be processed or fail to retrieve text from the letter content.
   *       No patient letter, title, or content specified.
   */
  @PUT
  @PreAuthorize("#oauth2.hasScope( 'CLINICAL_NOTES_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_CLINICAL_NOTES', 'READ_WRITE' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.Full,
      description = "Allows access if the user has this permission for provider of the letter.")
  @Operation(
      summary = "Saves a patient letter",
      description = "Creates a new patient letter for the patient. "
          + "If you provide Rich Text Format(RTF) data, "
          + "please be aware that currently features like paragraph attributes, images and tables  "
          + "are not supported. "
          + "The RTF content must follow the Rich Text Format (RTF) specification by Microsoft "
          + "Corporation.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Data provided is not valid"),
          @ApiResponse(
              responseCode = "200",
              description = "Success. Returns the patient letter id generated.",
              content = @Content(
                  schema = @Schema(
                      type = "integer", example = "1", description = "Patient letter id")))
      })
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public Integer createPatientLetter(
      @RequestBody(description = "New patient letter") NewPatientLetterDto newPatientLetterDto)
      throws RtfConversionException, ProtossException {

    if (newPatientLetterDto == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "No Patient Letter specified.");
    }
    if (newPatientLetterDto.getContentText() == null) {
      // letter must have content to be considered valid.
      throw Error.webApplicationException(Status.BAD_REQUEST, "No Letter content specified.");
    }
    if (newPatientLetterDto.getTitle() == null) {
      // letter must have a title to be considered valid.
      throw Error.webApplicationException(Status.BAD_REQUEST, "No Letter title specified.");
    }
    LetterManager letterInterface = getImpl(LetterManager.class);
    NewPatientLetter patientLetter = mapDto(newPatientLetterDto, NewPatientLetter.class);

    // Accuro doesn't directly support RTF, so we convert it to styled
    // content in byte format.
    patientLetter
        .setContent(rtfService.getStyledContentBytes(newPatientLetterDto.getContentText()));

    // Accuro displays a summary as plain text, so we must guarantee the
    // summary is plain text.
    String summary = rtfService.getPainText(newPatientLetterDto.getContentText());
    patientLetter.setSummary(summary);

    return letterInterface.createPatientLetter(patientLetter, getUser());


  }

}

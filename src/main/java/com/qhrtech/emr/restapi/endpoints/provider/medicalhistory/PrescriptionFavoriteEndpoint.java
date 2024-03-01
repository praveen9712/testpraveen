
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.prescription.PrescriptionMacroManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMedicationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMacro;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionFavoriteDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionMedicationDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This endpoint collection is designed to retrieve prescription favorite information. User can save
 * many prescriptions under one favorite so {@link PrescriptionFavoriteDto} has the list of
 * prescription id.
 *
 * <p>
 * How to create new prescription favorite
 * <ul>
 * <li>Run Accuro</li>
 * <li>Log in</li>
 * <li>Select patient</li>
 * <li>Go to EMR</li>
 * <li>Go to Encounter Notes</li>
 * <li>Click Yellow Star symbol under Active/External Medication</li>
 * <li>Click Green Plus(+) symbol in the pop up panel</li>
 * <li>Type title</li>
 * <li>Click Green Plus(+) symbol to add prescription in the favorite</li>
 * </ul>
 * </p>
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 */
@Component
@Path("v1/provider-portal/prescription-favorites")
@Facet("provider-portal")
@Tag(name = "Prescription Favorite Endpoints",
    description = "Exposes prescription favorite endpoints")
public class PrescriptionFavoriteEndpoint extends AbstractEndpoint {

  /**
   * Get all existing prescription favorite
   *
   * @return The list of prescription favorite or {@code empty list} if not found.
   * @throws ProtossException If there has been a database error.
   */
  @Operation(
      summary = "Retrieves prescription favorites",
      description = "This endpoint collection is designed to retrieve prescription favorite "
          + "information. User can save\n"
          + " many prescriptions under one favorite so PrescriptionFavoriteDto has the list of\n"
          + " prescription id.\n"
          + " \n"
          + " <p>\n"
          + " How to create new prescription favorite\n"
          + " <ul>\n"
          + " <li>Run Accuro</li>\n"
          + " <li>Log in</li>\n"
          + " <li>Select patient</li>\n"
          + " <li>Go to EMR</li>\n"
          + " <li>Go to Encounter Notes</li>\n"
          + " <li>Click Yellow Star symbol under Active/External Medication</li>\n"
          + " <li>Click Green Plus(+) symbol in the pop up panel</li>\n"
          + " <li>Type title</li>\n"
          + " <li>Click Green Plus(+) symbol to add prescription in the favorite</li>\n"
          + " </ul>\n"
          + " </p>Retrieves all prescription favorites for the logged in user.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PrescriptionFavoriteDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access if the user has this permission for any provider.")
  public List<PrescriptionFavoriteDto> getAll() throws ProtossException {
    PrescriptionMacroManager manager = getImpl(PrescriptionMacroManager.class);
    return mapDto(manager.getRxMacros(), PrescriptionFavoriteDto.class, ArrayList::new);
  }

  /**
   * Get prescription favorite for a given unique prescription favorite id.
   *
   * @param favoriteId The prescription favorite id
   * @return The prescription favorite.
   * @throws ProtossException If there has been a database error.
   * @HTTP 404 Not found if there is no data matched with the given id.
   */
  @Operation(
      summary = "Retrieves prescription favorite",
      description = "Retrieves a prescription favorite for the given id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PrescriptionFavoriteDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "favoriteId",
              description = "favorite id",
              in = ParameterIn.PATH)})
  @GET
  @Path("/{favoriteId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access if the user has this permission for any provider.")
  public PrescriptionFavoriteDto getById(
      @Parameter(hidden = true) @PathParam("favoriteId") int favoriteId)
      throws ProtossException {
    PrescriptionMacro favoriteFromProtoss = getFavorite(favoriteId);

    if (favoriteFromProtoss == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Prescription Favorite not found");
    }

    return mapDto(favoriteFromProtoss, PrescriptionFavoriteDto.class);
  }

  /**
   * Get prescriptions for the given favorite id. The prescriptions have been added on the
   * prescription favorite and not related to any patient.
   *
   * @param favoriteId The favorite id
   * @return The prescription.
   * @throws ProtossException If there has been a database error.
   */
  @Operation(
      summary = "Retrieves prescriptions for a favorite id",
      description = "Retrieves all prescriptions for the given favorite id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PrescriptionMedicationDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "favoriteId",
              description = "favorite id",
              in = ParameterIn.PATH)})
  @GET
  @Path("/{favoriteId}/prescriptions")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescriptions prescribed by a provider the user has this "
          + "permission for (if Accuro is configured to enforce provider permissions for "
          + "prescriptions).")
  public List<PrescriptionMedicationDto> getPrescriptionsByFavoriteId(
      @Parameter(hidden = true) @PathParam("favoriteId") int favoriteId) throws ProtossException {

    PrescriptionMedicationManager prescriptionMedicationManager =
        getImpl(PrescriptionMedicationManager.class);

    // filter out by given prescription ids
    List<PrescriptionMedication> prescriptionsFromProtoss =
        prescriptionMedicationManager.getByFavouriteId(favoriteId);

    return mapDto(prescriptionsFromProtoss, PrescriptionMedicationDto.class, ArrayList::new);
  }

  /**
   * Get a single prescription by the given favorite id and prescription id. The prescription has
   * been added on the prescription favorite and not related to any patient.
   *
   * @param favoriteId The favorite id.
   * @param rxId The prescription id.
   * @return The prescription.
   * @throws ProtossException If there has been a database error.
   * @HTTP 404 Not found if there is no data matched with the given favorite id.
   * @HTTP 404 Not found if there is no data matched with the given prescription id.
   */
  @Operation(
      summary = "Retrieves a prescription for a favorite id and a prescription id ",
      description = "Retrieves a prescription for the given favorite id and "
          + "the given prescription id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = PrescriptionMedicationDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "favoriteId",
              description = "favorite id",
              in = ParameterIn.PATH),
          @Parameter(
              name = "rxId",
              description = "prescription id",
              in = ParameterIn.PATH)})
  @GET
  @Path("/{favoriteId}/prescriptions/{rxId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'EMR_MEDS', 'READ_ONLY' ) ")
  @ProviderPermission(type = AccessType.EMR, level = AccessLevel.ReadOnly,
      description = "Allows access to prescriptions prescribed by a provider the user has this "
          + "permission for (if Accuro is configured to enforce provider permissions for "
          + "prescriptions).")
  public PrescriptionMedicationDto getPrescriptionByPrescriptionId(
      @Parameter(hidden = true) @PathParam("favoriteId") int favoriteId,
      @Parameter(hidden = true) @PathParam("rxId") int rxId) throws ProtossException {
    PrescriptionMacro favorites = getFavorite(favoriteId);

    // validate favorite id
    if (favorites == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Prescription Favorite not found");
    }

    List<Integer> prescriptionIds = favorites.getPrescriptionIds();

    // validate prescription id
    if (!prescriptionIds.contains(rxId)) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Prescription not found");
    }

    PrescriptionMedicationManager prescriptionMedicationManager =
        getImpl(PrescriptionMedicationManager.class);

    // retrieve prescription by the given id

    List<PrescriptionMedication> prescriptions =
        prescriptionMedicationManager.getByFavouriteId(favoriteId);
    PrescriptionMedication prescriptionFromProtoss = null;

    for (PrescriptionMedication rx : prescriptions) {
      if (rx.getPrescriptionId() == rxId) {
        prescriptionFromProtoss = rx;
        break;
      }
    }

    if (prescriptionFromProtoss == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Prescription not found");
    }

    return mapDto(prescriptionFromProtoss, PrescriptionMedicationDto.class);
  }

  private PrescriptionMacro getFavorite(int favoriteId) throws ProtossException {
    PrescriptionMacroManager manager = getImpl(PrescriptionMacroManager.class);
    return manager.getRxMacros()
        .stream()
        .filter(f -> favoriteId == f.getId())
        .findFirst()
        .orElse(null);
  }
}

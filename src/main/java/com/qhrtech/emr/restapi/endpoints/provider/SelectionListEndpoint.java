
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.SelectionListName;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This {@code SelectionListEndpoint} collection is designed to expose the list of the configurable
 * values.
 */
@Component("provider-selection-list")
@Path("/v1/provider-portal/selection-lists")
@Facet("provider-portal")
@Tag(name = "Selection List Endpoints", description = "Exposes selection list endpoints")
public class SelectionListEndpoint extends AbstractEndpoint {


  /**
   * Get all the selection lists available for a list name. Acceptable list names are available in
   * the enum {@link SelectionListName}.
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Request successful
   */
  @GET
  @Path("/{list-name}")
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Operation(
      summary = "Get all the selection lists available for a list name",
      description = "Get all the selection lists available for a list name. "
          + "Acceptable list names are available in the enum, SlectionListName",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = String.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public List<String> getSelectionList(@PathParam("list-name") String listName)
      throws ProtossException {

    SelectionListName list = SelectionListName.lookup(listName);

    if (list == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "The given list name doesn't exist: " + listName);
    }

    SelectionListManager selectionListManager = getImpl(SelectionListManager.class);
    return selectionListManager.getSelectionList(list.getListName());

  }

  /**
   * Get all available patient chart unlock reasons.
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Request successful
   */
  @GET
  @Path("/chart-unlock-reasons")
  @PreAuthorize("#oauth2.hasScope('user/provider.PatientChartLock.read')")
  @Operation(
      summary = "Get all available patient chart unlock reasons.",
      description = "Retrieves all available patient chart unlock reasons.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = String.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public List<String> getChartUnlockReason() throws ProtossException {

    return getSelectionList(SelectionListName.CHART_UNLOCK_REASON.getResourceName());

  }

  /**
   * Get all the statuses available for the referral orders.
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Request successful
   */
  @GET
  @Path("/referral-statuses")
  @PreAuthorize("#oauth2.hasAnyScope( 'LETTER_READ', 'LETTER_WRITE' ) ")
  @Operation(
      summary = "Get all the statuses available for the referral orders",
      description = "Get all the statuses available for the referral orders.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = String.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public List<String> getReferralStatus()
      throws ProtossException {

    return getSelectionList(SelectionListName.REFERRAL_STATUS.getResourceName());

  }


  /**
   * Get all the types available for the referral orders.
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Request successful
   */
  @Operation(
      summary = "Returns the Referral Types",
      description = "Returns the List of Referral Types for the referral order.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If any required field "
                  + "is missing or not valid"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      type = "String",
                      example = "ReferralType")))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  @GET
  @Path("/referral-types")
  @PreAuthorize("#oauth2.hasAnyScope( 'LETTER_READ', 'LETTER_WRITE' ) ")
  public List<String> getReferralTypes()
      throws ProtossException {

    return getSelectionList(SelectionListName.REFERRAL_TYPE.getResourceName());

  }

  /**
   * Update(replace) the Selection List with the given list name and the given list of values.
   * Acceptable list names are available in the enum {@link SelectionListName}.
   *
   * <p>
   * If the user requests with the empty list the stored data will be removed.
   * </p>
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 204 Request successful but no content
   */
  @PUT
  @Path("/{list-name}")
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Operation(
      summary = "Update(replace) the Selection List with the given list name "
          + "and the given list of values",
      description = "Update(replace) the Selection List with the given list name "
          + "and the given list of values. "
          + "Acceptable list names are available in the enum, SelectionListName. "
          + "If the user requests with the empty list the stored data will be removed.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content"),
          @ApiResponse(
              responseCode = "400",
              description = "If the given list has duplicates"),
          @ApiResponse(
              responseCode = "404",
              description = "If the given list name not found")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public void updateStatusList(
      @PathParam("list-name") String listName, List<String> selections)
      throws ProtossException {

    if (selections == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The given list name or the given list to be updated should not be null.");
    }

    if (new HashSet<>(selections).size() < selections.size()) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "List should not have duplicates.");
    }

    SelectionListName list = SelectionListName.lookup(listName);

    if (list == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "The given list name doesn't exist: " + listName);
    }

    SelectionListManager selectionListManager = getImpl(SelectionListManager.class);
    selectionListManager.setSelectionList(list.getListName(), selections);

  }

  /**
   * Update(replace) the referral order status with the given list of statuses.
   *
   * <p>
   * If the user requests with the empty list the stored data will be removed.
   * </p>
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 204 Request successful but no content
   */
  @PUT
  @Path("/referral-statuses")
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Operation(
      summary = "Update(replace) the referral order status with the given list of statuses",
      description = "Update(replace) the referral order status with the given list of statuses. "
          + "If the user requests with the empty list the stored data will be removed.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public void updateOrderStatus(List<String> selections)
      throws ProtossException {

    updateStatusList(SelectionListName.REFERRAL_STATUS.getResourceName(), selections);

  }

  /**
   * Update(replace) the referral order type with the given list of types.
   *
   * <p>
   * If the user requests with the empty list the stored data will be removed.
   * </p>
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 204 Request successful but no content
   */
  @PUT
  @Path("/referral-types")
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Operation(
      summary = "Update(replace) the referral order type with the given list of types",
      description = "Update(replace) the referral order type with the given list of types. "
          + "If the user requests with the empty list the stored data will be removed.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public void updateReferralType(List<String> selections)
      throws ProtossException {

    updateStatusList(SelectionListName.REFERRAL_TYPE.getResourceName(), selections);

  }

  /**
   * Get all the reasons available for the mask removal.
   *
   * @return the mask removal reasons
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Request successful
   */
  @GET
  @Path("/mask-removal-reasons")
  @PreAuthorize("#oauth2.hasScope('user/provider.Mask.read')")
  @Operation(
      summary = "Get all the reasons available for the mask removal",
      description = "Get all the reasons available for the mask removal.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = String.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public List<String> getMaskRemovalReasons() throws ProtossException {
    return getSelectionList(SelectionListName.MASK_REMOVAL_REASON.getResourceName());
  }

  /**
   * Get all the types available for the patient address.
   *
   * @throws DataAccessException If there has been a database error.
   * @HTTP 200 Request successful
   */
  @GET
  @Path("/address-types")
  @PreAuthorize("#oauth2.hasAnyScope( 'PATIENT_DEMOGRAPHICS_READ', 'PATIENT_DEMOGRAPHICS_WRITE' ) ")
  @Operation(
      summary = "Retrieve all the types available for the patient address",
      description = "Get all the types available for the patient address",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = String.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true)})
  public List<String> getAddressTypes()
      throws ProtossException {

    return getSelectionList(SelectionListName.ADDRESS_TYPE.getResourceName());

  }


}

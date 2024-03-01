
package com.qhrtech.emr.restapi.endpoints.provider.medicalhistory;

import com.qhrtech.emr.accuro.api.medicalhistory.HistoryRegularItemManager;
import com.qhrtech.emr.accuro.api.medicalhistory.HistoryTypeManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.medicalhistory.HistoryRegularItem;
import com.qhrtech.emr.accuro.model.medicalhistory.HistoryType;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.HistoryRegularItemDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.models.swagger.RolePermission;
import com.qhrtech.emr.restapi.models.swagger.RolePermissions;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/provider-portal/history-regular")
@Facet("provider-portal")
@Tag(name = "HistoryRegular Endpoints",
    description = "Exposes history regular endpoints")
public class HistoryRegularEndpoint extends AbstractEndpoint {

  /**
   * Retrieves all history regular sub type items associated to the specified type id.
   * <p>
   * Retrieves all sub type items if no type is specified.
   * </p>
   *
   * @param typeId The history type id.
   * @return A List of {@link HistoryRegularItemDto}s ordered by description of the item.
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DIAGNOSTICS', 'READ_ONLY' ) "
      + "and #accuro.hasAccess( 'EMR_MEDICAL_HISTORY', 'READ_ONLY' )")
  @Operation(
      summary = "Retrieves history regular items",
      description = "Retrieves history regular sub type items associated to the specified "
          + "type id.\n\nRetrieves all sub type items if no type is specified.\n",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(
                      schema = @Schema(implementation = HistoryRegularItemDto.class))))})
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "PATIENT_DIAGNOSTICS"),
          @RolePermission(type = "EMR_MEDICAL_HISTORY")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "historyTypeId",
              description = "The history type id",
              in = ParameterIn.QUERY,
              schema = @Schema(type = "integer"))})
  public List<HistoryRegularItemDto> getForHistoryItems(
      @Parameter(hidden = true) @QueryParam("historyTypeId") Integer typeId)
      throws ProtossException {
    HistoryRegularItemManager historyItemManager = getImpl(HistoryRegularItemManager.class);
    List<HistoryRegularItem> items;
    if (typeId == null) {
      HistoryTypeManager typeManager = getImpl(HistoryTypeManager.class);
      items = new ArrayList<>();
      for (HistoryType type : typeManager.getAllHistoryTypes()) {
        items.addAll(historyItemManager.getForType(type.getId()));
      }
    } else {
      items = historyItemManager.getForType(typeId);
    }

    return mapDto(items, HistoryRegularItemDto.class, ArrayList::new);
  }

  /**
   * Retrieves a {@link HistoryRegularItemDto} for a given history regular sub type identity.
   *
   * @param hitoryRegularId The history regular item identity.
   * @return The {@link HistoryRegularItemDto}.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 If the specified history item is not found.
   */
  @GET
  @Path("/{historyRegularId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'EMR_MEDS_READ', 'EMR_MEDS_WRITE' ) "
      + "and #accuro.hasAccess( 'PATIENT_DIAGNOSTICS', 'READ_ONLY' ) "
      + "and #accuro.hasAccess( 'EMR_MEDICAL_HISTORY', 'READ_ONLY' )")
  @Operation(
      summary = "Retrieves history item by id",
      description = "Retrieves the history regular item for a given history regular sub type id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(schema = @Schema(implementation = HistoryRegularItemDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Resource doesn't exist with the given id")})
  @RolePermissions(
      operation = LogicalOperation.AND,
      rolePermissions = {
          @RolePermission(type = "PATIENT_DIAGNOSTICS"),
          @RolePermission(type = "EMR_MEDICAL_HISTORY")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Provider level authorization grant",
              in = ParameterIn.HEADER,
              required = true),
          @Parameter(
              name = "historyRegularId",
              description = "The history regular id",
              in = ParameterIn.PATH,
              schema = @Schema(type = "integer"))})
  public HistoryRegularItemDto getByItemId(
      @Parameter(hidden = true) @PathParam("historyRegularId") int hitoryRegularId)
      throws ProtossException {
    HistoryRegularItemManager historyItemManager = getImpl(HistoryRegularItemManager.class);
    HistoryRegularItem item = historyItemManager.getById(hitoryRegularId);
    if (item == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }
    return mapDto(item, HistoryRegularItemDto.class);
  }
}

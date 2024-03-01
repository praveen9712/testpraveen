
package com.qhrtech.emr.restapi.endpoints.spade;

import com.qhrtech.emr.accdocs.model.docs.SpadeDocument;
import com.qhrtech.emr.accdocs.model.docs.SpadeFolder;
import com.qhrtech.emr.accuro.api.docs.SpadeDocumentManager;
import com.qhrtech.emr.accuro.api.docs.SpadeFolderManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.spade.SpadeDocumentDto;
import com.qhrtech.emr.restapi.models.dto.spade.SpadeFolderDto;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>SpadeFolderEndpoint</code> collection is designed to provide CRUD operations for spade
 * folders for use with the spade workflow. Authorization of client credential is required.
 *
 * @RequestHeader Authorization Client credential grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @HTTP 400 Bad request or business validation failure
 */
@Component("spade-folder-endpoint")
@Path("/v1/spade/folders")
@Tag(name = "Spade Folder Endpoints", description = "Exposes spade folder endpoints")
public class SpadeFolderEndpoint extends AbstractEndpoint {

  /**
   * Get all Spade folders.
   *
   * @return A Set of {@link SpadeFolderDto} objects
   * @throws DataAccessException If there has been a database error.
   */
  @Operation(
      summary = "Get all Spade folders.",
      description = "Get all Spade folders.",
      responses = {
          @ApiResponse(
              responseCode = "401",
              description = "If access is denied or "
                  + "preference not set for any field"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = SpadeFolderDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client Credentials grant",
              in = ParameterIn.HEADER,
              required = true)})
  @GET
  @PreAuthorize("#oauth2.hasAnyScope( 'SPADE_DOCUMENT_READ', 'SPADE_DOCUMENT_WRITE' ) ")
  public List<SpadeFolderDto> getFolders() throws ProtossException {

    SpadeFolderManager spadeFolderManager = getImpl(SpadeFolderManager.class);
    List<SpadeFolder> spadeFolderList = spadeFolderManager.getAllSpadeFolders();

    return mapDto(spadeFolderList, SpadeFolderDto.class, ArrayList::new);
  }

  /**
   * Get Spade folder by Id.
   *
   * @return The {@link SpadeFolderDto} object
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 In case folder not found.
   */
  @Operation(
      summary = "Get Spade folder by Id.",
      description = "Get Spade folder by Id.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If any required field "
                  + "is missing or not valid"),
          @ApiResponse(
              responseCode = "401",
              description = "If access is denied or "
                  + "preference not set for any field"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = SpadeFolderDto.class)))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client Credentials grant",
              in = ParameterIn.HEADER,
              required = true)})
  @GET
  @Path("/{folderId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'SPADE_DOCUMENT_READ', 'SPADE_DOCUMENT_WRITE' ) ")
  public SpadeFolderDto getFolderById(@PathParam("folderId") Integer folderId)
      throws ProtossException {

    SpadeFolderManager spadeFolderManager = getImpl(SpadeFolderManager.class);
    SpadeFolder spadeFolder = spadeFolderManager.getSpadeFolder(folderId);
    if (spadeFolder == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Folder does not exists with Id: " + folderId);
    }
    return mapDto(spadeFolder, SpadeFolderDto.class);
  }

  /**
   * Get spade documents by folder Id.
   *
   * @return The {@link SpadeDocumentDto} objects
   * @throws DataAccessException If there has been a database error.
   * @HTTP 404 In case folder or documents not found.
   */
  @Operation(
      summary = "Get spade documents by folder Id.",
      description = "Get spade documents by folder Id.",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "If any required field "
                  + "is missing or not valid"),
          @ApiResponse(
              responseCode = "401",
              description = "If access is denied or "
                  + "preference not set for any field"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = SpadeFolderDto.class))))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client Credentials grant",
              in = ParameterIn.HEADER,
              required = true)})
  @GET
  @Path("/{folderId}/documents")
  @Facet("internal")
  @Hidden
  @PreAuthorize("#oauth2.hasAnyScope( 'API_INTERNAL' )")
  public Set<SpadeDocumentDto> getDocumentsByFolderId(@PathParam("folderId") Integer folderId)
      throws ProtossException {

    SpadeFolderManager spadeFolderManager = getImpl(SpadeFolderManager.class);
    SpadeFolder spadeFolder = spadeFolderManager.getSpadeFolder(folderId);

    if (spadeFolder == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Folder does not exists with Id: " + folderId);
    }

    SpadeDocumentManager spadeDocumentManager = getImpl(SpadeDocumentManager.class);
    Set<SpadeDocument> documents = spadeDocumentManager.getSpadeDocumentListByFolder(folderId);

    if (CollectionUtils.isEmpty(documents)) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Documents does not exists with folder Id: " + folderId);
    }
    Set<SpadeDocumentDto> docs = mapDto(documents, SpadeDocumentDto.class, HashSet::new);
    return docs;
  }

  /**
   * Update a folder of type {@link SpadeFolderDto}.
   *
   * @param folderDto object of type {@link SpadeFolderDto}
   * @return No content response if update is successful.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Unable to process json for this request.
   **/
  @Operation(
      summary = "Update a folder",
      description = "Update a Spade folder",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Unable to process json for this request."),
          @ApiResponse(
              responseCode = "401",
              description = "If access is denied or "
                  + "preference not set for any field"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client Credentials grant",
              in = ParameterIn.HEADER,
              required = true)})
  @PUT
  @Path("/{folderId}")
  @PreAuthorize("#oauth2.hasScope( 'SPADE_DOCUMENT_WRITE' ) ")
  public Response updateFolder(@PathParam("folderId") int folderId,
      @Valid SpadeFolderDto folderDto)
      throws ProtossException {

    if (folderId != folderDto.getId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "The provided folder id does not match the specified resource.");
    }

    SpadeFolderManager folderTypeManager = getImpl(SpadeFolderManager.class);
    SpadeFolder folder = mapDto(folderDto, SpadeFolder.class);

    folderTypeManager.updateSpadeFolder(folder, getUser());
    return Response.status(Response.Status.NO_CONTENT).type(MediaType.APPLICATION_JSON_TYPE)
        .build();
  }

  /**
   * Create a folder of type {@link SpadeFolderDto}.
   *
   * @param folderDto object of type {@link SpadeFolderDto}
   * @return Folder Id of the {@link SpadeFolderDto} object created.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Unable to process json for this request.
   **/
  @Operation(
      summary = "Create a folder",
      description = "Create a Spade folder",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Unable to process json for this request."),
          @ApiResponse(
              responseCode = "401",
              description = "If access is denied or "
                  + "preference not set for any field"),
          @ApiResponse(
              responseCode = "200",
              description = "No Content")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client Credentials grant",
              in = ParameterIn.HEADER,
              required = true)})
  @POST
  @PreAuthorize("#oauth2.hasScope( 'SPADE_DOCUMENT_WRITE' ) ")
  public int createFolder(@Valid SpadeFolderDto folderDto)
      throws ProtossException {

    SpadeFolderManager folderTypeManager = getImpl(SpadeFolderManager.class);
    SpadeFolder folder = mapDto(folderDto, SpadeFolder.class);
    return folderTypeManager.createSpadeFolder(folder, getUser());
  }

  /**
   * Delete a folder {@link SpadeFolderDto}
   *
   * @param folderId folder ID of {@link SpadeFolderDto} object to be deleted.
   * @return No content response if update is successful.
   * @throws DataAccessException If there has been a database error. And when no folder found to
   *         delete.
   **/
  @Operation(
      summary = "Delete a folder",
      description = "Delete a Spade folder",
      responses = {
          @ApiResponse(
              responseCode = "400",
              description = "Unable to process json for this request."),
          @ApiResponse(
              responseCode = "401",
              description = "If access is denied or "
                  + "preference not set for any field"),
          @ApiResponse(
              responseCode = "204",
              description = "No Content")})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client Credentials grant",
              in = ParameterIn.HEADER,
              required = true)})
  @DELETE
  @Path("/{folderId}")
  @PreAuthorize("#oauth2.hasScope( 'SPADE_DOCUMENT_WRITE' ) ")
  public Response deleteFolder(@PathParam("folderId") Integer folderId)
      throws ProtossException {

    SpadeFolderManager spadeFolderManager = getImpl(SpadeFolderManager.class);
    spadeFolderManager.deleteSpadeFolder(folderId, getUser());
    return Response.status(Response.Status.NO_CONTENT).type(MediaType.APPLICATION_JSON_TYPE)
        .build();
  }
}

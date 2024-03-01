
package com.qhrtech.emr.restapi.endpoints.spade;

import com.qhrtech.emr.accdocs.model.docs.DocsData;
import com.qhrtech.emr.accdocs.model.docs.DocsMetadata;
import com.qhrtech.emr.accdocs.model.docs.SpadeDocument;
import com.qhrtech.emr.accuro.api.docs.DocsDataManager;
import com.qhrtech.emr.accuro.api.docs.SpadeDocumentManager;
import com.qhrtech.emr.accuro.api.docs.SpadeFolderManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.spade.SpadeDocumentDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>SpadeDocumentEndpoint</code> collection is designed to provide CRUD operations for
 * spade documents for use with the spade workflow. Client credential authorization with scope of
 * SPADE_DOCUMENT_WRITE is required.
 *
 * @RequestHeader Authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @HTTP 400 Bad request or business validation failure
 */
@Component("spade-document-endpoint")
@Path("/v1/spade/documents")
@Facet("spade-portal")
@Tag(name = "Spade Document Endpoints", description = "Exposes spade document endpoints")
public class SpadeDocumentEndpoint extends AbstractEndpoint {

  /**
   * Creates a spade document by storing the document meta data and the contents in the database.
   *
   * @param metadata Multipart request body containing the {@link SpadeDocumentDto} object
   * @param file Multipart request body containing document contents(binary data)
   * @return Response with the document id of the document created
   * @HTTP 400 If there are field or business validation errors
   * @HTTP 400 If the folderId is valid but non-existing. A response message of the form:
   *       {"error":"Spade folder not found of id:<strong>[<i>folderId</i>]</strong>"} will be
   *       returned
   * @HTTP 401 If access is denied or preference not set for any field
   * @HTTP 413 If document uploaded is more than <>size placeholder
   **/
  @Operation(
      summary = "Creates a spade document",
      description = "Creates a spade document by storing the document meta data and the contents "
          + "in the database.",
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
              responseCode = "413",
              description = "If document uploaded is more"
                  + " than <>size placeholder"),
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              content = @Content(
                  schema = @Schema(
                      type = "Integer",
                      example = "10")))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client level authorization grant ",
              in = ParameterIn.HEADER,
              required = true)
      })
  @POST
  @Consumes("multipart/form-data")
  @PreAuthorize("#oauth2.hasScope('SPADE_DOCUMENT_WRITE')")
  public int createDocument(
      @RequestBody(description = "Metadata object that needs to be created", required = true,
          content = @Content(mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = SpadeDocumentDto.class))) @Multipart(
                  value = "metadata",
                  type = MediaType.APPLICATION_JSON) @Valid SpadeDocumentDto metadata,
      @Parameter(description = "File attachment", required = true,
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA,
              schema = @Schema(type = "string", format = "binary"))) @Multipart(
                  value = "file") Attachment file)
      throws ProtossException {

    // Check if folder is existing
    SpadeFolderManager folderManager = getImpl(SpadeFolderManager.class);
    if (folderManager.getSpadeFolder(metadata.getFolderId()) == null) {
      // NOTE: Any changes to this response message will be breaking for clients of this endpoint.
      // Should this message be changed, all stakeholders must be notified.
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Spade folder not found of id:" + metadata.getFolderId());
    }

    byte[] fileBytes = file.getObject(byte[].class);

    SpadeDocumentManager documentManager = getImpl(SpadeDocumentManager.class);
    DocsMetadata docMetadata = mapDto(metadata, DocsMetadata.class);

    return documentManager.createSpadeDocument(docMetadata, fileBytes, metadata.getFolderId(),
        getUser());
  }

  /**
   * Retrieves the document in multi-parts. One part contains document metadata and other part
   * contains documents contents(binary).
   *
   * @param id path parameter corresponds to document Id that needs to be retrieved.
   * @return Multipart response which two parts:
   *         <p>
   *         {@link SpadeDocumentDto} object of content-type application/json <br>
   *         Document binary contents of content-type corresponds to mime-type which was set during
   *         document creation.
   *         </p>
   * @HTTP 400 if there are field or business validation errors.
   * @HTTP 401 if access is denied or preference not set for any field
   * @HTTP 404 if document not found
   **/
  @Operation(
      summary = "Retrieves the document in multi-parts.",
      description = "Retrieves the document in multi-parts. One part contains "
          + "document metadata and other part contains documents contents(binary).",
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
              responseCode = "404",
              description = "If document is not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Created",
              content = @Content(
                  schema = @Schema(
                      type = "Integer",
                      example = "10")))})
  @Parameters(
      value = {
          @Parameter(
              name = "authorization",
              description = "Client Credentials grant",
              in = ParameterIn.HEADER,
              required = true)})
  @GET
  @Path("/{id}")
  @Produces("multipart/form-data")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  public Response getDocument(
      @PathParam(value = "id") int id)
      throws ProtossException {

    SpadeDocumentManager documentManager = getImpl(SpadeDocumentManager.class);
    SpadeDocument metadata = documentManager.getSpadeDocument(id);
    if (metadata == null) {
      throw Error.webApplicationException(Status.NOT_FOUND,
          "Document doesn't exist with id: " + id);
    }
    DocsDataManager docsDataManager = getImpl(DocsDataManager.class);
    DocsData docsData = docsDataManager.getDocsData(metadata.getDataId());

    SpadeDocumentDto documentDto = mapDto(metadata, SpadeDocumentDto.class);

    Map<String, Object> map = new LinkedHashMap<>();
    map.put(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, documentDto);
    String mediaType = (metadata.getMimeType() != null)
        ? metadata.getMimeType()
        : org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
    map.put(mediaType, docsData.getContents());
    return Response.ok().entity(map).build();
  }

  /**
   * Updates spade documents metadata.
   *
   * @param id path parameter of document Id
   * @param documentDto {@link SpadeDocumentDto} object
   * @return Response with 204 in case of success
   * @HTTP 400 if there are field or business validation errors.
   * @HTTP 401 if access is denied or preference not set for any field
   * @HTTP 404 if document is not found
   **/
  @Operation(
      summary = "Updates spade documents metadata.",
      description = "Updates spade documents metadata.",
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
              responseCode = "404",
              description = "If document is not found"),
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
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  public void updateDocMetaData(
      @PathParam(value = "id") int id,
      @Valid SpadeDocumentDto documentDto)
      throws ProtossException {

    if (id != documentDto.getDocumentId()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "DocumentId does not match with the resource: " + documentDto.getDocumentId());
    }
    SpadeDocumentManager documentManager = getImpl(SpadeDocumentManager.class);
    SpadeDocument spadeDocument = documentManager.getSpadeDocument(id);

    // Deleted flag, folder id cannot be changed by this endpoint
    if (spadeDocument.getFolderId() != documentDto.getFolderId()
        || spadeDocument.isDeleted() != documentDto.isDeleted()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Deleted flag or folder id cannot be changed");
    }

    DocsMetadata docMetadata = mapDto(documentDto, DocsMetadata.class);
    docMetadata.setMetadataId(spadeDocument.getMetadataId());
    documentManager.updateDocsMetadata(docMetadata, getUser());
  }

  /**
   * Deletes spade document.
   *
   * @param id path parameter corresponds to document Id that needs to be deleted
   * @param type Query parameter which decides if the document has to be hard deleted or not. In
   *        case value is 'hard', the document would be hard deleted. In all other cases, document
   *        would be soft deleted.
   * @return Response with 204 in case of success
   * @HTTP 400 if there are field or business validation errors.
   * @HTTP 401 if access is denied or preference not set for any field
   * @HTTP 404 if document is not found
   **/
  @Operation(
      summary = "Deletes spade document.",
      description = "Deletes spade document.",
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
              responseCode = "404",
              description = "If document is not found"),
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
  @Path("/{id}")
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Hidden
  public void deleteDocument(
      @PathParam(value = "id") int id,
      @QueryParam("type") String type)
      throws ProtossException {
    SpadeDocumentManager documentManager = getImpl(SpadeDocumentManager.class);

    if (!StringUtils.isBlank(type) && type.equalsIgnoreCase("hard")) {
      documentManager.removeSpadeDocument(id, getUser());
    } else {
      documentManager.deleteSpadeDocument(id, getUser());
    }
  }
}


package com.qhrtech.emr.restapi.endpoints.patient;

import com.qhrtech.emr.accdocs.api.docs.AccDocManager;
import com.qhrtech.emr.accdocs.model.docs.AccDoc;
import com.qhrtech.emr.accuro.api.docs.DocumentManager;
import com.qhrtech.emr.accuro.model.docs.Document;
import com.qhrtech.emr.accuro.model.docs.FileType;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.DocumentDto;
import com.qhrtech.emr.restapi.models.dto.PatientFoldersDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.util.MimeTypeUtils;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>PatientDocumentEndpoint</code> collection is designed to expose the Document DTO and
 * related public endpoints. Patient level authorization.
 *
 * @RequestHeader Authorization Patient authorization grant
 * @HTTP 200 Authorized
 * @HTTP 401 Unauthorized
 */
@Component("patient-document-endpoint")
@Path("/v1/patient-portal/documents")
@Facet("patient-portal")
@PreAuthorize("#oauth2.isOAuth() and !#oauth2.isClient() and #accuro.isPatient()")
@Tag(name = "Document Endpoints - Patient",
    description = "Exposes patient portal document endpoints")
public class DocumentEndpoint extends AbstractEndpoint {

  /**
   * Get the media type by Document Type ID
   *
   * @param accuroFileTypes A map of Accuro file types
   * @param documentTypeId Document Type ID
   * @return A Media Type
   */
  private static String getMediaType(
      Map<Integer, FileType> accuroFileTypes,
      Integer documentTypeId) {
    if (documentTypeId == null) {
      return MimeTypeUtils.MIME_APPLICATION_OCTET_STREAM;
    }
    FileType type = accuroFileTypes.get(documentTypeId);
    if (type == null) {
      return MimeTypeUtils.MIME_APPLICATION_OCTET_STREAM;
    } else {
      return MimeTypeUtils.getMediaType(type.getType());
    }
  }

  /**
   * Get a collection of base document filters
   *
   * @return A predicate of base document filters.
   */
  private static Predicate<Document> getBaseDocumentFilters() {
    Predicate<Document> basePredicate = d -> d.getDeleted().equals(0);
    basePredicate
        .and(d -> d.getArchived().equals(0))
        .and(d -> d.getActive().equals(1));
    return basePredicate;
  }

  /**
   * Builds a predicate based on the combination of parameters passed and the base document filters.
   *
   * @param folder A folder name
   * @param subType A folder sub-type name
   * @return a predicate of document filters
   */
  private static Predicate<Document> getDocumentFilters(
      String folder, String subType) {
    Predicate<Document> basePredicate = getBaseDocumentFilters();
    if (folder == null && subType == null) {
      return basePredicate;
    } else if (folder != null && subType != null) {
      return basePredicate
          .and(d -> d.getPathType().equalsIgnoreCase(folder))
          .and(d -> d.getSubtype().equalsIgnoreCase(subType));
    } else if (folder != null) {
      return basePredicate
          .and(d -> d.getPathType().equalsIgnoreCase(folder));
    } else {
      return basePredicate
          .and(d -> d.getSubtype().equalsIgnoreCase(subType));
    }
  }

  /**
   * Retrieves a set of path type(folder) including sub type(sub folder).
   *
   * @return path type(folder) and sub type(sub folder)
   *
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/folders/")
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves patient folders",
      description = "Retrieves folders for a patient including sub type(sub folder).",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = PatientFoldersDto.class)))
      })
  public PatientFoldersDto getPatientFolders() throws ProtossException {

    int patientId = getPatientId();
    DocumentManager documentManager = getImpl(DocumentManager.class);
    Set<Document> docs = documentManager
        .getDocumentsForPatient(patientId)
        .stream()
        .filter(getBaseDocumentFilters())
        .collect(Collectors.toSet());

    // get path type(folder) and sub type(sub folder) of this patient
    PatientFoldersDto patientFolders = new PatientFoldersDto();
    Set<String> folders = new HashSet<>();
    Set<String> subType = new HashSet<>();

    docs.forEach(doc -> {
      folders.add(doc.getPathType());

      if (!StringUtils.isBlank(doc.getSubtype())) {
        subType.add(doc.getSubtype());
      }
    });

    patientFolders.setPatientId(patientId);
    patientFolders.setFolders(folders);
    patientFolders.setSubtypes(subType);

    return patientFolders;
  }

  /**
   * Retrieves a list of all the documents, filtered by path type or subtype if set.
   *
   * @param folder (optional) Folder parameter
   * @param subType (optional) Subtype parameter (category).
   * @return Set of Document DTOs
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/")
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves documents",
      description = "Retrieves a set of all the documents, filtered by path type or subtype if "
          + "set the specific option in Accuro.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = DocumentDto.class))))
      })
  public Set<DocumentDto> getDocuments(
      @Parameter(description = "Folder name") @QueryParam("folder") String folder,
      @Parameter(description = "Sub-folder(sub type) name") @QueryParam("subtype") String subType)
      throws ProtossException {

    DocumentManager documentManager = getImpl(DocumentManager.class);
    Set<Document> docsFiltered = documentManager
        .getDocumentsForPatient(getPatientId())
        .stream()
        .filter(getDocumentFilters(folder, subType))
        .collect(Collectors.toSet());

    if (docsFiltered.isEmpty()) {
      return Collections.emptySet();
    }

    Map<Integer, FileType> fileTypes = fileTypesById(documentManager);

    Set<DocumentDto> documents = docsFiltered.stream().map(d -> {
      DocumentDto document = mapDto(d, DocumentDto.class);

      return document;
    }).collect(Collectors.toSet());

    return documents;
  }

  /**
   * Retrieves the actual bytes by document ID.
   *
   * @param documentId Document ID
   * @return the actual bytes of the document file
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("/{documentId}")
  @Parameter(
      name = "authorization",
      description = "Patient authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves a document",
      description = "Retrieves the actual bytes by document id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success. Returns file. In response, media type is shown as "
                  + "application/octet-stream but it can be application/pdf as well "
                  + "if the file is of type pdf. ",
              content = @Content(mediaType = "application/octet-stream",
                  schema = @Schema(
                      type = "file", format = "binary"))),
          @ApiResponse(
              responseCode = "404",
              description = "Document not found")})
  public Response getDoc(
      @Parameter(description = "Document id") @PathParam("documentId") Integer documentId)
      throws ProtossException {

    DocumentManager documentManager = getImpl(DocumentManager.class);
    Document document = documentManager.getDocumentById(documentId);

    if (document == null || document.getPatientId() != getPatientId()) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Document not found");
    }

    Map<Integer, FileType> fileTypes = fileTypesById(documentManager);
    String mediaType = getMediaType(fileTypes, document.getFileType());

    AccDocManager accDocManager = getImpl(AccDocManager.class);
    AccDoc accDoc = accDocManager.getAccDocByFileName(
        document.getFileName(),
        document.getPathName());

    if (accDoc == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Document not found");
    }
    return Response.ok(accDoc.getContents(), mediaType).build();
  }

  /**
   * Returns a map of file types by ID.
   *
   * @param manager Documentation Manager object
   * @return A map of file types by ID
   * @throws DataAccessException If there has been a database error.
   */
  private Map<Integer, FileType> fileTypesById(
      DocumentManager manager) throws ProtossException {

    Set<FileType> fileSet = manager.getFileTypes();

    return fileSet.stream().collect(Collectors.toMap(FileType::getTypeId, t -> t));
  }
}

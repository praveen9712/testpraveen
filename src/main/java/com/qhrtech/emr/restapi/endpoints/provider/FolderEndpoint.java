
package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.docs.FolderTypeManager;
import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.model.docs.FolderType;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SaveException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.dto.FolderDto;
import com.qhrtech.emr.restapi.models.dto.SubFolderDto;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.security.exceptions.PreferenceDisabledException;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This <code>FolderEndpoint</code> collection is designed to provide CRUD operations for document
 * folders and sub-folders. Provider level authorization is required.
 *
 * @RequestHeader Authorization Provider level authorization grant
 * @HTTP 200 Request successful
 * @HTTP 401 Consumer unauthorized
 * @HTTP 400 Bad request or business validation failure
 */
@Component("provider-folder-endpoint")
@Path("/v1/provider-portal/")
@Facet("provider-portal")
@Tag(name = "Folder Endpoints",
    description = "Exposes all folder endpoints")
public class FolderEndpoint extends AbstractEndpoint {

  public static final String PREFERENCE_NOT_SET_FOR_SUBTYPE =
      "Preference not enabled to view sub types.";

  /**
   * Gets all the folders and their associated sub-folders(sub-types). Also, allows '%' wild-card
   * search with the folder name(not the sub-folder name).
   *
   * <p>
   * To search 'Image Folder', below keywords are valid:
   * <ul>
   * <li>Image Folder</li>
   * <li>image Folder</li>
   * <li>%ma%</li>
   * <li>%der</li>
   * <li>Image %</li>
   * </ul>
   * </p>
   *
   * @param folderName The folder name to search for. If it is empty or null the endpoint will
   *        return all available folders. If it's a wild-card the results will be matching folders.
   *        And is a case-insensitive.
   * @return A Set of {@link FolderDto} objects
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("folders")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_READ', 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess('DOCUMENTS', 'READ_ONLY' )   ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves Folders",
      description = "Gets all the folders and their associated sub-folders(sub-types)."
          + "Also, allows '%' wild-card search with the folder name(not the sub-folder name).\n\n"
          + "To search 'Image Folder', below keywords are valid: \n\n"
          + "* Image Folder\n\n"
          + "* image Folder\n\n"
          + "* %ma%\n\n"
          + "* %der\n\n"
          + "* Image %\n\n",
      responses = {@ApiResponse(
          responseCode = "200",
          description = "Success",
          content = @Content(
              array = @ArraySchema(
                  schema = @Schema(implementation = FolderDto.class))))})
  @Parameter(name = "folderName",
      description = "The folder name to search for. "
          + "If it is empty or null the endpoint will return all available folders. "
          + "If it's a wild-card the results will be matching folders. "
          + "And is a case-insensitive.",
      in = ParameterIn.QUERY)
  public Set<FolderDto> getFolders(
      @Parameter(hidden = true) @QueryParam("folderName") String folderName)
      throws ProtossException {

    FolderTypeManager folderTypeManager = getImpl(FolderTypeManager.class);

    Map<FolderType, Set<FolderType>> folders =
        folderTypeManager.getAllFolderRelationsByFolderName(folderName);

    Set<FolderDto> foldersSet = processSubFolders(folders);

    return foldersSet;
  }

  /**
   * Get folder by Id and associated sub-folders(sub-types) which are available in the system.
   *
   * @return A {@link FolderDto} object
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("folders/{folderId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_READ', 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess('DOCUMENTS', 'READ_ONLY' )   ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves the folder",
      description = "Gets the folder and all its associated sub-folders for the folder id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(
                      implementation = FolderDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found")
      })
  public FolderDto getFolderById(
      @Parameter(description = "Folder id") @PathParam("folderId") Integer folderId)
      throws ProtossException {

    FolderTypeManager folderTypeManager = getImpl(FolderTypeManager.class);

    Map<FolderType, Set<FolderType>> folders =
        folderTypeManager.getFolderRelationsByFolderId(folderId);

    if (folders.isEmpty()) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }

    Set<FolderDto> foldersSet = processSubFolders(folders);

    FolderDto folder = foldersSet.iterator().next();

    return folder;
  }

  /**
   * Get all the sub-folders(sub-types) available in the system.
   *
   * @return A Set of {@link SubFolderDto} objects
   * @throws DataAccessException If there has been a database error.
   */
  @GET
  @Path("folders/sub-folders")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_READ', 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess('DOCUMENTS', 'READ_ONLY' )   ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves sub-folders",
      description = "Gets a set of all sub-folders available in the system.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(
                      implementation = SubFolderDto.class))))
      })
  public Set<SubFolderDto> getSubFolders() throws ProtossException {

    FolderTypeManager folderTypeManager = getImpl(FolderTypeManager.class);

    Set<FolderType> folderSubtypes = folderTypeManager.getSubFolders();
    Set<SubFolderDto> foldersSet = new HashSet<>();
    for (FolderType folder : folderSubtypes) {
      SubFolderDto folderTypeDto = mapDto(folder, SubFolderDto.class);
      foldersSet.add(folderTypeDto);
    }
    return foldersSet;
  }

  /**
   * Get sub-folder by Id.
   *
   * @return A {@link SubFolderDto} object
   */
  @GET
  @Path("folders/sub-folders/{subFolderId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_READ', 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess('DOCUMENTS', 'READ_ONLY' )   ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Retrieves the sub-folder",
      description = "Gets the sub-folder for the id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  examples = @ExampleObject("{\"id\": 1, \"name\": \"Document\"}"),
                  schema = @Schema(
                      implementation = SubFolderDto.class

                  ))),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found")
      })
  public SubFolderDto getSubFolderById(
      @Parameter(
          description = "Id of the sub-folder") @PathParam("subFolderId") Integer subFolderId)
      throws ProtossException {

    FolderTypeManager folderTypeManager = getImpl(FolderTypeManager.class);

    FolderType folderSubtype = folderTypeManager.getSubFolderById(subFolderId);

    if (folderSubtype == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource Not Found");
    }

    SubFolderDto subFolderDto = mapDto(folderSubtype, SubFolderDto.class);

    return subFolderDto;
  }

  /**
   * Create a folder of type {@link FolderDto}
   *
   * <p>
   * The folder can be created with below mentioned scenarios:
   * </p>
   * <p>
   * a) To create a folder without any sub-folders to link with: only folder name field is needed
   * and rest of the fields must be null.
   * </p>
   * <p>
   * b)To create a folder with linked sub-folders: along with folder name, desired
   * {@link SubFolderDto}s must be provided to the request.
   * </p>
   * <p>
   * b.1) To link a new sub-folder, provide only sub-folder name in the {@link SubFolderDto} object.
   * This will create the sub-folder and link it to the folder created.
   * </p>
   * <p>
   * b.2) To link an existing sub-folder, provide the {@link SubFolderDto} object with both id and
   * name. This will link to the created folder.
   *
   * </p>
   *
   * @param folder object of type {@link FolderDto}
   * @return Folder Id of the {@link FolderDto} object created.
   * @throws TimeZoneNotFoundException If TimeZone is not set in the DB.
   * @throws SaveException An exception occurred during a save action.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 Unable to process json for this request.
   * @HTTP 400 If the folder name field or any of the sub-folders(if intend to link any sub-folder)
   *       name field is empty or null.
   * @HTTP 403 If try to create duplicate folder or sub-folder.
   * @HTTP 403 If a sub-folder is not found to be linked.
   **/
  @POST
  @Path("folders")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess( 'DOCUMENTS', 'READ_WRITE' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Saves the folder",
      description = "Creates the folder.</br>"
          + "The folder can be created with below mentioned scenarios:</br>"
          + "a) To create the folder without any sub-folders to link with: only folder name field "
          + "is needed and rest of the fields must be null.</br>"
          + "b)To create the folder with linked sub-folders: along with folder name, desired "
          + "sub-folder must be provided to the request.</br>"
          + "&nbsp;b.1) To link the new sub-folder, provide only sub-folder name."
          + "This will create the sub-folder and link it to the folder "
          + "created.</br>"
          + "&nbsp;b.2) To link the existing sub-folder, provide the sub-folder "
          + "with both id and name. This will link to the created folder.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success. Returns folder id.",
              content = @Content(
                  schema = @Schema(
                      type = "integer", example = "1", description = "folder id"))),
          @ApiResponse(
              responseCode = "404",
              description = "Resource not found")
      })
  public int createFolder(
      @Parameter(description = "Object of folder") @Valid FolderDto folder)
      throws ProtossException {

    Set<FolderType> folderSubTypes = new HashSet<>();
    if (!(folder.getSubFolders() == null)) {
      folderSubTypes = folder.getSubFolders().stream().map(x -> mapDto(x, FolderType.class))
          .collect(Collectors.toSet());
    }

    FolderTypeManager folderTypeManager = getImpl(FolderTypeManager.class);

    int folderId = folderTypeManager.createFolder(folder.getName(), folderSubTypes, getUser());

    return folderId;
  }

  /**
   * Link or unlink sub-folders to a folder.
   *
   * <p>
   * a) To link sub-folders: Must provide folder ID, name, current {@link SubFolderDto}s(existing
   * linked sub-folders to the folder) and new {@link SubFolderDto}s(must exist in the system) to be
   * linked. For example: If a folder is linked with sub-folder A and B, in order to link sub-folder
   * C, provide all the three sub-folders in the update request.
   * </p>
   * <p>
   * b)To unlink sub-folders: Must provide folder ID, name, and provide all the {@link SubFolderDto}
   * objects which should be finally linked to the folder. For example: If a folder is linked with
   * sub-folder A and B, in order to unlink sub-folder A, provide only sub-folder B in the update
   * request. To unlink all sub-folders, {@link SubFolderDto} collection object must be {@code
   * empty}.
   *
   * </p>
   *
   * @param folderType object of type {@link FolderDto}
   * @return HTTP status 200 if the folder was updated successfully
   * @throws TimeZoneNotFoundException If TimeZone is not set in the DB
   * @throws SaveException Business validation exception.
   * @throws DataAccessException If there has been a database error.
   **/
  @PUT
  @Path("folders/{folderId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess( 'DOCUMENTS', 'READ_WRITE' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Updates folder links",
      description = "Link or unlink sub-folders to the folder.</br>"
          + "a) To link sub-folders: Must provide folder id, name, current sub-folders(existing "
          + "linked sub-folders to the folder) and new sub-folders(must exist in the system) to "
          + "be linked. For example: If the folder is linked with sub-folder A and B, in order to "
          + "link sub-folder C, provide all the three sub-folders in the update request.</br>"
          + "b) To unlink sub-folders: Must provide folder id, name, and provide all the "
          + "sub-folders which should be finally linked to the folder. "
          + "For example: If the folder is linked with sub-folder A and B, in order to unlink "
          + "sub-folder A, provide only sub-folder B in the update request. "
          + "To unlink all sub-folders, the sub folder collection must be empty.",
      responses = {
          @ApiResponse(
              responseCode = "200", description = "OK"),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid folder id or folder information not provided")
      })
  public Response updateFolderLinks(
      @Parameter(description = "The folder id") @PathParam("folderId") Integer folderId,
      @Parameter(description = "Object of folder") FolderDto folderType)
      throws ProtossException {

    if (folderType == null) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Folder information is missing.");
    }

    if (folderId == null || folderId < 0 || !folderId.equals(folderType.getId())) {
      throw Error.webApplicationException(Status.BAD_REQUEST, "Invalid folder id.");
    }

    FolderType folder = mapDto(folderType, FolderType.class);
    Set<FolderType> folderSubTypes = new HashSet<>();
    if (folderType.getSubFolders() != null) {
      folderSubTypes =
          folderType.getSubFolders().stream().map(x -> mapDto(x, FolderType.class))
              .collect(Collectors.toSet());
    }
    FolderTypeManager folderTypeManager = getImpl(FolderTypeManager.class);

    folderTypeManager.updateFolderRelations(folder, folderSubTypes, getUser());

    return Response.ok().build();
  }

  /**
   * Delete a folder {@link FolderDto}
   *
   * @param folderId folder ID of {@link FolderDto} object to be deleted.
   * @return HTTP status 200 if the folder was deleted successfully
   * @throws TimeZoneNotFoundException If the TimeZone is not set in the Accuro Database.
   * @throws DataAccessException If there has been a database error. And when no folder found to
   *         delete.
   **/
  @DELETE
  @Path("folders/{folderId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess( 'DOCUMENTS', 'READ_WRITE' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Deletes the folder",
      description = "Deletes the folder.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "OK")})
  @Parameter(name = "folderId",
      description = "The folder id of the folder to be deleted",
      in = ParameterIn.PATH)
  public Response deleteFolder(
      @Parameter(hidden = true) @PathParam("folderId") Integer folderId)
      throws ProtossException {

    FolderTypeManager folderTypeManager = getImpl(FolderTypeManager.class);

    folderTypeManager.deleteFolder(folderId, getUser());

    return Response.ok().build();
  }

  /**
   * Create a sub-folder of type {@link SubFolderDto}. In this request only sub-folder is created.
   * This sub-folder can be later linked to any parent folder.
   * <p>
   * Sub-folder name field must be provided and id field can be left blank.
   * </p>
   *
   * @param subFolderDto {@link SubFolderDto}.
   * @return Folder id of the sub-folder created
   * @throws TimeZoneNotFoundException If TimeZone is not set in the DB
   * @throws SaveException If the provided sub-folder name already exists.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 400 If the sub-folder name is blank or null.
   * @HTTP 403 If the sub-folder name already exists.
   **/
  @POST
  @Path("folders/sub-folders")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess( 'DOCUMENTS', 'READ_WRITE' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Saves the sub-folder",
      description = "Creates the sub-folder. In this request, only sub-folder will be created. "
          + "This sub-folder can be later linked to any parent folder.</br> "
          + "The sub-folder name field must be provided and the id field can be left blank. ",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Success. Return sub-folder id.",
              content = @Content(
                  schema = @Schema(
                      type = "integer", description = "The sub-folder id", example = "1")))})
  public Response createSubFolder(
      @Parameter(description = "The sub-folder") @Valid SubFolderDto subFolderDto)
      throws ProtossException, TimeZoneNotFoundException, SaveException {

    if (!"true".equalsIgnoreCase(subTypePreferenceSet())) {
      throw new PreferenceDisabledException(PREFERENCE_NOT_SET_FOR_SUBTYPE);
    }
    FolderTypeManager folderTypeManager = getImpl(FolderTypeManager.class);
    int subFolderId = folderTypeManager.createSubFolder(subFolderDto.getName(), getUser());

    return Response.ok().entity(subFolderId).build();
  }

  /**
   * Delete a sub-folder of type {@link SubFolderDto}. This will delete the sub-folder and unlink it
   * from the all its linked parent folders.
   * <p>
   * The sub-folder cannot be deleted if it is already in use.
   * </p>
   *
   * @param subFolderId folder ID of {@link SubFolderDto} object to be deleted.
   * @return HTTP status 200 if the sub-folder was deleted successfully
   * @throws TimeZoneNotFoundException If TimeZone is not set in the DB
   * @throws BusinessLogicException If there is failure of any business validation.
   * @throws DataAccessException If there has been a database error, or no sub-folder found to
   *         delete.
   **/
  @DELETE
  @Path("folders/sub-folders/{subFolderId}")
  @PreAuthorize("#oauth2.hasAnyScope( 'DOCUMENTS_WRITE' ) "
      + "and #accuro.hasAccess( 'DOCUMENTS', 'READ_WRITE' ) ")
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  @Operation(
      summary = "Deletes the sub-folder",
      description = "Deletes the sub-folder. This will delete the sub-folder "
          + "and unlink it from all the linked parent folders.</br>"
          + "The sub-folder cannot be deleted if it is already in use.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "OK")
      })
  public Response deleteSubFolder(
      @Parameter(
          description = "The sub-folder id") @PathParam("subFolderId") Integer subFolderId)
      throws ProtossException {

    FolderTypeManager folderTypeManager = getImpl(FolderTypeManager.class);

    if (!"true".equalsIgnoreCase(subTypePreferenceSet())) {
      throw new PreferenceDisabledException(PREFERENCE_NOT_SET_FOR_SUBTYPE);
    }

    folderTypeManager.deleteSubFolder(subFolderId, getUser());
    return Response.ok().build();
  }

  private Set<FolderDto> processSubFolders(Map<FolderType, Set<FolderType>> folders) {
    Set<FolderDto> foldersSet = new HashSet<>();
    for (FolderType folder : folders.keySet()) {
      Set<SubFolderDto> folderSubTypes = new HashSet<>();
      FolderDto folderDto = mapDto(folder, FolderDto.class);
      for (FolderType folderSubType : folders.get(folder)) {

        if (folderSubType.getId() != null) {
          SubFolderDto subFolderDto = mapDto(folderSubType, SubFolderDto.class);
          folderSubTypes.add(subFolderDto);
        }
      }

      folderDto.setSubFolders(folderSubTypes);
      foldersSet.add(folderDto);

    }
    return foldersSet;
  }

  private String subTypePreferenceSet() throws ProtossException {
    AccuroPreferenceManager preferenceManager = getImpl(AccuroPreferenceManager.class);
    return preferenceManager.getSystemPreference("ShowDocumentsSubType");
  }
}

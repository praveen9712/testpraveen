
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@Path("/v1/accuro-preferences")
@Tag(name = "Accuro Preferences Endpoint",
    description = "Exposes accuro preferences endpoint")
public class AccuroPreferencesEndpoint extends AbstractEndpoint {

  private final Logger log = LoggerFactory.getLogger(getClass());

  /**
   * Return the preference value associated with the path and name
   *
   * @param path Preference path
   * @param name Preference name
   * @return Preference value associated with the path and name
   * @throws DatabaseInteractionException If there has been a database error.
   * @HTTP 404 If the record does not exist in the database.
   * @HTTP 200 Success
   */
  @GET
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Operation(
      summary = "Retrieves preference value",
      description = "Retrieves preference value associated with the path and name.",
      responses = {
          @ApiResponse(
              responseCode = "404",
              description = "Preference not found"),
          @ApiResponse(
              responseCode = "200",
              description = "Success",
              content = @Content(
                  schema = @Schema(implementation = String.class)))})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)
  public String getPreferenceValue(
      @QueryParam("path") String path,
      @QueryParam("name") String name)
      throws DatabaseInteractionException {
    validateInputs(path, name);
    AccuroPreferenceManager manager = getImpl(AccuroPreferenceManager.class);

    String preferenceValue = manager.getPreference(path, name);
    if (preferenceValue == null) {
      log.error("preferenceValue is null");
      throw Error.webApplicationException(
          Status.NOT_FOUND, "Resource not found.");
    }

    return preferenceValue;
  }

  private void validateInputs(String path, String name) {

    if (StringUtils.isBlank(path) || StringUtils.isBlank(name)) {
      throw Error.webApplicationException(
          Status.BAD_REQUEST, "path and name parameters should be provided.");
    }

  }

  /**
   * Create or update preferences for the given path.
   *
   * @param path The path for which preferences should be updated.
   * @param name The name associated with the preferences.
   * @param value The new value for the preferences.
   * @return A 204 response if the operation is successful.
   */
  @PUT
  @PreAuthorize("#oauth2.hasScope('API_INTERNAL')")
  @Facet("internal")
  @Operation(
      summary = "Create or Update Preferences",
      description = "Creates or updates preferences for the given path and name.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Success"),
          @ApiResponse(
              responseCode = "400",
              description = "If the path is not valid."),
          @ApiResponse(
              responseCode = "401",
              description = "Access forbidden. Permission is required.")})
  @Parameter(
      name = "authorization",
      description = "Provider level authorization grant",
      in = ParameterIn.HEADER,
      required = true)

  public Response createOrUpdatePreferences(
      @QueryParam("path") String path,
      @QueryParam("name") String name,
      @RequestBody(
          description = "The value for preference") String value)
      throws DatabaseInteractionException {
    validateInputs(path, name);
    AccuroPreferenceManager manager = getImpl(AccuroPreferenceManager.class);

    if (StringUtils.isBlank(value)) {
      log.error("The request body cannot be blank.");
      throw Error.webApplicationException(
          Status.BAD_REQUEST, "The request body cannot be blank.");
    }

    try {
      manager.createOrUpdatePreferenceValue(path, name, value);
    } catch (NoDataFoundException exp) {
      log.error("The request body cannot be blank.", exp);
      throw Error.webApplicationException(Status.BAD_REQUEST, exp.getMessage());
    }
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}


package com.qhrtech.emr.restapi.endpoints.provider;

import com.qhrtech.emr.accuro.api.security.UserAuthenticationManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.security.AccuroUserDetails;
import com.qhrtech.emr.restapi.security.MultiTenantAuthenticationKeyGenerator;
import com.webcohesion.enunciate.metadata.Facet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This endpoint is use to change the current office associated with an access token so that
 * subsequent API calls with that access token will operate as if they are for the specified office.
 *
 * @HTTP 204 Request successful
 * @HTTP 401 Consumer unauthorized
 * @see MultiTenantAuthenticationKeyGenerator
 */
@Component
@Path("/v1/provider-portal/current-office")
@Facet("provider-portal")
@Tag(name = "ChangeOffice Endpoints",
    description = "Exposes endpoints to change office")
public class ChangeOfficeEndpoint extends AbstractEndpoint {

  private static final String KEY_OFFICE = "office";
  private static final String KEY_ORIGINAL_OFFICE = "original_office";

  @Autowired
  private TokenStore tokenStore;

  /**
   * Change which office is considered the "current office" when making calls using the current
   * access token.
   *
   * @param officeId Id of the office that a user wants to switch too.
   * @throws DataAccessException If there has been a database error.
   * @HTTP 403 If the user does not have access to the office requested.
   */
  @PUT
  @Operation(
      summary = "Change which office is considered the \"current office\" when making calls "
          + "using the current access token.",
      description = "This endpoint is use to change the current office associated with an access "
          + "token so that subsequent API calls with that access token will operate as if they are "
          + "for the specified office.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "Request successful"),
          @ApiResponse(
              responseCode = "401",
              description = "Consumer unauthorized"),
          @ApiResponse(
              responseCode = "403",
              description = "If the user does not have access to the office requested")
      })
  public void changeOffice(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Office id - the office that a user wants to switch too.",
          content = @Content(
              schema = @Schema(type = "integer", example = "1"))) @RequestBody int officeId)
      throws ProtossException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!(auth instanceof OAuth2Authentication)) {
      throw Error.webApplicationException(Status.UNAUTHORIZED, "Unable to change office.");
    }

    // This should be a safe assumption as any user accessing the provider-portal
    // should be enforced to be a provider by the ResourceServerConfiguration
    AccuroUserDetails userDetails = (AccuroUserDetails) auth.getPrincipal();
    int userId = userDetails.getUserId();

    UserAuthenticationManager authManager = getImpl(UserAuthenticationManager.class);
    Set<Integer> offices = authManager.getOfficeIds(userId);

    if (!offices.contains(officeId)) {
      throw Error.webApplicationException(Status.FORBIDDEN,
          "User does not belong to the requested Office.");
    }
    OAuth2Authentication accessAuthentication = (OAuth2Authentication) auth;

    // Determine the original office id so we can generate the same
    // authentication key hash and update the existing access token instead
    // of creating an entirely new access token
    Integer origOfficeId;
    {
      origOfficeId = (Integer) accessAuthentication.getOAuth2Request().getExtensions()
          .get(KEY_ORIGINAL_OFFICE);
      if (origOfficeId == null) {
        origOfficeId =
            (Integer) accessAuthentication.getOAuth2Request().getExtensions().get(KEY_OFFICE);
      }
    }

    accessAuthentication.getOAuth2Request().getExtensions().put(KEY_OFFICE, officeId);
    accessAuthentication.getOAuth2Request().getExtensions().put(KEY_ORIGINAL_OFFICE, origOfficeId);

    // We need the access token to get the refresh token, and to save the updated authentication
    OAuth2AccessToken accessToken = tokenStore.getAccessToken(accessAuthentication);

    // Setting this to null eliminates the perpetual addition of decodedDetails object
    accessAuthentication.setDetails(null);
    tokenStore.storeAccessToken(accessToken, accessAuthentication);

    OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();

    if (refreshToken != null) {
      // If we dont also update the refresh token, the office change will revert when that token is
      // used
      OAuth2Authentication refreshAuthentication =
          tokenStore.readAuthenticationForRefreshToken(refreshToken);
      refreshAuthentication.getOAuth2Request().getExtensions().put(KEY_OFFICE, officeId);
      refreshAuthentication.getOAuth2Request().getExtensions().put(KEY_ORIGINAL_OFFICE,
          origOfficeId);

      // We need to remove the existing refresh token so there are not two entries for the same
      // token in the store
      tokenStore.removeRefreshToken(refreshToken);
      tokenStore.storeRefreshToken(refreshToken, refreshAuthentication);
    }
  }
}

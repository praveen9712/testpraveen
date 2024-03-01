
package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.restapi.endpoints.provider.ChangeOfficeEndpoint;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

/**
 * This class is used to generate an authentication key that will be unique for each tenant and
 * office in addition to the standard client, scopes and username.
 * <p>
 * The key generating by this class will also take into account the original office of an
 * authentication to avoid breaking the change office endpoint.
 *
 * @see AccuroRequestFactory
 * @see ChangeOfficeEndpoint
 */
public class MultiTenantAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

  private static final String KEY_CLIENT_ID = "client_id";
  private static final String KEY_SCOPE = "scope";
  private static final String KEY_USERNAME = "username";
  private static final String KEY_TENANT = "tenant";
  private static final String KEY_OFFICE = "office";
  private static final String KEY_ORIGINAL_OFFICE = "original_office";

  @Override
  public String extractKey(OAuth2Authentication authentication) {
    if (authentication == null) {
      return "";
    }

    Map<String, String> values = new LinkedHashMap<>();
    OAuth2Request authorizationRequest = authentication.getOAuth2Request();
    if (!authentication.isClientOnly()) {
      values.put(KEY_USERNAME, authentication.getName());

      // Set tenant for Accuro user or patient user
      if (authentication.getPrincipal() instanceof AccuroUserDetails) {
        values.put(KEY_TENANT, ((AccuroUserDetails) authentication.getPrincipal()).getTenantId());
      } else if (authentication.getPrincipal() instanceof PatientUserDetails) {
        values.put(KEY_TENANT, ((PatientUserDetails) authentication.getPrincipal()).getTenantId());
      }
    }
    values.put(KEY_CLIENT_ID, authorizationRequest.getClientId());
    if (authorizationRequest.getScope() != null) {
      values.put(KEY_SCOPE, OAuth2Utils.formatParameterList(authorizationRequest.getScope()));
    }
    if (authorizationRequest.getExtensions() != null) {

      // Office that may be accessed with this authentication
      Integer officeId = (Integer) authorizationRequest.getExtensions().get(KEY_OFFICE);

      // Original office that must be used to generate the hash so the
      // change office endpoint will continue functioning as expected
      // Note: Original office should be removed from this key generator
      // when the ChangeOfficeEndpoint is removed.
      Integer origOfficeId =
          (Integer) authorizationRequest.getExtensions().get(KEY_ORIGINAL_OFFICE);
      if (origOfficeId == null) {
        // Original office is not specified so we use office as usual
        values.put(KEY_OFFICE, officeId == null ? null : officeId.toString());
      } else {
        // Original office was specified so we use the original office
        // for this key to allow change office to function as expected
        values.put(KEY_OFFICE, origOfficeId.toString());
      }
    }
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(
          "MD5 algorithm not available.  Fatal (should be in the JDK).");
    }

    try {
      byte[] bytes = digest.digest(values.toString().getBytes("UTF-8"));
      return String.format("%032x", new BigInteger(1, bytes));
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(
          "UTF-8 encoding not available.  Fatal (should be in the JDK).");
    }
  }
}

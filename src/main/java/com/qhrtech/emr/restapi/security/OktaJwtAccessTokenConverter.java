
package com.qhrtech.emr.restapi.security;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;


public class OktaJwtAccessTokenConverter extends JwtAccessTokenConverter {

  public static final String OKTA = "Okta";
  private static final String REQUESTED_ACRON = "requested_acron";
  private static final String ACCURO_ACRONYM = "accuro_acronym_list";
  private static final String SQUID = "squid";
  private static final String TENANT_PARAM_KEY = "tenant";
  private static final String CLIENT_ID = "oauth_client_id";
  private static final String EXP = "exp";
  private static final String CID = "cid";
  private static final String SCP = "scp";
  private static final String ACRON = "ACRON-";
  private static final String SERVICE_USER_ID = "service_user_id";
  private final Logger log = LoggerFactory.getLogger(getClass());
  private AccessTokenVerifier parser;

  public OktaJwtAccessTokenConverter(AccessTokenVerifier parser) {
    this.parser = parser;
  }

  @Override
  public Map<String, Object> decode(String token) {

    Jwt jwt;
    try {
      jwt = parser.decode(token);
    } catch (JwtVerificationException | IllegalArgumentException e) {
      String message = "Error verifying the access token";
      if (e.getCause() != null && e.getCause().getMessage().contains("JWT expired")) {
        message = "Access token expired: " + token;
      }
      log.error("Exception while JWT validation: ", e);
      throw new InvalidTokenException(message, e);
    }

    return jwt.getClaims();
  }

  @Override
  public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {

    DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(value);
    Map<String, Object> info = new HashMap<>(map);
    token.setExpiration(new Date(new Long((Integer) map.get(EXP)) * 1000L));
    token.setScope(getScopes(map));
    token.setValue(value);
    token.setAdditionalInformation(info);
    return token;
  }

  private Set<String> getScopes(Map<String, ?> map) {

    HashSet<String> scopes = map.get(SCP) == null
        ? new HashSet<>()
        : new HashSet<>((ArrayList<String>) map.get(SCP));

    return scopes;
  }

  @Override
  public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
    Map<String, String> parameters = new HashMap<>();
    String clientId = (String) map.get(CID);
    String message = "Invalid access token: ";
    // even if the client ID is null, it works, as UUID session is created
    // with the combination of client and user ID. We are still able to generate UUID session.
    if (clientId == null) {
      log.error(message
          + " Either CID claim is missing or its value is null.");
      throw new InvalidTokenException(message + "Unable to get client details.");
    }

    parameters.put(CID, clientId);
    Set<String> resourceIds = new HashSet<>(Arrays.asList("oauth2-resource", OKTA));
    Set<String> scope = getScopes(map);

    OAuth2Request request =
        new OAuth2Request(parameters, clientId, null, true, scope, resourceIds, null, null,
            null);

    String userIdentity = (String) map.get(SQUID);
    String serviceUserId = (String) map.get(SERVICE_USER_ID);
    String accuroAcronym = getAcronym(map);
    request.getExtensions().put(TENANT_PARAM_KEY, accuroAcronym);
    if (StringUtils.isBlank(userIdentity) || StringUtils.isBlank(serviceUserId)) {
      parameters.put(GRANT_TYPE, "client_credentials");
    } else {
      parameters.put(GRANT_TYPE, "authorization_code");
    }

    request.getExtensions().put(CLIENT_ID, clientId);
    Authentication user = extractUserAuth(map);
    return new OAuth2Authentication(request, user);
  }

  private Authentication extractUserAuth(Map<String, ?> map) {
    // Get the squid from the okta
    String userIdentity = (String) map.get(SQUID);
    // Get the service_user_id from the okta
    String serviceUserId = (String) map.get(SERVICE_USER_ID);
    if (!StringUtils.isBlank(userIdentity) && !StringUtils.isBlank(serviceUserId)) {
      log.error("One of the userIdentity, serviceUserId should be null");
      throw new InvalidTokenException(
          "Both userIdentity and serviceUserId are not null. One of them must be null.");
    }

    if (!StringUtils.isBlank(userIdentity)) {
      log.info("SQUID  available. Token is clientOnly.");
      // The username is in the default claim with name sub
      UserDetails user = OktaUserDetails.fromSquid(userIdentity, getAcronym(map));

      return new UsernamePasswordAuthenticationToken(user, "N/A", null);
    }

    if (!StringUtils.isBlank(serviceUserId)) {
      log.info("SERVICEUSERID available. Token is clientOnly.");

      if (!StringUtils.isNumeric(serviceUserId)) {
        log.error("service user id must be numeric");
        throw new InvalidTokenException(
            "The token service user id is improperly formatted");
      }

      // The username is in the default claim with name sub
      UserDetails user = OktaUserDetails.fromThirdPartyServiceUser(Integer.valueOf(serviceUserId),
          getAcronym(map));
      return new UsernamePasswordAuthenticationToken(user, "N/A", null);


    }
    log.error("one of userIdentity, serviceUserId should be set");
    return null;
  }

  private String getAcronym(Map<String, ?> map) {
    String message = "Invalid access token: ";

    String requestedAcron = (String) map.get(REQUESTED_ACRON);
    if (StringUtils.isNotBlank(requestedAcron)) {
      return parseAcron(requestedAcron);
    }
    log.info("JWT claim - requested_acron, has ACRON details missing "
        + "so try to lookup accuro_acronym_list.");

    ArrayList<String> acronymList = (ArrayList<String>) map.get(ACCURO_ACRONYM);
    if (CollectionUtils.isEmpty(acronymList)
        || StringUtils.isBlank(acronymList.get(0))) {
      log.error(message + "JWT claims - requested_acron and accuro_acronym_list, have"
          + " ACRON details missing.");
      throw new InvalidTokenException(message + "ACRON details missing.");
    }

    // Currently approach is to use the accuro_acronym_list[0] as the ACRON to trust
    String acron = ((ArrayList<String>) map.get(ACCURO_ACRONYM)).get(0);

    return parseAcron(acron);
  }

  private String parseAcron(String value) {
    if (value.startsWith(ACRON)) {
      value = value.substring(ACRON.length());
    }
    return value;
  }


}

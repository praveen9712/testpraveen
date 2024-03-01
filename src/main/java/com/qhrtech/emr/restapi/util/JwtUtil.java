
package com.qhrtech.emr.restapi.util;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;

/**
 *
 * @author kevin.kendall
 */
public class JwtUtil {

  public static Long getIdpUserId(JwtConsumer consumer, String token) throws InvalidJwtException {

    JwtClaims claims = consumer.processToClaims(token);

    Object resourceOwnerId = claims.getClaimValue("resource_owner_id");
    Object expObj = claims.getClaimValue("exp");
    if (!(resourceOwnerId instanceof Long) || !(expObj instanceof Long)) {
      return null;
    }

    long idpUserId = (long) resourceOwnerId;
    long expiry = (long) expObj;

    if (expiry < System.currentTimeMillis() / 1000) {
      return null;
    }

    return idpUserId;
  }
}

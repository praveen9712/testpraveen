
package com.qhrtech.emr.restapi.security.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class LoggingWebResponseExceptionTranslator extends
    DefaultWebResponseExceptionTranslator {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
    log.error("Error in Authentication Server", e);
    return super.translate(e);
  }
}

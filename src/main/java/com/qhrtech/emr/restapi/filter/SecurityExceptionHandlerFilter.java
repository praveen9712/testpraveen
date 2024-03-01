
package com.qhrtech.emr.restapi.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qhrtech.emr.restapi.models.dto.security.ErrorResponse;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

public class SecurityExceptionHandlerFilter extends OncePerRequestFilter {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (RuntimeException e) {
      log.error("Exception in filter chain", e);
      processExceptionImpl((HttpServletResponse) response,
          e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  private String convertObjectToJson(Object object) throws JsonProcessingException {
    if (object == null) {
      return null;
    }
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(object);
  }

  private void processExceptionImpl(HttpServletResponse response, String message, int status)
      throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    ErrorResponse error = new ErrorResponse(message);
    response.getWriter().write(convertObjectToJson(error));
  }
}

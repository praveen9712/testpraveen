
package com.qhrtech.emr.restapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qhrtech.emr.restapi.models.dto.security.ErrorResponse;
import com.qhrtech.emr.restapi.security.exceptions.FilterException;
import com.qhrtech.emr.restapi.security.exceptions.InvalidDataSourceException;
import com.qhrtech.emr.restapi.security.exceptions.InvalidTenantException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @author bryan.bergen
 */
public class ExceptionProcessFilter implements Filter {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private ObjectMapper jsonObjectMapper;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(
        this,
        filterConfig.getServletContext());
  }

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      chain.doFilter(request, response);
    } catch (FilterException e) {
      log.error("Exception in filter chain", e);
      processExceptionImpl((HttpServletResponse) response, e.getMessage(), e.getStatus());
    } catch (ProcessingException e) {
      log.error("Exception in filter chain", e);
      if (e.getCause() instanceof ConnectException) {
        processExceptionImpl((HttpServletResponse) response,
            "We are unable to retrieve database information. Please try again later. ",
            HttpStatus.INTERNAL_SERVER_ERROR.value());
      } else if (e.getCause() instanceof SocketTimeoutException) {
        processExceptionImpl((HttpServletResponse) response,
            "We are unable to retrieve database information. Please try again later. ",
            HttpStatus.INTERNAL_SERVER_ERROR.value());
      } else {
        processExceptionImpl((HttpServletResponse) response,
            "Unable to verify the request at this point of time",
            HttpStatus.INTERNAL_SERVER_ERROR.value());
      }
    } catch (InvalidDataSourceException e) {
      log.error("Exception in filter chain", e);
      processExceptionImpl((HttpServletResponse) response, e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value());
    } catch (InvalidTenantException e) {
      log.error(e.getMessage(), e);
      processExceptionImpl((HttpServletResponse) response,
          "Configuration error: Accuro clinic not found.",
          HttpStatus.UNAUTHORIZED.value());
    } catch (Exception e) {
      log.error("Exception in filter chain", e);
      processExceptionImpl((HttpServletResponse) response,
          "There was an error. Please check if the right parameters are being passed",
          HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

  private void processExceptionImpl(HttpServletResponse response, String message, int status)
      throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    ErrorResponse error = new ErrorResponse(message);
    response.getWriter().write(jsonObjectMapper.writeValueAsString(error));
  }

}

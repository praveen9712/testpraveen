
package com.qhrtech.emr.restapi.filter;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class SecurityExceptionHandlerFilterTest {

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private FilterChain filterChain;
  @InjectMocks
  private SecurityExceptionHandlerFilter filter;

  @Before
  public void setup() throws ServletException, IOException {
    filter = spy(new SecurityExceptionHandlerFilter());
    openMocks(this);
  }

  @Test
  public void testDoFilter() throws IOException, ServletException {

    doNothing().when(filterChain).doFilter(request,
        response);
    filter.doFilterInternal(request, response, filterChain);
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  public void testDoFilterException() throws IOException, ServletException {

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(printWriter);

    RuntimeException runtimeException = new RuntimeException("Exception in filter chain");
    doThrow(runtimeException).when(filterChain).doFilter(request,
        response);
    filter.doFilterInternal(request, response, filterChain);
    verify(filterChain, times(1)).doFilter(request, response);
  }
}

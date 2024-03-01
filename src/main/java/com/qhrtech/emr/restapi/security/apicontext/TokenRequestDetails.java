
package com.qhrtech.emr.restapi.security.apicontext;


import java.util.Objects;
import org.springframework.http.HttpMethod;

/**
 * Additional details that can be included along with an oauth token in order to make authentication
 * decisions.
 *
 * @author james.michaud
 */
public class TokenRequestDetails {

  private final HttpMethod httpMethod;
  private final String requestUri;
  private final String requestParamTenantId;

  /**
   * @param httpMethod the Http method of the incoming api request.
   * @param requestUri the URI of the incoming api request.
   * @param requestParamTenantId the value of tenantId if it is included in the incoming http
   *        request url parameters.
   */
  public TokenRequestDetails(HttpMethod httpMethod, String requestUri,
      String requestParamTenantId) {
    this.httpMethod = httpMethod;
    this.requestUri = requestUri;
    this.requestParamTenantId = requestParamTenantId;
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public String getRequestParamTenantId() {
    return requestParamTenantId;
  }

  public String getRequestUri() {
    return requestUri;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TokenRequestDetails that = (TokenRequestDetails) o;
    return httpMethod == that.httpMethod && Objects.equals(requestUri, that.requestUri)
        && Objects.equals(requestParamTenantId, that.requestParamTenantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(httpMethod, requestUri, requestParamTenantId);
  }
}


package com.qhrtech.emr.restapi.filter;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.openMocks;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.security.AccuroUserDetails;
import com.qhrtech.emr.restapi.security.OktaJwtAccessTokenConverter;
import com.qhrtech.emr.restapi.security.OktaUserDetails;
import com.qhrtech.emr.restapi.security.PatientUserDetails;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;


public class MultiTenantLoggingFilterTest {

  @Mock
  private ServletRequest request;

  @Mock
  private ServletResponse response;

  @Mock
  private FilterChain filterChain;

  @InjectMocks
  private MultiTenantLoggingFilter filter;

  @Before
  public void setup() {
    filter = spy(new MultiTenantLoggingFilter());
    openMocks(this);
    doReturn(true).when(filter).isTenantRequired();
  }

  @Test
  public void testLoggingFilterForOktaClient() throws Exception {
    Set<String> resourceIds =
        new HashSet<>(Arrays.asList("oauth2-resource", OktaJwtAccessTokenConverter.OKTA));
    String accuroAcronym = TestUtilities.nextString(5);
    OAuth2Request oauthRequest =
        new OAuth2Request(null, null, null, true, null, resourceIds, null, null,
            null);
    oauthRequest.getExtensions().put("tenant", accuroAcronym);
    Authentication auth = new OAuth2Authentication(oauthRequest, null);
    SecurityContextHolder.getContext().setAuthentication(auth);

    Assert.assertEquals("[" + accuroAcronym + "]", filter.tenantKey(request));

  }

  @Test
  public void testLoggingFilterForClientOnly() throws Exception {
    Set<String> resourceIds = new HashSet<>(Arrays.asList("oauth2-resource"));
    String accuroAcronym = TestUtilities.nextString(5);
    OAuth2Request oauthRequest =
        new OAuth2Request(null, null, null, true, null, resourceIds, null, null,
            null);
    oauthRequest.getExtensions().put("tenant", accuroAcronym);
    Authentication auth = new OAuth2Authentication(oauthRequest, null);
    SecurityContextHolder.getContext().setAuthentication(auth);
    doReturn(accuroAcronym).when(request).getParameter(TenantIdCheckFilter.TENANT_PARAM_KEY);
    Assert.assertEquals("[" + accuroAcronym + "]", filter.tenantKey(request));

  }

  @Test
  public void testLoggingFilterForNonOktaAccuroUser() throws Exception {
    Set<String> resourceIds = new HashSet<>(Arrays.asList("oauth2-resource"));
    String accuroAcronym = TestUtilities.nextString(5);
    AccuroUserDetails userDetails = new AccuroUserDetails.Builder()
        .setTenantId(accuroAcronym)
        .setUserId(TestUtilities.nextId())
        .setUsername(TestUtilities.nextString(10))
        .build();

    OAuth2Request oauthRequest =
        new OAuth2Request(null, null, null, true, null, resourceIds, null, null,
            null);
    oauthRequest.getExtensions().put("tenant", accuroAcronym);
    Authentication auth = new OAuth2Authentication(oauthRequest,
        new UsernamePasswordAuthenticationToken(userDetails, "N/A", null));
    SecurityContextHolder.getContext().setAuthentication(auth);
    doReturn(accuroAcronym).when(request).getParameter(TenantIdCheckFilter.TENANT_PARAM_KEY);
    Assert.assertEquals("[" + accuroAcronym + "]", filter.tenantKey(request));

  }


  @Test
  public void testLoggingFilterForNonOktaPatientUser() throws Exception {
    Set<String> resourceIds = new HashSet<>(Arrays.asList("oauth2-resource"));
    String accuroAcronym = TestUtilities.nextString(5);
    PatientUserDetails userDetails = new PatientUserDetails.Builder()
        .setTenantId(accuroAcronym)
        .setPatientId(TestUtilities.nextId())
        .setUsername(TestUtilities.nextString(10))
        .build();

    OAuth2Request oauthRequest =
        new OAuth2Request(null, null, null, true, null, resourceIds, null, null,
            null);
    oauthRequest.getExtensions().put("tenant", accuroAcronym);
    Authentication auth = new OAuth2Authentication(oauthRequest,
        new UsernamePasswordAuthenticationToken(userDetails, "N/A", null));
    SecurityContextHolder.getContext().setAuthentication(auth);
    doReturn(accuroAcronym).when(request).getParameter(TenantIdCheckFilter.TENANT_PARAM_KEY);
    Assert.assertEquals("[" + accuroAcronym + "]", filter.tenantKey(request));

  }

  @Test
  public void testLoggingFilterForNonOktaUser() throws Exception {
    Set<String> resourceIds = new HashSet<>(Arrays.asList("oauth2-resource"));
    String accuroAcronym = TestUtilities.nextString(5);
    OktaUserDetails userDetails =
        OktaUserDetails.fromSquid(TestUtilities.nextString(5), accuroAcronym);

    OAuth2Request oauthRequest =
        new OAuth2Request(null, null, null, true, null, resourceIds, null, null,
            null);
    oauthRequest.getExtensions().put("tenant", accuroAcronym);
    Authentication auth = new OAuth2Authentication(oauthRequest,
        new UsernamePasswordAuthenticationToken(userDetails, "N/A", null));
    SecurityContextHolder.getContext().setAuthentication(auth);
    doReturn(accuroAcronym).when(request).getParameter(TenantIdCheckFilter.TENANT_PARAM_KEY);
    Assert.assertEquals("[BAD REQUEST]", filter.tenantKey(request));

  }

  @Test
  public void testLoggingFilterForLocalUser() throws Exception {
    doReturn(false).when(filter).isTenantRequired();
    Assert.assertEquals("", filter.tenantKey(request));

  }


  @Test
  public void testLoggingFilterForWithNoAuthentication() {
    SecurityContextHolder.getContext().setAuthentication(null);
    Assert.assertEquals("[BAD REQUEST]", filter.tenantKey(request));

  }

}

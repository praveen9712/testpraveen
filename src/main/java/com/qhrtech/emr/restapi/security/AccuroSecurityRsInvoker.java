/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.accuro.model.security.AccuroUser;
import com.qhrtech.emr.accuro.model.security.ExternalAccess;
import com.qhrtech.emr.restapi.filter.RestApiModuleCheckFilter;
import com.qhrtech.emr.restapi.models.dto.security.ErrorResponse;
import java.lang.reflect.Method;
import java.util.Set;
import javax.ws.rs.core.Response;
import org.apache.cxf.message.Exchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author Blake Dickie
 */
public class AccuroSecurityRsInvoker extends ExceptionHandlingInvoker {

  @Value("${tenants.type:single}")
  private String tenantType;

  public AccuroSecurityRsInvoker() {
  }

  @Override
  protected Object performInvocation(
      Exchange exchange,
      final Object serviceObject,
      Method m,
      Object[] paramArray) throws Exception {

    ApiSecurityContext context = ApiSecurityManager.getInstance().getCurrentSecurityContext();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // If authentication is not OAuth 2 then bypass this invoker
    if (!(auth instanceof OAuth2Authentication)) {
      return super.performInvocation(exchange, serviceObject, m, paramArray);
    }

    if (auth.getPrincipal() instanceof AccuroUserDetails
        || auth.getPrincipal() instanceof OktaUserDetails) {
      AccuroUser accuroUser = context.getAccuroApiContext().getAccuroUser();

      if (accuroUser == null) {
        ErrorResponse error =
            new ErrorResponse("The user associated with your token no longer exists.");
        return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
      }

      if (!accuroUser.isActive()) {
        ErrorResponse error =
            new ErrorResponse("The user associated with your token has been deactivated.");
        return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
      }

      // User does not have external access for REST enabled
      Set<String> scopes = context.getScopes();
      if (!scopes.contains(RestApiModuleCheckFilter.IMPLICIT_EXTERNAL_ACCESS)
          && !context.getAccuroApiContext().getUserPermissions().getExternalAccess()
              .contains(ExternalAccess.REST)) {
        ErrorResponse error =
            new ErrorResponse("External access is not enabled for this user.");
        return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
      }
    }

    return super.performInvocation(exchange, serviceObject, m, paramArray);
  }
}

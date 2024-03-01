
package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.restapi.models.endpoints.Error;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response.Status;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods;

public class CustomOAuth2SecurityExpressionMethods extends OAuth2SecurityExpressionMethods {


  public CustomOAuth2SecurityExpressionMethods(
      Authentication authentication) {
    super(authentication);
  }

  public static final String QHR_SCOPE_PATTERN =
      "(user|patient)/(provider|consumer).(\\w+|\\*).(create|read|delete|update|\\*)";


  @Override
  public boolean hasScope(String scope) {

    List<String> scopes = new ArrayList<>();
    scopes.add(scope);
    addWildCardScopes(scopes, scope);
    return super.hasAnyScope(scopes.toArray(new String[scopes.size()]));
  }


  @Override
  public boolean hasAnyScope(String... scopes) {
    List<String> allScopes = new ArrayList<>();
    for (String scope : scopes) {
      allScopes.add(scope);
      addWildCardScopes(allScopes, scope);
    }

    return super.hasAnyScope(allScopes.toArray(new String[allScopes.size()]));

  }

  private void addWildCardScopes(List<String> scopes, String scope) {
    // e.g user/provider.Task.read
    if (scope.matches(QHR_SCOPE_PATTERN)) {
      String[] separatedScope = scope.split("\\.");
      String contextAndDomain = separatedScope[0];
      String resource = separatedScope[1];
      String verb = separatedScope[2];

      if ("*".equals(resource) || "*".equals(verb)) {
        throw Error.webApplicationException(Status.INTERNAL_SERVER_ERROR,
            "Wild card(s) are not supported in the scope names.");
      }

      // e.g: user/provider.Task.*
      scopes.add(contextAndDomain + "." + resource + ".*");

      // e.g: user/provider.*.read
      scopes.add(contextAndDomain + ".*." + verb);

      // e.g: user/provider.*.*
      scopes.add(contextAndDomain + ".*.*");
    }
  }

}

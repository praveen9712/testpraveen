
package com.qhrtech.emr.restapi.openapi;

import com.qhrtech.emr.restapi.models.swagger.FeaturePermission;
import com.qhrtech.emr.restapi.models.swagger.FeaturePermissions;
import com.qhrtech.emr.restapi.models.swagger.LogicalOperation;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermission;
import com.qhrtech.emr.restapi.models.swagger.ProviderPermissions;
import com.qhrtech.emr.restapi.models.swagger.RolePermission;
import com.qhrtech.emr.restapi.models.swagger.RolePermissions;
import com.qhrtech.emr.restapi.util.PATCH;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.v3.jaxrs2.util.ReaderUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.tags.Tag;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.security.access.prepost.PreAuthorize;

public class OpenApiReader extends Reader {

  @Override
  public OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources) {
    OpenAPI openApiSpec = super.read(classes, resources);

    classes.forEach(cl -> {
      // To match a class + method to an entry in the generated OpenApi spec we use the path +
      // method.
      String apiPath = cl.getAnnotation(Path.class).value();
      Method[] methods = cl.getDeclaredMethods();

      for (int i = 0; i < methods.length; i++) {
        Method method = methods[i];
        if (method.isAnnotationPresent(DELETE.class) || method.isAnnotationPresent(GET.class)
            || method.isAnnotationPresent(HEAD.class) || method.isAnnotationPresent(OPTIONS.class)
            || method.isAnnotationPresent(POST.class) || method.isAnnotationPresent(PUT.class)
            || method.isAnnotationPresent(PATCH.class)) {
          String methodPath =
              method.getAnnotation(Path.class) != null ? method.getAnnotation(Path.class).value()
                  : "";
          String httpMethod = ReaderUtils.extractOperationMethod(method, OpenAPIExtensions.chain());

          // It's possible with JAX-RS annotations to miss slashes so we insert them to be safe and
          // then clean up any duplicates
          String pathFromMethodAndClass = ("/" + apiPath + "/" + methodPath).replaceAll("//+", "/");
          pathFromMethodAndClass = pathFromMethodAndClass.replaceAll("/$", "");
          PathItem pathItem = openApiSpec.getPaths().get(pathFromMethodAndClass);
          if (pathItem != null) {
            Operation methodOperation =
                pathItem.readOperationsMap().get(HttpMethod
                    .valueOf(httpMethod.toUpperCase()));

            if (methodOperation != null) {

              for (int ix = 0; ix < method.getParameterAnnotations().length; ix++) {
                Annotation[] annotations = method.getParameterAnnotations()[ix];
                for (Annotation a : annotations) {
                  if (a.toString().contains("org.apache.cxf.jaxrs.ext.multipart.Multipart1")) {

                    Multipart part = (Multipart) a;
                    if (method.getGenericParameterTypes()[ix].getTypeName()
                        .contains("multipart.Attachment")) {

                      String description = "File to Upload";

                      String typeName = method.getParameterTypes()[ix].getTypeName();
                      if (typeName.equals("java.util.List") || typeName.equals("java.util.Set")) {
                        description += "(Multiple files can be provided)";
                      }

                      Schema schema = new Schema();
                      schema.format("binary");
                      schema.type("string");

                      Parameter p = new Parameter();
                      p.description(description);
                      p.schema(schema);
                      p.name(part.value());
                      p.setIn("formData");

                      if (!methodOperation.getParameters().contains(p)) {
                        methodOperation.getParameters().add(p);
                      }
                    }
                  }
                }
              }

              PreAuthorize methodPreAuthorizeAnnotation = method.getAnnotation(PreAuthorize.class);
              String methodPreAuthorizeExpression =
                  methodPreAuthorizeAnnotation != null ? methodPreAuthorizeAnnotation.value() : "";

              PreAuthorize classPreAuthorizeAnnotation =
                  cl.getAnnotation(PreAuthorize.class);
              String classPreAuthorizeExpression =
                  classPreAuthorizeAnnotation != null ? classPreAuthorizeAnnotation.value() : "";
              String fullPreAuthorizeExpression =
                  methodPreAuthorizeExpression + " " + classPreAuthorizeExpression;

              if (!StringUtils.isBlank(fullPreAuthorizeExpression)) {

                List<String> scopes = findScopes(fullPreAuthorizeExpression);
                if (!scopes.isEmpty()) {
                  SecurityRequirement security = new SecurityRequirement();
                  security.addList("oauth2", scopes);
                  List<SecurityRequirement> methodSecurity = new ArrayList<>();
                  methodSecurity.add(security);
                  methodOperation.setSecurity(methodSecurity);
                }

                String access = findAccess(fullPreAuthorizeExpression, method);

                if (StringUtils.isNotBlank(access)) {
                  methodOperation
                      .addExtension("x-accuroRoles", Collections.singletonList(access));
                }
              }

              Object providerPermissions = findProviderPermissions(method);
              if (providerPermissions != null) {
                methodOperation.addExtension("x-accuroProviderPermissions", providerPermissions);
              }

              String featurePermissions = findFeaturePermissions(method);
              if (!StringUtils.isBlank(featurePermissions)) {
                methodOperation.addExtension("x-accuroFeaturePermissions", featurePermissions);
              }
            }
          }
        }
      }
    });

    openApiSpec.getTags().sort(new SortByTags());
    openApiSpec.getComponents().setSecuritySchemes(null);
    return openApiSpec;
  }

  List<String> findScopes(String expression) {
    Set<String> allMatches = new HashSet<>();
    Matcher legacyScopeHasAnyMatcher = Pattern.compile(
        "#oauth2.hasAnyScope\\(([\\s]?'(?<g1>\\w+)'[\\s]?\\))")
        .matcher(expression);
    while (legacyScopeHasAnyMatcher.find()) {
      if (legacyScopeHasAnyMatcher.group("g1") != null) {
        allMatches.add(legacyScopeHasAnyMatcher.group("g1"));
      }
    }
    Matcher multiLegacyScopeHasAnyMatcher = Pattern.compile(
        "#oauth2.hasAnyScope\\(([\\s]?'(?<g2>\\w+)'[\\s]?,[\\s]?'(?<g3>\\w+)'[\\s]?\\))")
        .matcher(expression);

    while (multiLegacyScopeHasAnyMatcher.find()) {
      if (multiLegacyScopeHasAnyMatcher.group("g2") != null) {
        allMatches.add(multiLegacyScopeHasAnyMatcher.group("g2"));
      }
      if (multiLegacyScopeHasAnyMatcher.group("g3") != null) {
        allMatches.add(multiLegacyScopeHasAnyMatcher.group("g3"));
      }
    }
    Matcher newScopeHasAnyMatcher = Pattern.compile(
        "#oauth2.hasAnyScope\\([\\s]?'((?<g5>user\\/provider.*?)')")
        .matcher(expression);

    while (newScopeHasAnyMatcher.find()) {
      if (newScopeHasAnyMatcher.group("g5") != null) {
        allMatches.add(newScopeHasAnyMatcher.group("g5"));
      }
    }
    Matcher hasScopeMatcher = Pattern.compile(
        "#oauth2.hasScope\\([\\s]?'((?<g4>.*?)')")
        .matcher(expression);
    while (hasScopeMatcher.find()) {
      if (hasScopeMatcher.group("g4") != null) {
        allMatches.add(hasScopeMatcher.group("g4"));
      }
    }


    return new ArrayList<>(allMatches);
  }

  String findAccess(String expression, Method method) {


    RolePermissions permissions = method.getAnnotation(RolePermissions.class);

    if (permissions != null) {
      StringBuilder builder = new StringBuilder();
      for (RolePermission permission : permissions.rolePermissions()) {
        if (!StringUtils.isBlank(builder.toString())) {
          builder.append(" ").append(permissions.operation().name()).append(" ");
        }
        builder.append(permission.type()).append(" with access level: ")
            .append(permission.accessLevel());
      }
      return builder.toString();
    }


    Set<String> access = new HashSet<>();
    Matcher m = Pattern.compile(
        "#accuro.hasAccess\\([\\s']+(?<g1>\\w+)|\\G[\\s,']+(?<g2>\\w+)|\\G[\\s,']")
        .matcher(expression);
    StringBuilder builder = new StringBuilder();
    while (m.find()) {
      if (m.group("g1") != null) {
        builder = new StringBuilder();
        builder.append(m.group("g1"));
      }
      if (m.group("g2") != null) {
        builder.append(" with access level: ")
            .append(m.group("g2"))
            .append(" ");
        access.add(builder.toString());
      }
    }
    return builder.toString();
  }

  private HashMap<String, String> findProviderPermissions(Method method) {
    if (method.getAnnotation(ProviderPermission.class) != null) {
      ProviderPermission permission = method.getAnnotation(ProviderPermission.class);
      return convertProviderPermissionToExtension(permission);
    }

    ProviderPermissions permissions = method.getAnnotation(ProviderPermissions.class);
    if (permissions != null) {
      HashMap permissionsExtension = new HashMap();
      if (permissions.operation() == LogicalOperation.OR) {
        permissionsExtension.put("logicalOperator", " (Any one permission type is required.) ");
      } else {
        permissionsExtension.put("logicalOperator", " (All permission types are required. ) ");
      }


      ArrayList permissionList = new ArrayList();
      for (ProviderPermission permission : permissions.providerPermissions()) {
        permissionList.add(convertProviderPermissionToExtension(permission));
      }
      permissionsExtension.put("permissions", permissionList);

      return permissionsExtension;
    }
    return null;
  }

  private String findFeaturePermissions(Method method) {
    if (method.getAnnotation(FeaturePermission.class) != null) {
      FeaturePermission permission = method.getAnnotation(FeaturePermission.class);
      StringBuilder builder = new StringBuilder();
      builder.append(permission.type().name())
          .append(" (" + permission.type().getDescription() + ")").append(permission.description());
      return builder.toString();
    }

    FeaturePermissions permissions = method.getAnnotation(FeaturePermissions.class);
    StringBuilder builder = new StringBuilder();
    if (permissions != null) {
      for (FeaturePermission permission : permissions.featurePermissions()) {
        if (!StringUtils.isBlank(builder.toString())) {
          builder.append(" ").append(permissions.operation().name()).append(" ");
        }
        builder.append(permission.type()).append("(" + permission.type().getDescription() + ")")
            .append(permission.description());
      }
    }
    return builder.toString();
  }

  class SortByTags implements Comparator<Tag> {

    @Override
    public int compare(Tag tag1, Tag tag2) {
      return tag1.getName().compareToIgnoreCase(tag2.getName());
    }
  }

  private HashMap<String, String> convertProviderPermissionToExtension(
      ProviderPermission permission) {
    HashMap permissionExtension = new HashMap();
    permissionExtension.put("type", permission.type());
    permissionExtension.put("level", permission.level());
    permissionExtension.put("description", permission.description());
    permissionExtension.put("logicalOperator", "");

    return permissionExtension;
  }
}

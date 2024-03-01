/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.accuro.model.security.ExternalAccess;
import com.qhrtech.emr.accuro.model.security.UserPermissions;
import com.qhrtech.emr.accuro.permissions.FeatureType;
import com.qhrtech.emr.accuro.permissions.roles.RoleSection;
import com.qhrtech.emr.accuro.permissions.roles.RoleSectionAccess;
import com.qhrtech.emr.restapi.util.AccapiScopes;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.Authentication;

/**
 * @author Blake Dickie
 */
public class AccuroSecurityExpressionMethods {

  private final Authentication authentication;
  private final ApiSecurityContext context;

  public AccuroSecurityExpressionMethods(Authentication authentication) {
    this.authentication = authentication;
    context = ApiSecurityManager.getInstance().getCurrentSecurityContext();
  }

  public boolean isSysAdmin() {
    return false;
  }


  /**
   * Checks if this OAuth user is a patient.
   *
   * @return boolean indicating if this user is a patient
   */
  public boolean isPatient() {
    return authentication.getPrincipal() instanceof PatientUserDetails;
  }

  /**
   * Checks if this OAuth user is a provider.
   *
   * More specifically this method checks if the authentication is an Accuro user which is assumed
   * to be a provider.
   *
   * @return boolean indicating if this user is a provider
   */
  public boolean isProvider() {
    return authentication.getPrincipal() instanceof AccuroUserDetails
        || authentication.getPrincipal() instanceof OktaUserDetails;
  }

  /**
   * Check if the user has a feature type enabled.
   *
   * If the feature type cannot be found then <code>false</code> will be returned
   *
   * @param name Feature type against which user access has to be checked.
   * @return boolean indicating if the feature is enabled
   */
  public boolean hasFeature(String name) {
    UserPermissions userPermissions = context.getAccuroApiContext().getUserPermissions();
    if (userPermissions == null) {
      return false;
    }
    FeatureType type = FeatureType.lookup(name);
    if (type == null) {
      return false;
    }
    Map<Integer, Set<FeatureType>> featurePermissions = userPermissions.getFeaturePermissions();

    boolean enabled = false;
    for (int officeId : featurePermissions.keySet()) {
      if (featurePermissions.get(officeId).contains(type)) {
        enabled = true;
        break;
      }
    }
    return enabled;
  }

  /**
   * Check the users access for a role section.
   *
   * If the role section or access cannot be found then <code>false</code> will be returned
   *
   * When the required access level is <code>RoleSectionAccess.NO_ACCESS</code> <code>true</code>
   * will only be returned if the user has <code>RoleSectionAccess.NO_ACCESS</code>
   *
   * When the required access level is <code>RoleSectionAccess.READ_ONLY</code> <code>true</code>
   * will be returned if the user has <code>RoleSectionAccess.READ_WRITE</code>
   *
   * @param section Role section being checked
   * @param access Required level of access
   * @return boolean indicating the user has the required access level
   */
  public boolean hasAccess(String section, String access) {

    /**
     * This to avoid roles check for first party access and authorized user access
     */
    if (context.getScopes().contains(AccapiScopes.QHR_FIRST_PARTY.getApiScope())
        || context.getScopes().contains(AccapiScopes.QHR_AUTHORIZED_USER.getApiScope())) {
      return true;
    }

    UserPermissions userPermissions = context.getAccuroApiContext().getUserPermissions();
    if (userPermissions == null) {
      return false;
    }

    RoleSection roleSection = RoleSection.lookup(section);
    RoleSectionAccess roleAccess = RoleSectionAccess.lookup(access);
    if (roleSection == null || roleAccess == null) {
      return false;
    }
    Set<RoleSectionAccess> roleAccesses = new HashSet<>();

    for (int officeId : userPermissions.getRoleSectionAccess().keySet()) {
      RoleSectionAccess roleSectionAccess =
          userPermissions.getRoleSectionAccess().get(officeId).get(roleSection);
      roleAccesses.add(roleSectionAccess);
    }

    if (roleAccesses.isEmpty()) {
      return false;
    }

    if (roleAccesses.contains(roleAccess)) {
      return true;
    }

    // scenario where user has write permission but required is read only.
    // In that cases, user is allowed to access endpoint.
    if (RoleSectionAccess.READ_ONLY.equals(roleAccess)
        && roleAccesses.contains(RoleSectionAccess.READ_WRITE)) {
      return true;
    }

    return false;
  }

  /**
   * Check if the user has external access enabled.
   *
   * If the external access cannot be found then <code>false</code> will be returned
   *
   * @param access External access being checked
   * @return boolean indicating if the user has the required external access enabled
   */
  public boolean hasExternalAccess(String access) {
    UserPermissions userPermissions = context.getAccuroApiContext().getUserPermissions();
    if (userPermissions == null) {
      return false;
    }
    ExternalAccess type = ExternalAccess.lookup(access);
    if (type == null) {
      return false;
    }
    return userPermissions.getExternalAccess().contains(type);
  }

}

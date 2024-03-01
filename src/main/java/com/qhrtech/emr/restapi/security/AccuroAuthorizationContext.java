
package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.accuro.api.AuthorizationContext;
import com.qhrtech.emr.accuro.model.security.UserPermissions;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.accuro.permissions.FeatureType;
import com.qhrtech.emr.accuro.permissions.OpenProviderPermission;
import com.qhrtech.emr.accuro.permissions.ProviderPermission;
import com.qhrtech.emr.accuro.permissions.roles.RoleSection;
import com.qhrtech.emr.accuro.permissions.roles.RoleSectionAccess;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AccuroAuthorizationContext implements AuthorizationContext {
  private UserPermissions userPermissions;

  public AccuroAuthorizationContext(UserPermissions userPermissions) {
    this.userPermissions = userPermissions;
  }

  @Override
  public Map<Integer, Set<Integer>> getOfficeProviderAccess(AccessType accessType,
      AccessLevel accessLevel) {
    Map<Integer, Set<Integer>> officeProvidersMap = new HashMap<>();
    Map<Integer, Set<ProviderPermission>> providerPermissions =
        userPermissions.getProviderPermissions();
    if (providerPermissions != null) {
      for (Integer office : providerPermissions.keySet()) {
        for (ProviderPermission permission : providerPermissions.get(office)) {
          Map<AccessType, AccessLevel> permissionAccess = permission.getPermissionAccess();
          AccessLevel actualAccessLevel = permissionAccess.get(accessType);
          Set<Integer> providerIds = new HashSet<>();
          if (actualAccessLevel != null && (actualAccessLevel.equals(accessLevel)
              || (actualAccessLevel.equals(AccessLevel.Full)
                  && accessLevel.equals(AccessLevel.ReadOnly)))) {
            if (officeProvidersMap.get(permission.getOfficeId()) != null) {
              providerIds = officeProvidersMap.get(permission.getOfficeId());
            }
            providerIds.add(permission.getProviderId());
            officeProvidersMap.put(permission.getOfficeId(), providerIds);
          }
        }
      }
    }
    return officeProvidersMap;
  }

  @Override
  public Set<Integer> getOpenProviderAccess(AccessType accessType, AccessLevel accessLevel) {
    Set<Integer> providerIds = new HashSet<>();
    Set<OpenProviderPermission> openProviderPermissions =
        userPermissions.getOpenProviderPermissions();
    if (openProviderPermissions != null) {
      for (OpenProviderPermission permission : openProviderPermissions) {
        Map<AccessType, AccessLevel> accessMap = permission.getPermissionAccess();
        if (accessMap.get(accessType) != null && accessMap.get(accessType).equals(accessLevel)) {
          providerIds.add(permission.getProviderId());
        }
      }
    }
    return providerIds;
  }

  @Override
  public Set<Integer> getOfficeRoleSectionAccess(RoleSection roleSection,
      RoleSectionAccess roleSectionAccess) {
    Set<Integer> offices = new HashSet<>();
    Map<Integer, Map<RoleSection, RoleSectionAccess>> roleSectionAccessMap =
        userPermissions.getRoleSectionAccess();
    for (Integer office : roleSectionAccessMap.keySet()) {
      Map<RoleSection, RoleSectionAccess> roleSectionRoleSectionAccessMap =
          roleSectionAccessMap.get(office);
      RoleSectionAccess actualAccess = roleSectionRoleSectionAccessMap.get(roleSection);
      if (actualAccess != null) {
        if (actualAccess.equals(roleSectionAccess)
            || (roleSectionAccess.equals(RoleSectionAccess.READ_ONLY) && actualAccess
                .equals(RoleSectionAccess.READ_WRITE))) {
          offices.add(office);
        }
      }
    }
    return offices;
  }

  @Override
  public Set<Integer> getOfficeFeatureAccess(FeatureType type) {
    Set<Integer> offices = new HashSet<>();
    Map<Integer, Set<FeatureType>> featurePermissions = userPermissions.getFeaturePermissions();
    for (Integer office : featurePermissions.keySet()) {
      Set<FeatureType> featureTypes = featurePermissions.get(office);
      if (featureTypes.contains(type)) {
        offices.add(office);
      }
    }
    return offices;
  }
}

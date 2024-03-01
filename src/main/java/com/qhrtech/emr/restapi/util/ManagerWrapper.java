
package com.qhrtech.emr.restapi.util;


import com.qhrtech.emr.accuro.api.AuthorizationContext;
import com.qhrtech.emr.accuro.db.DatabaseType;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.services.ManagerConstructor;
import com.qhrtech.emr.restapi.services.listeners.ListenerType;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.sql.DataSource;

public class ManagerWrapper {


  private ManagerWrapper() {
  }

  /**
   * This method returns the protoss manager func which would instantiate the manager. This method
   * sets the default Databasetype as Accuro.
   *
   * @param constructor constructor which takes datasource as input and returns the definition for
   *        the protoss manager creation.
   * @return {@link ManagerConstructor }
   */
  public static <T> ManagerConstructor<T> withDatasource(Function<DataSource, T> constructor) {
    return (dataSourceService, securityContextService, listenerPoolService,
        tenant, permissions) -> {

      DataSource dataSource = dataSourceService.getDataSource(tenant, DatabaseType.Accuro);
      return constructor.apply(dataSource);
    };
  }

  /**
   * This method returns the protoss manager func which would instantiate the manager.
   *
   * @param constructor constructor which takes datasource as input and returns the definition for
   *        the protoss manager creation.
   * @param type Database type which should be used to generate the datasource
   * @return {@link ManagerConstructor }
   */
  public static <T> ManagerConstructor<T> withDatasource(Function<DataSource, T> constructor,
      DatabaseType type) {
    return (dataSourceService, securityContextService, listenerPoolService,
        tenant, permissions) -> {

      DataSource dataSource = dataSourceService.getDataSource(tenant, type);
      return constructor.apply(dataSource);
    };

  }

  /**
   * This method returns the protoss manager func which would instantiate the manager. This method
   * use two datasources to instantiate the manager. First database type is always
   * DatabaseType.Accuro and other one is always DatabaseType.Documents
   *
   * @param constructor constructor which takes two datasource as input and returns the definition
   *        for the protoss manager creation.
   * @return {@link ManagerConstructor }
   */
  public static <T> ManagerConstructor<T> withDualDb(
      BiFunction<DataSource, DataSource, T> constructor) {
    return (dataSourceService, securityContextService, listenerPoolService,
        tenant, permissions) -> {

      DataSource medadminDatasource = dataSourceService.getDataSource(tenant, DatabaseType.Accuro);
      DataSource documentsDatasource =
          dataSourceService.getDataSource(tenant, DatabaseType.Documents);
      return constructor.apply(medadminDatasource, documentsDatasource);
    };
  }

  /**
   * This method returns the protoss manager func which would instantiate the manager. This method
   * sets the default Databasetype as Accuro.
   *
   * @param constructor constructor which takes datasource, authorization context and audit log user
   *        as input and returns the definition for the protoss manager creation.
   * @return {@link ManagerConstructor }
   */
  public static <T> ManagerConstructor<T> withPermissions(
      TriFunction<DataSource, AuthorizationContext, AuditLogUser, T> constructor) {
    return (dataSourceService, securityContextService, listenerPoolService,
        tenant, skipPermissions) -> {

      AuthorizationContext authorizationContext = skipPermissions ? null
          : securityContextService.getAuthorizationContext();

      DataSource dataSource = dataSourceService.getDataSource(tenant, DatabaseType.Accuro);
      AuditLogUser auditLogUser = Objects.isNull(securityContextService.getSecurityContext()) ? null
          : securityContextService.getSecurityContext().getUser();

      return constructor.apply(dataSource, authorizationContext, auditLogUser);
    };
  }

  /**
   * This method returns the protoss manager constructor which would instantiate the manager. This
   * method sets the default Databasetype as Accuro.
   *
   * @param constructor function which takes datasource, listener, authorization context and audit
   *        log user as input and returns the definition for the protoss manager creation.
   * @return {@link ManagerConstructor }
   */
  public static <T, U> ManagerConstructor<T> withPermissionsAndListener(
      ProtossManagerConstructor<U, T> constructor,
      ListenerType listenerType) {
    return (dataSourceService, securityContextService, listenerPoolService,
        tenant, skipPermissions) -> {

      AuthorizationContext authorizationContext = skipPermissions ? null
          : securityContextService.getAuthorizationContext();

      DataSource dataSource = dataSourceService.getDataSource(tenant, DatabaseType.Accuro);
      AuditLogUser auditLogUser = Objects.isNull(securityContextService.getSecurityContext()) ? null
          : securityContextService.getSecurityContext().getUser();

      return constructor
          .apply(dataSource, listenerPoolService.getListenerPool(listenerType, dataSource),
              authorizationContext, auditLogUser);
    };
  }

}

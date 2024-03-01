
package com.qhrtech.emr.restapi.endpoints.utilities;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.AuthorizationContext;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentListener;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentListenerPool;
import com.qhrtech.emr.accuro.db.DatabaseType;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.security.AccuroAuthorizationContext;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.security.datasource.DataSourceService;
import com.qhrtech.emr.restapi.services.ListenerPoolService;
import com.qhrtech.emr.restapi.services.ManagerConstructor;
import com.qhrtech.emr.restapi.services.SecurityContextService;
import com.qhrtech.emr.restapi.services.listeners.ListenerType;
import com.qhrtech.emr.restapi.util.ManagerWrapper;
import com.qhrtech.emr.restapi.util.ProtossManagerConstructor;
import com.qhrtech.emr.restapi.util.TriFunction;
import com.qhrtech.util.RandomUtils;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.sql.DataSource;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class ManagerWrapperTest {

  private final DataSourceService dataSourceService;
  private final SecurityContextService securityContextService;
  private final ListenerPoolService listenerPoolService;

  // test data
  private final String tenant = RandomUtils.getString(15);
  private final AuditLogUser auditLogUser;
  // string is used for easy comparison and factory produces any object
  private final String resultingClass = RandomUtils.getString(20);
  private final AuthorizationContext authorizationContext;
  private final DataSource medadminDatasource = new DriverManagerDataSource();
  private final DataSource documentsDatasource = new DriverManagerDataSource();

  public ManagerWrapperTest() {
    dataSourceService = mock(DataSourceService.class);
    securityContextService = mock(SecurityContextService.class);
    listenerPoolService = mock(ListenerPoolService.class);
    authorizationContext = new AccuroAuthorizationContext(null);
    ApiSecurityContext mockContext = mock(ApiSecurityContext.class);
    when(securityContextService.getAuthorizationContext())
        .thenReturn(authorizationContext);
    when(securityContextService.getSecurityContext()).thenReturn(mockContext);
    auditLogUser =
        new AuditLogUser(TestUtilities.nextInt(), TestUtilities.nextInt(),
            TestUtilities.nextString(20), TestUtilities.nextString(20),
            TestUtilities.nextString(20));
    when(mockContext.getUser()).thenReturn(auditLogUser);
    when(dataSourceService.getDataSource(tenant, DatabaseType.Accuro))
        .thenReturn(medadminDatasource);
    when(dataSourceService.getDataSource(tenant, DatabaseType.Documents))
        .thenReturn(documentsDatasource);
  }

  @Test
  public void testProvideWithSingleDatasource() throws Exception {
    // setup
    Function<DataSource, String> managerConstructor = (d) -> {
      assertEquals(medadminDatasource, d);
      return resultingClass;
    };

    // run
    ManagerConstructor<String> constructor =
        ManagerWrapper.withDatasource(managerConstructor);

    // verify
    assertNotNull(constructor);
    String instance = constructor
        .createInstance(dataSourceService, securityContextService, listenerPoolService, tenant,
            true);
    assertNotNull(constructor);
    assertEquals(resultingClass, instance);
    verify(dataSourceService).getDataSource(tenant, DatabaseType.Accuro);
  }

  @Test
  public void testProvideWithSpecificDatasources() throws Exception {
    // setup
    Function<DataSource, String> managerConstructor = (d) -> resultingClass;
    DatabaseType databaseType = RandomUtils.getEnumValue(DatabaseType.class);

    // run
    ManagerConstructor<String> constructor =
        ManagerWrapper.withDatasource(managerConstructor, databaseType);

    // verify
    assertNotNull(constructor);
    String instance = constructor
        .createInstance(dataSourceService, securityContextService, listenerPoolService, tenant,
            true);
    assertNotNull(constructor);
    assertEquals(resultingClass, instance);
    verify(dataSourceService).getDataSource(tenant, databaseType);
  }


  @Test
  public void testProvideWithMultipleDatasource() throws Exception {
    // setup
    BiFunction<DataSource, DataSource, String> managerConstructor = (d, d2) -> {
      assertEquals(medadminDatasource, d);
      assertEquals(documentsDatasource, d2);
      return resultingClass;
    };

    // run
    ManagerConstructor<String> constructor =
        ManagerWrapper.withDualDb(managerConstructor);

    // verify
    assertNotNull(constructor);
    String instance = constructor
        .createInstance(dataSourceService, securityContextService, listenerPoolService, tenant,
            true);

    assertNotNull(constructor);
    assertEquals(resultingClass, instance);
    verify(dataSourceService).getDataSource(tenant, DatabaseType.Accuro);
    verify(dataSourceService).getDataSource(tenant, DatabaseType.Documents);
  }

  @Test
  public void testProvideWithPermissionsSkip() throws Exception {
    // setup
    TriFunction<DataSource, AuthorizationContext, AuditLogUser, String> managerConstructor =
        (d, a, u) -> {
          assertEquals(medadminDatasource, d);
          return resultingClass;
        };

    // run
    ManagerConstructor<String> constructor =
        ManagerWrapper.withPermissions(managerConstructor);

    // verify
    assertNotNull(constructor);
    String instance = constructor
        .createInstance(dataSourceService, securityContextService, listenerPoolService, tenant,
            true);
    assertNotNull(constructor);
    assertEquals(resultingClass, instance);
    verify(dataSourceService).getDataSource(tenant, DatabaseType.Accuro);
    verify(securityContextService, times(2)).getSecurityContext();
  }

  @Test
  public void testProvideWithPermissions() throws Exception {
    // setup
    TriFunction<DataSource, AuthorizationContext, AuditLogUser, String> managerConstructor =
        (d, a, u) -> {
          assertEquals(medadminDatasource, d);
          assertEquals(authorizationContext, a);
          assertEquals(auditLogUser, u);
          return resultingClass;
        };

    // run
    ManagerConstructor<String> constructor =
        ManagerWrapper.withPermissions(managerConstructor);

    // verify
    assertNotNull(constructor);
    String instance = constructor
        .createInstance(dataSourceService, securityContextService, listenerPoolService, tenant,
            false);
    assertNotNull(constructor);
    assertEquals(resultingClass, instance);
    verify(dataSourceService).getDataSource(tenant, DatabaseType.Accuro);
    verify(securityContextService, times(2)).getSecurityContext();
    verify(securityContextService).getAuthorizationContext();
  }

  @Test
  public void testProvideWithListenerPool() throws Exception {
    // setup
    AppointmentListener appointmentListener = new AppointmentListenerPool(null);
    when(listenerPoolService.getListenerPool(ListenerType.APPOINTMENT, medadminDatasource))
        .thenReturn(appointmentListener);

    ProtossManagerConstructor<AppointmentListener, String> managerConstructor =
        (d, l, a, u) -> {
          assertEquals(medadminDatasource, d);
          assertEquals(appointmentListener, l);
          assertEquals(authorizationContext, a);
          assertEquals(auditLogUser, u);
          return resultingClass;
        };

    // run
    ManagerConstructor<String> constructor =
        ManagerWrapper.withPermissionsAndListener(managerConstructor, ListenerType.APPOINTMENT);

    // verify
    assertNotNull(constructor);
    String instance = constructor
        .createInstance(dataSourceService, securityContextService, listenerPoolService, tenant,
            false);
    assertNotNull(constructor);
    assertEquals(resultingClass, instance);
    verify(dataSourceService).getDataSource(tenant, DatabaseType.Accuro);
    verify(securityContextService, times(2)).getSecurityContext();
    verify(securityContextService).getAuthorizationContext();
    verify(listenerPoolService).getListenerPool(ListenerType.APPOINTMENT, medadminDatasource);
  }
}


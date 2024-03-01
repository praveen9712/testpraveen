
package com.qhrtech.emr.restapi.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentListenerPool;
import com.qhrtech.emr.restapi.services.impl.ImplementationNotFoundException;
import com.qhrtech.emr.restapi.services.impl.ListenerPoolServiceImpl;
import com.qhrtech.emr.restapi.services.listeners.ListenerType;
import com.qhrtech.util.RandomUtils;
import javax.sql.DataSource;
import org.junit.Test;

public class ListenerPoolServiceTest {

  private final DataSource mockDs;

  public ListenerPoolServiceTest() {
    mockDs = mock(DataSource.class);
  }

  private ListenerPoolService listenerPoolService = new ListenerPoolServiceImpl();

  @Test(expected = ImplementationNotFoundException.class)
  public void testListenerPoolNull() {
    listenerPoolService.getListenerPool(RandomUtils.getEnumValue(ListenerType.class), null);
  }

  @Test
  public void testAppointmentListenerPool() {
    Object listenerPool = listenerPoolService.getListenerPool(ListenerType.APPOINTMENT, mockDs);
    assertNotNull(listenerPool);
    assertEquals(AppointmentListenerPool.class, listenerPool.getClass());
  }

  @Test
  public void testAllListenerPools() {
    for (ListenerType type : ListenerType.values()) {
      Object listenerPool =
          listenerPoolService.getListenerPool(type, mockDs);
      listenerPoolService.getListenerPool(type, mockDs);
      assertNotNull(listenerPool);
    }
  }
}

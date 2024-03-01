/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.restapi.services.ListenerPoolService;
import com.qhrtech.emr.restapi.services.listeners.ListenerType;
import com.qhrtech.util.SoftCache;
import java.util.EnumMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListenerPoolServiceImpl implements ListenerPoolService {

  private final Map<ListenerType, SoftCache<DataSource, ?>> listenerPoolCache;

  @Autowired
  public ListenerPoolServiceImpl() {
    listenerPoolCache = new EnumMap<>(ListenerType.class);
    for (ListenerType type : ListenerType.values()) {
      listenerPoolCache.put(type, new SoftCache<>(ListenerType.APPOINTMENT.getTransformer()));
    }
  }

  @Override
  public <T> T getListenerPool(ListenerType type, DataSource ds) {
    if (!listenerPoolCache.containsKey(type) || null == ds) {
      throw new ImplementationNotFoundException(
          "The requested listener pool has not been initialized");
    }

    SoftCache<DataSource, ?> dataSourceListenerPool =
        listenerPoolCache.get(type);

    return (T) dataSourceListenerPool.lookup(ds);
  }
}

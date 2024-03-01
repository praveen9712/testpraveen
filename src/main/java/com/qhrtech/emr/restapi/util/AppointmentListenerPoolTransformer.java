
package com.qhrtech.emr.restapi.util;

import com.qhrtech.emr.accuro.api.scheduling.AppointmentListenerPool;
import com.qhrtech.emr.restapi.services.listeners.WaitRoomListener;
import com.qhrtech.util.transformer.Transformer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.sql.DataSource;

public class AppointmentListenerPoolTransformer implements
    Transformer<DataSource, AppointmentListenerPool> {

  @Override
  public AppointmentListenerPool transform(DataSource ds) {
    Executor executor = Executors.newSingleThreadExecutor();
    AppointmentListenerPool pool = new AppointmentListenerPool(executor);
    registerWaitRoomListener(pool, ds);
    return pool;
  }

  private void registerWaitRoomListener(AppointmentListenerPool pool, DataSource ds) {
    pool.register(new WaitRoomListener(ds));
  }
}

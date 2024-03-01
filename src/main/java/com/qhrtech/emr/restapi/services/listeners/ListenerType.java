
package com.qhrtech.emr.restapi.services.listeners;

import com.qhrtech.emr.accuro.api.scheduling.AppointmentListenerPool;
import com.qhrtech.emr.restapi.util.AppointmentListenerPoolTransformer;
import com.qhrtech.util.transformer.Transformer;
import javax.sql.DataSource;

public enum ListenerType {
  APPOINTMENT {
    @Override
    public Transformer<DataSource, AppointmentListenerPool> getTransformer() {
      return new AppointmentListenerPoolTransformer();
    }
  };

  public abstract <T> Transformer<DataSource, T> getTransformer();
}


package com.qhrtech.emr.restapi.services;


import com.qhrtech.emr.restapi.services.listeners.ListenerType;
import javax.sql.DataSource;

public interface ListenerPoolService {

  /**
   * Gets a listener pool for the given datasource and type. Will create and cache the listener pool
   * if it doesnt exist yet. This due to type erasure this method will return an Object.
   *
   * @param type The type of listener pool
   * @param ds the datasource for which the listener pool will be associated
   *
   * @see com.qhrtech.emr.restapi.services.listeners.ListenerType to determine the implementation
   *      for the given type.
   */
  <T> T getListenerPool(ListenerType type, DataSource ds);

}

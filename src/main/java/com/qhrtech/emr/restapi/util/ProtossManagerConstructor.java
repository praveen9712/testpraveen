
package com.qhrtech.emr.restapi.util;

import com.qhrtech.emr.accuro.api.AuthorizationContext;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import javax.sql.DataSource;

/**
 * Represents a Protoss Manager's constructor which does provider permissions and has listener
 * pools.
 * 
 * @param <U> the listener pool
 * @param <R> the implementation class
 */
@FunctionalInterface
public interface ProtossManagerConstructor<U, R> {

  /**
   * Applies this function to the given argument.
   *
   * @param t the function argument
   * @param u the function argument
   * @param w the function argument
   * @return the function result
   */
  R apply(DataSource t, U u, AuthorizationContext v, AuditLogUser w);
}

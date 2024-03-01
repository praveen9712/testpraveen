/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.apicontext;

import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;

/**
 * Loads an {@link AccuroApiContext} which contains details about the requesting user.
 *
 * @author james.michaud
 */
public interface AccuroApiContextService {

  /**
   * @param accuroApiToken the AccuroApiToken adapter
   *
   * @return a populated AccuroApiContext based on the token provided.
   */
  AccuroApiContext getAccuroApiUserContext(AccuroApiTokenAdapter accuroApiToken);

}

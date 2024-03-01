/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.accuro.model.exceptions.ProtossException;

/**
 *
 * @author jesse.pasos
 */
public interface ModuleService {

  /**
   * Checks that Rest API module has been enabled for this tenant.
   *
   * @param tenantId - id of the requesting tenant
   *
   * @return boolean - indicates whether or not the Rest API Module has been enabled for the tenant.
   */
  boolean isRestApiEnabled(String tenantId) throws ProtossException;

  /**
   * Checks that Azure Documents module has been enabled for this tenant.
   *
   * @return boolean - indicates whether or not the API Module has been enabled for the tenant.
   */
  boolean isAccBlobEnabled() throws ProtossException;

}

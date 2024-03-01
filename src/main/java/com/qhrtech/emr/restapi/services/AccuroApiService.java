/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services;

/**
 *
 * @author kevin.kendall
 */
public interface AccuroApiService {

  <D extends T, T> D getImpl(Class<T> interfaceClass, String tenantId);

  <D extends T, T> D getImpl(Class<T> interfaceClass, String tenantId,
      boolean skipPermissionsCheck);
}

/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.models.registry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author bryan.bergen
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantDataSourceResult {

  private TenantDataSourceDetails dataSource;

  public TenantDataSourceDetails getDataSource() {
    return dataSource;
  }

  public void setDataSource(TenantDataSourceDetails dataSource) {
    this.dataSource = dataSource;
  }

}

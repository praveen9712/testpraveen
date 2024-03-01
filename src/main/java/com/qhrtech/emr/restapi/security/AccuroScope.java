/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security;

/**
 * @author kevin.kendall
 */
public class AccuroScope {

  private String id;
  private String name;
  private String summary;

  public AccuroScope() {

  }

  public AccuroScope(String id, String name, String summary) {
    this.id = id;
    this.name = name;
    this.summary = summary;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSummary() {
    return summary;
  }
}

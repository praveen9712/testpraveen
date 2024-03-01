/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.restapi.models.registry.TenantDataSourceResult;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/**
 * Service for retrieving DataSource details from the Registry Web Service
 *
 * @author bryan.bergen
 * @see TenantDataSourceResult
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/rest/internal/mta/")
public interface RegistryServiceApi {

  @GET
  @Path("uuid/{uuid}")
  public TenantDataSourceResult getDataSourceByUuid(
      @PathParam("uuid") String uuid,
      @HeaderParam(HttpHeaders.AUTHORIZATION) String auth);

  @GET
  @Path("acron/{acron}")
  public TenantDataSourceResult getDataSourceByAcron(
      @PathParam("acron") String acron,
      @HeaderParam(HttpHeaders.AUTHORIZATION) String auth);

  @GET
  @Path("medeoID/{medeoID}")
  public TenantDataSourceResult getDataSourceByMedeoId(
      @PathParam("medeoID") long medeoId,
      @HeaderParam(HttpHeaders.AUTHORIZATION) String auth);
}

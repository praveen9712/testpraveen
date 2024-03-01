/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services.impl;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.qhrtech.emr.restapi.external.IdpTokenManager;
import com.qhrtech.emr.restapi.external.MedeoPatientsApi;
import com.qhrtech.emr.restapi.external.Registration;
import com.qhrtech.emr.restapi.external.RegistrationWrapper;
import com.qhrtech.emr.restapi.models.dto.security.ErrorResponse;
import com.qhrtech.emr.restapi.services.MedeoService;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Calendar;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author kevin.kendall
 */
@Component
public class MedeoServiceImpl implements MedeoService {

  @Autowired
  private IdpTokenManager idpTokenManager;

  @Autowired
  private JacksonJsonProvider jsonProvider;

  @Value("${medeo.rails.url}")
  private String railsUrl;

  @Override
  public boolean signUp(
      String firstName,
      String lastName,
      String phoneNumber,
      String email,
      String password,
      Calendar birthdate,
      String phn) {
    Registration registration = new Registration();
    registration.setFirstName(firstName);
    registration.setLastName(lastName);
    registration.setPhoneNumber(phoneNumber);
    registration.setEmail(email);
    registration.setPassword(password);
    registration.setBirthDate(birthdate);
    registration.setPhn(phn);

    MedeoPatientsApi patientsApi = getMedeoPatientsApi();
    Response response = patientsApi.signUp(
        RegistrationWrapper.wrap(registration),
        idpTokenManager.getAuthenticationHeader());

    /**
     * Have to do custom error handling as Apache CXF has a problem parsing responses with a status
     * of 422. NPE on ResponseImpl.getFamily() : line 108 as the exact status does not exist in the
     * base HTTP standard.
     *
     * This has been fixed by at least Apache CXF 3.0.2 and this code can be removed if moved to a
     * newer version of Apache CXF.
     */
    if (response.getStatus() == 422) {
      Object entity = response.getEntity();
      if (entity instanceof InputStream) {
        InputStream in = (InputStream) entity;
        String s;
        try {
          s = IOUtils.readStringFromStream(in);
        } catch (IOException ex) {
          s = "Unknown 422 error.";
        }
        throw new WebApplicationException(
            Response.serverError().entity(new ErrorResponse(s))
                .type(MediaType.APPLICATION_JSON_TYPE).build());
      }
      return false;
    } else if (response.getStatus() >= 300) {
      Class<?> exceptionClass =
          ExceptionUtils.getWebApplicationExceptionClass(response, WebApplicationException.class);
      try {
        Constructor<?> ctr = exceptionClass.getConstructor(Response.class);
        throw (WebApplicationException) ctr.newInstance(response);
      } catch (Exception ex2) {
        throw new WebApplicationException(ex2, response);
      }
    }
    return true;
  }

  private MedeoPatientsApi getMedeoPatientsApi() {
    String url = railsUrl.endsWith("/") ? railsUrl.substring(0, railsUrl.length() - 1) : railsUrl;
    MedeoPatientsApi api = JAXRSClientFactory.create(
        url + "/api/patients/",
        MedeoPatientsApi.class,
        Arrays.asList(jsonProvider),
        true);
    return api;
  }

}

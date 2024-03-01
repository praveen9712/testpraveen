/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.qhrtech.emr.restapi.models.dto.security.ErrorResponse;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.log4j.Logger;

/**
 *
 * @author andrew.bondarenko
 */
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> {

  private final Logger log = Logger.getLogger(getClass());

  @Override
  public Response toResponse(WebApplicationException t) {
    if (t.getCause() != null) {
      if (t.getCause() instanceof JsonProcessingException) {

        String message = "Unable to process json for this request.";

        if (t.getCause() instanceof JsonMappingException) {
          List<Reference> path = ((JsonMappingException) t.getCause()).getPath();

          StringBuilder builder = new StringBuilder();
          int flag = 0;
          for (JsonMappingException.Reference reference : path) {
            if (flag != 0) {
              builder.append(", ");
            }
            builder.append(reference.getFieldName() + " ");

            flag++;

          }
          if (path.isEmpty()) {
            message = "Unable to process json. Invalid input object";
          } else {
            message = "Unable to process json. Invalid value at field(s): " + builder.toString();
          }


        }


        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(new ErrorResponse(message))
            .type(MediaType.APPLICATION_JSON)
            .build();
      } else {
        log.error(t.getMessage(), t.getCause());
      }
    }
    return t.getResponse();
  }
}

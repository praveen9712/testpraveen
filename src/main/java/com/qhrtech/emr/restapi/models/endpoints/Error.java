
package com.qhrtech.emr.restapi.models.endpoints;

import com.qhrtech.emr.restapi.models.dto.security.ErrorResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Blake Dickie
 */
public class Error {

  public static WebApplicationException webApplicationException(Response.Status status,
      String message,
      Throwable throwable) throws WebApplicationException {
    Response response = Response.status(status)
        .entity(new ErrorResponse(message))
        .type(MediaType.APPLICATION_JSON_TYPE)
        .build();
    throw new WebApplicationException(throwable, response);
  }

  public static WebApplicationException webApplicationException(Response.Status status,
      String message)
      throws WebApplicationException {
    throw webApplicationException(status, message, null);
  }


  /**
   * @deprecated Use {@link Error#webApplicationException(Status, String)}
   */
  @Deprecated
  public static WebApplicationException returnBadRequestResult(String message)
      throws WebApplicationException {
    throw webApplicationException(Status.BAD_REQUEST, message);
  }

  /**
   * @deprecated Use {@link Error#webApplicationException(Status, String)}
   */
  @Deprecated
  public static WebApplicationException returnNotFoundResult(String message)
      throws WebApplicationException {
    throw webApplicationException(Status.NOT_FOUND, message);
  }

  /**
   * @deprecated Use {@link Error#webApplicationException(Status, String)}
   */
  @Deprecated
  public static WebApplicationException returnUnauthorizedResult(String message) {
    throw webApplicationException(Status.UNAUTHORIZED, message);
  }

  /**
   * @deprecated Use {@link Error#webApplicationException(Status, String)}
   */
  @Deprecated
  public static WebApplicationException returnForbiddenResult(String message) {
    throw webApplicationException(Status.FORBIDDEN, message);
  }

  /**
   * @deprecated Use {@link Error#webApplicationException(Status, String)}
   */
  @Deprecated
  public static WebApplicationException returnInternalServerErrorResult(String message) {
    throw webApplicationException(Status.INTERNAL_SERVER_ERROR, message);
  }

  /**
   * @deprecated Use {@link Error#webApplicationException(Status, String, Throwable)}
   */
  @Deprecated
  public static WebApplicationException returnInternalServerErrorResult(String message,
      Throwable t) {
    throw webApplicationException(Status.INTERNAL_SERVER_ERROR, message, t);
  }

}

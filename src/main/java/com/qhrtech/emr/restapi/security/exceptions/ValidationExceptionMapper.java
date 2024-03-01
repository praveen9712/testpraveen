
package com.qhrtech.emr.restapi.security.exceptions;

import com.qhrtech.emr.accuro.utils.Transformer;
import com.qhrtech.emr.restapi.models.dto.security.ConstraintViolation;
import com.qhrtech.emr.restapi.models.dto.security.ErrorResponse;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

  private static final Logger log = LoggerFactory.getLogger(ValidationExceptionMapper.class);

  @Override
  public Response toResponse(ValidationException exception) {
    if (exception instanceof ConstraintViolationException) {

      ConstraintViolationException violationException = (ConstraintViolationException) exception;
      Transformer<javax.validation.ConstraintViolation, ConstraintViolation> transformer =
          new ConstraintConverter();

      List<ConstraintViolation> constraintViolations = violationException.getConstraintViolations()
          .stream()
          .map(transformer::transform).peek(v -> log.warn(v.getConstraint()))
          .collect(Collectors.toList());

      ErrorResponse error = new ErrorResponse("Request Constraint Violation");
      error.setConstraintViolations(constraintViolations);
      return Response.status(Status.BAD_REQUEST)
          .entity(error)
          .type(MediaType.APPLICATION_JSON_TYPE)
          .build();
    }
    log.error("Unknown Validation Exception", exception);
    throw Error.webApplicationException(Status.INTERNAL_SERVER_ERROR, exception.getMessage());
  }

  private static class ConstraintConverter
      implements Transformer<javax.validation.ConstraintViolation, ConstraintViolation> {

    @Override
    public ConstraintViolation transform(javax.validation.ConstraintViolation input) {
      ConstraintViolation constraintViolation = new ConstraintViolation();
      String field = input.getPropertyPath().toString();
      if (field.contains(".")) {
        int lastIndex = field.lastIndexOf(".");
        field = field.substring(lastIndex + 1);
      }
      String constraint = input.getMessage();
      String value =
          input.getInvalidValue() == null ? "(null)" : input.getInvalidValue().toString();

      constraintViolation.setField(field);
      constraintViolation.setConstraint(constraint);
      constraintViolation.setValue(value);
      return constraintViolation;
    }
  }

}

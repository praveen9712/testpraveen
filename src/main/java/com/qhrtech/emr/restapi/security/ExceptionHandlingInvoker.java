
package com.qhrtech.emr.restapi.security;

import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.BusinessLogicException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ConversationException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.LabException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ModuleAccessException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ResourceConflictException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SaveException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.NoDataFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedProvinceException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.AccuroUserValidationException;
import com.qhrtech.emr.accuro.model.exceptions.security.AuthenticationException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientFeatureAccessException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.accuro.model.exceptions.security.LoginException;
import com.qhrtech.emr.accuro.model.exceptions.security.MaskException;
import com.qhrtech.emr.accuro.model.exceptions.security.SecurityException;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.security.exceptions.PreferenceDisabledException;
import com.qhrtech.emr.restapi.security.exceptions.ValidationExceptionMapper;
import com.qhrtech.emr.restapi.services.exceptions.MD5Exception;
import com.qhrtech.emr.restapi.services.exceptions.RestServiceException;
import com.qhrtech.emr.restapi.services.exceptions.RtfConversionException;
import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.ArrayUtils;
import org.apache.cxf.jaxrs.JAXRSInvoker;
import org.apache.cxf.message.Exchange;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author Blake Dickie
 */


public class ExceptionHandlingInvoker extends JAXRSInvoker {

  private Status getBusinessLogicStatus(Throwable t) {
    if (t instanceof SaveException) {
      return Status.BAD_REQUEST;
    } else if (t instanceof ResourceConflictException) {
      return Status.CONFLICT;
    } else if (t instanceof ConversationException) {
      return Status.BAD_REQUEST;
    } else if (t instanceof LabException) {
      return Status.BAD_REQUEST;
    } else if (t instanceof ModuleAccessException) {
      return Status.FORBIDDEN;
    }
    // default status for BusinessLogicExceptions
    return Status.BAD_REQUEST;
  }

  private Status getSecurityStatus(Throwable t) {
    if (t instanceof AccuroUserValidationException) {
      return Status.UNAUTHORIZED;
    } else if (t instanceof AuthenticationException) {
      return Status.UNAUTHORIZED;
    } else if (t instanceof InsufficientFeatureAccessException) {
      return Status.FORBIDDEN;
    } else if (t instanceof InsufficientPermissionsException) {
      return Status.FORBIDDEN;
    } else if (t instanceof InsufficientRolesException) {
      return Status.FORBIDDEN;
    } else if (t instanceof LoginException) {
      return Status.UNAUTHORIZED;
    } else if (t instanceof MaskException) {
      return Status.FORBIDDEN;
    } else if (t instanceof ForbiddenException) {
      return Status.FORBIDDEN;
    }
    // default return for all SecurityExceptions
    return Status.FORBIDDEN;
  }

  private Status getDataAccessStatus(Throwable t) {
    if (t instanceof DatabaseInteractionException) {
      return Status.INTERNAL_SERVER_ERROR;
    } else if (t instanceof NoDataFoundException) {
      return Status.NOT_FOUND;
    } else if (t instanceof TimeZoneNotFoundException) {
      return Status.BAD_REQUEST;
    } else if (t instanceof UnsupportedSchemaVersionException) {
      return Status.BAD_REQUEST;
    } else if (t instanceof UnsupportedProvinceException) {
      return Status.BAD_REQUEST;
    }
    // default status for all DataAccessExceptions
    return Status.INTERNAL_SERVER_ERROR;
  }

  /**
   * Get the status code for any Protoss Exception
   */
  private Status getStatusCodeForProtossException(Throwable t) {
    if (t instanceof SecurityException) {
      return getSecurityStatus(t);
    } else if (t instanceof DataAccessException) {
      return getDataAccessStatus(t);
    } else if (t instanceof BusinessLogicException) {
      return getBusinessLogicStatus(t);
    } else {
      return Status.INTERNAL_SERVER_ERROR;
    }
  }

  private ProtossExceptionDetails getDetailsForProtossException(Throwable t) {
    ProtossExceptionDetails protossExceptionDetails = new ProtossExceptionDetails();
    protossExceptionDetails.setStatus(getStatusCodeForProtossException(t));
    protossExceptionDetails.setMessage(t.getMessage());
    if (t instanceof MaskException) {
      Set<Integer> maskedIDs = ((MaskException) t).getMaskedIDs();
      StringBuilder maskingErrorMessage = new StringBuilder();
      maskingErrorMessage.append(t.getMessage());
      maskingErrorMessage.append(" Masked ID(s): ");
      maskingErrorMessage.append(Arrays
          .toString(ArrayUtils.toPrimitive(maskedIDs.toArray(new Integer[maskedIDs.size()]))));
      protossExceptionDetails.setMessage(maskingErrorMessage.toString());
      return protossExceptionDetails;
    } else if (t instanceof DatabaseInteractionException) {
      if (t.getCause() instanceof SQLException) {

        SQLException sqlException = (SQLException) t.getCause();
        StringBuilder sqlErrorCodeMessage = new StringBuilder();
        // Handle foreign key constraints
        if (sqlException.getErrorCode() == 547) {
          return getCustomErrorsForSqlErrorCodes(
              " : One or more fields references a non existent entity.", t, sqlErrorCodeMessage,
              protossExceptionDetails);
        }
        // Handle non null constraints
        if (sqlException.getErrorCode() == 515) {
          return getCustomErrorsForSqlErrorCodes(
              " : One or more required fields contains a null value.", t, sqlErrorCodeMessage,
              protossExceptionDetails);
        }
        // Handle resource conflict constraints
        if (sqlException.getErrorCode() == 2601 || sqlException.getErrorCode() == 2627) {
          return getCustomErrorsForSqlErrorCodes(
              " : One or more fields in the request must be unique.", t, sqlErrorCodeMessage,
              protossExceptionDetails);
        }
        // Handle data truncation error
        if (sqlException.getErrorCode() == 8152) {
          return getCustomErrorsForSqlErrorCodes(
              " : Length of one of the fields exceeds the limit. "
                  + "Refer to the endpoint documentation on the field validations.",
              t, sqlErrorCodeMessage,
              protossExceptionDetails);
        }
      }
      return protossExceptionDetails;
    } else {
      // This is the generic case for all Protoss Exceptions
      return protossExceptionDetails;
    }
  }

  @Override
  protected Object performInvocation(
      Exchange exchange,
      final Object serviceObject,
      Method m,
      Object[] paramArray) throws Exception {
    try {
      return super.performInvocation(exchange, serviceObject, m, paramArray);
    } catch (Throwable t) {
      if (t instanceof InvocationTargetException) {
        t = t.getCause();
      }
      if (t instanceof ProtossException) {
        ProtossExceptionDetails protossExceptionDetails = getDetailsForProtossException(t);
        throw Error.webApplicationException(protossExceptionDetails.getStatus(),
            protossExceptionDetails.getMessage(), t);
      } else if (t instanceof WebApplicationException) {
        throw (WebApplicationException) t;
      } else if (t instanceof AccessDeniedException) {
        throw Error.webApplicationException(Status.UNAUTHORIZED, t.getMessage());
      } else if (t instanceof PreferenceDisabledException) {
        throw Error.webApplicationException(Status.UNAUTHORIZED, t.getMessage());
      } else if (t instanceof RtfConversionException) {
        throw Error.webApplicationException(Status.BAD_REQUEST, t.getMessage());
      } else if (t instanceof MD5Exception) {
        throw Error.webApplicationException(Status.BAD_REQUEST, t.getMessage());
      } else if (t instanceof IllegalArgumentException) {
        throw Error.webApplicationException(Status.BAD_REQUEST, t.getMessage());
      } else if (t instanceof ModuleAccessException) {
        throw Error.webApplicationException(Status.BAD_REQUEST, t.getMessage());
      } else if (t instanceof SupportingResourceNotFoundException) {
        throw Error.webApplicationException(Status.BAD_REQUEST, t.getMessage());
      } else if (t instanceof ConstraintViolationException) {
        return new ValidationExceptionMapper().toResponse((ConstraintViolationException) t);
      } else if (t instanceof RestServiceException) {
        throw Error.webApplicationException(Status.INTERNAL_SERVER_ERROR, t.getMessage(), t);
      } else if (t instanceof StorageServiceException) {
        throw Error.webApplicationException(Status.INTERNAL_SERVER_ERROR, t.getMessage(), t);
      } else if (t instanceof UnsupportedOperationException) {
        throw Error.webApplicationException(Status.BAD_REQUEST, t.getMessage());
      }
      throw Error.webApplicationException(Status.INTERNAL_SERVER_ERROR,
          "Error processing request. Caused by: " + t, t);
    }
  }

  private ProtossExceptionDetails getCustomErrorsForSqlErrorCodes(String message, Throwable t,
      StringBuilder sqlErrorCodeMessage, ProtossExceptionDetails protossExceptionDetails) {

    sqlErrorCodeMessage.append(t.getMessage());
    sqlErrorCodeMessage.append(message);

    protossExceptionDetails.setMessage(sqlErrorCodeMessage.toString());
    protossExceptionDetails.setStatus(Status.BAD_REQUEST);
    return protossExceptionDetails;
  }

  private class ProtossExceptionDetails {

    private Status status;
    private String message;

    public Status getStatus() {
      return status;
    }

    public void setStatus(Status status) {
      this.status = status;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }

}

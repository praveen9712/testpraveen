
package com.qhrtech.emr.restapi.security;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.ConversationException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.LabException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SaveException;
import com.qhrtech.emr.accuro.model.exceptions.businesslogic.SupportingResourceNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DataAccessException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.TimeZoneNotFoundException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedProvinceException;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.UnsupportedSchemaVersionException;
import com.qhrtech.emr.accuro.model.exceptions.security.AccuroUserValidationException;
import com.qhrtech.emr.accuro.model.exceptions.security.AuthenticationException;
import com.qhrtech.emr.accuro.model.exceptions.security.ForbiddenException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientFeatureAccessException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientPermissionsException;
import com.qhrtech.emr.accuro.model.exceptions.security.InsufficientRolesException;
import com.qhrtech.emr.accuro.model.exceptions.security.MaskException;
import com.qhrtech.emr.restapi.security.exceptions.PreferenceDisabledException;
import com.qhrtech.emr.restapi.services.exceptions.MD5Exception;
import com.qhrtech.emr.restapi.services.exceptions.RestServiceException;
import com.qhrtech.emr.restapi.services.exceptions.RtfConversionException;
import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import org.apache.cxf.message.Exchange;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

public class ExceptionHandlingInvokerTest {

  private final ExceptionHandlingInvoker exceptionHandlingInvoker = new ExceptionHandlingInvoker();
  private final Exchange exchange = mock(Exchange.class);
  private Object exceptionHandlingInvokerTest;

  @Before
  public void setParams() {
    exceptionHandlingInvokerTest = new ExceptionHandlingInvokerTest();
  }

  @Test
  public void testSqlExceptionResourceConflictWithUniqueIndex() throws Exception {
    Method testMethod = this.getClass().getMethod("throwSqlException", int.class);
    Object[] objects = new Object[1];
    objects[0] = 2601;

    try {
      exceptionHandlingInvoker
          .performInvocation(exchange, exceptionHandlingInvokerTest, testMethod, objects);
    } catch (Exception exception) {
      assertTrue(exception.getCause() instanceof DatabaseInteractionException);
      WebApplicationException webApplicationException = (WebApplicationException) exception;
      assertTrue(webApplicationException.getResponse().getStatus() == 400);
    }
  }

  @Test
  public void testSqlExceptionResourceConflictWithUniqueKeyConstraint() throws Exception {
    Method testMethod = this.getClass().getMethod("throwSqlException", int.class);
    Object[] objects = new Object[1];
    objects[0] = 2627;

    try {
      exceptionHandlingInvoker
          .performInvocation(exchange, exceptionHandlingInvokerTest, testMethod, objects);
    } catch (Exception exception) {
      assertTrue(exception.getCause() instanceof DatabaseInteractionException);
      WebApplicationException webApplicationException = (WebApplicationException) exception;
      assertTrue(webApplicationException.getResponse().getStatus() == 400);
    }

  }

  @Test
  public void testSqlExceptionForNotNullFields() throws Exception {
    Method testMethod = this.getClass().getMethod("throwSqlException", int.class);
    Object[] objects = new Object[1];
    objects[0] = 515;

    try {
      exceptionHandlingInvoker
          .performInvocation(exchange, exceptionHandlingInvokerTest, testMethod, objects);
    } catch (Exception exception) {
      assertTrue(exception.getCause() instanceof DatabaseInteractionException);
      WebApplicationException webApplicationException = (WebApplicationException) exception;
      assertTrue(webApplicationException.getResponse().getStatus() == 400);
    }

  }

  @Test
  public void testSqlExceptionForForeignKeyConstraint() throws Exception {
    Method testMethod = this.getClass().getMethod("throwSqlException", int.class);
    Object[] objects = new Object[1];
    objects[0] = 547;

    try {
      exceptionHandlingInvoker
          .performInvocation(exchange, exceptionHandlingInvokerTest, testMethod, objects);
    } catch (Exception exception) {
      assertTrue(exception.getCause() instanceof DatabaseInteractionException);
      WebApplicationException webApplicationException = (WebApplicationException) exception;
      assertTrue(webApplicationException.getResponse().getStatus() == 400);
    }

  }

  @Test
  public void testSqlExceptionForDataTruncation() throws Exception {
    Method testMethod = this.getClass().getMethod("throwSqlException", int.class);
    Object[] objects = new Object[1];
    objects[0] = 8152;

    try {
      exceptionHandlingInvoker
          .performInvocation(exchange, exceptionHandlingInvokerTest, testMethod, objects);
    } catch (Exception exception) {
      assertTrue(exception.getCause() instanceof DatabaseInteractionException);
      WebApplicationException webApplicationException = (WebApplicationException) exception;
      assertTrue(webApplicationException.getResponse().getStatus() == 400);
    }

  }

  @Test
  public void TestInternalServerErrorException() throws Exception {


    List<String> exceptions = Arrays.asList("throwDatabaseInteractionException",
        "throwDataAccessException", "throwProtossException",
        "RestServiceException",
        "StorageServiceException");
    for (String exceptionName : exceptions) {
      Method testMethod = this.getClass().getMethod(exceptionName);
      Object[] objects = new Object[0];
      try {
        exceptionHandlingInvoker
            .performInvocation(exchange, exceptionHandlingInvokerTest, testMethod, objects);
      } catch (Exception exception) {

        WebApplicationException webApplicationException = (WebApplicationException) exception;
        assertTrue(webApplicationException.getResponse().getStatus() == 500);
      }
    }


  }

  public Throwable RestServiceException() throws RestServiceException {
    throw new RestServiceException("Test");
  }

  public Throwable StorageServiceException() throws StorageServiceException {
    throw new StorageServiceException("Test");
  }



  @Test
  public void testForbiddenException() throws Exception {

    List<String> exceptions = Arrays.asList("throwForbiddenException", "throwMaskException",
        "InsufficientFeatureAccessException",
        "InsufficientPermissionsException",
        "InsufficientRolesException");
    for (String exceptionName : exceptions) {
      Method testMethod = this.getClass().getMethod(exceptionName);
      Object[] objects = new Object[0];
      try {
        exceptionHandlingInvoker
            .performInvocation(exchange, exceptionHandlingInvokerTest, testMethod, objects);
      } catch (Exception exception) {

        WebApplicationException webApplicationException = (WebApplicationException) exception;
        assertTrue(webApplicationException.getResponse().getStatus() == 403);
      }
    }

  }

  public Throwable InsufficientFeatureAccessException() throws InsufficientFeatureAccessException {
    throw new InsufficientFeatureAccessException("Test");
  }

  public Throwable InsufficientPermissionsException() throws InsufficientPermissionsException {
    throw new InsufficientPermissionsException("Test");
  }

  public Throwable InsufficientRolesException() throws InsufficientRolesException {
    throw new InsufficientRolesException("Test");
  }

  @Test
  public void testUnAurhtorizedException() throws Exception {

    List<String> exceptions = Arrays.asList("AccessDeniedException", "PreferenceDisabledException",
        "AccuroUserValidationException",
        "AuthenticationException");
    for (String exceptionName : exceptions) {
      Method testMethod = this.getClass().getMethod(exceptionName);
      Object[] objects = new Object[0];
      try {
        exceptionHandlingInvoker
            .performInvocation(exchange, exceptionHandlingInvokerTest, testMethod, objects);
      } catch (Exception exception) {

        WebApplicationException webApplicationException = (WebApplicationException) exception;
        assertTrue(webApplicationException.getResponse().getStatus() == 401);
      }
    }

  }

  public Throwable AccessDeniedException() throws AccessDeniedException {
    throw new AccessDeniedException("Test");
  }

  public Throwable PreferenceDisabledException() throws PreferenceDisabledException {
    throw new PreferenceDisabledException("Test");
  }

  public Throwable AccuroUserValidationException() throws AccuroUserValidationException {
    throw new AccuroUserValidationException("Test");
  }

  public Throwable AuthenticationException() throws AuthenticationException {
    throw new AuthenticationException("Test");
  }



  @Test
  public void testInsuffiCientPermissionsException() throws Exception {
    Method testMethod = this.getClass().getMethod("throwInsufficientPermissionsException");
    Object[] objects = new Object[0];

    try {
      exceptionHandlingInvoker
          .performInvocation(exchange, exceptionHandlingInvokerTest, testMethod, objects);
    } catch (Exception exception) {
      assertTrue(exception.getCause() instanceof InsufficientPermissionsException);
      WebApplicationException webApplicationException = (WebApplicationException) exception;
      assertTrue(webApplicationException.getResponse().getStatus() == 403);
    }

  }


  @Test
  public void testBadRequestException() throws Exception {

    List<String> exceptions = Arrays.asList("throwUnsupportedSchemaVersionException",
        "throwSaveException", "RtfConversionException", "MD5Exception",
        "SupportingResourceNotFoundException", "UnsupportedOperationException",
        "TimeZoneNotFoundException", "UnsupportedSchemaVersionException",
        "UnsupportedProvinceException", "ConversationException", "LabException");
    for (String exceptionName : exceptions) {
      Method testMethod = this.getClass().getMethod(exceptionName);
      Object[] objects = new Object[0];
      try {
        exceptionHandlingInvoker
            .performInvocation(exchange, exceptionHandlingInvokerTest, testMethod, objects);
      } catch (Exception exception) {

        WebApplicationException webApplicationException = (WebApplicationException) exception;
        assertTrue(webApplicationException.getResponse().getStatus() == 400);
      }
    }


  }

  public Throwable ConversationException() throws ConversationException {
    throw new ConversationException("Test");
  }

  public Throwable LabException() throws LabException {
    throw new LabException("Test");
  }

  public Throwable TimeZoneNotFoundException() throws TimeZoneNotFoundException {
    throw new TimeZoneNotFoundException("Test");
  }

  public Throwable UnsupportedSchemaVersionException() throws UnsupportedSchemaVersionException {
    throw new UnsupportedSchemaVersionException("Test");
  }

  public Throwable UnsupportedProvinceException() throws UnsupportedProvinceException {
    throw new UnsupportedProvinceException("Test");
  }


  public Throwable UnsupportedOperationException() {
    throw new UnsupportedOperationException("Test");
  }

  public Throwable SupportingResourceNotFoundException()
      throws SupportingResourceNotFoundException {
    throw new SupportingResourceNotFoundException("Test");
  }

  public Throwable MD5Exception() throws MD5Exception {
    throw new MD5Exception("Test", null);
  }

  public Throwable RtfConversionException() throws RtfConversionException {
    throw new RtfConversionException("Test");
  }

  public Throwable throwSqlException(int errorCode)
      throws DatabaseInteractionException {
    SQLException sqlException = new SQLException("", "", errorCode);
    throw new DatabaseInteractionException("Test", sqlException);
  }

  public Throwable throwDatabaseInteractionException()
      throws DatabaseInteractionException {
    throw new DatabaseInteractionException("Test");
  }

  public Throwable throwMaskException()
      throws MaskException {
    List<Integer> ids = Arrays.asList(1, 2, 3);
    throw new MaskException("Test For Masking", ids);
  }

  public Throwable throwDataAccessException()
      throws DataAccessException {
    throw new DataAccessException("Test");
  }

  public Throwable throwForbiddenException()
      throws ForbiddenException {
    throw new ForbiddenException("Test");
  }

  public Throwable throwInsufficientPermissionsException()
      throws InsufficientPermissionsException {
    throw new InsufficientPermissionsException("Test");
  }

  public Throwable throwSaveException()
      throws SaveException {
    throw new SaveException("Test");
  }

  public Throwable throwProtossException()
      throws ProtossException {
    throw new ProtossException("Test");
  }

  public Throwable throwUnsupportedSchemaVersionException()
      throws UnsupportedSchemaVersionException {
    throw new UnsupportedSchemaVersionException("Test");
  }

}



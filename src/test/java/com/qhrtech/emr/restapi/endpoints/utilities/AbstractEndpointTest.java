
package com.qhrtech.emr.restapi.endpoints.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.qhrtech.emr.restapi.config.serialization.AccuroObjectMapperFactory;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.security.AccuroSecurityRsInvoker;
import com.qhrtech.emr.restapi.security.exceptions.ValidationExceptionMapper;
import com.qhrtech.emr.restapi.security.exceptions.WebApplicationExceptionHandler;
import com.qhrtech.emr.restapi.services.ManagerMapping;
import com.qhrtech.emr.restapi.util.CalendarParamConverterProvider;
import com.qhrtech.emr.restapi.utilities.AbstractTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.parsing.Parser;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Supplier;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.validation.JAXRSBeanValidationInInterceptor;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Base Test Class for running unit tests on Accuro Api End Points.
 *
 * @param <T> Type of end point to test.
 */
public abstract class AbstractEndpointTest<T extends AbstractEndpoint> extends AbstractTest<T> {

  private static final String DEFAULT_BASE_URL = "http://localhost:9999/rest";

  /**
   * Class flag for the tested controller
   */
  private final Class<T> endpointType;

  /**
   * Base url. Must be a valid protocol running on an open port.
   */
  private final String baseUrl;

  /**
   * Jackson Databind Serialization Mapper.
   */
  private final ObjectMapper objectMapper;

  /**
   * Mocked out CXF Servlet the tests will be run against.
   */
  private Server mockServer;

  /**
   * Constructor for specifying a custom base url.
   *
   * @param baseUrl Custom base url.
   * @param endpoint Controller under test.
   * @param endpointType Class flag for controller under test.
   */
  public AbstractEndpointTest(String baseUrl, T endpoint, Class<T> endpointType) {
    super(endpoint);
    this.baseUrl = baseUrl;
    this.endpointType = endpointType;
    this.objectMapper = AccuroObjectMapperFactory.newJsonObjectMapper();
    MockitoAnnotations.initMocks(this);

  }

  /**
   * Constructor when the base url does not need to be specified.
   *
   * @param endpoint Controller under test.
   * @param endpointType Class flag for controller under test.
   */
  public AbstractEndpointTest(T endpoint, Class<T> endpointType) {
    this(DEFAULT_BASE_URL, endpoint, endpointType);
  }


  @Before
  public void setupDefaultTimeZone() {
    // Setting UTC time zone for the tests
    TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
    TimeZone.setDefault(utcTimeZone);
    DateTimeZone.setDefault(DateTimeZone.forTimeZone(utcTimeZone));
  }

  // verifies the Managers used are correctly mapped to their interface.
  @Before
  public void verifyRegisteredProtossManagers() {

    Map<Class, Object> containerServices = getContainerServices();
    containerServices.keySet()
        .stream()
        .map(ManagerMapping::getConstructor)
        .forEach(
            (a) -> Assert
                .assertNotNull("Not all managers have an implementation defined.", a));
  }

  @Before
  public void setupServer() {
    JAXRSServerFactoryBean serverBean = new JAXRSServerFactoryBean();
    serverBean.setResourceClasses(endpointType);
    // setting the custom invoker so at to throws correct status code.
    serverBean.setInvoker(new AccuroSecurityRsInvoker());
    serverBean.setProviders(
        Arrays.asList(new JacksonJsonProvider(objectMapper),
            new CalendarParamConverterProvider(),
            new WebApplicationExceptionHandler(),
            new ValidationExceptionMapper()));
    serverBean.setInInterceptors(Collections.singletonList(new JAXRSBeanValidationInInterceptor()));
    serverBean.setResourceProvider(endpointType, new SingletonResourceProvider(testObject, true));
    serverBean.setAddress(baseUrl);
    mockServer = serverBean.create();
  }

  @Before
  public void setupRestAssured() {
    RestAssured.config = RestAssuredConfig.config()
        .objectMapperConfig(new ObjectMapperConfig(new RestAssuredObjectMapper(objectMapper)));
    RestAssured.defaultParser = Parser.JSON;
  }

  @After
  public void tearDownServer() {
    if (mockServer != null) {
      mockServer.stop();
      mockServer.destroy();
    }

  }

  /**
   * Rest Assured will only return an array of entities when we extract the test results.
   * <p>
   * This convenience method will covert the array to a typed collection.
   *
   * @param args The array to convert.
   * @param collectionFactory The supplier of the collection instance (e.g. ArrayList::new).
   * @param <C> Type of collection
   * @param <D> Type of entity in the collection
   * @return A collection of entities from the array.
   */
  protected <C extends Collection<D>, D> C toCollection(D[] args, Supplier<C> collectionFactory) {
    C c = collectionFactory.get();
    c.addAll(Arrays.asList(args));
    return c;
  }


  /**
   * Allows implementing classes access to the base url for submitting http requests to the mocked
   * server.
   *
   * @return Base url used by the mocked server.
   */
  protected String getBaseUrl() {
    return baseUrl;
  }

  /**
   * Provides implemented classes access to the serializer used by the controller under test.
   *
   * @return The Object Serializer used by the controller under test.
   */
  protected ObjectMapper getObjectMapper() {
    return objectMapper;
  }


  /**
   * This method validates the DTO before it is being passed as request parameter to any endpoint.
   */
  protected <T> void validateDto(T t) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<T>> violations = validator.validate(t);
    Assert.assertTrue("Unexpected " + violations.size() + " violations "
        + violations, violations.isEmpty());
  }
}

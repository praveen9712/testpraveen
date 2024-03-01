
package com.qhrtech.emr.restapi.validators;

import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.prescribeit.DispenseNotificationDto;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BigDecimalTypeValidatorTest {

  private Validator validator;

  @Before
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  private static final String errorMessage1 = "Max. field length including decimal digits is 18 ."
      + " Max. digits after decimal can be: 2";

  @Test
  public void testIsValidMethodWithNullValue() {
    BigDecimalTypeValidator validator = new BigDecimalTypeValidator();

    Assert.assertTrue(validator.isValid(null, null));

  }



  @Test
  public void dateCustomValidatorTest1() {

    DispenseNotificationDto notificationDto = new DispenseNotificationDto();
    notificationDto.setQuantityRemainingValue(new BigDecimal(1.121));
    notificationDto.setUuid(UUID.randomUUID());
    notificationDto.setMedicationName(TestUtilities.nextString(10));

    Set<ConstraintViolation<DispenseNotificationDto>> violations =
        validator.validate(notificationDto);
    Assert.assertTrue(!violations.isEmpty());
    violations.forEach(x -> {

      assertEquals(x.getMessage(), errorMessage1);

    });

  }

  @Test
  public void dateCustomValidatorTest2() {

    DispenseNotificationDto notificationDto = new DispenseNotificationDto();
    // value with length greater than 18
    notificationDto.setQuantityRemainingValue(new BigDecimal("1234567890123456789"));
    notificationDto.setUuid(UUID.randomUUID());
    notificationDto.setMedicationName(TestUtilities.nextString(10));

    Set<ConstraintViolation<DispenseNotificationDto>> violations =
        validator.validate(notificationDto);
    Assert.assertTrue(!violations.isEmpty());
    violations.forEach(x -> {

      assertEquals(x.getMessage(), errorMessage1);

    });

  }

  @Test
  public void dateCustomValidatorTest3() {

    DispenseNotificationDto notificationDto = new DispenseNotificationDto();
    // value with length greater with decimal 18
    notificationDto.setQuantityRemainingValue(new BigDecimal("12345678901234567.89"));
    notificationDto.setUuid(UUID.randomUUID());
    notificationDto.setMedicationName(TestUtilities.nextString(10));

    Set<ConstraintViolation<DispenseNotificationDto>> violations =
        validator.validate(notificationDto);
    Assert.assertTrue(!violations.isEmpty());
    violations.forEach(x -> {

      assertEquals(x.getMessage(), errorMessage1);

    });

  }

  @Test
  public void dateCustomValidatorTest4() {

    DispenseNotificationDto notificationDto = new DispenseNotificationDto();
    // value with length max allowable value with decimal i.e 18
    notificationDto.setQuantityRemainingValue(new BigDecimal("12345678901234567.8"));
    notificationDto.setUuid(UUID.randomUUID());
    notificationDto.setMedicationName(TestUtilities.nextString(10));

    Set<ConstraintViolation<DispenseNotificationDto>> violations =
        validator.validate(notificationDto);
    Assert.assertTrue(violations.isEmpty());


  }

  @Test
  public void dateCustomValidatorTest5() {

    DispenseNotificationDto notificationDto = new DispenseNotificationDto();
    // value with length max allowable value i.e 18
    notificationDto.setQuantityRemainingValue(new BigDecimal("123456789012345678"));
    notificationDto.setUuid(UUID.randomUUID());
    notificationDto.setMedicationName(TestUtilities.nextString(10));

    Set<ConstraintViolation<DispenseNotificationDto>> violations =
        validator.validate(notificationDto);
    Assert.assertTrue(!violations.isEmpty());
    violations.forEach(x -> {

      assertEquals(x.getMessage(), errorMessage1);

    });


  }

  @Test
  public void dateCustomValidatorTest6() {

    DispenseNotificationDto notificationDto = new DispenseNotificationDto();
    notificationDto.setQuantityRemainingValue(new BigDecimal("0.11"));
    notificationDto.setUuid(UUID.randomUUID());
    notificationDto.setMedicationName(TestUtilities.nextString(10));

    Set<ConstraintViolation<DispenseNotificationDto>> violations =
        validator.validate(notificationDto);
    Assert.assertTrue(violations.isEmpty());

    notificationDto.setQuantityRemainingValue(new BigDecimal("1.11"));
    Set<ConstraintViolation<DispenseNotificationDto>> violations1 =
        validator.validate(notificationDto);
    Assert.assertTrue(violations1.isEmpty());

    notificationDto.setQuantityRemainingValue(new BigDecimal("121"));
    Set<ConstraintViolation<DispenseNotificationDto>> violations2 =
        validator.validate(notificationDto);
    Assert.assertTrue(violations2.isEmpty());

  }
}

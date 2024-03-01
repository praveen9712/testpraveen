
package com.qhrtech.emr.restapi.validators;

import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.restapi.models.dto.DocumentDto;
import com.qhrtech.emr.restapi.models.dto.prescribeit.ErxCancelRequestsDto;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LocalDateTime2RangeValidatorTest {

  private Validator validator;

  @Before
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testIsValidMethod() {
    LocalDateTime2RangeValidator dateTime2Validator = new LocalDateTime2RangeValidator();

    LocalDateTime dateTime = new LocalDateTime(1, 1, 1, 0, 0, 0, 0);

    Assert.assertTrue(dateTime2Validator.isValid(dateTime, null));
    Assert.assertTrue(dateTime2Validator.isValid(null, null));
    LocalDateTime dateTime2 = new LocalDateTime(0, 1, 1, 0, 0, 0, 0);
    Assert.assertFalse(dateTime2Validator.isValid(dateTime2, null));

    LocalDateTime dateTime3 = new LocalDateTime(0, 1, 1, 0, 0, 0, 0);
    Assert.assertFalse(dateTime2Validator.isValid(dateTime3, null));

  }

  @Test
  public void dateCustomValidatorTest() {

    ErxCancelRequestsDto requestDto = new ErxCancelRequestsDto();
    requestDto.setProviderId(1234);
    requestDto.setPatientId(12);
    requestDto.setExternalId(UUID.randomUUID());
    requestDto.setUserId(1);
    requestDto.setRequestReasonCode("1");

    requestDto.setDateRead(LocalDateTime.parse("-1"));

    Set<ConstraintViolation<ErxCancelRequestsDto>> violations = validator.validate(requestDto);
    Assert.assertTrue(!violations.isEmpty());
    violations.forEach(x -> {

      assertEquals(x.getMessage(),
          "The date time must be between 01-01-01(include) and 9999-12-31(include)");

    });

    requestDto.setDateRead(LocalDateTime.parse("10000-01-01"));

    Set<ConstraintViolation<ErxCancelRequestsDto>> violations1 = validator.validate(requestDto);
    Assert.assertTrue(!violations1.isEmpty());
    violations1.forEach(x -> {

      assertEquals(x.getMessage(),
          "The date time must be between 01-01-01(include) and 9999-12-31(include)");

    });

  }

}

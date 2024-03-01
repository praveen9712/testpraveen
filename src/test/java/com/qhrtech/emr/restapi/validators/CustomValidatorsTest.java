
package com.qhrtech.emr.restapi.validators;

import static org.junit.Assert.assertEquals;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.DocumentDto;
import com.qhrtech.emr.restapi.models.dto.DocumentReviewDto;
import java.util.Collections;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

public class CustomValidatorsTest {

  private Validator validator;


  @Before
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }



  @Test
  public void dateCustomValidatorTest() {

    DocumentDto documentDto = getDocument();
    documentDto.setDocumentDate(LocalDateTime.parse("2020-1-1"));
    documentDto.setReceivedDate(LocalDateTime.parse("2020-1-1"));
    Set<ConstraintViolation<DocumentDto>> violations = validator.validate(documentDto);
    violations.forEach(x -> {

      assertEquals(x.getMessage(), "The date cannot be in future");

    });

  }

  @Test
  public void fromTypeValidatorTest() {
    DocumentDto documentDto = getDocument();
    documentDto.setFromType(TestUtilities.nextString(10));
    Set<ConstraintViolation<DocumentDto>> violations = validator.validate(documentDto);
    violations.forEach(x -> {
      assertEquals(x.getMessage(), "From type should be either null or contain one "
          + "of these values-  Physician, Insurer, Contact, Patient, OneTimeRecipient");

    });
  }


  @Test
  public void fileExtensionValidatorTest() {
    DocumentDto documentDto = getDocument();
    documentDto.setFileName(TestUtilities.nextString(4) + ".a");
    Set<ConstraintViolation<DocumentDto>> violations = validator.validate(documentDto);
    violations.forEach(x -> {
      assertEquals(x.getMessage(), "Valid file extension is PDF.");
    });
  }

  @Test
  public void fileExtensionValidatorWithoutPeriodTest() {
    DocumentDto documentDto = getDocument();
    documentDto.setFileName(TestUtilities.nextString(4));
    Set<ConstraintViolation<DocumentDto>> violations = validator.validate(documentDto);
    violations.forEach(x -> {
      assertEquals(x.getMessage(), "Valid file extension is PDF.");
    });
  }

  @Test
  public void documentDtoValidTest() {
    var documentDto = getDocument();
    Set<ConstraintViolation<DocumentDto>> violations = validator.validate(documentDto);
    assert (violations.isEmpty());
  }

  @Test
  public void documentReviewInValidTest() {
    DocumentDto documentDto = getDocument();
    Set<ConstraintViolation<DocumentDto>> violations = validator.validate(documentDto);

    for (DocumentReviewDto reviewDto : documentDto.getReviews()) {
      DocumentReviewDto documentReviewDto = reviewDto;
      documentReviewDto.setProviderId(0);
      documentDto.setReviews(Collections.singleton(documentReviewDto));
    }
    violations.forEach(x -> {
      assertEquals(x.getMessage(), "For the provided reviewed date, physician "
          + "ID can not be null or 0.");

    });
  }



  private DocumentDto getDocument() {
    DocumentDto documentDto = new DocumentDto();
    DocumentReviewDto documentReviewDto = new DocumentReviewDto();
    LocalDateTime date = LocalDateTime.now();
    LocalDateTime todaysDate =
        LocalDateTime.parse("" + date.getDayOfYear() + "-" + date.getMonthOfYear()
            + "-" + date.getDayOfMonth());
    documentReviewDto.setReviewDate(todaysDate);
    documentReviewDto.setProviderId(TestUtilities.nextInt());
    documentDto.setDocumentDate(todaysDate);
    documentDto.setReceivedDate(todaysDate);
    documentDto.setDocumentId(3);
    documentDto.setPriority(1);
    documentDto.setFileName(TestUtilities.nextString(10) + ".pdf");
    documentDto.setFolderId(2);
    documentDto.setReviews(Collections.singleton(documentReviewDto));
    return documentDto;
  }
}

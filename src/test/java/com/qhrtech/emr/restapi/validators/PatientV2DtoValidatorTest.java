
package com.qhrtech.emr.restapi.validators;

import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.ContactType;
import com.qhrtech.emr.restapi.models.dto.patientv2.AddressDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.AlbertaDetailsDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.DemographicsDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.EmailDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.ManitobaDetailsDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.NovaScotiaDetailsDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.OntarioDetailsDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.PatientDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.PersonalHealthCardDto;
import com.qhrtech.emr.restapi.models.dto.patientv2.PhoneDto;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatientV2DtoValidatorTest {

  private Validator validator;


  @Before
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }


  @Test
  public void testRegexFields() {

    DemographicsDto demographicsDto = new DemographicsDto();
    PhoneDto phoneDto = new PhoneDto();
    phoneDto.setNumber("(123) 456-789_");

    demographicsDto.setPhones(Collections.singletonList(phoneDto));
    demographicsDto.setFirstName(TestUtilities.nextString(5));
    demographicsDto.setLastName(TestUtilities.nextString(5));
    PatientDto patientDto = new PatientDto();
    patientDto.setDemographics(demographicsDto);
    patientDto.setPatientStatusId(1);

    Set<ConstraintViolation<PatientDto>> violations = validator.validate(patientDto);
    Assert.assertEquals(2, violations.size());

    AddressDto addressDto = new AddressDto();
    addressDto.setPostalZip(TestUtilities.nextString(5));
    demographicsDto.setAddresses(Collections.singletonList(addressDto));

    violations = validator.validate(patientDto);
    Assert.assertEquals(2, violations.size());

    AlbertaDetailsDto albertaDetailsDto = new AlbertaDetailsDto();
    albertaDetailsDto.setType(TestUtilities.nextString(10));
    patientDto.setAlbertaDetails(albertaDetailsDto);

    violations = validator.validate(patientDto);
    Assert.assertEquals(3, violations.size());

    albertaDetailsDto.setNewBornCode(TestUtilities.nextString(10));
    violations = validator.validate(patientDto);
    Assert.assertEquals(4, violations.size());

    PersonalHealthCardDto healthCard = new PersonalHealthCardDto();
    healthCard.setPhn(TestUtilities.nextString(9));
    demographicsDto.setHealthCard(healthCard);
    violations = validator.validate(patientDto);

    Assert.assertEquals(4, violations.size());

    // setting the corret values and testing
    phoneDto.setNumber("(123) 456-7890");
    phoneDto.setContactType(ContactType.CellPhone);
    addressDto.setPostalZip("A1A2B2");
    albertaDetailsDto.setType("PYST");
    albertaDetailsDto.setNewBornCode("ADOP");
    healthCard.setPhn("123456789");
    violations = validator.validate(patientDto);
    Assert.assertEquals(0, violations.size());

    // as they are not required fields, they should work for the null values as well
    phoneDto.setNumber(null);
    addressDto.setPostalZip(null);
    albertaDetailsDto.setType(null);
    albertaDetailsDto.setNewBornCode(null);
    healthCard.setPhn(null);
    violations = validator.validate(patientDto);
    Assert.assertEquals(0, violations.size());

    // test with blank fields
    phoneDto.setNumber("");
    addressDto.setPostalZip("");
    albertaDetailsDto.setType("");
    albertaDetailsDto.setNewBornCode("");
    healthCard.setPhn("");
    violations = validator.validate(patientDto);
    Assert.assertEquals(0, violations.size());

    addressDto.setPostalZip("                                                          ");
    violations = validator.validate(patientDto);
    Assert.assertEquals(0, violations.size());
  }

  @Test
  public void validatePatientV2Dto() {

    PatientDto patientDto = new PatientDto();

    PhoneDto phoneDto = new PhoneDto();
    String inValidPhoneNumber = "(123) 456-___";
    phoneDto.setNumber(inValidPhoneNumber);
    phoneDto.setContactType(ContactType.CellPhone);
    String inValidFileNumber = TestUtilities.nextString(31);
    String inValidRegNumber = TestUtilities.nextString(21);
    String inValidPaperChartNote = TestUtilities.nextString(256);
    patientDto.setFileNumber(inValidFileNumber);
    patientDto.setRegistrationNumber(inValidRegNumber);
    patientDto.setPaperChartNote(inValidPaperChartNote);

    AlbertaDetailsDto detailsV2Dto = new AlbertaDetailsDto();
    String invalidType = TestUtilities.nextString(5);
    String invalidNewBornCode = TestUtilities.nextString(5);
    // PYST,RECP or RFRC
    detailsV2Dto.setType(invalidType);
    detailsV2Dto.setNewBornCode(invalidNewBornCode);
    patientDto.setAlbertaDetails(detailsV2Dto);

    ManitobaDetailsDto manitobaDetailsDto = new ManitobaDetailsDto();
    String invalidMBregNum = TestUtilities.nextString(7);
    manitobaDetailsDto.setHealthRegistrationNumber(invalidMBregNum);
    patientDto.setManitobaDetails(manitobaDetailsDto);

    NovaScotiaDetailsDto nvDetailsDto = new NovaScotiaDetailsDto();
    String invalidNvHcn = TestUtilities.nextString(10);
    nvDetailsDto.setGuardianHcn(invalidNvHcn);
    patientDto.setNovaScotiaDetails(nvDetailsDto);

    PersonalHealthCardDto healthCardDto = new PersonalHealthCardDto();
    String invalidHealthCardNv = TestUtilities.nextString(10);
    healthCardDto.setPhn(invalidHealthCardNv);
    nvDetailsDto.setSecondaryHealthCard(healthCardDto);

    OntarioDetailsDto ontarioDetailsDto = new OntarioDetailsDto();
    String invalidValidationMessage = TestUtilities.nextString(151);
    ontarioDetailsDto.setValidationMessage(invalidValidationMessage);
    patientDto.setOntarioDetails(ontarioDetailsDto);

    EmailDto emailDto = new EmailDto();
    String invalidEmail = TestUtilities.nextString(51);
    emailDto.setAddress(invalidEmail);
    DemographicsDto demographicsDto = new DemographicsDto();
    demographicsDto.setEmail(emailDto);

    demographicsDto.setPhones(Collections.singletonList(phoneDto));
    AddressDto addressDto = new AddressDto();
    String invalidZipCode = TestUtilities.nextString(7);
    addressDto.setPostalZip(invalidZipCode);
    demographicsDto.setAddresses(Collections.singletonList(addressDto));

    LocalDate inValidDate = new LocalDate(1700, 1, 1);
    patientDto.setReferredDate(inValidDate);

    demographicsDto.setFirstName(TestUtilities.nextString(5));
    demographicsDto.setLastName(TestUtilities.nextString(5));
    patientDto.setDemographics(demographicsDto);
    patientDto.setPatientStatusId(1);
    Set<ConstraintViolation<PatientDto>> violations = validator.validate(patientDto);
    int expectedCount = violations.size();
    Set<String> actualCount = new HashSet<>();
    violations.forEach(x -> {
      if (x.getInvalidValue() instanceof LocalDateTime) {
        Assert.assertTrue(x.getMessage().startsWith("The date time must"));
        actualCount.add(x.getMessage());
      }

      if (x.getInvalidValue() instanceof LocalDate) {
        Assert.assertTrue(x.getMessage().startsWith("The date must"));
        actualCount.add(x.getMessage());
      }

      if (x.getInvalidValue().equals(invalidEmail)) {
        Assert.assertTrue(x.getMessage().startsWith("The email address size should not be"));
        actualCount.add(x.getMessage());
      }

      if (x.getInvalidValue().equals(invalidValidationMessage)) {
        Assert.assertTrue(x.getMessage().startsWith("The validation message size should not "));
        actualCount.add(x.getMessage());
      }

      if (x.getInvalidValue().equals(invalidHealthCardNv)) {
        Assert.assertTrue(x.getMessage().startsWith("Health card number should have numbers"));
        actualCount.add(x.getMessage());
      }
      if (x.getInvalidValue().equals(invalidNvHcn)) {
        Assert.assertTrue(x.getMessage().startsWith("The guardian Hcn size should not"));
        actualCount.add(x.getMessage());
      }

      if (x.getInvalidValue().equals(invalidMBregNum)) {
        Assert.assertTrue(x.getMessage().startsWith("Health registration number size should"));
        actualCount.add(x.getMessage());
      }

      if (x.getInvalidValue().equals(inValidFileNumber)) {
        Assert.assertTrue(x.getMessage().startsWith("FileNumber size should"));
        actualCount.add(x.getMessage());
      }

      if (x.getInvalidValue().equals(inValidPhoneNumber)) {
        Assert.assertTrue(x.getMessage().startsWith("Valid phone number pattern"));
        actualCount.add(x.getMessage());
      }
      if (x.getInvalidValue().equals(inValidRegNumber)) {
        Assert.assertTrue(x.getMessage().startsWith("Registration number size"));
        actualCount.add(x.getMessage());
      }
      if (x.getInvalidValue().equals(inValidPaperChartNote)) {
        Assert.assertTrue(x.getMessage().startsWith("Paper chart note size"));
        actualCount.add(x.getMessage());
      }

      if (x.getInvalidValue().equals(invalidType)) {
        Assert.assertTrue(x.getMessage().startsWith("Valid person types are"));
        actualCount.add(x.getMessage());
      }
      if (x.getInvalidValue().equals(invalidNewBornCode)) {
        Assert.assertTrue(x.getMessage().startsWith("Valid new born codes are: ADOP"));
        actualCount.add(x.getMessage());
      }
      if (x.getInvalidValue().equals(invalidZipCode)) {
        Assert.assertTrue(x.getMessage().startsWith("Valid Postal or zip code"));
        actualCount.add(x.getMessage());
      }

    });

    Assert.assertEquals(expectedCount, actualCount.size());
  }

  @Test
  public void TestPatientDtoRequiredFields() {
    PatientDto patientDto = new PatientDto();

    Set<ConstraintViolation<PatientDto>> violations = validator.validate(patientDto);
    Assert.assertEquals(1, violations.size());
    patientDto.setPatientStatusId(1);

    violations = validator.validate(patientDto);
    Assert.assertEquals(0, violations.size());
  }


  @Test
  public void TestAlbertaDetailsDto() {
    AlbertaDetailsDto detailsV2Dto = new AlbertaDetailsDto();
    // PYST,RECP or RFRC
    detailsV2Dto.setType("RFRC");

    Set<ConstraintViolation<AlbertaDetailsDto>> violations = validator.validate(detailsV2Dto);
    violations.forEach(x -> {

      System.out.println(x.getMessage());

    });

  }
}

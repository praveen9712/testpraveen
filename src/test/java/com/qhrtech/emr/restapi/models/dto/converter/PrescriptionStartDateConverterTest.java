
package com.qhrtech.emr.restapi.models.dto.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.dozer.DozerConverter;
import org.joda.time.LocalDate;
import org.junit.Test;

public class PrescriptionStartDateConverterTest {

  private final DozerConverter<String, LocalDate> converter;

  public PrescriptionStartDateConverterTest() {
    this.converter = new PrescriptionStartDateConverter();
  }

  @Test
  public void testConvertToNullInput() {
    LocalDate actual = converter.convertTo(null);
    assertNull(actual);
  }

  @Test
  public void testConvertToPrimaryInput() {
    LocalDate expected = LocalDate.parse("2001-12-31");
    LocalDate actual = converter.convertTo("20011231");
    assertEquals(expected, actual);
  }

  @Test
  public void testConvertToSecondaryInput() {
    LocalDate expected = LocalDate.parse("2020-05-15");
    LocalDate actual = converter.convertTo("05/15/2020");
    assertEquals(expected, actual);
  }

  @Test
  public void testConvertToInvalidInput() {
    LocalDate actual = converter.convertTo("banana");
    assertNull(actual);
  }

  @Test
  public void testConvertFromNullInput() {
    String actual = converter.convertFrom(null);
    assertNull(actual);
  }

  @Test
  public void testConvertFromValidInput() {
    String expected = "19760921";
    String actual = converter.convertFrom(LocalDate.parse("1976-09-21"));
    assertEquals(expected, actual);
  }

}

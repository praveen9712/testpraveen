
package com.qhrtech.emr.restapi.models.dto.converter;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.dozer.DozerConverter;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class PrescriptionStartDateConverter extends DozerConverter<String, LocalDate> {

  private static final String PRIMARY_DATE_FORMAT = "yyyyMMdd";
  private static final String SECONDARY_DATE_FORMAT = "MM/dd/yyyy";
  private static final Pattern PRIMARY_DATE_PATTERN =
      Pattern.compile("(\\d{8})");
  private static final Pattern SECONDARY_DATE_PATTERN =
      Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})");

  public PrescriptionStartDateConverter() {
    super(String.class, LocalDate.class);
  }

  @Override
  public LocalDate convertTo(String source, LocalDate destination) {

    if (StringUtils.isBlank(source)) {
      return null;
    }

    if (PRIMARY_DATE_PATTERN.matcher(source).matches()) {
      return DateTimeFormat.forPattern(PRIMARY_DATE_FORMAT).parseLocalDate(source);
    } else if (SECONDARY_DATE_PATTERN.matcher(source).matches()) {
      return DateTimeFormat.forPattern(SECONDARY_DATE_FORMAT).parseLocalDate(source);
    } else {
      return null;
    }
  }

  @Override
  public String convertFrom(LocalDate source, String destination) {
    if (source == null) {
      return null;
    }
    return source.toString("yyyyMMdd");
  }

}

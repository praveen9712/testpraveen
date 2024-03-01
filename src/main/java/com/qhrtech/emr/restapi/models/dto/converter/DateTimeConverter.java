
package com.qhrtech.emr.restapi.models.dto.converter;

import org.dozer.DozerConverter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

public class DateTimeConverter extends DozerConverter<DateTime, LocalDateTime> {

  public DateTimeConverter() {
    super(DateTime.class, LocalDateTime.class);
  }

  @Override
  public LocalDateTime convertTo(DateTime source, LocalDateTime destination) {
    if (source == null) {
      return null;
    }
    return new LocalDateTime(source);
  }

  @Override
  public DateTime convertFrom(LocalDateTime source, DateTime destination) {
    if (source == null) {
      return null;
    }
    return new DateTime(source, DateTimeZone.UTC);
  }

}

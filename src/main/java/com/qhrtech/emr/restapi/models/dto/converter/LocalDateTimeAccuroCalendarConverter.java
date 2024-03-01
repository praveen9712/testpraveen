
package com.qhrtech.emr.restapi.models.dto.converter;

import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import org.dozer.DozerConverter;
import org.joda.time.LocalDateTime;

public class LocalDateTimeAccuroCalendarConverter
    extends DozerConverter<LocalDateTime, AccuroCalendar> {

  public LocalDateTimeAccuroCalendarConverter() {
    super(LocalDateTime.class, AccuroCalendar.class);
  }

  @Override
  public AccuroCalendar convertTo(LocalDateTime localDateTime, AccuroCalendar calendar) {
    if (localDateTime == null) {
      return null;
    }

    return AccuroCalendar.wrap(localDateTime.toDateTime().toCalendar(null));

  }

  @Override
  public LocalDateTime convertFrom(AccuroCalendar calendar, LocalDateTime localDateTime) {
    if (calendar == null) {
      return null;
    }
    return LocalDateTime.fromCalendarFields(AccuroCalendar.unwrap(calendar));
  }
}

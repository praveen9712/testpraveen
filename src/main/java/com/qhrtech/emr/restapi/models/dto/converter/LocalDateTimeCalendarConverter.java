
package com.qhrtech.emr.restapi.models.dto.converter;

import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.utils.time.TimeUtil;
import com.qhrtech.emr.restapi.config.serialization.AccuroCalendarBasedDeserializer;
import java.util.Calendar;
import java.util.TimeZone;
import org.dozer.DozerConverter;
import org.joda.time.LocalDateTime;

public class LocalDateTimeCalendarConverter extends DozerConverter<LocalDateTime, Calendar> {

  public LocalDateTimeCalendarConverter() {
    super(LocalDateTime.class, Calendar.class);
  }

  @Override
  public Calendar convertTo(LocalDateTime localDateTime, Calendar calendar) {
    if (localDateTime == null) {
      return null;
    }
    String s = localDateTime.toString();
    AccuroCalendar parse =
        AccuroCalendarBasedDeserializer.parse(s, TimeUtil.getTimeZone());

    return AccuroCalendar.unwrap(parse);



  }

  @Override
  public LocalDateTime convertFrom(Calendar calendar, LocalDateTime localDateTime) {
    if (calendar == null) {
      return null;
    }
    return LocalDateTime.fromCalendarFields(calendar);
  }
}

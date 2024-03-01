
package com.qhrtech.emr.restapi.util;

import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.utils.time.TimeFormatUtil;
import com.qhrtech.emr.restapi.config.serialization.AccuroCalendarBasedDeserializer;
import java.util.Calendar;
import javax.ws.rs.ext.ParamConverter;

/**
 *
 * @author kevin.kendall
 */
public class CalendarParamConverter implements ParamConverter<Calendar> {

  private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ";

  @Override
  public Calendar fromString(String value) throws IllegalArgumentException {
    AccuroCalendar calendar = AccuroCalendarBasedDeserializer.parse(value, null);
    Calendar cal = AccuroCalendar.unwrap(calendar);
    if (cal == null) {
      throw new IllegalArgumentException("Invalid date " + value);
    }
    return cal;
  }

  @Override
  public String toString(Calendar value) throws IllegalArgumentException {
    String date = TimeFormatUtil.formatDate(value, FORMAT);
    return date;
  }
}

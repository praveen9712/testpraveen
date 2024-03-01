
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.utils.time.TimeFormatUtil;
import java.io.IOException;

/**
 * A JSON serializer that formats AccuroCalendar objects.
 */
public class AccuroCalendarSerializer extends StdScalarSerializer<AccuroCalendar> {

  public AccuroCalendarSerializer() {
    super(AccuroCalendar.class);
  }

  @Override
  public boolean isEmpty(SerializerProvider provider, AccuroCalendar value) {
    return value == null || AccuroCalendar.unwrap(value) == null;
  }

  @Override
  public void serialize(AccuroCalendar value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
    if (isEmpty(provider, value)) {
      jgen.writeNull();
      return;
    }

    String format;
    switch (value.getPrecision()) {
      case Year:
        format = "yyyy";
        break;
      case Month:
        format = "yyyy-MM";
        break;
      case Day:
        format = "yyyy-MM-dd";
        break;
      case Hour:
        format = "yyyy-MM-dd'T'HHZZZ";
        break;
      case Minute:
        format = "yyyy-MM-dd'T'HH:mmZZZ";
        break;
      case Second:
        format = "yyyy-MM-dd'T'HH:mm:ssZZZ";
        break;
      case Millisecond:
        format = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ";
        break;
      default:
        throw new IllegalArgumentException("Unknown Calendar Prescition: " + value.getPrecision());
    }

    jgen.writeString(TimeFormatUtil.formatDate(AccuroCalendar.unwrap(value), format));
  }
}

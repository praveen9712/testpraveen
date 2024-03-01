
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import java.io.IOException;
import javax.ws.rs.core.Response.Status;
import org.joda.time.LocalDate;

public class JodaLocalDateDeserializer extends StdDeserializer<LocalDate> {
  protected JodaLocalDateDeserializer() {
    super(LocalDate.class);
  }

  @Override
  public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    // Check if the date value is not passed as a String
    if (jsonParser.getCurrentTokenId() == 7) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "LocalDate " + jsonParser.getCurrentName()
              + "  field do not accept as whole number Int type. Must be in double quotes ");
    }
    return new LocalDateDeserializer().deserialize(jsonParser, deserializationContext);
  }
}

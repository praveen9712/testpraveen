
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import java.io.IOException;
import javax.ws.rs.core.Response.Status;
import org.joda.time.LocalDateTime;

public class JodaLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
  {
  }

  protected JodaLocalDateTimeDeserializer() {
    super(LocalDateTime.class);
  }

  @Override
  public LocalDateTime deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) throws IOException {
    // Check if the date value is not passed as a String
    if (jsonParser.getCurrentTokenId() == 7) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "LocalDateTime field " + jsonParser.getCurrentName()
              + " do not accept as whole number Int type. Must be in double quotes ");
    }
    return new LocalDateTimeDeserializer().deserialize(jsonParser, deserializationContext);
  }

}



package com.qhrtech.emr.restapi.models.dto.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.EyeCode;
import java.io.IOException;

public class EyeCodeSerializer extends JsonSerializer<EyeCode> {

  @Override
  public void serialize(EyeCode value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
    jgen.writeStartObject();
    jgen.writeFieldName("name");
    jgen.writeString(value.name());
    jgen.writeFieldName("abbreviation");
    jgen.writeString(value.getAbbreviation());
    jgen.writeEndObject();
  }

}

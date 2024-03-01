
package com.qhrtech.emr.restapi.models.dto.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.LifeStageType;
import java.io.IOException;

public class LifeStageSerializer extends JsonSerializer<LifeStageType> {

  @Override
  public void serialize(LifeStageType value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
    jgen.writeStartObject();
    jgen.writeFieldName("name");
    jgen.writeString(value.name());
    jgen.writeFieldName("description");
    jgen.writeString(value.getDescription());
    jgen.writeEndObject();
  }
}

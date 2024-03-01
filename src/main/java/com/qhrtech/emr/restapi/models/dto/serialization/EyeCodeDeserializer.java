
package com.qhrtech.emr.restapi.models.dto.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.EyeCode;
import java.io.IOException;

public class EyeCodeDeserializer extends JsonDeserializer<EyeCode> {

  @Override
  public EyeCode deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    String abbreviation = node.get("abbreviation").asText();
    return EyeCode.lookup(abbreviation);
  }
}

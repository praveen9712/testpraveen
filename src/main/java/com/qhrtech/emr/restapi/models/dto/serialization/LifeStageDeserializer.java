
package com.qhrtech.emr.restapi.models.dto.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.qhrtech.emr.restapi.models.dto.medicalhistory.LifeStageType;
import java.io.IOException;

public class LifeStageDeserializer extends JsonDeserializer<LifeStageType> {

  @Override
  public LifeStageType deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    String description = node.get("description").asText();
    return LifeStageType.lookup(description);
  }
}


package com.qhrtech.emr.restapi.endpoints.utilities;

import com.fasterxml.jackson.databind.type.TypeFactory;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import java.io.IOException;

/**
 * AccuroAPI uses a Jackson Databind object mapper. This class implements that mapper as a
 * restassured object mapper for use in testing.
 */
public class RestAssuredObjectMapper implements ObjectMapper {
  private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper;

  public RestAssuredObjectMapper(com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
    jacksonObjectMapper = objectMapper;
  }

  @Override
  public Object deserialize(ObjectMapperDeserializationContext context) {
    try {

      return jacksonObjectMapper
          .readValue(context.getDataToDeserialize().asString(),
              TypeFactory.rawClass(context.getType()));

    } catch (IOException e) {
      throw new RuntimeException("Error deserializing " + context.getType(), e);
    }
  }

  @Override
  public Object serialize(ObjectMapperSerializationContext context) {
    try {
      return jacksonObjectMapper.writeValueAsString(context.getObjectToSerialize());
    } catch (IOException e) {
      throw new RuntimeException(
          "Error serializing " + context.getObjectToSerialize().getClass().getName(), e);
    }
  }
}

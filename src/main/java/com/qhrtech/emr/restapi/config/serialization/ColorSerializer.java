
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.awt.Color;
import java.io.IOException;

public class ColorSerializer extends JsonSerializer<Color> {

  /**
   * Serialize the Color object into JSON.
   *
   * @param value The Color object to serialize
   * @param jgen JsonGenerator to do the writing.
   * @param provider Serializer Provider.
   */
  @Override
  public void serialize(Color value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
    String hex;
    if (value.getAlpha() == 255) {
      hex = String.format("#%02X%02X%02X", value.getRed(), value.getGreen(), value.getBlue());
    } else {
      hex = String.format("#%02X%02X%02X%02X", value.getRed(), value.getGreen(), value.getBlue(),
          value.getAlpha());
    }
    jgen.writeString(hex);
  }
}

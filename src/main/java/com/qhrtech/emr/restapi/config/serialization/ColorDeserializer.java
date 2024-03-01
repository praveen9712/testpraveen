
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.awt.Color;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorDeserializer extends JsonDeserializer<Color> {

  /**
   * Deserialize a JSON color object into a Java Color object.
   *
   * @param jp The JSON Parser object
   * @param ctxt The deserialization context
   * @return A Java Color Object.
   */
  @Override
  public Color deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    ObjectCodec oc = jp.getCodec();
    JsonNode node = oc.readTree(jp);

    String hexPattern = "(#)([0-9A-Z]{6,8})";
    Pattern pattern = Pattern.compile(hexPattern, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(node.asText());

    if (matcher.find()) {
      String hexValue = matcher.group(2);
      if (hexValue.length() == 8) {
        // We have an alpha value.
        int red = Integer.parseInt(hexValue.substring(0, 2), 16);
        int green = Integer.parseInt(hexValue.substring(2, 4), 16);
        int blue = Integer.parseInt(hexValue.substring(4, 6), 16);
        int alpha = Integer.parseInt(hexValue.substring(6, 8), 16);
        return new Color(red, green, blue, alpha);
      } else if (hexValue.length() == 6) {
        // We have no alpha value.
        int red = Integer.parseInt(hexValue.substring(0, 2), 16);
        int green = Integer.parseInt(hexValue.substring(2, 4), 16);
        int blue = Integer.parseInt(hexValue.substring(4, 6), 16);
        return new Color(red, green, blue);
      } else {
        // Invalid format.
        return null;
      }
    } else {
      return null;
    }
  }
}

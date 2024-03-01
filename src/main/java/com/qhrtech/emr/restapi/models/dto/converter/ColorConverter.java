
package com.qhrtech.emr.restapi.models.dto.converter;

import java.awt.Color;
import org.dozer.DozerConverter;

public class ColorConverter extends DozerConverter<Color, Color> {

  public ColorConverter() {
    super(Color.class, Color.class);
  }

  @Override
  public Color convertFrom(Color source, Color destination) {
    return source;
  }

  @Override
  public Color convertTo(Color source, Color destination) {
    return source;
  }

}

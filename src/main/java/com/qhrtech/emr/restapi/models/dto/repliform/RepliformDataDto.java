
package com.qhrtech.emr.restapi.models.dto.repliform;

import com.qhrtech.emr.dataaccess.annotation.Model;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.joda.time.LocalDateTime;

@Model
public final class RepliformDataDto implements Serializable {

  private int id;

  @NotNull
  @Size(max = 200)
  private String fieldName;

  @Size(max = 200)
  private String tagName;

  @NotNull
  @Size(max = 200)
  private String textValue;

  private BigDecimal numericValue;

  @DocumentationExample("2022-02-15 07:44:59.000")
  @TypeHint(String.class)
  private LocalDateTime dateValue;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  public String getTextValue() {
    return textValue;
  }

  public void setTextValue(String textValue) {
    this.textValue = textValue;
  }

  public BigDecimal getNumericValue() {
    return numericValue;
  }

  public void setNumericValue(BigDecimal numericValue) {
    this.numericValue = numericValue;
  }

  @DocumentationExample("2022-02-15 07:44:59.000")
  @TypeHint(String.class)
  public LocalDateTime getDateValue() {
    return dateValue;
  }

  public void setDateValue(LocalDateTime dateValue) {
    this.dateValue = dateValue;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RepliformDataDto that = (RepliformDataDto) o;

    if (id != that.id) {
      return false;
    }
    if (!fieldName.equals(that.fieldName)) {
      return false;
    }
    if (!tagName.equals(that.tagName)) {
      return false;
    }
    if (!textValue.equals(that.textValue)) {
      return false;
    }
    if (!numericValue.equals(that.numericValue)) {
      return false;
    }
    return dateValue.equals(that.dateValue);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + fieldName.hashCode();
    result = 31 * result + tagName.hashCode();
    result = 31 * result + textValue.hashCode();
    result = 31 * result + numericValue.hashCode();
    result = 31 * result + dateValue.hashCode();
    return result;
  }
}

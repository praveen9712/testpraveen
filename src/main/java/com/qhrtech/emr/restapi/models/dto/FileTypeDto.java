
package com.qhrtech.emr.restapi.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "FileType data transfer object model")
public class FileTypeDto {

  @JsonProperty("id")
  @Schema(description = "Unique file type id", example = "3")
  private int typeId;

  @JsonProperty("type")
  @Schema(description = "File Type (extension)", example = "pdf")
  private String type;

  @JsonProperty("description")
  @Schema(description = "File type description", example = "PDF Document")
  private String description;

  /**
   * Unique File Type ID
   *
   * @documentationExample 3
   *
   * @return File Type ID
   */
  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  /**
   * File Type (extension)
   *
   * @documentationExample pdf
   *
   * @return File Type
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * File type description
   *
   * @documentationExample PDF Document
   *
   * @return File type description
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FileTypeDto)) {
      return false;
    }
    FileTypeDto that = (FileTypeDto) o;
    return getTypeId() == that.getTypeId()
        && Objects.equals(getType(), that.getType())
        && Objects.equals(getDescription(), that.getDescription());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTypeId(), getType(), getDescription());
  }
}


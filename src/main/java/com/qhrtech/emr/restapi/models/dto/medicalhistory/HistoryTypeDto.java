
package com.qhrtech.emr.restapi.models.dto.medicalhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import javax.ws.rs.core.Response;

/**
 * The history type data transfer object model.
 *
 * @see com.qhrtech.emr.accuro.model.medicalhistory.HistoryType
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "History type data transfer object model")
public class HistoryTypeDto {

  @JsonProperty("id")
  @Schema(description = "Id of the history type", example = "1")
  private int id;

  @JsonProperty("typeName")
  @Schema(description = "The history type name", example = "Diagnostic History")
  private String typeName;

  @JsonProperty("description")
  @Schema(description = "The history type description", example = "Diagnostic History")
  private String description;

  @JsonProperty("builtin")
  @Schema(description = "Flag which indicates if the item is built-in", example = "true")
  private boolean builtin;

  @JsonProperty("historyType")
  @Schema(description = "The history type",
      example = "REGULAR")
  private HistoryType historyType;

  @JsonProperty("requireTrackingDate")
  @Schema(description = "Flag which indicates if the history type requires a tracking date",
      example = "true")
  private boolean requireTrackingDate;

  @JsonProperty("alternateBackground")
  @Schema(description = "Flag which indicates if the history type is alternate background",
      example = "false")
  private boolean alternateBackground;

  @JsonProperty("imported")
  @Schema(description = "Flag which indicates if the history type is imported", example = "false")
  private boolean imported;

  @JsonProperty("showIfEmpty")
  @Schema(description = "Flag which indicates if the history type is shown when empty",
      example = "true")
  private boolean showIfEmpty;

  /**
   * A unique history type id.
   *
   * @documentationExample 1
   *
   * @return The history type id
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The history type name.
   *
   * @documentationExample Diagnostic History
   *
   * @return The history type name
   */
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  /**
   * The history type description.
   *
   * @documentationExample Diagnostic History
   *
   * @return The history type description
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * The flag indicating if the item is builtin.
   *
   * @documentationExample true
   *
   * @return {@code true} if builtin or {@code false} if not.
   */
  public boolean isBuiltin() {
    return builtin;
  }

  public void setBuiltin(boolean builtin) {
    this.builtin = builtin;
  }

  /**
   * The history type.
   *
   * @documentationExample REGULAR
   *
   * @return history type.
   */
  public HistoryType getHistoryType() {
    return historyType;
  }

  public void setHistoryType(HistoryType type) {
    this.historyType = type;
  }

  /**
   * The flag indicating if the history type requires a tracking date.
   *
   * @documentationExample true
   *
   * @return {@code true} if requires a tracking date or {@code false} if not.
   */
  public boolean isRequireTrackingDate() {
    return requireTrackingDate;
  }

  public void setRequireTrackingDate(boolean requireTrackingDate) {
    this.requireTrackingDate = requireTrackingDate;
  }

  /**
   * The flag indicating if the history type is alternate background.
   *
   * @documentationExample true
   *
   * @return {@code true} if alternate background or {@code false} if not.
   */
  public boolean isAlternateBackground() {
    return alternateBackground;
  }

  public void setAlternateBackground(boolean alternateBackground) {
    this.alternateBackground = alternateBackground;
  }

  /**
   * The flag indicating if the history type is imported.
   *
   * @documentationExample true
   *
   * @return {@code true} if imported or {@code false} if not.
   */
  public boolean isImported() {
    return imported;
  }

  public void setImported(boolean imported) {
    this.imported = imported;
  }

  /**
   * The flag indicating if the history type is shown when empty.
   *
   * @documentationExample true
   *
   * @return {@code true} if shown or {@code false} if not.
   */
  public boolean getShowIfEmpty() {
    return showIfEmpty;
  }

  public void setShowIfEmpty(boolean showIfEmpty) {
    this.showIfEmpty = showIfEmpty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof HistoryTypeDto)) {
      return false;
    }

    HistoryTypeDto that = (HistoryTypeDto) o;

    if (id != that.id) {
      return false;
    }
    if (builtin != that.builtin) {
      return false;
    }
    if (requireTrackingDate != that.requireTrackingDate) {
      return false;
    }
    if (alternateBackground != that.alternateBackground) {
      return false;
    }
    if (imported != that.imported) {
      return false;
    }
    if (showIfEmpty != that.showIfEmpty) {
      return false;
    }
    if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) {
      return false;
    }
    if (description != null ? !description.equals(that.description) : that.description != null) {
      return false;
    }
    return historyType == that.historyType;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (builtin ? 1 : 0);
    result = 31 * result + (historyType != null ? historyType.hashCode() : 0);
    result = 31 * result + (requireTrackingDate ? 1 : 0);
    result = 31 * result + (alternateBackground ? 1 : 0);
    result = 31 * result + (imported ? 1 : 0);
    result = 31 * result + (showIfEmpty ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "HistoryTypeDto{" + "typeId=" + id + ", typeName='" + typeName + '\''
        + ", description='"
        + description + '\'' + ", builtin=" + builtin + ", historyType=" + historyType
        + ", requireTrackingDate="
        + requireTrackingDate + ", alternateBackground=" + alternateBackground + ", imported="
        + imported
        + ", showIfEmpty=" + showIfEmpty + '}';
  }

  /**
   * The HistoryType enum. Determines different custom history types.
   *
   * <li>{@link #REGULAR}</li>
   * <li>{@link #FREE_TEXT}</li>
   * <li>{@link #URL}</li>
   * <li>{@link #TRACKING}</li>
   *
   */
  @Schema(description = "History type enum. Determines different custom history types")
  public enum HistoryType {
    /**
     * Patient History Regular Item
     */
    REGULAR("REGULAR"),
    /**
     * Patient History Text Item
     */
    FREE_TEXT("FREE_TEXT"),
    /**
     * Patient History URL Item
     */
    URL("URL"),
    /**
     * Patient History Tracking Item
     */
    TRACKING("TRACKING");

    private final String type;

    HistoryType(String type) {
      this.type = type;
    }

    public static HistoryType lookup(String type) {
      return Arrays.stream(values()).filter(e -> e.type.equals(type)).findFirst().orElse(null);
    }

    public static HistoryType fromString(String name) {
      return Arrays.stream(values()).filter(e -> e.name().equalsIgnoreCase(name)).findFirst()
          .orElseThrow(() -> Error.webApplicationException(Response.Status.BAD_REQUEST,
              "Invalid History Type: " + name));
    }
  }
}

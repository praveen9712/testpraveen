
package com.qhrtech.emr.restapi.models.dto.attachment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * Waitlist attachment data transfer object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Waitlist attachment data transfer object")
public class WaitlistAttachmentDto {

  @JsonProperty("id")
  @Schema(description = "The unique id of the waitlist attachment. This field is "
      + "not required during POST request.", example = "1")
  private int id;

  @JsonProperty("waitlistId")
  @Schema(description = "The unique id of the waitlist(read-only).", example = "1")
  private Integer waitlistId;

  @JsonProperty("itemId")
  @Schema(description = "The unique id of the item", example = "1")
  private int itemId;

  @JsonProperty("itemCategory")
  @Schema(description = "The category of the item. This field is ENUM and "
      + "can contain only restricted values.", example = "Letter")
  private ItemCategoryDto itemCategoryDto;

  /**
   * The unique id of the waitlist attachment.
   *
   * @documentationExample 1
   *
   * @return The waitlist id.
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * The unique id of the waitlist.
   *
   * @documentationExample 1
   *
   * @return The waitlist id.
   */
  public Integer getWaitlistId() {
    return waitlistId;
  }

  public void setWaitlistId(Integer waitlistId) {
    this.waitlistId = waitlistId;
  }

  /**
   * The unique id of the item.
   *
   * @documentationExample 1
   *
   * @return The item id.
   */
  public int getItemId() {
    return itemId;
  }

  public void setItemId(int itemId) {
    this.itemId = itemId;
  }

  /**
   * The category of the item.
   *
   * @documentationExample Letter
   *
   * @return The item category.
   */
  public ItemCategoryDto getItemCategoryDto() {
    return itemCategoryDto;
  }

  public void setItemCategoryDto(ItemCategoryDto itemCategoryDto) {
    this.itemCategoryDto = itemCategoryDto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof WaitlistAttachmentDto)) {
      return false;
    }

    WaitlistAttachmentDto that = (WaitlistAttachmentDto) o;

    if (getId() != that.getId()) {
      return false;
    }
    if (getItemId() != that.getItemId()) {
      return false;
    }
    if (!Objects.equals(getWaitlistId(), that.getWaitlistId())) {
      return false;
    }
    return getItemCategoryDto() == that.getItemCategoryDto();
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + (getWaitlistId() != null ? getWaitlistId().hashCode() : 0);
    result = 31 * result + getItemId();
    result = 31 * result + (getItemCategoryDto() != null ? getItemCategoryDto().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("WaitlistAttachmentDto{");
    sb.append("id=").append(id);
    sb.append(", waitlistId=").append(waitlistId);
    sb.append(", itemId=").append(itemId);
    sb.append(", itemCategory=").append(itemCategoryDto);
    sb.append('}');
    return sb.toString();
  }
}

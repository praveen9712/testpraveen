
package com.qhrtech.emr.restapi.models.dto.attachment;

public enum ItemCategoryDto {
  Letter(6),
  Document(7);

  private int categoryId;

  ItemCategoryDto(int categoryId) {
    this.categoryId = categoryId;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public static ItemCategoryDto lookup(int categoryId) {
    for (ItemCategoryDto category : values()) {
      if (category.getCategoryId() == categoryId) {
        return category;
      }
    }
    return null;
  }
}

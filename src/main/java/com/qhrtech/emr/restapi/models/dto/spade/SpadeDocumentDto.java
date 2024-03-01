
package com.qhrtech.emr.restapi.models.dto.spade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.qhrtech.emr.restapi.validators.CheckFutureDate;
import com.webcohesion.enunciate.metadata.DocumentationExample;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.joda.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpadeDocumentDto implements Serializable {

  private int documentId;
  private int folderId;
  private boolean deleted;
  private String folderName;
  @NotNull(message = "fileName cannot be null.")
  private String fileName;

  @NotNull(message = "mimeType cannot be null.")
  private String mimeType;

  @NotNull(message = "hashValue cannot be null.")
  private String hashValue;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @NotNull(message = "timeCreated cannot be null.")
  @CheckFutureDate
  private LocalDateTime timeCreated;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @NotNull(message = "timeModified cannot be null.")
  @CheckFutureDate
  private LocalDateTime timeModified;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @CheckFutureDate
  private LocalDateTime timeAccessed;

  private String originalPermissions;
  private String originalOwnership;
  private String originalPath;
  private String relativePath;
  private String executable;
  private String zoneId;
  private Long configClient;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @CheckFutureDate
  private LocalDateTime localTimeCreated;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @CheckFutureDate
  private LocalDateTime localTimeModified;

  @JsonSerialize(using = ToStringSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @CheckFutureDate
  private LocalDateTime localTimeAccess;

  /**
   * Document Id of the spade document. Not required during create request.
   *
   * @return document Id
   * @documentationExample 2
   */
  public int getDocumentId() {
    return this.documentId;
  }

  public void setDocumentId(int documentId) {
    this.documentId = documentId;
  }

  /**
   * Folder Id of the spade document. Required during create request.
   *
   * @return Folder Id
   * @documentationExample 2
   */

  public int getFolderId() {
    return this.folderId;
  }

  public void setFolderId(int folderId) {
    this.folderId = folderId;
  }

  /**
   * Boolean which tells if document is deleted or not.
   *
   * @return boolean
   * @documentationExample true
   */

  public boolean isDeleted() {
    return this.deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  /**
   * Name of the Spade folder in which document is saved.
   *
   * @return Folder Name
   * @documentationExample Spade Folder 1
   */
  public String getFolderName() {
    return this.folderName;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  /**
   * Name of the Spade documents
   *
   * @return Document Name
   * @documentationExample Spade Document 1
   */
  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Media type of the spade document
   *
   * @return Media type
   * @documentationExample application/pdf
   */
  public String getMimeType() {
    return this.mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * Hex value of the document Hash.
   *
   * @return Hex Value
   * @documentationExample 9a0fbe1cb99d581ecd7fb005e0ab28840059690737a7123630012a7c4b74ec9c92a3b
   *                       6a86e08bb8f208c6e8e4016fcc90f52d9cec0390698e2659e3b1ad671cb
   */
  public String getHashValue() {
    return this.hashValue;
  }

  public void setHashValue(String hashValue) {
    this.hashValue = hashValue;
  }

  /**
   * Time of the document created.
   *
   * @return Time Created
   * @documentationExample 2019-05-10T01:00:00.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTimeCreated() {
    return this.timeCreated;
  }

  public void setTimeCreated(LocalDateTime timeCreated) {
    this.timeCreated = timeCreated;
  }

  /**
   * Time of the document modified.
   *
   * @return Time modified
   * @documentationExample 2019-05-10T01:00:00.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTimeModified() {
    return this.timeModified;
  }

  public void setTimeModified(LocalDateTime timeModified) {
    this.timeModified = timeModified;
  }

  /**
   * Time of the document accessed.
   *
   * @return Time accessed
   * @documentationExample 2019-05-10T01:00:00.000
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getTimeAccessed() {
    return this.timeAccessed;
  }

  public void setTimeAccessed(LocalDateTime timeAccessed) {
    this.timeAccessed = timeAccessed;
  }

  /**
   * Document permissions.
   *
   * @return Permissions of document
   * @documentationExample confidential
   */
  public String getOriginalPermissions() {
    return this.originalPermissions;
  }

  public void setOriginalPermissions(String originalPermissions) {
    this.originalPermissions = originalPermissions;
  }

  /**
   * Owner of the document
   *
   * @return Owner of the document
   * @documentationExample firstName.lastName
   */
  public String getOriginalOwnership() {
    return this.originalOwnership;
  }

  /**
   * Document ownership.
   *
   * @return Permissions of document
   * @documentationExample Read
   */
  public void setOriginalOwnership(String originalOwnership) {
    this.originalOwnership = originalOwnership;
  }

  /**
   * Path of the document
   *
   * @return Path of the document
   * @documentationExample S:\folderName
   */
  public String getOriginalPath() {
    return this.originalPath;
  }

  public void setOriginalPath(String originalPath) {
    this.originalPath = originalPath;
  }

  /**
   * The relative path of the document
   *
   * @return The relativePath
   * @documentationExample S:\folderName
   */
  public String getRelativePath() {
    return relativePath;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  /**
   * The Executable of the document
   *
   * @return The executable
   * @documentationExample exe
   */
  public String getExecutable() {
    return executable;
  }

  public void setExecutable(String executable) {
    this.executable = executable;
  }

  /**
   * The Zone id
   *
   * @return The zoneId
   * @documentationExample 131
   */
  public String getZoneId() {
    return zoneId;
  }

  public void setZoneId(String zoneId) {
    this.zoneId = zoneId;
  }

  /**
   * The config client
   *
   * @return 12
   */
  public Long getConfigClient() {
    return configClient;
  }

  public void setConfigClient(Long configClient) {
    this.configClient = configClient;
  }

  /**
   * The Created date time of the document
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLocalTimeCreated() {
    return localTimeCreated;
  }

  public void setLocalTimeCreated(LocalDateTime localTimeCreated) {
    this.localTimeCreated = localTimeCreated;
  }

  /**
   * The Last modified date time of the document
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLocalTimeModified() {
    return localTimeModified;
  }

  public void setLocalTimeModified(LocalDateTime localTimeModified) {
    this.localTimeModified = localTimeModified;
  }

  /**
   * The Access date time of the document
   *
   * @return The Datetime
   */
  @DocumentationExample("2020-02-10T00:00:00.000")
  @TypeHint(String.class)
  public LocalDateTime getLocalTimeAccess() {
    return localTimeAccess;
  }

  public void setLocalTimeAccess(LocalDateTime localTimeAccess) {
    this.localTimeAccess = localTimeAccess;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SpadeDocumentDto that = (SpadeDocumentDto) o;

    if (documentId != that.documentId) {
      return false;
    }
    if (folderId != that.folderId) {
      return false;
    }
    if (deleted != that.deleted) {
      return false;
    }
    if (!Objects.equals(folderName, that.folderName)) {
      return false;
    }
    if (!Objects.equals(fileName, that.fileName)) {
      return false;
    }
    if (!Objects.equals(mimeType, that.mimeType)) {
      return false;
    }
    if (!Objects.equals(hashValue, that.hashValue)) {
      return false;
    }
    if (!Objects.equals(timeCreated, that.timeCreated)) {
      return false;
    }
    if (!Objects.equals(timeModified, that.timeModified)) {
      return false;
    }
    if (!Objects.equals(timeAccessed, that.timeAccessed)) {
      return false;
    }
    if (!Objects.equals(originalPermissions, that.originalPermissions)) {
      return false;
    }
    if (!Objects.equals(originalOwnership, that.originalOwnership)) {
      return false;
    }
    if (!Objects.equals(originalPath, that.originalPath)) {
      return false;
    }
    if (!Objects.equals(relativePath, that.relativePath)) {
      return false;
    }
    if (!Objects.equals(executable, that.executable)) {
      return false;
    }
    if (!Objects.equals(zoneId, that.zoneId)) {
      return false;
    }
    if (!Objects.equals(configClient, that.configClient)) {
      return false;
    }
    if (!Objects.equals(localTimeCreated, that.localTimeCreated)) {
      return false;
    }
    if (!Objects.equals(localTimeModified, that.localTimeModified)) {
      return false;
    }
    return Objects.equals(localTimeAccess, that.localTimeAccess);
  }

  @Override
  public int hashCode() {
    int result = documentId;
    result = 31 * result + folderId;
    result = 31 * result + (deleted ? 1 : 0);
    result = 31 * result + (folderName != null ? folderName.hashCode() : 0);
    result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
    result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
    result = 31 * result + (hashValue != null ? hashValue.hashCode() : 0);
    result = 31 * result + (timeCreated != null ? timeCreated.hashCode() : 0);
    result = 31 * result + (timeModified != null ? timeModified.hashCode() : 0);
    result = 31 * result + (timeAccessed != null ? timeAccessed.hashCode() : 0);
    result = 31 * result + (originalPermissions != null ? originalPermissions.hashCode() : 0);
    result = 31 * result + (originalOwnership != null ? originalOwnership.hashCode() : 0);
    result = 31 * result + (originalPath != null ? originalPath.hashCode() : 0);
    result = 31 * result + (relativePath != null ? relativePath.hashCode() : 0);
    result = 31 * result + (executable != null ? executable.hashCode() : 0);
    result = 31 * result + (zoneId != null ? zoneId.hashCode() : 0);
    result = 31 * result + (configClient != null ? configClient.hashCode() : 0);
    result = 31 * result + (localTimeCreated != null ? localTimeCreated.hashCode() : 0);
    result = 31 * result + (localTimeModified != null ? localTimeModified.hashCode() : 0);
    result = 31 * result + (localTimeAccess != null ? localTimeAccess.hashCode() : 0);
    return result;
  }
}

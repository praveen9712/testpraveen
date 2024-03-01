
package com.qhrtech.emr.restapi.models.endpoints;

import com.webcohesion.enunciate.metadata.Facet;

/**
 * Representation of a JSON patch operation. Read this
 * <a href="https://tools.ietf.org/html/rfc6902">Patch RFC</a> reference for more information.
 *
 * @author kevin.kendall
 */
@Facet("internal")
public class PatchOperation {

  private Type op;
  private String path;
  private String from;
  private String value;

  /**
   * Operation Type
   *
   * @return The operation type Enum
   *
   * @documentationExample add
   */
  public Type getOp() {
    return op;
  }

  public void setOp(Type op) {
    this.op = op;
  }

  /**
   * Resource path
   *
   * @return The resource path
   *
   * @documentationExample /dir/path
   */
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  /**
   * Originating resource path
   *
   * @return The originating resource path
   *
   * @documentationExample /source/path
   */
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * Value
   *
   * @return The patch value
   *
   * @documentationExample foo
   */
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Type of patch operation
   */
  public enum Type {
    /**
     * <a href="https://tools.ietf.org/html/rfc6902#section-4.1">Add</a>
     */
    add,
    /**
     * <a href="https://tools.ietf.org/html/rfc6902#section-4.2">Remove</a>
     */
    remove,
    /**
     * <a href="https://tools.ietf.org/html/rfc6902#section-4.3">Replace</a>
     */
    replace,
    /**
     * <a href="https://tools.ietf.org/html/rfc6902#section-4.4">Move</a>
     */
    move,
    /**
     * <a href="https://tools.ietf.org/html/rfc6902#section-4.5">Copy</a>
     */
    copy,
    /**
     * <a href="https://tools.ietf.org/html/rfc6902#section-4.6">Test</a>
     */
    test
  }

}


package com.qhrtech.emr.restapi.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bryan.bergen
 */
public class MimeTypeUtils {

  public static final String MIME_APPLICATION_OCTET_STREAM = "application/octet-stream";
  public static final String MIME_APPLICATION_MSWORD = "application/msword";
  public static final String MIME_TEXT_PLAIN = "text/plain";
  public static final String MIME_APPLICATION_VND_MSEXCEL = "application/vnd.ms-excel";
  public static final String MIME_IMAGE_JPEG = "image/jpeg";
  public static final String MIME_IMAGE_PNG = "image/png";
  public static final String MIME_TEXT_HTML = "text/html";
  public static final String MIME_IMAGE_GIF = "image/gif";
  public static final String MIME_APPLICATION_PDF = "application/pdf";
  private static final Map<String, String> mappings;

  static {
    mappings = new HashMap<>();
    mappings.put("doc", MIME_APPLICATION_MSWORD);
    mappings.put("txt", MIME_TEXT_PLAIN);
    mappings.put("xls", MIME_APPLICATION_VND_MSEXCEL);
    mappings.put("jpg", MIME_IMAGE_JPEG);
    mappings.put("jpeg", MIME_IMAGE_JPEG);
    mappings.put("png", MIME_IMAGE_PNG);
    mappings.put("gif", MIME_IMAGE_GIF);
    mappings.put("pdf", MIME_APPLICATION_PDF);
    mappings.put("html", MIME_TEXT_HTML);
    mappings.put("htm", MIME_TEXT_HTML);
  }

  /**
   * Returns common mappings of file extentions to Mime type.
   *
   * This list is intentionally non-exhaustive, and aligned with the file types typically stored in
   * the Accuro database.
   *
   * @param extension The extension to look up, without the delimiter.
   *
   * @return Mime type associated with the extension, or application/octet-stream if no association
   *         is found.
   *
   * @throws NullPointerException If {@code extension} is null
   */
  public static String getMediaType(String extension) {
    return mappings.getOrDefault(extension.trim().toLowerCase(), MIME_APPLICATION_OCTET_STREAM);
  }

}

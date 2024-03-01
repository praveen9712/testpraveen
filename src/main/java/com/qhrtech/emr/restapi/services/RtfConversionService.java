
package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.restapi.services.exceptions.RtfConversionException;

/**
 * Service for dealing with RTF formatted data.
 *
 * @author David.Huang
 */
public interface RtfConversionService {

  /**
   * Convert RTF formatted data into plain text.
   *
   * @param rtfText RTF formatted data
   *
   * @return Plain text.
   *
   * @throws RtfConversionException If there is an error converting the RTF string to plain text.
   * @throws NullPointerException If rtfText is null.
   */
  String getPainText(String rtfText) throws RtfConversionException;

  /**
   * Converts string content into RTF formatted binary data.
   *
   * @param content String content
   *
   * @return Byte array of styled content.
   *
   * @throws RtfConversionException If there is an error converting the RTF string to styled bytes.
   */
  byte[] getStyledContentBytes(String content) throws RtfConversionException;

}


package com.qhrtech.emr.restapi.util;

import com.qhrtech.emr.restapi.services.exceptions.MD5Exception;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomMD5 extends CheckSumFile {

  public static final String ABCDEF = "0123456789abcdef";
  private static char[] HEX = ABCDEF.toCharArray();
  private MessageDigest md = null;

  public CustomMD5(String arg) throws MD5Exception {
    length = 0;
    filename = null;
    try {
      md = MessageDigest.getInstance(arg);
    } catch (NoSuchAlgorithmException e) {

      throw new MD5Exception("MD5 encryption exception", e);

    }
  }

  private static String format(byte[] bytes, boolean uppercase) {
    if (bytes == null) {
      return "";
    }
    StringBuffer sb = new StringBuffer(bytes.length * 2);
    int b;
    for (int i = 0; i < bytes.length; i++) {
      b = bytes[i] & 0xFF;
      sb.append(HEX[b >>> 4]);
      sb.append(HEX[b & 0x0F]);
    }
    return (uppercase ? sb.toString().toUpperCase() : sb.toString());
  }

  @Override
  public void reset() {
    md.reset();
    length = 0;
  }

  @Override
  public void update(byte[] buffer, int offset, int len) {
    md.update(buffer, offset, len);
    length += len;
  }

  @Override
  public String toString() {
    return getHexValue();
  }

  public String getHexValue() {
    return format(md.digest(), uppercase);
  }


}

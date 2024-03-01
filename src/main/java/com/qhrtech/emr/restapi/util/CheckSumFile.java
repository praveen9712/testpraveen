
package com.qhrtech.emr.restapi.util;

import java.util.zip.Checksum;

public class CheckSumFile implements Checksum {


  protected long value;
  protected long length;
  protected String separator;
  protected String filename;
  protected boolean hex; // output in hex?
  protected boolean uppercase; // hex in uppercase?


  public CheckSumFile() {
    value = 0;
    length = 0;
    separator = "\t";
    filename = null;
    hex = false;
    uppercase = false;
  }

  public void update(byte[] bytes, int offset, int length) {
    for (int i = offset; i < length; i++) {
      update(bytes[i]);
    }
  }

  public void update(int b) {
    length++;
  }

  public void update(byte b) {
    update((int) (b & 0xFF));
  }

  public void reset() {
    value = 0;
    length = 0;
  }

  public long getValue() {
    return value;
  }


  public void readByteArray(byte[] data) {
    update(data, 0, data.length);
  }
}

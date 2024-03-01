/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.security.db;

import com.optimedirect.cypher.DecryptionEngine;
import com.optimedirect.cypher.OSCCypher;
import java.util.Arrays;

/**
 * This class is a simplification of the com.optimedirect.security.Cypher class that can be found in
 * accuro-core.
 *
 * @author kevin.kendall
 */
public class CypherUtil {

  private static final DecryptionEngine ENGINE = new DecryptionEngine();
  private static final byte[] FULL_KEY;

  static {
    OSCCypher cipher = new OSCCypher();
    String key = cipher.decrypt(
        "006b16321f8b415eb043e00a86ea66f87b3a1ff1c8c1bb8fcb9bc8136849569a29aff90dab4c9e64cd3"
            + "59e5426bfd255196d2fa8365895f9cbfbafab8868732f1e74b9db3b2a0309c18499b1b15a4d1d3d1"
            + "43dac0e9eb39ee8ec943f361230d2968a6d65779e1ed07e2efbb9e064621ca10e883b228aa8032d9"
            + "7e5fdaa6ba3f1c6");
    FULL_KEY = key.getBytes();
  }

  public static byte[] decrypt(byte[] bytes) throws Exception {
    int offset = bytes[bytes.length - 1];
    byte[] key = Arrays.copyOfRange(FULL_KEY, offset, offset + 24);
    byte[] data = Arrays.copyOf(bytes, bytes.length - 1);
    return ENGINE.decrypt(key, data);
  }

  public static byte[] decrypt(String encrypted) throws Exception {
    String[] split = encrypted.split("\\|");
    byte[] bytes = new byte[split.length];
    for (int i = 0; i < split.length; i++) {
      bytes[i] = Byte.parseByte(split[i]);
    }
    return decrypt(bytes);
  }

  public static String decryptToString(String encrypted) throws Exception {
    return new String(decrypt(encrypted));
  }
}

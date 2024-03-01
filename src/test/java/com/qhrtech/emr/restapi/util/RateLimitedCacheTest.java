
package com.qhrtech.emr.restapi.util;

import com.qhrtech.util.RandomUtils;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;
import junit.framework.TestCase;

public class RateLimitedCacheTest extends TestCase {

  private RateLimitedCache<String, Boolean> serviceProxy;
  private Function<String, Boolean> mockService;
  private Duration ttl = Duration.ofMillis(2000);

  public void testGetServiceThrowsRuntime() {
    String key = RandomUtils.getString(10);
    RuntimeException expected = new RuntimeException("");
    mockService = input -> {
      throw expected;
    };
    serviceProxy = new RateLimitedCache(mockService, ttl);

    try {
      serviceProxy.get(key);
    } catch (RuntimeException actual) {
      assertEquals(expected, actual);
      return;
    }
    fail("An exception was expected to be thrown");
  }

  public void testGetServiceReturnsNull() {
    String key = RandomUtils.getString(10);
    mockService = input -> null;
    serviceProxy = new RateLimitedCache(mockService, ttl);

    Optional<Boolean> result = serviceProxy.get(key);
    assertNotNull(result);
    assertFalse(result.isPresent());
  }

  public void testGetServiceReturnsValue() {
    String key = RandomUtils.getString(10);
    mockService = input -> input == key;
    serviceProxy = new RateLimitedCache(mockService, ttl);

    Optional<Boolean> result = serviceProxy.get(key);
    assertTrue(result.isPresent());
    assertTrue(result.get());
  }

  public void testUpdate() {
    String key = RandomUtils.getString(10);
    mockService = input -> input == key;
    serviceProxy = new RateLimitedCache(mockService, ttl);

    boolean updated = serviceProxy.update(key);
    assertTrue(updated);
    updated = serviceProxy.update(key);
    assertFalse(updated);
    updated = serviceProxy.update("not" + key);
    assertTrue(updated);
    updated = serviceProxy.update("not" + key);
    assertFalse(updated);
  }

  public void testUpdateZeroTtl() {
    String key = RandomUtils.getString(10);
    mockService = input -> input == key;
    serviceProxy = new RateLimitedCache(mockService, Duration.ZERO);

    boolean updated = serviceProxy.update(key);
    assertTrue(updated);
    updated = serviceProxy.update(key);
    assertTrue(updated);
  }

  public void testUpdateNullResult() {
    String key = RandomUtils.getString(10);
    mockService = input -> null;
    serviceProxy = new RateLimitedCache(mockService, ttl);

    boolean updated = serviceProxy.update(key);
    // null result should also be cached
    assertTrue(updated);
    updated = serviceProxy.update(key);
    assertFalse(updated);
  }
}

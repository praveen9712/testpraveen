
package com.qhrtech.emr.restapi.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Cache that rate limits access to its underlying resource.
 */
public class RateLimitedCache<K, T> {

  private final Map<K, CachedItem<T>> cache = new ConcurrentHashMap<>();
  private final Duration ttl;
  private final Function<K, CachedItem<T>> wrappedService;

  public RateLimitedCache(Function<K, T> underlyingService, Duration ttl) {
    this.ttl = ttl;
    this.wrappedService = underlyingService.andThen(CachedItem::of);
  }

  /**
   * Retrieve an item from the cache. If the item does not exist attempt to retrieve it from the
   * underlying service and cache the result.
   *
   * @param key - the key to get.
   * @return the value
   */
  public Optional<T> get(K key) {
    return cache.computeIfAbsent(key, wrappedService).getItem();
  }

  /**
   * Update an item in the cache using the underlying datasource. If update has been called for the
   * given key within the rate limit set, no update is performed.
   *
   * @param key - the key to update.
   * @return true if the underlying datasource was called. False otherwise.
   */
  public boolean update(K key) {
    CachedItem<T> existingItem = cache.get(key);
    if (existingItem == null) {
      cache.put(key, wrappedService.apply(key));
      return true;
    }
    Instant expiryTime = existingItem.getCachedTime().plusMillis(ttl.toMillis());
    if (Instant.now().isBefore(expiryTime)) {
      return false;
    }
    cache.put(key, wrappedService.apply(key));
    return true;
  }

  protected static final class CachedItem<T> {
    private final Optional<T> item;
    private final Instant cachedTime;

    private CachedItem(T item) {
      this.item = Optional.ofNullable(item);
      this.cachedTime = Instant.now();
    }

    protected static <T> CachedItem<T> of(T value) {
      return new CachedItem<>(value);
    }

    protected Instant getCachedTime() {
      return cachedTime;
    }

    protected Optional<T> getItem() {
      return item;
    }
  }
}

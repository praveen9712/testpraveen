
package com.qhrtech.emr.restapi.security.datasource;

/**
 * Service for caching values.
 *
 * @author james.michaud
 *
 */
public interface CachingService<T> {

  /**
   * Retrieve an item from the cache.
   *
   * @param key - the key for which to get.
   *
   * @return the value if it exists in the cache. Null otherwise.
   *
   */
  T get(String key);

  /**
   * Insert an item into the cache.
   *
   * @param key - the key to assign to the value.
   * @param key - the value to cache.
   *
   */
  void put(String key, T value);

  /**
   * Evict an item from the cache.
   *
   * @param key - the key for which to evict.
   *
   */
  void evict(String key);
}

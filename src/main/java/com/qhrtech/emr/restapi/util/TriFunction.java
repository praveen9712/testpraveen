
package com.qhrtech.emr.restapi.util;

/**
 * Represents a function that accepts three arguments and produces a result.
 * 
 * @param <T>
 * @param <U>
 * @param <W>
 * @param <R>
 */
@FunctionalInterface
public interface TriFunction<T, U, W, R> {

  /**
   * Applies this function to the given argument.
   *
   * @param t the function argument
   * @param u the function argument
   * @param w the function argument
   * @return the function result
   */
  R apply(T t, U u, W w);
}

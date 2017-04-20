package org.nryotaro.edgar.service;


import org.springframework.stereotype.Component;

/**
 * A simple org.nryotaro.amanatsu.service that can increment a number.
 */
@Component
public class CountingService {
  /**
   * Increment the given number by one.
   */
  public int increment(int count) {
    return count + 1;
  }
}

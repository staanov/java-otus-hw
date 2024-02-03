package io.github.staanov;

import io.github.staanov.annotations.After;
import io.github.staanov.annotations.Before;

public class Test {

  @Before
  public void setUp() {
    System.out.println("Before test annotation");
  }

  @After
  public void tearDown() {
    System.out.println("After test annotation");
  }

  @io.github.staanov.annotations.Test
  public void testOne() {
    System.out.println("Test 1 passed");
  }

  @io.github.staanov.annotations.Test
  public void testTwo() {
    throw new RuntimeException("Failed Test 2");
  }

  @io.github.staanov.annotations.Test
  public void testThree() {
    System.out.println("Test 3 passed");
  }
}

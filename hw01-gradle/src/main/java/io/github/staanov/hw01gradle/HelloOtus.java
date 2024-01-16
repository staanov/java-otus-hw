package io.github.staanov.hw01gradle;

import com.google.common.collect.Lists;

import java.util.List;

public class HelloOtus {
  public static void printReversedNames() {
    List<String> names = Lists.newArrayList("Alice", "Bob", "Charlie");
    System.out.println(Lists.reverse(names));
  }
}

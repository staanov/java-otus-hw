package io.github.staanov;

import io.github.staanov.proxy.Ioc;
import io.github.staanov.proxy.TestLoggingInterface;

public class Main {
  public static void main(String[] args) {
    TestLoggingInterface testLogging = Ioc.createTestLogging();

    testLogging.calculation(1);
    testLogging.calculation(10, 20);
    testLogging.calculation(100, 200, "three hundred");
  }
}

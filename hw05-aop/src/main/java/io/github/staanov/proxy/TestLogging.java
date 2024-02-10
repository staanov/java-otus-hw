package io.github.staanov.proxy;

import io.github.staanov.annotation.Log;

public class TestLogging implements TestLoggingInterface {
  @Log
  @Override
  public void calculation(int param1) {
    System.out.println(param1);
  }

  @Log
  @Override
  public void calculation(int param1, int param2) {
    System.out.printf("%d + %d%n", param1, param2);
  }

  @Log
  @Override
  public void calculation(int param1, int param2, String param3) {
    System.out.printf("%d + %d + %s%n", param1, param2, param3);
  }
}

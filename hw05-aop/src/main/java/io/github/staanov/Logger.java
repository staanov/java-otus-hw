package io.github.staanov;

import java.util.Arrays;

public class Logger {

  private static final String LOG_MESSAGE = "executed method: %s, param: %s";

  private Logger() {
  }

  public static void log(String name, Object[] args) {
    System.out.printf(LOG_MESSAGE + "%n", name, Arrays.toString(args));
  }
}

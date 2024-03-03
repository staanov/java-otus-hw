package ru.otus.processor.homework;

import java.time.LocalDateTime;

public class TimeProviderImpl implements TimeProvider {
  @Override
  public int getSeconds() {
    return LocalDateTime.now().getSecond();
  }
}

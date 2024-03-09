package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class EvenSecondExceptionProcessor implements Processor {

  private final TimeProvider time;

  public EvenSecondExceptionProcessor(TimeProvider time) {
    this.time = time;
  }

  @Override
  public Message process(Message message) {
    if (isSecondEven(time.getSeconds())) {
      throw new EvenSecondException();
    }
    return message;
  }

  private boolean isSecondEven(int second) {
    return second % 2 == 0;
  }
}

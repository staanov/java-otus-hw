package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExecutorsMain {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorsMain.class);
  private static final int MIN = 1;
  private static final int MAX = 10;
  private static final String FIRST_THREAD_NAME = "Поток 1";
  private static final String SECOND_THREAD_NAME = "Поток 2";
  private static final Lock LOCK = new ReentrantLock();
  private static final Condition TRY_AGAIN = LOCK.newCondition();

  private static boolean next;

  private int counter = 0;
  private final String currentThreadName;

  public static void main(String[] args) {
    new Thread(() -> new ExecutorsMain().task(), FIRST_THREAD_NAME).start();
    new Thread(() -> new ExecutorsMain().task(), SECOND_THREAD_NAME).start();
  }

  public ExecutorsMain() {
    this.currentThreadName = Thread.currentThread().getName();
  }

  private void task() {
    while (counter < MAX) {
      lock();
      inc();
      unlock();
    }
    while (counter > MIN) {
      lock();
      dec();
      unlock();
    }
  }

  private void lock() {
    LOCK.lock();
    orderThreads();
  }

  private void inc() {
    counter++;
    LOGGER.info("%s: %s".formatted(Thread.currentThread().getName(), counter));
  }

  private void dec() {
    counter--;
    LOGGER.info("%s: %s".formatted(Thread.currentThread().getName(), counter));
  }

  private void unlock() {
    try {
      TRY_AGAIN.signalAll();
    } finally {
      LOCK.unlock();
    }
  }

  private void orderThreads() {
    if (isFirst()) {
      next = true;
    } else if (isNext()) {
      next = false;
    } else {
      await();
      orderThreads();
    }
  }

  private boolean isFirst() {
    return currentThreadName.equals(FIRST_THREAD_NAME) && !next;
  }

  private boolean isNext() {
    return !currentThreadName.equals(FIRST_THREAD_NAME) && next;
  }

  private void await() {
    try {
      TRY_AGAIN.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}

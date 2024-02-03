package io.github.staanov;

import io.github.staanov.annotations.Before;
import io.github.staanov.annotations.Test;
import io.github.staanov.annotations.After;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class Runner {
  private int executed;
  private int passed;
  private int failed;

  public void run(String className) throws ClassNotFoundException {
    Class<?> clazz = Class.forName(className);
    Method[] methods = clazz.getMethods();

    Method before = RunnerHelper.getMethodByAnnotation(methods, Before.class);
    Method[] tests = RunnerHelper.getAnnotatedMethods(methods, Test.class);
    Method after = RunnerHelper.getMethodByAnnotation(methods, After.class);

    for (Method test : tests) {
      execute(getInstance(clazz), before, test, after);
    }

    RunnerHelper.printResults(executed, passed, failed);
  }

  private <T> T getInstance(Class<T> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  private <T> void execute(T obj, Method before, Method test, Method after) {
    try {
      if (Objects.nonNull(before)) {
        invokeSimple(before, obj);
      }
      invokeWithCounter(test, obj);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (Objects.nonNull(after)) {
        invokeSimple(after, obj);
      }
      executed++;
    }
  }

  private <T> void invokeSimple(Method method, T obj) {
    try {
      method.invoke(obj);
    } catch (IllegalAccessException | InvocationTargetException e) {
      System.out.println(e.getCause());
    }
  }

  private <T> void invokeWithCounter(Method method, T obj) {
    try {
      method.invoke(obj);
      passed++;
    } catch (IllegalAccessException | InvocationTargetException e) {
      System.out.println(e.getCause());
      failed++;
    }
  }

}

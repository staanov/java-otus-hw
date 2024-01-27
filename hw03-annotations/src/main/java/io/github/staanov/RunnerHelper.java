package io.github.staanov;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class RunnerHelper {

  private static final String TESTS_FINISHED = "All tests execution is finished";
  private static final String TEST_RESULTS = "Executed: %d. Passed: %d. Failed: %d.";

  public static Method[] getAnnotatedMethods(Method[] methods, Class<? extends Annotation> annotation) {
    return Arrays.stream(methods).filter(method -> method.isAnnotationPresent(annotation)).toArray(Method[]::new);
  }

  public static Method getMethodByAnnotation(Method[] methods, Class<? extends Annotation> annotation) {
    var filteredMethods = getAnnotatedMethods(methods, annotation);
    return Arrays.stream(filteredMethods).findFirst().orElse(null);
  }

  public static void printResults(int executed, int passed, int failed) {
    System.out.println("---------------------------");
    System.out.println(TESTS_FINISHED);
    System.out.printf((TEST_RESULTS) + "%n", executed, passed, failed);
  }
}

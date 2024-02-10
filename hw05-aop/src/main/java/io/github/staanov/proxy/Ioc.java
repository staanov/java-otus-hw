package io.github.staanov.proxy;

import io.github.staanov.Logger;
import io.github.staanov.MethodSignature;
import io.github.staanov.annotation.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Ioc {

  public Ioc() {
  }

  public static TestLoggingInterface createTestLogging() {
    var interfaces = TestLogging.class.getInterfaces();
    var handler = new TestInvocationHandler(new TestLogging(), Log.class);
    return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(), interfaces, handler);
  }

  static class TestInvocationHandler implements InvocationHandler {

    private final TestLoggingInterface testLogging;
    private final Set<MethodSignature> annotatedMethods;

    public TestInvocationHandler(TestLoggingInterface testLogging, Class<? extends Annotation> annotation) {
      this.testLogging = testLogging;
      this.annotatedMethods = getAnnotatedMethods(testLogging, annotation);
    }

    private Set<MethodSignature> getAnnotatedMethods(TestLoggingInterface testLogging, Class<? extends Annotation> annotation) {
      return Arrays.stream(testLogging.getClass().getMethods())
          .filter(method -> method.isAnnotationPresent(annotation))
          .map(MethodSignature::new)
          .collect(Collectors.toSet());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (isMethodAnnotated(method)) {
        return invokeWithLog(testLogging, method, args);
      }
      return method.invoke(testLogging, args);
    }

    private boolean isMethodAnnotated(Method method) {
      var methodSignature = new MethodSignature(method);
      return annotatedMethods.contains(methodSignature);
    }

    private Object invokeWithLog(TestLoggingInterface testLogging, Method method, Object[] args) throws  Throwable {
      Logger.log(method.getName(), args);
      return method.invoke(testLogging, args);
    }
  }
}

package io.github.staanov;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class MethodSignature {

  private final String name;
  private final Class<?>[] parameterTypes;
  private final Class<?> returnType;

  public MethodSignature(Method method) {
    this.name = method.getName();
    this.parameterTypes = method.getParameterTypes();
    this.returnType = method.getReturnType();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MethodSignature that = (MethodSignature) o;
    return Objects.equals(name, that.name) && Arrays.equals(parameterTypes, that.parameterTypes) && Objects.equals(returnType, that.returnType);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(name, returnType);
    result = 31 * result + Arrays.hashCode(parameterTypes);
    return result;
  }
}

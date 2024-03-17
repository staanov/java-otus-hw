package ru.otus.jdbc.mapper;

import ru.otus.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

  private final Class<T> clazz;

  public EntityClassMetaDataImpl(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public String getName() {
    return clazz.getSimpleName();
  }

  @Override
  public Constructor<T> getConstructor() {
    try {
      return clazz.getDeclaredConstructor();
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Field getIdField() {
    return Stream.of(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst().orElseThrow();
  }

  @Override
  public List<Field> getAllFields() {
    return List.of(clazz.getDeclaredFields());
  }

  @Override
  public List<Field> getFieldsWithoutId() {
    return Stream.of(clazz.getDeclaredFields()).filter(field -> !field.isAnnotationPresent(Id.class)).toList();
  }
}

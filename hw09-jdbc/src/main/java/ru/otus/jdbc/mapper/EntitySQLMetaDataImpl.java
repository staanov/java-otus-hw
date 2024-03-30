package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Client;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

  private static final String SELECT_ALL = "SELECT * FROM %s";
  private static final String SELECT_BY_ID = "SELECT * FROM %s WHERE %s = ?";
  private static final String INSERT = "INSERT INTO %s (%s) VALUES (%s)";
  private static final String UPDATE = "UPDATE %s SET %s WHERE %s = ?";

  private static final String INSERT_VALUE = "?";
  private static final String UPDATE_FIELD = "%s = ?";

  private final String className;
  private final String idFieldName;
  private final List<Field> fieldsWithoutId;

  public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
    this.className = entityClassMetaData.getName();
    this.idFieldName = entityClassMetaData.getIdField().getName();
    this.fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
  }

  @Override
  public String getSelectAllSql() {
    return SELECT_ALL.formatted(className);
  }

  @Override
  public String getSelectByIdSql() {
    return SELECT_BY_ID.formatted(className, idFieldName);
  }

  @Override
  public String getInsertSql() {
    var insertFields = fieldsWithoutId.stream().map(Field::getName).collect(Collectors.joining(","));
    var insertValues = fieldsWithoutId.stream().map(field -> INSERT_VALUE).collect(Collectors.joining(","));
    return INSERT.formatted(className, insertFields, insertValues);
  }

  @Override
  public String getUpdateSql() {
    var updateFields = fieldsWithoutId.stream().map(field -> UPDATE_FIELD.formatted(field.getName())).collect(Collectors.joining(","));
    return UPDATE.formatted(className, updateFields, idFieldName);
  }
}

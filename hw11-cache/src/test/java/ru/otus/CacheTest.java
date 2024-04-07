package ru.otus;

import ru.otus.base.TestContainersConfig;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientCachedImpl;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.DataTemplateJdbc;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaDataImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@DisplayName("Кэш должен ")
public class CacheTest {
  private static TestContainersConfig.CustomPostgreSQLContainer CONTAINER;
  private final String dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
  private final String dbUserName = System.getProperty("app.datasource.demo-db.username");
  private final String dbPassword = System.getProperty("app.datasource.demo-db.password");

  private MyCache<Long, Client> myCache;
  private DBServiceClient serviceClient;
  private DBServiceClient cachedServiceClient;

  @BeforeAll
  public static void init() {
    CONTAINER = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
    CONTAINER.start();
  }

  @AfterAll
  public static void shutdown() {
    CONTAINER.stop();
  }

  @BeforeEach
  void setUp() {
    var dataSource = new DriverManagerDataSource(dbUrl, dbUserName, dbPassword);
    flywayMigrations(dataSource);
    var transactionRunner = new TransactionRunnerJdbc(dataSource);
    var dbExecutor = new DbExecutorImpl();

    EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
    EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
    var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);

    myCache = new MyCache<>();
    serviceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
    cachedServiceClient = new DbServiceClientCachedImpl(serviceClient, myCache);
    IntStream.range(0, 100).forEach(id -> cachedServiceClient.saveClient(new Client("" + id)));
  }

  @AfterEach
  void tearDown() throws SQLException {
    var dataSource = new DriverManagerDataSource(dbUrl, dbUserName, dbPassword);
    try(Connection connection = dataSource.getConnection()) {
      connection.createStatement().executeUpdate("DELETE FROM client");
      connection.commit();
    }
  }

  @DisplayName("отдавать данные быстрее, чем БД")
  @Test
  void testThatCacheIsFasterThanDb() {
    var ids = serviceClient.findAll().stream().map(Client::getId).toList();

    var dbReadStart = Instant.now();
    var dbClients = getClientsById(ids, serviceClient);
    var dbTime = Duration.between(dbReadStart, Instant.now());
    var dbTimeDividedBy100Times = dbTime.dividedBy(100L); // значение получено опытным путем

    var cacheReadStart = Instant.now();
    var cachedClients = getClientsById(ids, cachedServiceClient);
    var cachedTime = Duration.between(cacheReadStart, Instant.now());

    Assertions.assertThat(cachedClients).hasSameSizeAs(dbClients);
    Assertions.assertThat(cachedTime).isLessThan(dbTime).isLessThan(dbTimeDividedBy100Times);
  }

  @DisplayName("очищаться при вызове GC")
  @Test
  void testThatCacheIsClearedAfterGC() {
    Assertions.assertThat(myCache.isEmpty()).isFalse();
    System.gc();

    await().atMost(10, TimeUnit.SECONDS).until(myCache::isEmpty);
    Assertions.assertThat(myCache.isEmpty()).isTrue();
  }

  private void flywayMigrations(DataSource dataSource) {
    var flyway = Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:/db")
        .load();
    flyway.migrate();
  }

  private List<Client> getClientsById(List<Long> ids, DBServiceClient serviceClient) {
    return ids.stream()
        .map(serviceClient::getClient)
        .map(Optional::orElseThrow)
        .collect(Collectors.toList());
  }
}

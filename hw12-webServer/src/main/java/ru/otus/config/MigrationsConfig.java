package ru.otus.config;

import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;

public class MigrationsConfig {
  public static void create(String url, String username, String password) {
    new MigrationsExecutorFlyway(url, username, password).executeMigrations();
  }
}

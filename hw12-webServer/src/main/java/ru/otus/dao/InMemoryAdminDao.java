package ru.otus.dao;

import ru.otus.helpers.HashHelper;
import ru.otus.model.Admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAdminDao implements AdminDao {
  private final Map<Long, Admin> admins;

  public InMemoryAdminDao() {
    admins = new HashMap<>();
    admins.put(1L, new Admin(1L, "Крис Гир", "admin", HashHelper.hash("admin")));
  }

  @Override
  public Optional<Admin> findByLogin(String login) {
    return admins.values()
        .stream()
        .filter(admin -> admin.getLogin().equals(login))
        .findFirst();
  }
}

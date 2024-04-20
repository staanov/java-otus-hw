package ru.otus.crm.service;

import ru.otus.cachehw.MyCache;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientCachedImpl implements DBServiceClient {

  private final DBServiceClient serviceClient;
  private final MyCache<Long, Client> myCache;

  public DbServiceClientCachedImpl(DBServiceClient serviceClient, MyCache<Long, Client> myCache) {
    this.serviceClient = serviceClient;
    this.myCache = myCache;
  }

  @Override
  public Client saveClient(Client client) {
    var savedClient = serviceClient.saveClient(client);
    myCache.put(savedClient.getId(), savedClient);
    return savedClient;
  }

  @Override
  public Optional<Client> getClient(long id) {
    var optionalClient = getClientFromCache(id);
    if (optionalClient.isPresent()) {
      return optionalClient;
    }
    return getClientFromDbAndUpdateCache(id);
  }

  @Override
  public List<Client> findAll() {
    return serviceClient.findAll();
  }

  private Optional<Client> getClientFromCache(long id) {
    return Optional.ofNullable(myCache.get(id));
  }

  private Optional<Client> getClientFromDbAndUpdateCache(long id) {
    var optionalClient = serviceClient.getClient(id);
    if (optionalClient.isPresent()) {
      var client = optionalClient.get();
      myCache.put(client.getId(), client);
    }
    return optionalClient;
  }
}

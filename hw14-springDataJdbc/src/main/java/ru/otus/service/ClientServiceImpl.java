package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

  private static final Logger LOG = LoggerFactory.getLogger(ClientServiceImpl.class);

  private final ClientRepository dao;
  private final TransactionManager transactionManager;

  public ClientServiceImpl(ClientRepository dao, TransactionManager transactionManager) {
    this.dao = dao;
    this.transactionManager = transactionManager;
  }

  @Override
  public List<Client> findAll() {
    var clients = dao.findAll();
    LOG.info("all clients: {}", clients);
    return clients;
  }

  @Override
  public Client findById(Long id) {
    var client = dao.findById(id).orElseThrow();
    LOG.info("client found: {}", client);
    return client;
  }

  @Override
  public Client save(Client client) {
    return transactionManager.doInTransaction(() -> {
      var savedClient = dao.save(client);
      LOG.info("saved client: {}", savedClient);
      return savedClient;
    });
  }

  @Override
  public void delete(Long id) {
    dao.deleteById(id);
    LOG.info("deleted client id: {}", id);
  }
}

package ru.otus.dto;

import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDto {

  private Long id;
  private String name;
  private String address;
  private Set<String> phones = new HashSet<>();

  public ClientDto() {
  }

  public ClientDto(Client client) {
    this.id = client.getId();
    this.name = client.getName();
    this.address = client.getAddress().toString();
    this.phones = client.getPhones().stream().map(Phone::getNumber).collect(Collectors.toSet());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Set<String> getPhones() {
    return phones;
  }

  public void setPhones(Set<String> phones) {
    this.phones = phones;
  }
}

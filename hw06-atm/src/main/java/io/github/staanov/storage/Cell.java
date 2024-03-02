package io.github.staanov.storage;

import io.github.staanov.exception.NotEnoughBanknotesException;

public interface Cell {
  int getAmount();
  void add(int quantity);
  boolean hasBanknotes();
  boolean hasAmount(int requestedAmount);
  int getBanknoteQuantity(int amount);
  BanknoteCell withdraw(int quantity) throws NotEnoughBanknotesException;
}

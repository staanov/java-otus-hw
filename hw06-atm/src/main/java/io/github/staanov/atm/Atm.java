package io.github.staanov.atm;

import io.github.staanov.storage.BanknoteCell;
import io.github.staanov.exception.NotEnoughBanknotesException;

import java.util.Set;

public interface Atm {
  void receive(Set<BanknoteCell> banknotes);
  Set<BanknoteCell> withdraw(int requestedAmount) throws NotEnoughBanknotesException;
  int getBalance();
}

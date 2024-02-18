package io.github.staanov.atm;

import io.github.staanov.storage.BanknoteCell;
import io.github.staanov.exception.NotEnoughBanknotesException;
import io.github.staanov.storage.BanknoteStorage;

import java.util.HashSet;
import java.util.Set;

public class AtmImpl implements Atm {

  private final BanknoteStorage storage;

  public AtmImpl(BanknoteStorage storage) {
    this.storage = storage;
  }

  @Override
  public void receive(Set<BanknoteCell> cells) {
    cells.forEach(cell -> {
      if (storage.getBanknotes().contains(cell)) {
        findValueInStorage(cell).add(cell.getQuantity());
      } else {
        storage.getBanknotes().add(cell);
      }
    });
  }

  @Override
  public Set<BanknoteCell> withdraw(int requestedAmount) throws NotEnoughBanknotesException {
    validateBalance(requestedAmount);

    Set<BanknoteCell> withdrawal = new HashSet<>();

    for (var cell : storage.getBanknotes()) {
      if (cell.hasBanknotes() && cell.hasAmount(requestedAmount)) {
        var quantity = cell.getBanknoteQuantity(requestedAmount);
        if (quantity > 0) {
          var withdrawCell = cell.withdraw(quantity);
          requestedAmount -= withdrawCell.getAmount();
          withdrawal.add(withdrawCell);
        }
      }
    }

    return withdrawal;
  }

  @Override
  public int getBalance() {
    return storage.getBanknotes().stream().mapToInt(BanknoteCell::getAmount).sum();
  }

  private BanknoteCell findValueInStorage(BanknoteCell cell) {
    return storage.getBanknotes().stream()
        .filter(persistedCell -> persistedCell.getBanknoteValue().equals(cell.getBanknoteValue()))
        .findFirst().orElse(null);
  }

  private void validateBalance(int requestedAmount) throws NotEnoughBanknotesException {
    if (getBalance() < requestedAmount) {
      throw new NotEnoughBanknotesException();
    }
  }
}

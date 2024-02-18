package io.github.staanov.storage;

import io.github.staanov.exception.NotEnoughBanknotesException;
import io.github.staanov.model.Banknote;
import io.github.staanov.model.BanknoteValue;

public class BanknoteCell implements Cell {

  private final Banknote banknote;
  private int quantity;

  public BanknoteCell(Banknote banknote, int quantity) {
    this.banknote = banknote;
    this.quantity = quantity;
  }

  @Override
  public int getAmount() {
    return banknote.getValue().getDenomination() * quantity;
  }

  @Override
  public void add(int quantity) {
    this.quantity += quantity;
  }

  @Override
  public boolean hasBanknotes() {
    return getQuantity() > 0;
  }

  @Override
  public boolean hasAmount(int requestedAmount) {
    return requestedAmount - getAmount() >= 0;
  }

  @Override
  public int getBanknoteQuantity(int amount) {
    return amount / getBanknoteDenomination();
  }

  @Override
  public BanknoteCell withdraw(int requestedQuantity) throws NotEnoughBanknotesException {
    if (areBanknotesEnough(requestedQuantity)) {
      quantity -= requestedQuantity;
      return new BanknoteCell(banknote, requestedQuantity);
    } else {
      throw new NotEnoughBanknotesException();
    }
  }

  public BanknoteValue getBanknoteValue() {
    return banknote.getValue();
  }

  public int getQuantity() {
    return quantity;
  }

  private int getBanknoteDenomination() {
    return getBanknoteValue().getDenomination();
  }

  private boolean areBanknotesEnough(int requestedQuantity) {
    return quantity - requestedQuantity >= 0;
  }
}

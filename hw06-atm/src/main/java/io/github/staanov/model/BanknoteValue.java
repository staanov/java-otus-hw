package io.github.staanov.model;

public enum BanknoteValue {
  TEN(10),
  FIFTY(50),
  ONE_HUNDRED(100),
  TWO_HUNDRED(200),
  FIVE_HUNDRED(500),
  ONE_THOUSAND(1000),
  TWO_THOUSAND(2000),
  FIVE_THOUSAND(5000);

  private final int denomination;

  BanknoteValue(int value) {
    denomination = value;
  }

  public int getDenomination() {
    return denomination;
  }
}

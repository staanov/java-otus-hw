package io.github.staanov.model;

import java.util.Objects;

public class Banknote {
  private final BanknoteValue value;

  public Banknote(BanknoteValue value) {
    this.value = value;
  }

  public BanknoteValue getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Banknote banknote = (Banknote) o;
    return value == banknote.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}

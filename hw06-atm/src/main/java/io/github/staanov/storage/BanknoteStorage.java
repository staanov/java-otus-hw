package io.github.staanov.storage;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class BanknoteStorage {

  private static final Comparator<BanknoteCell> ORDER_BY_VALUE_DESC = Comparator.comparing(BanknoteCell::getBanknoteValue).reversed();
  private final Set<BanknoteCell> storage = new TreeSet<>(ORDER_BY_VALUE_DESC);

  public Set<BanknoteCell> getBanknotes() {
    return storage;
  }
}

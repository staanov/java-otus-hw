package io.github.staanov;

import io.github.staanov.model.Banknote;
import io.github.staanov.model.BanknoteValue;
import io.github.staanov.storage.BanknoteCell;

import java.util.HashSet;
import java.util.Set;

public class BanknoteFactory {
  public static Set<BanknoteCell> getCells(int quantity) {
    Set<BanknoteCell> cells = new HashSet<>();

    for (var value : BanknoteValue.values()) {
      cells.add(new BanknoteCell(new Banknote(value), quantity));
    }

    return cells;
  }
}

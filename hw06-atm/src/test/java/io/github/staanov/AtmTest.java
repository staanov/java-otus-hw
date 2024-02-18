package io.github.staanov;

import io.github.staanov.atm.Atm;
import io.github.staanov.atm.AtmImpl;
import io.github.staanov.exception.NotEnoughBanknotesException;
import io.github.staanov.storage.BanknoteCell;
import io.github.staanov.storage.BanknoteStorage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

@DisplayName("Банкомат Atm должен")
public class AtmTest {

  private Atm atm;
  private Set<BanknoteCell> banknotes;

  @BeforeEach
  void setUp() {
    atm = new AtmImpl(new BanknoteStorage());
    banknotes = BanknoteFactory.getCells(1);
  }

  @Test
  @DisplayName("принимать купюры")
  void testReceive() {
    var banknotesSum = getSum(banknotes);
    var balanceBeforeReceiving = atm.getBalance();

    atm.receive(banknotes);
    var balanceAfterReceiving = atm.getBalance();

    Assertions.assertThat(balanceBeforeReceiving).isZero();
    Assertions.assertThat(balanceAfterReceiving).isEqualTo(banknotesSum);
  }

  @Test
  @DisplayName("отдавать купюры")
  void testWithdraw() {
    int requestMoney = 5550;
    var balanceBeforeReceiving = atm.getBalance();

    atm.receive(banknotes);
    var balanceAfterReceiving = atm.getBalance();

    var withdraw = org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> atm.withdraw(requestMoney));
    var withdrawSum = getSum(withdraw);
    var balanceAfterWithdraw = atm.getBalance();

    Assertions.assertThat(balanceBeforeReceiving).isZero();
    Assertions.assertThat(withdrawSum).isEqualTo(requestMoney);
    var difference = balanceAfterReceiving - balanceAfterWithdraw;
    Assertions.assertThat(difference).isEqualTo(requestMoney);
  }

  @Test
  @DisplayName("выбрасывать исключение, если купюр нет")
  void testNotEnoughBanknotesException() {
    int requestMoney = 55500;
    var banknotesSum = getSum(banknotes);
    var balanceBeforeReceiving = atm.getBalance();

    atm.receive(banknotes);
    var balanceAfterReceiving = atm.getBalance();
    Assertions.assertThat(balanceBeforeReceiving).isZero();
    Assertions.assertThatExceptionOfType(NotEnoughBanknotesException.class)
        .isThrownBy(() -> atm.withdraw(requestMoney))
        .withMessage("Not enough banknotes in this ATM");
    Assertions.assertThat(balanceAfterReceiving).isEqualTo(banknotesSum);
  }

  private int getSum(Set<BanknoteCell> banknotes) {
    return banknotes.stream().mapToInt(BanknoteCell::getAmount).sum();
  }
}

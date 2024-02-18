package io.github.staanov.exception;

public class NotEnoughBanknotesException extends Exception {
  public NotEnoughBanknotesException() {
    super("Not enough banknotes in this ATM");
  }
}

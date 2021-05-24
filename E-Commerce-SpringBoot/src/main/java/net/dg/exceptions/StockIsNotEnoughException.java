package net.dg.exceptions;

public class StockIsNotEnoughException extends RuntimeException {
    static final String MESSAGE = "Stock is not enough.";

    public StockIsNotEnoughException(){ super(MESSAGE); }
}

package net.dg.exceptions;

public class StockIsNotEnoughException extends RuntimeException {
    static final String message = "Stock is not enough.";

    public StockIsNotEnoughException(){ super(message); }
}

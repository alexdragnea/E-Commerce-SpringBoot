package net.dg.exceptions;

public class StockIsNotEnoughException extends RuntimeException {
    final static String message = "Stock is not enough.";

    public StockIsNotEnoughException(){ super(message); }
}

package net.dg.exceptions;

public class EmptyCartException extends RuntimeException {
    final static String message = "Cart is empty.";

    public EmptyCartException() {
        super(message);
    }
}

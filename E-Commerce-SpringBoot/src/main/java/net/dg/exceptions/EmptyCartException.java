package net.dg.exceptions;

public class EmptyCartException extends RuntimeException {
    static final String message = "Cart is empty.";

    public EmptyCartException() {
        super(message);
    }
}

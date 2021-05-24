package net.dg.exceptions;

public class EmptyCartException extends RuntimeException {
    static final String MESSAGE = "Cart is empty.";

    public EmptyCartException() {
        super(MESSAGE);
    }
}

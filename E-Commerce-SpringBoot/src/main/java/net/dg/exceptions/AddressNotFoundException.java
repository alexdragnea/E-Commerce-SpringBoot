package net.dg.exceptions;

public class AddressNotFoundException extends RuntimeException {
    static final String MESSAGE = "Shipping address is not found, go to Account/Update shipping address.";

    public AddressNotFoundException() { super(MESSAGE); }
}

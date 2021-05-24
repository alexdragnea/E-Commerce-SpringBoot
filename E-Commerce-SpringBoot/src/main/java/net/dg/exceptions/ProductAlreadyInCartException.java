package net.dg.exceptions;

public class ProductAlreadyInCartException extends RuntimeException {
    static final String MESSAGE = "Product is already in cart.";

    public ProductAlreadyInCartException(){ super(MESSAGE); }

}

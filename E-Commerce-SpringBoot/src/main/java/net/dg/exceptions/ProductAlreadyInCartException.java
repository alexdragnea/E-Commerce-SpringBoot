package net.dg.exceptions;

public class ProductAlreadyInCartException extends RuntimeException {
    static final String message = "Product already in cart.";

    public ProductAlreadyInCartException(){ super(message); }

}

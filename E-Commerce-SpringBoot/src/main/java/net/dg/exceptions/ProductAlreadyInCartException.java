package net.dg.exceptions;

public class ProductAlreadyInCartException extends RuntimeException {
    final static String message = "Product already in cart.";

    public ProductAlreadyInCartException(){ super(message); }

}

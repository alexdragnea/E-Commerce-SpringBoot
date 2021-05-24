package net.dg.exceptions;

public class ProductNotFoundException extends RuntimeException {
    static final String MESSAGE = "Product/s not found based on search.";

    public ProductNotFoundException(){ super(MESSAGE); }
}

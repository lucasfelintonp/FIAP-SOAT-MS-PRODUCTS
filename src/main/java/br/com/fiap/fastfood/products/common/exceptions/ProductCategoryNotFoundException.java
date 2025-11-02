package br.com.fiap.fastfood.products.common.exceptions;

public class ProductCategoryNotFoundException extends RuntimeException {
    public ProductCategoryNotFoundException(String message) {
        super(message);
    }
}

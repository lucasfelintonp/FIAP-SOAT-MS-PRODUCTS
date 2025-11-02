package br.com.fiap.fastfood.products.common.exceptions;

public class ProductCategoryPersistenceException extends RuntimeException {
    public ProductCategoryPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

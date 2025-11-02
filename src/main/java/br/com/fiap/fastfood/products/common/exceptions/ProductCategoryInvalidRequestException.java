package br.com.fiap.fastfood.products.common.exceptions;

public class ProductCategoryInvalidRequestException extends RuntimeException {
    public ProductCategoryInvalidRequestException(String message) {
        super(message);
    }
}

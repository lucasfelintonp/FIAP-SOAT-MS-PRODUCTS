package br.com.fiap.fastfood.products.domain.use_cases;

import br.com.fiap.fastfood.products.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.products.domain.entities.ProductCategoryEntity;

public class GetProductCategoryUseCase {
    private final ProductCategoryGateway productCategoryGateway;

    public GetProductCategoryUseCase(ProductCategoryGateway productCategoryGateway) {
        this.productCategoryGateway = productCategoryGateway;
    }

    public ProductCategoryEntity run(Integer categoryId) {
        return productCategoryGateway.findById(categoryId);
    }
}

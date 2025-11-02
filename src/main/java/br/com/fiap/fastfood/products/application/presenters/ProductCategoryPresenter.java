package br.com.fiap.fastfood.products.application.presenters;

import br.com.fiap.fastfood.products.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.products.domain.entities.ProductCategoryEntity;

import java.util.List;

public class ProductCategoryPresenter {

    private ProductCategoryPresenter() {
        throw new IllegalStateException("ProductCategory presenter não pode ser instanciado, é uma classe de utilidade.");
    }

    public static ProductCategoryDTO create(ProductCategoryEntity productCategory) {
        return new ProductCategoryDTO(
                productCategory.id(),
                productCategory.name()
        );
    }

    public static List<ProductCategoryDTO> findAll(List<ProductCategoryEntity> productCategories) {
        return productCategories.stream()
                .map(category -> new ProductCategoryDTO(
                        category.id(),
                        category.name()
                ))
                .toList();
    }

}

package br.com.fiap.fastfood.products.infrastructure.interfaces;

import br.com.fiap.fastfood.products.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.products.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.products.application.dtos.UpdateProductCategoryDTO;

import java.util.List;

public interface ProductCategoryDatasource {
    ProductCategoryDTO create(CreateProductCategoryDTO productCategoryDTO);

    ProductCategoryDTO findById(Integer id);

    List<ProductCategoryDTO> findAll();

    ProductCategoryDTO update(UpdateProductCategoryDTO productCategoryDTO);

    void delete(Integer id);
}

package br.com.fiap.fastfood.category.application.controllers;

import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.infrastructure.interfaces.ProductCategoryDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryControllerTest {

    @Mock
    private ProductCategoryDatasource datasource;

    private ProductCategoryController controller;

    @BeforeEach
    void setUp() {
        controller = new ProductCategoryController(datasource);
    }

    @Test
    void createProductCategory_success_shouldReturnCreatedCategory() {
        var createDto = new CreateProductCategoryDTO("Bebidas");
        var returned = new ProductCategoryDTO(1, "Bebidas");

        when(datasource.create(createDto)).thenReturn(returned);

        var result = controller.createProductCategory(createDto);

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Bebidas", result.name());

        verify(datasource, times(1)).create(createDto);
    }

    @Test
    void createProductCategory_error_whenDatasourceFails_shouldPropagate() {
        var createDto = new CreateProductCategoryDTO("Bebidas");

        when(datasource.create(createDto)).thenThrow(new RuntimeException("DB down"));

        var ex = assertThrows(RuntimeException.class, () -> controller.createProductCategory(createDto));
        assertEquals("DB down", ex.getMessage());

        verify(datasource, times(1)).create(createDto);
    }

    @Test
    void getProductCategoryById_success_shouldReturnCategory() {
        var returned = new ProductCategoryDTO(2, "Lanches");

        when(datasource.findById(2)).thenReturn(returned);

        var result = controller.getProductCategoryById(2);

        assertNotNull(result);
        assertEquals(2, result.id());
        assertEquals("Lanches", result.name());

        verify(datasource, times(1)).findById(2);
    }

    @Test
    void getProductCategoryById_notFound_shouldPropagateIllegalArgument() {
        when(datasource.findById(99)).thenThrow(new IllegalArgumentException("Categoria de produto com ID 99 não encontrada."));

        var ex = assertThrows(IllegalArgumentException.class, () -> controller.getProductCategoryById(99));
        assertTrue(ex.getMessage().contains("Categoria de produto"));

        verify(datasource, times(1)).findById(99);
    }

    @Test
    void getAllProductCategories_success_shouldReturnList() {
        var list = List.of(
            new ProductCategoryDTO(1, "Bebidas"),
            new ProductCategoryDTO(2, "Lanches")
        );

        when(datasource.findAll()).thenReturn(list);

        var result = controller.getAllProductCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bebidas", result.get(0).name());
        assertEquals("Lanches", result.get(1).name());

        verify(datasource, times(1)).findAll();
    }

    @Test
    void updateProductCategory_success_shouldReturnUpdatedCategory() {
        var updateDto = new UpdateProductCategoryDTO(3, "Sobremesas");
        var returned = new ProductCategoryDTO(3, "Sobremesas");

        when(datasource.update(updateDto)).thenReturn(returned);

        var result = controller.updateProductCategory(updateDto);

        assertNotNull(result);
        assertEquals(3, result.id());
        assertEquals("Sobremesas", result.name());

        verify(datasource, times(1)).update(updateDto);
    }

    @Test
    void updateProductCategory_notFound_shouldPropagateIllegalArgument() {
        var updateDto = new UpdateProductCategoryDTO(50, "Inexistente");

        when(datasource.update(updateDto)).thenThrow(new IllegalArgumentException("Categoria de produto com ID 50 não encontrada."));

        var ex = assertThrows(IllegalArgumentException.class, () -> controller.updateProductCategory(updateDto));
        assertTrue(ex.getMessage().contains("Categoria de produto"));

        verify(datasource, times(1)).update(updateDto);
    }

    @Captor
    ArgumentCaptor<Integer> idCaptor;

    @Test
    void deleteProductCategory_success_shouldCallDelete() {
        var returned = new ProductCategoryDTO(4, "Acompanhamentos");

        when(datasource.findById(4)).thenReturn(returned);
        doNothing().when(datasource).delete(4);

        controller.deleteProductCategory(4);

        verify(datasource, times(1)).findById(4);
        verify(datasource, times(1)).delete(idCaptor.capture());
        assertEquals(4, idCaptor.getValue());
    }

    @Test
    void deleteProductCategory_notFound_shouldPropagateIllegalArgument() {
        when(datasource.findById(100)).thenThrow(new IllegalArgumentException("Categoria de produto com ID 100 não encontrada."));

        var ex = assertThrows(IllegalArgumentException.class, () -> controller.deleteProductCategory(100));
        assertTrue(ex.getMessage().contains("Categoria de produto"));

        verify(datasource, times(1)).findById(100);
        verify(datasource, never()).delete(anyInt());
    }
}


package br.com.fiap.fastfood.category.application.presenters;

import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryPresenterTest {

    @Test
    void create_shouldMapEntityToDto_success() {
        var entity = new ProductCategoryEntity(1, "Bebidas");

        ProductCategoryDTO dto = ProductCategoryPresenter.create(entity);

        assertNotNull(dto);
        assertEquals(1, dto.id());
        assertEquals("Bebidas", dto.name());
    }

    @Test
    void create_null_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> ProductCategoryPresenter.create(null));
    }

    @Test
    void findAll_shouldMapEntitiesToDtos_success() {
        var entities = List.of(
            new ProductCategoryEntity(1, "Bebidas"),
            new ProductCategoryEntity(2, "Lanches")
        );

        var dtos = ProductCategoryPresenter.findAll(entities);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Bebidas", dtos.get(0).name());
        assertEquals("Lanches", dtos.get(1).name());
    }

    @Test
    void findAll_emptyList_shouldReturnEmptyList() {
        var entities = List.<ProductCategoryEntity>of();

        var dtos = ProductCategoryPresenter.findAll(entities);

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void findAll_null_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> ProductCategoryPresenter.findAll(null));
    }
}


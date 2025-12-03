package br.com.fiap.fastfood.category.application.gateways;

import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.application.dtos.UpdateProductCategoryDTO;
import br.com.fiap.fastfood.category.common.exceptions.ProductCategoryPersistenceException;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;
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
class ProductCategoryGatewayUnitTest {

    @Mock
    private ProductCategoryDatasource datasource;

    private ProductCategoryGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new ProductCategoryGateway(datasource);
    }

    @Test
    void create_shouldReturnEntity_whenDatasourceSucceeds() {
        var input = new ProductCategoryEntity(null, "Bebidas");
        var returned = new ProductCategoryDTO(1, "Bebidas");

        when(datasource.create(new CreateProductCategoryDTO("Bebidas"))).thenReturn(returned);

        var result = gateway.create(input);

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Bebidas", result.name());

        verify(datasource, times(1)).create(new CreateProductCategoryDTO("Bebidas"));
    }

    @Test
    void create_shouldPropagatePersistenceException_whenDatasourceFails() {
        var input = new ProductCategoryEntity(null, "Bebidas");

        when(datasource.create(new CreateProductCategoryDTO("Bebidas")))
            .thenThrow(new ProductCategoryPersistenceException("Erro ao persistir categoria", new RuntimeException("sql")));

        var ex = assertThrows(ProductCategoryPersistenceException.class, () -> gateway.create(input));
        assertTrue(ex.getMessage().contains("Erro ao persistir categoria"));

        verify(datasource, times(1)).create(new CreateProductCategoryDTO("Bebidas"));
    }

    @Test
    void findById_shouldReturnEntity_whenDatasourceSucceeds() {
        var returned = new ProductCategoryDTO(2, "Lanches");

        when(datasource.findById(2)).thenReturn(returned);

        var result = gateway.findById(2);

        assertNotNull(result);
        assertEquals(2, result.id());
        assertEquals("Lanches", result.name());

        verify(datasource, times(1)).findById(2);
    }

    @Test
    void findById_shouldPropagateIllegalArgument_whenDatasourceNotFound() {
        when(datasource.findById(99)).thenThrow(new IllegalArgumentException("Categoria de produto com ID 99 não encontrada."));

        var ex = assertThrows(IllegalArgumentException.class, () -> gateway.findById(99));
        assertTrue(ex.getMessage().contains("Categoria de produto"));

        verify(datasource, times(1)).findById(99);
    }

    @Test
    void findAll_shouldReturnEntities_whenDatasourceSucceeds() {
        var list = List.of(
            new ProductCategoryDTO(1, "Bebidas"),
            new ProductCategoryDTO(2, "Lanches")
        );

        when(datasource.findAll()).thenReturn(list);

        var result = gateway.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bebidas", result.get(0).name());
        assertEquals("Lanches", result.get(1).name());

        verify(datasource, times(1)).findAll();
    }

    @Test
    void update_shouldReturnEntity_whenDatasourceSucceeds() {
        var entity = new ProductCategoryEntity(3, "Sobremesas");
        var returned = new ProductCategoryDTO(3, "Sobremesas");

        when(datasource.update(new UpdateProductCategoryDTO(3, "Sobremesas"))).thenReturn(returned);

        var result = gateway.update(entity);

        assertNotNull(result);
        assertEquals(3, result.id());
        assertEquals("Sobremesas", result.name());

        verify(datasource, times(1)).update(new UpdateProductCategoryDTO(3, "Sobremesas"));
    }

    @Test
    void update_shouldPropagateIllegalArgument_whenDatasourceNotFound() {
        var entity = new ProductCategoryEntity(50, "Inexistente");

        when(datasource.update(new UpdateProductCategoryDTO(50, "Inexistente")))
            .thenThrow(new IllegalArgumentException("Categoria de produto com ID 50 não encontrada."));

        var ex = assertThrows(IllegalArgumentException.class, () -> gateway.update(entity));
        assertTrue(ex.getMessage().contains("Categoria de produto"));

        verify(datasource, times(1)).update(new UpdateProductCategoryDTO(50, "Inexistente"));
    }

    @Captor
    ArgumentCaptor<Integer> idCaptor;

    @Test
    void delete_shouldCallDatasource_withCorrectId() {
        var entity = new ProductCategoryEntity(4, "Acompanhamentos");

        doNothing().when(datasource).delete(4);

        gateway.delete(entity);

        verify(datasource, times(1)).delete(idCaptor.capture());
        assertEquals(4, idCaptor.getValue());
    }

    @Test
    void delete_shouldPropagatePersistenceException_whenDatasourceFails() {
        var entity = new ProductCategoryEntity(6, "Promocao");

        doThrow(new ProductCategoryPersistenceException("Erro ao deletar", new RuntimeException("sql"))).when(datasource).delete(6);

        var ex = assertThrows(ProductCategoryPersistenceException.class, () -> gateway.delete(entity));
        assertTrue(ex.getMessage().contains("Erro ao deletar"));

        verify(datasource, times(1)).delete(6);
    }
}


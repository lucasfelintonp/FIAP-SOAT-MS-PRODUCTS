package br.com.fiap.fastfood.product.application.gateways;

import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.domain.entities.ProductEntity;
import br.com.fiap.fastfood.product.infrastructure.interfaces.ProductDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductGatewayTest {

    @Mock
    private ProductDatasource datasource;

    private ProductGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new ProductGateway(datasource);
    }

    private ProductDTO makeDto(UUID id, String name, Integer categoryId) {
        var now = LocalDateTime.now();
        return new ProductDTO(id, name, "Descrição de " + name, new BigDecimal("9.99"), true, "/img/" + name.toLowerCase() + ".jpg", categoryId, now, now);
    }

    @Test
    void findById_success_shouldMapToEntity_Coxinha() {
        UUID id = UUID.randomUUID();
        var dto = makeDto(id, "Coxinha", 2);

        when(datasource.findById(id)).thenReturn(dto);

        ProductEntity entity = gateway.findById(id);

        assertNotNull(entity);
        assertEquals(id, entity.id());
        assertEquals("Coxinha", entity.name());
        assertEquals(2, entity.categoryId());

        verify(datasource, times(1)).findById(id);
    }

    @Test
    void findById_error_whenDatasourceThrows() {
        UUID id = UUID.randomUUID();
        when(datasource.findById(id)).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> gateway.findById(id));

        verify(datasource, times(1)).findById(id);
    }

    @Test
    void findAll_success_shouldReturnBebidasList() {
        var now = LocalDateTime.now();
        var dto1 = makeDto(UUID.randomUUID(), "Refrigerante Lata",1);
        var dto2 = makeDto(UUID.randomUUID(), "Suco Natural",1);

        when(datasource.findAll(1)).thenReturn(List.of(dto1, dto2));

        var entities = gateway.findAll(1);

        assertEquals(2, entities.size());
        assertTrue(entities.stream().anyMatch(e -> e.name().equals("Refrigerante Lata")));

        verify(datasource, times(1)).findAll(1);
    }

    @Test
    void findAll_error_whenDatasourceThrows() {
        when(datasource.findAll(99)).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> gateway.findAll(99));

        verify(datasource, times(1)).findAll(99);
    }

    @Test
    void findAllByIds_success_shouldReturnMatchingProducts() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var dto1 = makeDto(id1, "Coxinha",2);
        var dto2 = makeDto(id2, "Enroladinho",2);

        when(datasource.findAllByIds(List.of(id1, id2))).thenReturn(List.of(dto1, dto2));

        var result = gateway.findAllByIds(List.of(id1, id2));

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(e -> e.id().equals(id1)));

        verify(datasource, times(1)).findAllByIds(List.of(id1, id2));
    }

    @Test
    void create_success_shouldReturnEntity_PromoCoxinha() {
        var entity = new ProductEntity(null, "Promo Coxinha", "Promo", new BigDecimal("3.00"), true, "/img/promo.jpg", 2, LocalDateTime.now(), LocalDateTime.now(), Optional.empty());
        var dto = makeDto(UUID.randomUUID(), "Promo Coxinha",2);

        when(datasource.create(any())).thenReturn(dto);

        var result = gateway.create(entity);

        assertNotNull(result);
        assertEquals("Promo Coxinha", result.name());

        verify(datasource, times(1)).create(any());
    }

    @Test
    void create_error_whenDatasourceFails() {
        var entity = new ProductEntity(null, "Refri Lata", "Refrigerante", new BigDecimal("4.00"), true, "/img/refri.jpg",1, LocalDateTime.now(), LocalDateTime.now(), Optional.empty());

        when(datasource.create(any())).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> gateway.create(entity));

        verify(datasource, times(1)).create(any());
    }

    @Test
    void update_success_shouldReturnUpdatedProduct_Salgar() {
        var id = UUID.randomUUID();
        var entity = new ProductEntity(id, "Salgar", "Salgado especial", new BigDecimal("7.50"), true, "/img/salgar.jpg",3, LocalDateTime.now(), LocalDateTime.now(), Optional.empty());
        var dto = makeDto(id, "Salgar",3);

        when(datasource.update(any())).thenReturn(dto);

        var result = gateway.update(entity);

        assertNotNull(result);
        assertEquals("Salgar", result.name());

        verify(datasource, times(1)).update(any());
    }

    @Test
    void update_error_whenDatasourceThrows() {
        var id = UUID.randomUUID();
        var entity = new ProductEntity(id, "NaoExiste", "Inexistente", new BigDecimal("0.00"), true, null, 99, LocalDateTime.now(), LocalDateTime.now(), Optional.empty());

        when(datasource.update(any())).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> gateway.update(entity));

        verify(datasource, times(1)).update(any());
    }

    @Test
    void delete_success_shouldReturnDeletedProduct() {
        var id = UUID.randomUUID();
        var dto = makeDto(id, "Promo Coxinha",2);

        when(datasource.delete(id)).thenReturn(dto);

        var result = gateway.delete(new ProductEntity(id, "", "", new BigDecimal("0"), true, null, 2, LocalDateTime.now(), LocalDateTime.now(), Optional.empty()));

        assertNotNull(result);
        assertEquals(id, result.id());

        verify(datasource, times(1)).delete(id);
    }

    @Test
    void delete_error_whenDatasourceThrows() {
        var id = UUID.randomUUID();
        when(datasource.delete(id)).thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class, () -> gateway.delete(new ProductEntity(id, "", "", new BigDecimal("0"), true, null, 2, LocalDateTime.now(), LocalDateTime.now(), Optional.empty())));

        verify(datasource, times(1)).delete(id);
    }

    @Test
    void enable_and_disable_shouldCallDatasource() {
        var id = UUID.randomUUID();

        doNothing().when(datasource).enable(id);
        doNothing().when(datasource).disable(id);

        gateway.enableProduct(id);
        gateway.disableProduct(id);

        verify(datasource, times(1)).enable(id);
        verify(datasource, times(1)).disable(id);
    }
}


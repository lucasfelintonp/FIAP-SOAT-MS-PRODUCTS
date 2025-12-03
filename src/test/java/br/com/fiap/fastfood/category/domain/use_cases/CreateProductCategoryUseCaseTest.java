package br.com.fiap.fastfood.category.domain.use_cases;

import br.com.fiap.fastfood.category.application.dtos.CreateProductCategoryDTO;
import br.com.fiap.fastfood.category.common.exceptions.ProductCategoryPersistenceException;
import br.com.fiap.fastfood.category.application.gateways.ProductCategoryGateway;
import br.com.fiap.fastfood.category.domain.entities.ProductCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductCategoryUseCaseTest {

    @Mock
    private ProductCategoryGateway gateway;

    private CreateProductCategoryUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateProductCategoryUseCase(gateway);
    }

    @Test
    void run_shouldReturnCreatedEntity_whenGatewaySucceeds() {
        // Arrange
        var dto = new CreateProductCategoryDTO("Bebidas");
        var createdEntity = new ProductCategoryEntity(100, "Bebidas");

        when(gateway.create(any(ProductCategoryEntity.class))).thenReturn(createdEntity);

        // Act
        ProductCategoryEntity result = useCase.run(dto);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.id());
        assertEquals("Bebidas", result.name());

        // Verify that gateway.create was called with an entity that has null id and the DTO name
        verify(gateway, times(1)).create(argThat(entity -> entity.id() == null && "Bebidas".equals(entity.name())));
    }

    @Test
    void run_shouldPropagatePersistenceException_whenGatewayFails() {
        // Arrange
        var dto = new CreateProductCategoryDTO("Bebidas");

        when(gateway.create(any(ProductCategoryEntity.class)))
                .thenThrow(new ProductCategoryPersistenceException("Erro ao persistir categoria", new RuntimeException("sql")));

        // Act & Assert
        var ex = assertThrows(ProductCategoryPersistenceException.class, () -> useCase.run(dto));
        assertTrue(ex.getMessage().contains("Erro ao persistir categoria"));

        verify(gateway, times(1)).create(any(ProductCategoryEntity.class));
    }
}


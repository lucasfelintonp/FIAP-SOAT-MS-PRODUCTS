package br.com.fiap.fastfood.product.infrastructure.database.adapters;

import br.com.fiap.fastfood.category.common.exceptions.ProductCategoryNotFoundException;
import br.com.fiap.fastfood.category.infrastructure.database.entities.ProductCategoryEntityJPA;
import br.com.fiap.fastfood.category.infrastructure.database.repositories.ProductCategoryRepository;
import br.com.fiap.fastfood.product.application.dtos.ProductDTO;
import br.com.fiap.fastfood.product.common.ProductsConstants;
import br.com.fiap.fastfood.product.common.exceptions.ProductNotFoundException;
import br.com.fiap.fastfood.product.infrastructure.database.entities.ProductEntityJPA;
import br.com.fiap.fastfood.product.infrastructure.database.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository categoryRepository;

    @InjectMocks
    private ProductAdapter adapter;

    private UUID coxinhaId;
    private UUID refrigeranteId;
    private UUID hamburguerId;
    private UUID batataFritaId;
    private UUID sorveteId;

    private ProductEntityJPA coxinhaEntity;
    private ProductEntityJPA refrigeranteEntity;
    private ProductEntityJPA hamburguerEntity;
    private ProductEntityJPA batataFritaEntity;
    private ProductEntityJPA sorveteEntity;

    private ProductCategoryEntityJPA lanchesCategory;
    private ProductCategoryEntityJPA bebidasCategory;
    private ProductCategoryEntityJPA acompanhamentosCategory;
    private ProductCategoryEntityJPA sobremesasCategory;

    @BeforeEach
    void setUp() {
        // IDs
        coxinhaId = UUID.randomUUID();
        refrigeranteId = UUID.randomUUID();
        hamburguerId = UUID.randomUUID();
        batataFritaId = UUID.randomUUID();
        sorveteId = UUID.randomUUID();

        // Categorias
        lanchesCategory = new ProductCategoryEntityJPA("Lanches");
        lanchesCategory.setId(1);

        bebidasCategory = new ProductCategoryEntityJPA("Bebidas");
        bebidasCategory.setId(2);

        acompanhamentosCategory = new ProductCategoryEntityJPA("Acompanhamentos");
        acompanhamentosCategory.setId(3);

        sobremesasCategory = new ProductCategoryEntityJPA("Sobremesas");
        sobremesasCategory.setId(4);

        // Produtos
        LocalDateTime now = LocalDateTime.now();

        coxinhaEntity = new ProductEntityJPA(
            coxinhaId, "Coxinha", "Coxinha de frango frita crocante",
            new BigDecimal("6.50"), true, "/images/coxinha.jpg",
            1, now, now, null
        );

        refrigeranteEntity = new ProductEntityJPA(
            refrigeranteId, "Refrigerante Coca-Cola", "Refrigerante Coca-Cola 350ml",
            new BigDecimal("5.00"), true, "/images/coca-cola.jpg",
            2, now, now, null
        );

        hamburguerEntity = new ProductEntityJPA(
            hamburguerId, "X-Burger", "Hambúrguer artesanal com queijo",
            new BigDecimal("18.90"), true, "/images/xburger.jpg",
            1, now, now, null
        );

        batataFritaEntity = new ProductEntityJPA(
            batataFritaId, "Batata Frita", "Porção de batata frita crocante 300g",
            new BigDecimal("12.00"), true, "/images/batata.jpg",
            3, now, now, null
        );

        sorveteEntity = new ProductEntityJPA(
            sorveteId, "Sorvete de Chocolate", "Sorvete cremoso sabor chocolate",
            new BigDecimal("8.50"), true, "/images/sorvete.jpg",
            4, now, now, null
        );
    }

    // ==================== CREATE TESTS ====================

    @Test
    void create_success_shouldCreateCoxinha() {
        var productDTO = new ProductDTO(
            null, "Coxinha", "Coxinha de frango frita crocante",
            new BigDecimal("6.50"), true, "/images/coxinha.jpg",
            1, null, null
        );

        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(coxinhaEntity);

        var result = adapter.create(productDTO);

        assertNotNull(result);
        assertEquals(coxinhaId, result.id());
        assertEquals("Coxinha", result.name());
        assertEquals("Coxinha de frango frita crocante", result.description());
        assertEquals(new BigDecimal("6.50"), result.price());
        assertTrue(result.isActive());
        assertEquals("/images/coxinha.jpg", result.imagePath());
        assertEquals(1, result.categoryId());

        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    @Test
    void create_success_shouldCreateRefrigerante() {
        var productDTO = new ProductDTO(
            null, "Refrigerante Coca-Cola", "Refrigerante Coca-Cola 350ml",
            new BigDecimal("5.00"), true, "/images/coca-cola.jpg",
            2, null, null
        );

        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(refrigeranteEntity);

        var result = adapter.create(productDTO);

        assertNotNull(result);
        assertEquals(refrigeranteId, result.id());
        assertEquals("Refrigerante Coca-Cola", result.name());
        assertEquals(new BigDecimal("5.00"), result.price());
        assertEquals(2, result.categoryId());

        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    @Test
    void create_success_shouldCreateHamburguer() {
        var productDTO = new ProductDTO(
            null, "X-Burger", "Hambúrguer artesanal com queijo",
            new BigDecimal("18.90"), true, "/images/xburger.jpg",
            1, null, null
        );

        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(hamburguerEntity);

        var result = adapter.create(productDTO);

        assertNotNull(result);
        assertEquals(hamburguerId, result.id());
        assertEquals("X-Burger", result.name());
        assertEquals(new BigDecimal("18.90"), result.price());

        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    @Test
    void create_error_whenRepositoryFails_shouldPropagateException() {
        var productDTO = new ProductDTO(
            null, "Coxinha", "Coxinha de frango",
            new BigDecimal("6.50"), true, "/images/coxinha.jpg",
            1, null, null
        );

        when(productRepository.save(any(ProductEntityJPA.class)))
                .thenThrow(new RuntimeException("Erro ao salvar produto no banco de dados"));

        var exception = assertThrows(RuntimeException.class, () -> adapter.create(productDTO));

        assertEquals("Erro ao salvar produto no banco de dados", exception.getMessage());
        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    // ==================== FIND BY ID TESTS ====================

    @Test
    void findById_success_shouldReturnCoxinha() {
        when(productRepository.findById(coxinhaId)).thenReturn(Optional.of(coxinhaEntity));

        var result = adapter.findById(coxinhaId);

        assertNotNull(result);
        assertEquals(coxinhaId, result.id());
        assertEquals("Coxinha", result.name());
        assertEquals("Coxinha de frango frita crocante", result.description());
        assertEquals(new BigDecimal("6.50"), result.price());
        assertEquals(1, result.categoryId());

        verify(productRepository, times(1)).findById(coxinhaId);
    }

    @Test
    void findById_success_shouldReturnRefrigerante() {
        when(productRepository.findById(refrigeranteId)).thenReturn(Optional.of(refrigeranteEntity));

        var result = adapter.findById(refrigeranteId);

        assertNotNull(result);
        assertEquals(refrigeranteId, result.id());
        assertEquals("Refrigerante Coca-Cola", result.name());
        assertEquals(new BigDecimal("5.00"), result.price());

        verify(productRepository, times(1)).findById(refrigeranteId);
    }

    @Test
    void findById_success_shouldReturnBatataFrita() {
        when(productRepository.findById(batataFritaId)).thenReturn(Optional.of(batataFritaEntity));

        var result = adapter.findById(batataFritaId);

        assertNotNull(result);
        assertEquals(batataFritaId, result.id());
        assertEquals("Batata Frita", result.name());
        assertEquals("Porção de batata frita crocante 300g", result.description());

        verify(productRepository, times(1)).findById(batataFritaId);
    }

    @Test
    void findById_error_whenProductNotFound_shouldThrowProductNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        var exception = assertThrows(ProductNotFoundException.class,
                () -> adapter.findById(nonExistentId));

        assertEquals(String.format(ProductsConstants.PRODUCT_NOT_FOUND, nonExistentId),
                exception.getMessage());
        assertTrue(exception.getMessage().contains("Produto com ID"));
        assertTrue(exception.getMessage().contains("não encontrado"));

        verify(productRepository, times(1)).findById(nonExistentId);
    }

    // ==================== FIND ALL TESTS ====================

    @Test
    void findAll_success_shouldReturnAllProductsWhenCategoryIdIsNull() {
        when(productRepository.findAllByDeletedAtIsNull())
                .thenReturn(List.of(coxinhaEntity, refrigeranteEntity, hamburguerEntity, batataFritaEntity, sorveteEntity));

        var result = adapter.findAll(null);

        assertNotNull(result);
        assertEquals(5, result.size());

        assertTrue(result.stream().anyMatch(p -> p.name().equals("Coxinha")));
        assertTrue(result.stream().anyMatch(p -> p.name().equals("Refrigerante Coca-Cola")));
        assertTrue(result.stream().anyMatch(p -> p.name().equals("X-Burger")));
        assertTrue(result.stream().anyMatch(p -> p.name().equals("Batata Frita")));
        assertTrue(result.stream().anyMatch(p -> p.name().equals("Sorvete de Chocolate")));

        verify(productRepository, times(1)).findAllByDeletedAtIsNull();
        verify(productRepository, never()).findAllByCategoryIdAndDeletedAtIsNull(anyInt());
    }

    @Test
    void findAll_success_shouldReturnOnlyLanchesWhenCategoryId1() {
        when(productRepository.findAllByCategoryIdAndDeletedAtIsNull(1))
                .thenReturn(List.of(coxinhaEntity, hamburguerEntity));

        var result = adapter.findAll(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.categoryId() == 1));
        assertTrue(result.stream().anyMatch(p -> p.name().equals("Coxinha")));
        assertTrue(result.stream().anyMatch(p -> p.name().equals("X-Burger")));

        verify(productRepository, times(1)).findAllByCategoryIdAndDeletedAtIsNull(1);
        verify(productRepository, never()).findAllByDeletedAtIsNull();
    }

    @Test
    void findAll_success_shouldReturnOnlyBebidasWhenCategoryId2() {
        when(productRepository.findAllByCategoryIdAndDeletedAtIsNull(2))
                .thenReturn(List.of(refrigeranteEntity));

        var result = adapter.findAll(2);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Refrigerante Coca-Cola", result.get(0).name());
        assertEquals(2, result.get(0).categoryId());

        verify(productRepository, times(1)).findAllByCategoryIdAndDeletedAtIsNull(2);
    }

    @Test
    void findAll_success_shouldReturnEmptyListWhenNoProducts() {
        when(productRepository.findAllByDeletedAtIsNull()).thenReturn(List.of());

        var result = adapter.findAll(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository, times(1)).findAllByDeletedAtIsNull();
    }

    @Test
    void findAll_success_shouldReturnEmptyListWhenNoBebidas() {
        when(productRepository.findAllByCategoryIdAndDeletedAtIsNull(2)).thenReturn(List.of());

        var result = adapter.findAll(2);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository, times(1)).findAllByCategoryIdAndDeletedAtIsNull(2);
    }

    @Test
    void findAll_error_whenRepositoryFails_shouldPropagateException() {
        when(productRepository.findAllByDeletedAtIsNull())
                .thenThrow(new RuntimeException("Erro ao buscar produtos"));

        var exception = assertThrows(RuntimeException.class, () -> adapter.findAll(null));

        assertEquals("Erro ao buscar produtos", exception.getMessage());
        verify(productRepository, times(1)).findAllByDeletedAtIsNull();
    }

    // ==================== FIND ALL BY IDS TESTS ====================

    @Test
    void findAllByIds_success_shouldReturnCoxinhaAndRefrigerante() {
        List<UUID> ids = List.of(coxinhaId, refrigeranteId);

        when(productRepository.findAllById(ids))
                .thenReturn(List.of(coxinhaEntity, refrigeranteEntity));

        var result = adapter.findAllByIds(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.name().equals("Coxinha")));
        assertTrue(result.stream().anyMatch(p -> p.name().equals("Refrigerante Coca-Cola")));

        verify(productRepository, times(1)).findAllById(ids);
    }

    @Test
    void findAllByIds_success_shouldReturnAllFastFoodProducts() {
        List<UUID> ids = List.of(coxinhaId, refrigeranteId, hamburguerId, batataFritaId, sorveteId);

        when(productRepository.findAllById(ids))
                .thenReturn(List.of(coxinhaEntity, refrigeranteEntity, hamburguerEntity, batataFritaEntity, sorveteEntity));

        var result = adapter.findAllByIds(ids);

        assertNotNull(result);
        assertEquals(5, result.size());

        verify(productRepository, times(1)).findAllById(ids);
    }

    @Test
    void findAllByIds_success_shouldReturnEmptyListWhenNoIdsMatch() {
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());

        when(productRepository.findAllById(ids)).thenReturn(List.of());

        var result = adapter.findAllByIds(ids);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository, times(1)).findAllById(ids);
    }

    @Test
    void findAllByIds_error_whenRepositoryFails_shouldPropagateException() {
        List<UUID> ids = List.of(coxinhaId);

        when(productRepository.findAllById(ids))
                .thenThrow(new RuntimeException("Erro ao buscar produtos por IDs"));

        var exception = assertThrows(RuntimeException.class, () -> adapter.findAllByIds(ids));

        assertEquals("Erro ao buscar produtos por IDs", exception.getMessage());
        verify(productRepository, times(1)).findAllById(ids);
    }

    // ==================== UPDATE TESTS ====================

    @Test
    void update_success_shouldUpdateCoxinhaPrice() {
        var updateDTO = new ProductDTO(
            coxinhaId, "Coxinha", "Coxinha de frango frita crocante",
            new BigDecimal("7.00"), true, "/images/coxinha.jpg",
            1, null, null
        );

        var updatedEntity = new ProductEntityJPA(
            coxinhaId, "Coxinha", "Coxinha de frango frita crocante",
            new BigDecimal("7.00"), true, "/images/coxinha.jpg",
            1, LocalDateTime.now(), LocalDateTime.now(), null
        );

        when(productRepository.findById(coxinhaId)).thenReturn(Optional.of(coxinhaEntity));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(lanchesCategory));
        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(updatedEntity);

        var result = adapter.update(updateDTO);

        assertNotNull(result);
        assertEquals(coxinhaId, result.id());
        assertEquals(new BigDecimal("7.00"), result.price());

        verify(productRepository, times(1)).findById(coxinhaId);
        verify(categoryRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    @Test
    void update_success_shouldUpdateRefrigeranteNameAndDescription() {
        var updateDTO = new ProductDTO(
            refrigeranteId, "Refrigerante Coca-Cola Zero", "Refrigerante Coca-Cola Zero Açúcar 350ml",
            null, null, null, null, null, null
        );

        var updatedEntity = new ProductEntityJPA(
            refrigeranteId, "Refrigerante Coca-Cola Zero", "Refrigerante Coca-Cola Zero Açúcar 350ml",
            new BigDecimal("5.00"), true, "/images/coca-cola.jpg",
            2, LocalDateTime.now(), LocalDateTime.now(), null
        );

        when(productRepository.findById(refrigeranteId)).thenReturn(Optional.of(refrigeranteEntity));
        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(updatedEntity);

        var result = adapter.update(updateDTO);

        assertNotNull(result);
        assertEquals(refrigeranteId, result.id());
        assertEquals("Refrigerante Coca-Cola Zero", result.name());
        assertEquals("Refrigerante Coca-Cola Zero Açúcar 350ml", result.description());

        verify(productRepository, times(1)).findById(refrigeranteId);
        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    @Test
    void update_success_shouldChangeHamburguerCategory() {
        var updateDTO = new ProductDTO(
            hamburguerId, null, null, null, null, null,
            3, null, null  // Mudando de Lanches para Acompanhamentos
        );

        var updatedEntity = new ProductEntityJPA(
            hamburguerId, "X-Burger", "Hambúrguer artesanal com queijo",
            new BigDecimal("18.90"), true, "/images/xburger.jpg",
            3, LocalDateTime.now(), LocalDateTime.now(), null
        );

        when(productRepository.findById(hamburguerId)).thenReturn(Optional.of(hamburguerEntity));
        when(categoryRepository.findById(3)).thenReturn(Optional.of(acompanhamentosCategory));
        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(updatedEntity);

        var result = adapter.update(updateDTO);

        assertNotNull(result);
        assertEquals(hamburguerId, result.id());
        assertEquals(3, result.categoryId());

        verify(productRepository, times(1)).findById(hamburguerId);
        verify(categoryRepository, times(1)).findById(3);
        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    @Test
    void update_error_whenProductNotFound_shouldThrowProductNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();
        var updateDTO = new ProductDTO(
            nonExistentId, "Produto Inexistente", null,
            null, null, null, null, null, null
        );

        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        var exception = assertThrows(ProductNotFoundException.class,
                () -> adapter.update(updateDTO));

        assertEquals(String.format(ProductsConstants.PRODUCT_NOT_FOUND, nonExistentId),
                exception.getMessage());

        verify(productRepository, times(1)).findById(nonExistentId);
        verify(productRepository, never()).save(any(ProductEntityJPA.class));
    }

    @Test
    void update_error_whenCategoryNotFound_shouldThrowProductCategoryNotFoundException() {
        var updateDTO = new ProductDTO(
            coxinhaId, null, null, null, null, null,
            999, null, null  // Categoria inexistente
        );

        when(productRepository.findById(coxinhaId)).thenReturn(Optional.of(coxinhaEntity));
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        var exception = assertThrows(ProductCategoryNotFoundException.class,
                () -> adapter.update(updateDTO));

        assertEquals(String.format(ProductsConstants.PRODUCT_CATEGORY_NOT_FOUND, 999),
                exception.getMessage());

        verify(productRepository, times(1)).findById(coxinhaId);
        verify(categoryRepository, times(1)).findById(999);
        verify(productRepository, never()).save(any(ProductEntityJPA.class));
    }

    @Test
    void update_error_whenSaveFails_shouldPropagateException() {
        var updateDTO = new ProductDTO(
            coxinhaId, "Coxinha Premium", null, null, null, null, null, null, null
        );

        when(productRepository.findById(coxinhaId)).thenReturn(Optional.of(coxinhaEntity));
        when(productRepository.save(any(ProductEntityJPA.class)))
                .thenThrow(new RuntimeException("Erro ao atualizar produto"));

        var exception = assertThrows(RuntimeException.class, () -> adapter.update(updateDTO));

        assertEquals("Erro ao atualizar produto", exception.getMessage());
        verify(productRepository, times(1)).findById(coxinhaId);
        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    // ==================== DELETE TESTS ====================

    @Test
    void delete_success_shouldSoftDeleteCoxinha() {
        when(productRepository.findById(coxinhaId)).thenReturn(Optional.of(coxinhaEntity));
        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(coxinhaEntity);

        var result = adapter.delete(coxinhaId);

        assertNotNull(result);
        assertEquals(coxinhaId, result.id());
        assertEquals("Coxinha", result.name());

        ArgumentCaptor<ProductEntityJPA> captor = ArgumentCaptor.forClass(ProductEntityJPA.class);
        verify(productRepository, times(1)).findById(coxinhaId);
        verify(productRepository, times(1)).save(captor.capture());

        assertNotNull(captor.getValue().getDeletedAt());
    }

    @Test
    void delete_success_shouldSoftDeleteRefrigerante() {
        when(productRepository.findById(refrigeranteId)).thenReturn(Optional.of(refrigeranteEntity));
        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(refrigeranteEntity);

        var result = adapter.delete(refrigeranteId);

        assertNotNull(result);
        assertEquals(refrigeranteId, result.id());

        verify(productRepository, times(1)).findById(refrigeranteId);
        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    @Test
    void delete_error_whenProductNotFound_shouldThrowProductNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        var exception = assertThrows(ProductNotFoundException.class,
                () -> adapter.delete(nonExistentId));

        assertEquals(String.format(ProductsConstants.PRODUCT_NOT_FOUND, nonExistentId),
                exception.getMessage());

        verify(productRepository, times(1)).findById(nonExistentId);
        verify(productRepository, never()).save(any(ProductEntityJPA.class));
    }

    @Test
    void delete_error_whenSaveFails_shouldPropagateException() {
        when(productRepository.findById(coxinhaId)).thenReturn(Optional.of(coxinhaEntity));
        when(productRepository.save(any(ProductEntityJPA.class)))
                .thenThrow(new RuntimeException("Erro ao deletar produto"));

        var exception = assertThrows(RuntimeException.class, () -> adapter.delete(coxinhaId));

        assertEquals("Erro ao deletar produto", exception.getMessage());
        verify(productRepository, times(1)).findById(coxinhaId);
        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    // ==================== ENABLE TESTS ====================

    @Test
    void enable_success_shouldEnableInactiveCoxinha() {
        coxinhaEntity.setIsActive(false);

        when(productRepository.findById(coxinhaId)).thenReturn(Optional.of(coxinhaEntity));
        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(coxinhaEntity);

        assertDoesNotThrow(() -> adapter.enable(coxinhaId));

        ArgumentCaptor<ProductEntityJPA> captor = ArgumentCaptor.forClass(ProductEntityJPA.class);
        verify(productRepository, times(1)).findById(coxinhaId);
        verify(productRepository, times(1)).save(captor.capture());

        assertTrue(captor.getValue().getIsActive());
    }

    @Test
    void enable_success_shouldNotChangeAlreadyActiveRefrigerante() {
        refrigeranteEntity.setIsActive(true);

        when(productRepository.findById(refrigeranteId)).thenReturn(Optional.of(refrigeranteEntity));

        assertDoesNotThrow(() -> adapter.enable(refrigeranteId));

        verify(productRepository, times(1)).findById(refrigeranteId);
        verify(productRepository, never()).save(any(ProductEntityJPA.class));
    }

    @Test
    void enable_error_whenProductNotFound_shouldThrowProductCategoryNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        var exception = assertThrows(ProductCategoryNotFoundException.class,
                () -> adapter.enable(nonExistentId));

        assertEquals(String.format(ProductsConstants.PRODUCT_NOT_FOUND, nonExistentId),
                exception.getMessage());

        verify(productRepository, times(1)).findById(nonExistentId);
        verify(productRepository, never()).save(any(ProductEntityJPA.class));
    }

    @Test
    void enable_error_whenSaveFails_shouldPropagateException() {
        coxinhaEntity.setIsActive(false);

        when(productRepository.findById(coxinhaId)).thenReturn(Optional.of(coxinhaEntity));
        when(productRepository.save(any(ProductEntityJPA.class)))
                .thenThrow(new RuntimeException("Erro ao habilitar produto"));

        var exception = assertThrows(RuntimeException.class, () -> adapter.enable(coxinhaId));

        assertEquals("Erro ao habilitar produto", exception.getMessage());
        verify(productRepository, times(1)).findById(coxinhaId);
        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    // ==================== DISABLE TESTS ====================

    @Test
    void disable_success_shouldDisableCoxinha() {
        coxinhaEntity.setIsActive(true);

        when(productRepository.findById(coxinhaId)).thenReturn(Optional.of(coxinhaEntity));
        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(coxinhaEntity);

        assertDoesNotThrow(() -> adapter.disable(coxinhaId));

        ArgumentCaptor<ProductEntityJPA> captor = ArgumentCaptor.forClass(ProductEntityJPA.class);
        verify(productRepository, times(1)).findById(coxinhaId);
        verify(productRepository, times(1)).save(captor.capture());

        assertFalse(captor.getValue().getIsActive());
    }

    @Test
    void disable_success_shouldDisableRefrigerante() {
        refrigeranteEntity.setIsActive(true);

        when(productRepository.findById(refrigeranteId)).thenReturn(Optional.of(refrigeranteEntity));
        when(productRepository.save(any(ProductEntityJPA.class))).thenReturn(refrigeranteEntity);

        assertDoesNotThrow(() -> adapter.disable(refrigeranteId));

        verify(productRepository, times(1)).findById(refrigeranteId);
        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }

    @Test
    void disable_error_whenProductNotFound_shouldThrowProductCategoryNotFoundException() {
        UUID nonExistentId = UUID.randomUUID();
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        var exception = assertThrows(ProductCategoryNotFoundException.class,
                () -> adapter.disable(nonExistentId));

        assertEquals(String.format(ProductsConstants.PRODUCT_NOT_FOUND, nonExistentId),
                exception.getMessage());

        verify(productRepository, times(1)).findById(nonExistentId);
        verify(productRepository, never()).save(any(ProductEntityJPA.class));
    }

    @Test
    void disable_error_whenSaveFails_shouldPropagateException() {
        when(productRepository.findById(coxinhaId)).thenReturn(Optional.of(coxinhaEntity));
        when(productRepository.save(any(ProductEntityJPA.class)))
                .thenThrow(new RuntimeException("Erro ao desabilitar produto"));

        var exception = assertThrows(RuntimeException.class, () -> adapter.disable(coxinhaId));

        assertEquals("Erro ao desabilitar produto", exception.getMessage());
        verify(productRepository, times(1)).findById(coxinhaId);
        verify(productRepository, times(1)).save(any(ProductEntityJPA.class));
    }
}


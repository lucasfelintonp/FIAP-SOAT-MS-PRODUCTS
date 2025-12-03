package br.com.fiap.fastfood.category.infrastructure.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryEntityJPATest {

    @Test
    void allArgsConstructor_shouldSetIdAndName() {
        var entity = new ProductCategoryEntityJPA(10, "Bebidas");

        assertEquals(10, entity.getId());
        assertEquals("Bebidas", entity.getName());
    }

    @Test
    void nameOnlyConstructor_shouldSetNameAndIdNull() {
        var entity = new ProductCategoryEntityJPA("Lanches");

        assertNull(entity.getId());
        assertEquals("Lanches", entity.getName());
    }

    @Test
    void noArgsConstructor_andSetters_shouldWork_forSobremesas() {
        var entity = new ProductCategoryEntityJPA();
        entity.setName("Sobremesas");
        entity.setId(5);

        assertEquals(5, entity.getId());
        assertEquals("Sobremesas", entity.getName());
    }

    @Test
    void equalsAndHashCode_shouldConsiderIdAndName() {
        var a = new ProductCategoryEntityJPA(1, "Bebidas");
        var b = new ProductCategoryEntityJPA(1, "Bebidas");
        var c = new ProductCategoryEntityJPA(2, "Lanches");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void equals_shouldReturnFalseForNullOrDifferentType() {
        var a = new ProductCategoryEntityJPA(1, "Bebidas");

        assertNotEquals(a, null);
        assertNotEquals(a, "not-an-entity");
    }

    @Test
    void jpaAnnotations_shouldBePresent_andNameConstraintsFollowDomain() throws NoSuchFieldException {
        var clazz = ProductCategoryEntityJPA.class;

        var idField = clazz.getDeclaredField("id");
        assertTrue(idField.isAnnotationPresent(Id.class), "id should be annotated with @Id");
        assertTrue(idField.isAnnotationPresent(GeneratedValue.class), "id should be annotated with @GeneratedValue");

        var nameField = clazz.getDeclaredField("name");
        Column column = nameField.getAnnotation(Column.class);
        assertNotNull(column, "name should be annotated with @Column");
        // domain expects up to 100 chars and not nullable
        assertEquals(100, column.length(), "name column length should be 100");
        assertFalse(column.nullable(), "name column should be not nullable");
    }
}


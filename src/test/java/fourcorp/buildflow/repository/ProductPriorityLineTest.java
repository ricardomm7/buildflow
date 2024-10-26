package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.Operation;
import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductPriorityLineTest {

    private ProductPriorityLine productPriorityLine;
    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        productPriorityLine = new ProductPriorityLine();
        product1 = new Product("ProductA", new LinkedList<>(List.of(new Operation("Cutting"), new Operation("Assembling"))));
        product2 = new Product("ProductB", new LinkedList<>(List.of(new Operation("Cutting2"), new Operation("Assembling2"))));
        product3 = new Product("ProductC", new LinkedList<>(List.of(new Operation("Cutting3"), new Operation("Assembling3"))));

        // Adiciona produtos com diferentes prioridades
        productPriorityLine.create(product1, PriorityOrder.HIGH);
        productPriorityLine.create(product2, PriorityOrder.NORMAL);
        productPriorityLine.create(product3, PriorityOrder.HIGH);
    }

    @Test
    void testCreateAndRetrieveAllProducts() {
        List<Product> allProducts = productPriorityLine.getAllProducts();
        assertEquals(3, allProducts.size(), "Should return all 3 products added.");
        assertTrue(allProducts.contains(product1), "Should contain ProductA.");
        assertTrue(allProducts.contains(product2), "Should contain ProductB.");
        assertTrue(allProducts.contains(product3), "Should contain ProductC.");
    }

    @Test
    void testGetProductsByPriority() {
        List<Product> highPriorityProducts = productPriorityLine.getProductsByPriority(PriorityOrder.HIGH);
        assertEquals(2, highPriorityProducts.size(), "Should return 2 products with HIGH priority.");
        assertTrue(highPriorityProducts.contains(product1), "Should contain ProductA with HIGH priority.");
        assertTrue(highPriorityProducts.contains(product3), "Should contain ProductC with HIGH priority.");

        List<Product> mediumPriorityProducts = productPriorityLine.getProductsByPriority(PriorityOrder.NORMAL);
        assertEquals(1, mediumPriorityProducts.size(), "Should return 1 product with MEDIUM priority.");
        assertTrue(mediumPriorityProducts.contains(product2), "Should contain ProductB with MEDIUM priority.");

        List<Product> lowPriorityProducts = productPriorityLine.getProductsByPriority(PriorityOrder.LOW);
        assertEquals(0, lowPriorityProducts.size(), "Should return 0 products with LOW priority.");
    }

    @Test
    void testRemoveAll() {
        productPriorityLine.removeAll();
        List<Product> allProductsAfterRemoval = productPriorityLine.getAllProducts();
        assertEquals(0, allProductsAfterRemoval.size(), "Should return an empty list after removing all products.");
    }

    @Test
    void testGetProductPriorityLineStructure() {
        var priorityLine = productPriorityLine.getProductPriorityLine();
        assertEquals(2, priorityLine.getKeys().size(), "Should contain 2 different priority levels.");
        assertTrue(priorityLine.getKeys().contains(PriorityOrder.HIGH), "Should contain HIGH priority key.");
        assertTrue(priorityLine.getKeys().contains(PriorityOrder.NORMAL), "Should contain MEDIUM priority key.");
    }
}

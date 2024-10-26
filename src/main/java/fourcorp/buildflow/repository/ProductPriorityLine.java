package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a repository that manages products with associated priority orders.
 * It allows for creating products, retrieving products by priority,
 * and managing the overall product priority line.
 */
public class ProductPriorityLine {
    private static MapLinked<Product, PriorityOrder, String> productPriorityLine;

    /**
     * Constructs a ProductPriorityLine instance, initializing the product priority line storage.
     */
    public ProductPriorityLine() {
        productPriorityLine = new MapLinked<>();
    }

    /**
     * Creates a new product with an associated priority order in the product priority line.
     *
     * @param product  the product to be added
     * @param priority the priority order associated with the product
     */
    public void create(Product product, PriorityOrder priority) {
        productPriorityLine.newItem(product, priority);
    }

    /**
     * Retrieves the entire product priority line.
     *
     * @return the product priority line as a MapLinked structure
     */
    public MapLinked<Product, PriorityOrder, String> getProductPriorityLine() {
        return productPriorityLine;
    }

    /**
     * Retrieves all products from the product priority line, regardless of priority.
     *
     * @return a list of all products in the product priority line
     */
    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        for (PriorityOrder priority : productPriorityLine.getKeys()) {
            List<Product> products = productPriorityLine.getByKey(priority);
            allProducts.addAll(products);
        }
        return allProducts;
    }

    /**
     * Retrieves products associated with a specific priority order.
     *
     * @param a the priority order to filter products by
     * @return a list of products associated with the specified priority order
     */
    public List<Product> getProductsByPriority(PriorityOrder a) {
        return productPriorityLine.getByKey(a);
    }

    /**
     * Removes all products from the product priority line.
     */
    public void removeAll() {
        productPriorityLine.removeAll();
    }
}

package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductPriorityLine {
    private static MapLinked<Product, PriorityOrder, String> productPriorityLine;

    public ProductPriorityLine() {
        productPriorityLine = new MapLinked<>();
    }

    public void create(Product product, PriorityOrder priority) {
        productPriorityLine.newItem(product, priority);
    }

    public MapLinked<Product, PriorityOrder, String> getProductPriorityLine() {
        return productPriorityLine;
    }

    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        for (PriorityOrder priority : productPriorityLine.getKeys()) {
            List<Product> products = productPriorityLine.getByKey(priority);
            allProducts.addAll(products);
        }
        return allProducts;
    }

    public List<Product> getProductsByPriority(PriorityOrder a) {
        return productPriorityLine.getByKey(a);
    }

    public void removeAll() {
        productPriorityLine.removeAll();
    }
}

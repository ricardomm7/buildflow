package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.PriorityOrder;
import fourcorp.buildflow.domain.Product;

import java.util.List;

public class ProductPriorityLine {
    private static MapLinked<Product, PriorityOrder, String> productPriorityLine;

    public void create(Product product, PriorityOrder priority) {
        productPriorityLine.newItem(product, priority);
    }

    public MapLinked<Product, PriorityOrder, String> getProductPriorityLine() {
        return productPriorityLine;
    }

    public List<Product> getProductsByPriority(PriorityOrder a) {
        return productPriorityLine.getByKey(a);
    }
}

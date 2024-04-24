package io.catalyte.demo.products;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();
    Product getProductsById(int id);
    Product createProduct(Product productToCreate);
    Product editProduct(Product productToEdit, int id);
    void deleteProduct(int id);
    boolean isUniqueSku(int Sku);
    boolean isValidProduct(Product productToValidate);
}

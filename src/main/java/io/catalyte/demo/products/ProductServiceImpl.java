package io.catalyte.demo.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * returns a list of all products in the database.
     *
     * @return  list of products
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * returns the product with the matching id, given it exists.
     *
     * @param id    search key to query the database
     * @return      product with the matching id
     */
    public Product getProductsById(int id) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not found");
        return optional.get();
    }

    /**
     * adds a product to the database.
     *
     * @param productToCreate   product to add to the database
     * @return                  the saved product
     */
    public Product createProduct(Product productToCreate) {
        if (!productToCreate.isValid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid object sent");
        if (!isUniqueSku(productToCreate.getSku())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Sku already in database");
        return productRepository.save(productToCreate);
    }

    /**
     * overwrites existing product with new product field values.
     *
     * @param productToEdit     product values to overwrite the existing product
     * @param id                id of product to be overwritten
     * @return                  the overwritten product
     */
    public Product editProduct(Product productToEdit, int id) {
        //verify path id is valid, path product is valid, and path id matches the object's id
        Product product = getProductsById(id);
        if (!productToEdit.isValid()) throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid object sent");
        if (productToEdit.getId() != id) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object id does not match path variable id");

        //verify sku doesn't match any other product
        List<Product> allProducts = productRepository.findAll();
        for (Product checkProduct: allProducts) {
            if ((checkProduct.getSku() == productToEdit.getSku()) && (checkProduct != product)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Sku already in database");
            }
        }

        //set all attributes to match the  passed product
        product.setName(productToEdit.getName());
        product.setDescription(productToEdit.getDescription());
        product.setSku(productToEdit.getSku());
        product.setPrice(productToEdit.getPrice());
        product.setType(productToEdit.getType());
        product.setManufacturer(productToEdit.getManufacturer());
        return productRepository.save(product);
    }

    /**
     * deletes a product with the given id from the database.
     *
     * @param id    id of the product to delete
     */
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    /**
     * iterates through all products in the database to determine whether the given sku is unique or not.
     *
     * @param Sku   sku to check for duplicates
     * @return      true if sku is unique, false otherwise
     */
    public boolean isUniqueSku(int Sku) {
        List<Product> allProducts = productRepository.findAll();
        for (Product product: allProducts) {
            if (product.getSku() == Sku) return false;
        }
        return true;
    }

    /**
     * verifies that the given product contains proper values for all fields
     * and the sku does not already exist in the database.
     *
     * @param productToValidate     product to validate
     * @return                      true if product is validated, false otherwise
     */
    public boolean isValidProduct(Product productToValidate) {
        //verify no default attributes and unique sku
        return productToValidate.isValid() && isUniqueSku(productToValidate.getSku());
    }
}

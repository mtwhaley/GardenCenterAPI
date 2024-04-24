package io.catalyte.demo.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTests {

    @Mock
    ProductRepository productRepository;
    ProductService productService;
    Product testProduct;
    Product testProduct2;
    List<Product> testList;

    @BeforeEach
    public void setUp() {
        productService = new ProductServiceImpl(productRepository);
        testProduct = new Product(123456, "Default Name", "Default Description", "Default Type", "Default Manufacturer", "9.99");
        testProduct2 = new Product(456789, "name2", "description2", "type", "manufacturer", "19.99");
        testList = new ArrayList<Product>();
        testList.add(testProduct);
    }

    @Test
    public void isUniqueSku_duplicateSku_returnsFalse() {
        when(productRepository.findAll()).thenReturn(testList);
        boolean result = productService.isUniqueSku(123456);
        assertFalse(result);
    }

    @Test
    public void isUniqueSku_uniqueSku_returnsTrue() {
        when(productRepository.findAll()).thenReturn(testList);
        boolean result = productService.isUniqueSku(654321);
        assertTrue(result);
    }

    @Test
    public void createProduct_withValidProduct_returnsPersistedProduct() {
        when(productRepository.findAll()).thenReturn(testList);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        Product result = productService.createProduct(testProduct2);
        assertEquals(result.getName(), "Default Name");
    }

    @Test
    public void createProduct_withInvalidProduct_throwsResponseException() {
        testProduct.setName(null);
        boolean exceptionFound=false;
        try {productService.createProduct(testProduct);}
        catch(ResponseStatusException e) {exceptionFound=true;}
        assertTrue(exceptionFound);
    }

    @Test
    public void createProduct_withDuplicateSku_throwsResponseException() {
        when(productRepository.findAll()).thenReturn(testList);
        testProduct2.setSku(testProduct.getSku());
        boolean exceptionFound=false;
        try {productService.createProduct(testProduct2);}
        catch(ResponseStatusException e) {exceptionFound=true;}
        assertTrue(exceptionFound);
    }

    @Test
    public void getProducts_returnsList() {
        when(productRepository.findAll()).thenReturn(testList);
        List<Product> list = productService.getProducts();
        assertEquals(list.get(0).getName(), "Default Name");
    }

    @Test
    public void getProductsById_withValidId_returnsProduct() {
        when(productRepository.findById(0)).thenReturn(Optional.ofNullable(testProduct));
        Product result = productService.getProductsById(0);
        assertEquals(result.getName(),  "Default Name");
    }

    @Test
    public void getProductsById_withInvalidId_throwsResponseException() {
        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        boolean exceptionFound=false;
        try {productService.getProductsById(5);}
        catch (ResponseStatusException e) {exceptionFound=true;}
        assertTrue(exceptionFound);
    }

    @Test
    public void editProduct_withValidProduct_returnsPersistedProduct() {
        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        testProduct2.setId(testProduct.getId());
        Product result = productService.editProduct(testProduct2, testProduct.getId());
        assertEquals(result.getName(), "name2");
    }

    @Test
    public void editProduct_withInvalidProduct_throwsResponseException() {
        boolean exceptionFound=false;
        testProduct2.setId(testProduct.getId());
        testProduct2.setName(null);
        try {
            productService.editProduct(testProduct2, 1);
        } catch (ResponseStatusException e) {
            exceptionFound=true;
        }
        assertTrue(exceptionFound);
    }

    @Test
    public void editProduct_withMismatchedIds_throwsResponseException() {
        boolean exceptionFound=false;
        Product clonedProduct = new Product(testProduct.getSku(), testProduct.getName(), testProduct.getDescription(), testProduct.getType(), testProduct.getManufacturer(), testProduct.getPrice());
        clonedProduct.setId(0);
        try {
            productService.editProduct(clonedProduct, 1);
        } catch (ResponseStatusException e) {
            exceptionFound=true;
        }
        assertTrue(exceptionFound);
    }

    @Test
    public void editProduct_withConflictingSku_throwsResponseException() {
        when(productRepository.findAll()).thenReturn(testList);
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.ofNullable(testProduct));
        boolean exceptionFound=false;
        testList.add(testProduct2);

        //create template to update testProduct
        Product clonedProduct = new Product(testProduct.getSku(), testProduct.getName(), testProduct.getDescription(), testProduct.getType(), testProduct.getManufacturer(), testProduct.getPrice());
        clonedProduct.setId(testProduct.getId());

        //set template sku to the same sku as testProduct2
        clonedProduct.setSku(testProduct2.getSku());

        try {
            productService.editProduct(clonedProduct, testProduct.getId());
        } catch (ResponseStatusException e) {
            exceptionFound=true;
        }
        assertTrue(exceptionFound);
    }

    @Test
    public void editProduct_withInvalidId_throwsResponseException() {
        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        boolean exceptionFound=false;
        Product clonedProduct = new Product(testProduct.getSku(), testProduct.getName(), testProduct.getDescription(), testProduct.getType(), testProduct.getManufacturer(), testProduct.getPrice());
        clonedProduct.setId(1);
        try {
            productService.editProduct(clonedProduct, 1);
        } catch (ResponseStatusException e) {
            exceptionFound=true;
        }
        assertTrue(exceptionFound);
    }

    @Test
    public void isValidProduct_withNullAttributes_returnsFalse() {
        testProduct2.setName(null);
        boolean result = productService.isValidProduct(testProduct2);
        assertFalse(result);
    }

    @Test
    public void isValidProduct_withDuplicateSku_returnsFalse() {
        when(productRepository.findAll()).thenReturn(testList);
        testProduct2.setSku(123456);
        boolean result = productService.isValidProduct(testProduct2);
        assertFalse(result);
    }

    @Test
    public void isValidProduct_withValidProduct_returnsTrue() {
        when(productRepository.findAll()).thenReturn(testList);
        boolean result = productService.isValidProduct(testProduct2);
        assertTrue(result);
    }
}
package io.catalyte.demo.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int sku;
    private String name, description, type, manufacturer, price;

    //constructors
    public Product() {}

    public Product(int sku, String name, String description, String type, String manufacturer, String price) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.type = type;
        this.manufacturer = manufacturer;
        this.price = price;
    }

    //getters
    public int getId() {return id;}
    public int getSku() {return sku;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getType() {return type;}
    public String getManufacturer() {return manufacturer;}
    public String getPrice() {return price;}

    //setters
    public void setId(int id) {this.id = id;}
    public void setSku(int sku) {this.sku = sku;}
    public void setName(String name) {this.name = name;}
    public void setDescription(String description) {this.description = description;}
    public void setType(String type) {this.type = type;}
    public void setManufacturer(String manufacturer) {this.manufacturer = manufacturer;}
    public void setPrice(String price) {this.price = price;}

    /**
     * validate whether the price attribute of this class is valid or not.
     * a valid price is a string which can be converted to a Double and contains exactly 2 decimal places.
     *
     * @return      true if the price attribute is a valid price, false otherwise.
     */
    private boolean validatePrice() {
        //validate price is in format of a double
        try {Double.valueOf(this.price);}
        catch (NumberFormatException e) {return false;}

        //validate exactly 2 decimals
        int decimalIndex = this.price.indexOf('.');
        if (decimalIndex == -1) return false;
        int stringLength = this.price.length();

        return decimalIndex + 3 == stringLength;
    }

    /**
     * validate the object, checking that all fields have non-null and non-zero values.
     * The price is also validated to be a string representation of a double with exactly 2 decimal places.
     *
     * @return      true if the object is valid, false otherwise.
     */
    @JsonIgnore
    public boolean isValid() {
        if (this.name == null) return false;
        if (this.description == null) return false;
        if (this.type == null) return false;
        if (this.manufacturer == null) return false;
        if (this.sku == 0) return false;
        if (this.price == null) return false;
        return (validatePrice());
    }
}

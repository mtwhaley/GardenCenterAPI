
# Garden Center Product API

A REST API designed for Garden Center LLC, connecting their database to user-facing web applications. The server will create a table in the database named, "product" and interact with it appropriately. Upon terminating the server processes, the product table will be removed from the database and the data will be lost.  

## Pre-requisites
This API connects to a PostGreSQL database, named "postgres". The server was developed to be executed using IntelliJ IDEA.

## Startup
Open the GardenCenterApplication file and use the green play button in the left gutter to start the application.

The server will start on localhost:8085

## JSON body
Use the following as a JSON body for a POST. We do not supply an id on the POST, as that is the application's job to manage.
```
{
    "sku": 123456,
    "name": "some name",
    "description": "some description",
    "type": "some type",
    "manufacturer": "some manufacturer",
    "price": "12.99"
}
```

## Usage
Use `localhost:8085` as the root of the API requests. Acceptable requests are as follows:
#### GET
    /products
&emsp;*returns an array of products*  

####    
    /products/<product-id>
&emsp;*returns the product with the specified id*

#### POST
    /products
&emsp;*adds the sent product to the database*
#### PUT
    products/<product-id>
&emsp;*updates the product with the specified id*

&nbsp;

&ensp;PUT requests should send a JSON body in the following format:
```
{
    "id": 1
    "sku": 123456,
    "name": "some name",
    "description": "some description",
    "type": "some type",
    "manufacturer": "some manufacturer",
    "price": "12.99"
}
```
&emsp;**note that the id in the JSON body must match the id in the path*

#### DELETE
    products/<product-id>
&emsp;*deletes the product with the specified id*

## Testing
Tests developed for product service implementation file, "ProductServiceImple", with 96% line coverage, 87% method coverage, and 86% branch coverage.

#### Test Instructions
To run tests with coverage, open the program in a code editor (IntelliJ) and navigate to:

    garden-center-product-web-service/src/test/java/io.catalyte.demo/product/ProductServiceImpleTests

Use the green play button in the gutter to the left of the class signature and select `Run 'ProductServiceImpleTests' with Coverage`.

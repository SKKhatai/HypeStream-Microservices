package com.hypestream.inventory.controller;

import com.hypestream.inventory.model.Product;
import com.hypestream.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RestController tells Spring that this class is a REST API controller.
 * It automatically serializes return values (like List<Product>) into JSON format.
 * 
 * @RequestMapping("/api/v1/products") defines the base path for all endpoints in this class.
 */
@RestController
@RequestMapping("/api/v1/products")
/**
 * @RequiredArgsConstructor is a Lombok annotation that generates a constructor
 * containing all final fields. This is the modern way to do "Constructor Dependency Injection".
 */
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    /**
     * @GetMapping maps HTTP GET requests to this method.
     * Path: GET /api/v1/products
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * @PostMapping maps HTTP POST requests to this method.
     * Used by Admins to create new products.
     * 
     * @RequestBody tells Spring to convert the incoming JSON payload into a Product object.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // =========================================================================
    // TODO: Write a GET endpoint that finds a product by its SKU.
    // 
    // Requirements:
    // 1. Use the HTTP GET method. The path should be "/{sku}" (which extends the base path to /api/v1/products/{sku})
    // 2. Use @PathVariable("sku") String sku to capture the SKU from the URL.
    // 3. Query the productRepository using findBySku(sku).
    // 4. Return a ResponseEntity<Product>. If found, return HttpStatus.OK (200). 
    //    If not found, return HttpStatus.NOT_FOUND (404).
    //
    // Hint on returning 404: 
    // productRepository.findBySku(sku) returns an Optional<Product>.
    // You can use: 
    // return productRepository.findBySku(sku)
    //         .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
    //         .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    // =========================================================================
    @GetMapping("/{sku}")
    public ResponseEntity<Product> getProductBySku(@PathVariable("sku") String sku){

        return productRepository.findBySku(sku)
        .map(product -> new ResponseEntity<>(product, HttpStatus.OK)) 
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

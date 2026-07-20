package com.hypestream.inventory.controller;

import com.hypestream.inventory.model.Product;
import com.hypestream.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")

@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

   
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{sku}")
    public ResponseEntity<Product> getProductBySku(@PathVariable("sku") String sku){

        return productRepository.findBySku(sku)
        .map(product -> new ResponseEntity<>(product, HttpStatus.OK)) 
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

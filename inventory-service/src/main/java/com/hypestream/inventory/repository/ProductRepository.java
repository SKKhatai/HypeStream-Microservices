package com.hypestream.inventory.repository;

import com.hypestream.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Repository tells Spring that this interface is a Database Access Object (DAO).
 * JpaRepository<Product, Long> takes two parameters:
 * 1. The Entity Class name (Product)
 * 2. The data type of the Entity's Primary Key (Long)
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Spring Data JPA has a feature called "Query Methods".
     * It parses the method name and automatically writes the SQL query!
     * 
     * For example, "findBySku" automatically generates:
     * "SELECT * FROM products WHERE sku = ?"
     */
    Optional<Product> findBySku(String sku);

    // =========================================================================
    // TODO: Write a method signature that finds a Product by its name.
    // Hint: It should return an Optional<Product> and follow the Spring Query Method naming pattern.
    // =========================================================================
    Optional<Product> findByName(String name);
    
    
}
package com.hypestream.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * The @Entity annotation tells Spring Data JPA/Hibernate that this class
 * maps directly to a table in our MySQL database.
 */
@Entity
/**
 * The @Table annotation allows us to define the specific name of the table in MySQL.
 * If omitted, Hibernate will name the table "product" (based on class name).
 */
@Table(name = "products")
/**
 * @Data is a Lombok annotation that automatically generates:
 * Getter methods, Setter methods, toString(), equals(), and hashCode() at compile time.
 * This keeps our Java code clean.
 */
@Data
/**
 * Lombok annotations to generate constructors automatically.
 * Hibernate requires a No-Args constructor to instantiate the object when reading from the DB.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /**
     * @Id marks this field as the Primary Key of the table.
     * @GeneratedValue defines the strategy. IDENTITY means MySQL will auto-increment the ID (1, 2, 3...)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column allows us to configure table constraints.
     * nullable = false means this field cannot be null in the database.
     * unique = true means two shoes cannot have the same SKU.
     */
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sku;

    /**
     * For prices and financial values, never use float or double in Java.
     * Float/Double have floating-point precision issues (e.g., 10.00 - 9.90 might equal 0.09999999).
     * BigDecimal guarantees exact mathematical precision.
     */
    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;
}

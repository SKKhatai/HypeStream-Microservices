package com.hypestream.inventory.seeder;

import com.hypestream.inventory.model.Product;
import com.hypestream.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @Component registers this class as a Spring-managed Bean.
 * 
 * Implementing CommandLineRunner tells Spring Boot to automatically run the "run"
 * method when the application starts up. This is perfect for database seeding.
 */
@Component
@RequiredArgsConstructor
@Slf4j // Lombok annotation that generates a Logger instance named "log"
public class DatabaseSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only seed data if the database is currently empty
        if (productRepository.count() == 0) {
            log.info("Database is empty. Seeding initial product catalog...");

            Product jordan1 = Product.builder()
                    .name("Air Jordan 1 Retro High")
                    .sku("AJ1-RETRO-001")
                    .price(new BigDecimal("180.00"))
                    .stock(100)
                    .build();

            Product dunkLow = Product.builder()
                    .name("Nike Dunk Low Panda")
                    .sku("DUNK-PANDA-002")
                    .price(new BigDecimal("115.00"))
                    .stock(150)
                    .build();

            // =========================================================================
            // TODO: Create a third product representing an "Air Max 90" shoe:
            // - Name: "Nike Air Max 90"
            // - SKU: "AM90-001"
            // - Price: 150.00 (Remember to use new BigDecimal("150.00"))
            // - Stock: 50
            //
            // Make sure to add it to the List.of() or save it directly using productRepository.
            // =========================================================================
            Product nikeaf = Product.builder()
                    .name("Nike Air Force 1")
                    .sku("AM90-001")
                    .price(new BigDecimal("8000.00"))
                    .stock(50)
                    .build();

            productRepository.saveAll(Arrays.asList(jordan1, dunkLow, nikeaf));
            
            log.info("Database seeded successfully with {} products.", productRepository.count());
        } else {
            log.info("Database already contains data. Skipping seeder.");
        }
    }
}

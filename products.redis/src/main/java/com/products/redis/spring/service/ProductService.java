package com.products.redis.spring.service;

import com.products.redis.spring.entity.Product;
import com.products.redis.spring.repository.ProductRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Product> hashOps;
    private static final String HASH_KEY = "productsu";

    public ProductService(ProductRepository productRepository, RedisTemplate<String, Object> redisTemplate) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
        this.hashOps = redisTemplate.opsForHash(); // initialize hashOps here
    }

    // Save new product to DB and Redis
    public Product saveProduct(Product product) {
        Product saved = productRepository.save(product);
        hashOps.put(HASH_KEY, String.valueOf(saved.getId()), saved);
        return saved;
    }

    // Get all products from Redis
    public List<Product> getAllProducts() {
        return List.copyOf(hashOps.entries(HASH_KEY).values());
    }

    // Update product in DB and Redis
    public Product updateProduct(Long id, Product updated) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(updated.getName());
        product.setPrice(updated.getPrice());
        Product saved = productRepository.save(product);
        hashOps.put(HASH_KEY, String.valueOf(saved.getId()), saved);
        return saved;
    }

    // Get product by ID from Redis first, then DB
    public Product getProductById(Long id) {
        Product product = hashOps.get(HASH_KEY, String.valueOf(id));
        if (product != null) return product;
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Delete product from DB and Redis
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        hashOps.delete(HASH_KEY, String.valueOf(id));
    }
}

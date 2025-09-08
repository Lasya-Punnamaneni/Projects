package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "products") // For annotations
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private HashOperations<String, String, Product> hashOperations;

    private static final String REDIS_KEY = "PRODUCTS";

    /** Single save or update: updates @CachePut and Redis hash */
    @CachePut(key = "#result.id")
    public Product saveProduct(Product product) {
        Product saved = productRepository.save(product);
        hashOperations.put(REDIS_KEY, saved.getId().toString(), saved);
        return saved;
    }

    /** Bulk save: saves all to DB and updates Redis hash manually */
    public List<Product> saveProducts(List<Product> products) {
        List<Product> savedProducts = productRepository.saveAll(products);
        for (Product p : savedProducts) {
            hashOperations.put(REDIS_KEY, p.getId().toString(), p);
        }
        return savedProducts;
    }

    /** Get product by ID: @Cacheable + Redis hash update */
    @Cacheable(key = "#id")
    public Product getProduct(Long id) {
        Product cached = hashOperations.get(REDIS_KEY, id.toString());
        if (cached != null) return cached;

        Optional<Product> dbProduct = productRepository.findById(id);
        dbProduct.ifPresent(p -> hashOperations.put(REDIS_KEY, p.getId().toString(), p));
        return dbProduct.orElse(null);
    }

    /** Get all products from DB */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /** Delete product by ID: remove from DB, Redis hash, and cache */
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        hashOperations.delete(REDIS_KEY, id.toString());
        cacheManager.getCache("products").evict(id);
    }

    /** Clear all DB + Redis hash + cache */
    public void clearAll() {
        productRepository.deleteAll();
        hashOperations.getOperations().delete(REDIS_KEY);
        cacheManager.getCache("products").clear();
    }

    /** Sync all DB products into Redis hash */
    public void syncAllToRedis() {
        List<Product> allProducts = productRepository.findAll();
        hashOperations.getOperations().delete(REDIS_KEY);
        for (Product p : allProducts) {
            hashOperations.put(REDIS_KEY, p.getId().toString(), p);
        }
    }
}

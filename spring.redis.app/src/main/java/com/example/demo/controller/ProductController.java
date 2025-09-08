package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /** Save a single product */
    @PostMapping
    public String save(@RequestBody Product product) {
        productService.saveProduct(product);
        return "Product saved successfully!";
    }

    /** Bulk save products */
    @PostMapping("/bulk")
    public String saveBulk(@RequestBody List<Product> products) {
        List<Product> saved = productService.saveProducts(products);
        return saved.size() + " products saved and cached in Redis!";
    }

    /** Get product by ID */
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /** Get all products from DB */
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    /** Delete product by ID */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted from DB and Redis!";
    }
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id); // ensure ID is set
        return productService.saveProduct(product); // @CachePut + hash update
    }

    /** Clear all products from DB and Redis */
    @DeleteMapping("/clear")
    public String clearAll() {
        productService.clearAll();
        return "All products cleared from DB and Redis!";
    }

    /** Sync all DB products into Redis hash */
    @PostMapping("/sync-redis")
    public String syncRedis() {
        productService.syncAllToRedis();
        return "All DB products synced to Redis!";
    }
}

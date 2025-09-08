package com.example.demo.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L; // required for Serializable

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    // --- constructors ---
    public Product() {
    }

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    // --- getters & setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

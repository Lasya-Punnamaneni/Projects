package com.login.usermanagement;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name="usertable")
public class User {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private long id;
private String firstname;
private String lastName;
@Column(unique = true, nullable = false)
private String username;
@Column(nullable=false)
private String password;
private LocalDateTime createdAt= LocalDateTime.now();
private LocalDateTime updatedAt= LocalDateTime.now();
 private String role;
  public long getId() {
        return id;
    }
public String getFirstname() {
    return firstname;
}
public void setFirstname(String firstname) {
    this.firstname = firstname;
}
public String getLastName() {
    return lastName;
}
public void setLastName(String lastName) {
    this.lastName = lastName;
}
public String getUsername() {
    return username;
}
public void setUsername(String username) {
    this.username = username;
}
public String getPassword() {
    return password;
}
public void setPassword(String password) {
    this.password = password;
}


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
}

package com.pro.ems_bk.controller;

import com.pro.ems_bk.entity.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @GetMapping("/employee") // added '/' to make it RESTful
    public Employee getEmployee() {
        // Use constructor properly (no `id:`, `firstName:` syntax in Java)
        Employee employee = new Employee(1L, "Lasya", "lasya@example.com", "IT");
        return employee;
    }
}

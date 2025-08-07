package com.vagarious.backend.hrms.controller;

import com.vagarious.backend.hrms.dto.EmployeeCreateRequest;
import com.vagarious.backend.hrms.model.User;
import com.vagarious.backend.hrms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeCreateRequest request) {
        try {
            User employee = userService.createEmployee(request.getUsername(), request.getPassword());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Employee created successfully");
            response.put("employee", employee);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/employees")
    public ResponseEntity<List<User>> getAllEmployees() {
        List<User> employees = userService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        boolean deleted = userService.deleteEmployee(id);
        if (deleted) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Employee deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Employee not found or cannot be deleted");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Admin API is working!");
    }
} 
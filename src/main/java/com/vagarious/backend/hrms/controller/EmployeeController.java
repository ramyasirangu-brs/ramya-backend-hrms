package com.vagarious.backend.hrms.controller;

import com.vagarious.backend.hrms.model.Employee;
import com.vagarious.backend.hrms.model.Experience;
import com.vagarious.backend.hrms.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // ✅ Create or Save Employee
    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        // ✅ Decode base64 resume if not null
        if (employee.getResume() != null) {
            try {
                String base64String = new String(employee.getResume());
                byte[] decodedResume = Base64.getDecoder().decode(base64String);
                employee.setResume(decodedResume);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        // ✅ Set employee reference in experiences
        List<Experience> experiences = employee.getExperienceDetails();
        if (experiences != null) {
            for (Experience exp : experiences) {
                exp.setEmployee(employee);
            }
        }

        // ✅ Save employee with all embedded & child data
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    // ✅ Get all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    // ✅ Get single employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable String id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Update employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @RequestBody Employee updatedEmployee) {
        return employeeRepository.findById(id).map(existing -> {
            updatedEmployee.setEmployeeId(id);

            // Set back-reference for experiences
            List<Experience> updatedExperiences = updatedEmployee.getExperienceDetails();
            if (updatedExperiences != null) {
                for (Experience exp : updatedExperiences) {
                    exp.setEmployee(updatedEmployee);
                }
            }

            return ResponseEntity.ok(employeeRepository.save(updatedEmployee));
        }).orElse(ResponseEntity.notFound().build());
    }
}

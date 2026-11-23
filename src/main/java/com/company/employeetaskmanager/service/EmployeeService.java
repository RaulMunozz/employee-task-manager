package com.company.employeetaskmanager.service;

import com.company.employeetaskmanager.model.Employee;
import com.company.employeetaskmanager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public Employee createEmployee(String name, String email, String department, double salary) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty");
        }

        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }

        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }

        Employee employee = new Employee(name, email, department, salary);
        return repository.save(employee);
    }

    public Employee updateEmployee(Long id, String name, String email, String department, double salary) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + id));

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty");
        }

        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }

        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }

        existing.setName(name);
        existing.setEmail(email);
        existing.setDepartment(department);
        existing.setSalary(salary);

        return repository.save(existing);
    }

    public boolean deleteEmployee(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
        repository.deleteById(id);
        return true;
    }

    public Employee getEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + id));
    }

    public long getEmployeeCount() {
        return repository.count();
    }

    public List<Employee> getAllEmployeesSorted() {
        return repository.findAllByOrderByNameAsc();
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        return repository.findByDepartment(department);
    }
}

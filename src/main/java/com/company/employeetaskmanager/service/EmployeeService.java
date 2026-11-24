package com.company.employeetaskmanager.service;

import com.company.employeetaskmanager.model.Employee;
import com.company.employeetaskmanager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class EmployeeService {

    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final int MAX_FIELD_LENGTH = 100;
    private static final double MAX_SALARY = 10_000_000;

    @Autowired
    private EmployeeRepository repository;

    public Employee createEmployee(String name, String email, String department, double salary) {
        validate(name, email, department, salary);
        return repository.save(new Employee(name.trim(), email.trim(), department.trim(), salary));
    }

    public Employee updateEmployee(Long id, String name, String email, String department, double salary) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + id));

        validate(name, email, department, salary);
        existing.setName(name.trim());
        existing.setEmail(email.trim());
        existing.setDepartment(department.trim());
        existing.setSalary(salary);

        return repository.save(existing);
    }

    private void validate(String name, String email, String department, double salary) {
        if (name == null || name.trim().isEmpty() || name.trim().length() > MAX_FIELD_LENGTH) {
            throw new IllegalArgumentException("Employee name cannot be empty or longer than 100 characters");
        }
        if (email == null || email.trim().length() > MAX_FIELD_LENGTH
                || !EMAIL.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (department == null || department.trim().isEmpty()
                || department.trim().length() > MAX_FIELD_LENGTH) {
            throw new IllegalArgumentException("Department cannot be empty or longer than 100 characters");
        }
        if (!Double.isFinite(salary) || salary < 0 || salary > MAX_SALARY) {
            throw new IllegalArgumentException("Invalid salary");
        }
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

package com.company.employeetaskmanager.facade;

import com.company.employeetaskmanager.model.Employee;
import com.company.employeetaskmanager.model.Task;
import com.company.employeetaskmanager.service.EmployeeService;
import com.company.employeetaskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AppFacade {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TaskService taskService;

    public Employee saveEmployee(String name, String email, String department, double salary) {
        return employeeService.createEmployee(name, email, department, salary);
    }

    public Employee updateEmployee(Long id, String name, String email, String department, double salary) {
        return employeeService.updateEmployee(id, name, email, department, salary);
    }

    public boolean deleteEmployee(Long id) {
        return employeeService.deleteEmployee(id);
    }

    public Employee findEmployeeById(Long id) {
        return employeeService.getEmployeeById(id);
    }

    public long countEmployees() {
        return employeeService.getEmployeeCount();
    }

    public List<Employee> findAllEmployeesByOrderByName() {
        return employeeService.getAllEmployeesSorted();
    }

    public List<Employee> findEmployeesByDepartment(String department) {
        return employeeService.getEmployeesByDepartment(department);
    }

    public Task saveTask(String taskName, Long employeeId, String deadline, String priority, String status) {
        return taskService.createTask(taskName, employeeId, deadline, priority, status);
    }

    public Task updateTask(Long id, String taskName, Long employeeId, String deadline, String priority, String status) {
        return taskService.updateTask(id, taskName, employeeId, deadline, priority, status);
    }

    public boolean deleteTask(Long id) {
        return taskService.deleteTask(id);
    }

    public Task findTaskById(Long id) {
        return taskService.getTaskById(id);
    }

    public long countTasks() {
        return taskService.getTaskCount();
    }

    public List<Task> findAllTasksByOrderByName() {
        return taskService.getAllTasksSorted();
    }

    public List<Task> findTasksByStatus(String status) {
        return taskService.getTasksByStatus(status);
    }
}
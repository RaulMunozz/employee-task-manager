package com.company.employeetaskmanager.ui;

import com.company.employeetaskmanager.facade.AppFacade;
import com.company.employeetaskmanager.model.Employee;
import com.company.employeetaskmanager.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class EmployeeTaskManagerCLI implements CommandLineRunner {

    @Autowired
    private AppFacade facade;

    private Scanner scanner;

    @Override
    public void run(String... args) {
        this.scanner = new Scanner(System.in);
        start();
    }

    public void start() {
        System.out.println("===============================================");
        System.out.println("    EMPLOYEE & TASK MANAGEMENT SYSTEM");
        System.out.println("===============================================\n");

        loadInitialData();

        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = readInt("Select option: ");

            switch (choice) {
                case 1:
                    handleEmployeeOperations();
                    break;
                case 2:
                    handleTaskOperations();
                    break;
                case 3:
                    displaySystemStatistics();
                    break;
                case 0:
                    System.out.println("\nSystem shutting down. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid selection. Please try again.");
            }
        }

        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Employee Management");
        System.out.println("2. Task Management");
        System.out.println("3. System Statistics");
        System.out.println("0. Exit Application");
        System.out.println("================================");
    }

    private void handleEmployeeOperations() {
        boolean returnToMain = false;

        while (!returnToMain) {
            System.out.println("\n========== EMPLOYEE OPERATIONS ==========");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Remove Employee");
            System.out.println("6. Filter by Department");
            System.out.println("0. Return to Main Menu");
            System.out.println("==========================================");

            int choice = readInt("Select option: ");

            switch (choice) {
                case 1:
                    processAddEmployee();
                    break;
                case 2:
                    processViewAllEmployees();
                    break;
                case 3:
                    processSearchEmployee();
                    break;
                case 4:
                    processUpdateEmployee();
                    break;
                case 5:
                    processRemoveEmployee();
                    break;
                case 6:
                    processFilterByDepartment();
                    break;
                case 0:
                    returnToMain = true;
                    break;
                default:
                    System.out.println("\nInvalid selection. Please try again.");
            }
        }
    }

    private void handleTaskOperations() {
        boolean returnToMain = false;

        while (!returnToMain) {
            System.out.println("\n========== TASK OPERATIONS ==========");
            System.out.println("1. Create Task");
            System.out.println("2. View All Tasks");
            System.out.println("3. Search Task");
            System.out.println("4. Update Task");
            System.out.println("5. Delete Task");
            System.out.println("6. Filter by Status");
            System.out.println("0. Return to Main Menu");
            System.out.println("======================================");

            int choice = readInt("Select option: ");

            switch (choice) {
                case 1:
                    processCreateTask();
                    break;
                case 2:
                    processViewAllTasks();
                    break;
                case 3:
                    processSearchTask();
                    break;
                case 4:
                    processUpdateTask();
                    break;
                case 5:
                    processDeleteTask();
                    break;
                case 6:
                    processFilterByStatus();
                    break;
                case 0:
                    returnToMain = true;
                    break;
                default:
                    System.out.println("\nInvalid selection. Please try again.");
            }
        }
    }

    private void processAddEmployee() {
        System.out.println("\n--- Add New Employee ---");
        String name = readString("Enter name: ");
        String email = readString("Enter email: ");
        String department = readString("Enter department: ");
        double salary = readDouble("Enter salary: ");

        try {
            Employee employee = facade.saveEmployee(name, email, department, salary);
            System.out.println("\nEmployee added successfully!");
            System.out.println(employee);
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void processViewAllEmployees() {
        System.out.println("\n========== ALL EMPLOYEES ==========");
        List<Employee> employees = facade.findAllEmployeesByOrderByName();

        if (employees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Employee emp : employees) {
                System.out.println(emp);
            }
            System.out.println("\nTotal: " + employees.size() + " employees");
        }
    }

    private void processSearchEmployee() {
        System.out.println("\n--- Search Employee ---");
        long id = readLong("Enter employee ID: ");

        try {
            Employee employee = facade.findEmployeeById(id);
            System.out.println("\nEmployee found:");
            System.out.println(employee);
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void processUpdateEmployee() {
        System.out.println("\n--- Update Employee ---");
        long id = readLong("Enter employee ID: ");

        try {
            Employee existing = facade.findEmployeeById(id);
            System.out.println("\nCurrent details:");
            System.out.println(existing);

            System.out.println("\nEnter new details:");
            String name = readString("Enter name: ");
            String email = readString("Enter email: ");
            String department = readString("Enter department: ");
            double salary = readDouble("Enter salary: ");

            Employee updated = facade.updateEmployee(id, name, email, department, salary);
            System.out.println("\nEmployee updated successfully!");
            System.out.println(updated);
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void processRemoveEmployee() {
        System.out.println("\n--- Remove Employee ---");
        long id = readLong("Enter employee ID: ");

        try {
            Employee employee = facade.findEmployeeById(id);
            System.out.println("\nEmployee to remove:");
            System.out.println(employee);

            String confirm = readString("Confirm deletion (yes/no): ");
            if (confirm.equalsIgnoreCase("yes")) {
                facade.deleteEmployee(id);
                System.out.println("\nEmployee removed successfully!");
            } else {
                System.out.println("\nDeletion cancelled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void processFilterByDepartment() {
        System.out.println("\n--- Filter by Department ---");
        String department = readString("Enter department: ");

        List<Employee> employees = facade.findEmployeesByDepartment(department);

        if (employees.isEmpty()) {
            System.out.println("\nNo employees found in department: " + department);
        } else {
            System.out.println("\nEmployees in " + department + ":");
            for (Employee emp : employees) {
                System.out.println(emp);
            }
            System.out.println("\nTotal: " + employees.size() + " employees");
        }
    }

    private void processCreateTask() {
        System.out.println("\n--- Create New Task ---");
        String taskName = readString("Enter task name: ");
        long employeeId = readLong("Enter employee ID: ");
        String deadline = readString("Enter deadline (YYYY-MM-DD): ");
        String priority = readString("Enter priority (HIGH/MEDIUM/LOW): ").toUpperCase();
        String status = readString("Enter status (TODO/IN_PROGRESS/DONE): ").toUpperCase();

        try {
            Task task = facade.saveTask(taskName, employeeId, deadline, priority, status);
            System.out.println("\nTask created successfully!");
            System.out.println(task);
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void processViewAllTasks() {
        System.out.println("\n========== ALL TASKS ==========");
        List<Task> tasks = facade.findAllTasksByOrderByName();

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
            System.out.println("\nTotal: " + tasks.size() + " tasks");
        }
    }

    private void processSearchTask() {
        System.out.println("\n--- Search Task ---");
        long id = readLong("Enter task ID: ");

        try {
            Task task = facade.findTaskById(id);
            System.out.println("\nTask found:");
            System.out.println(task);
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void processUpdateTask() {
        System.out.println("\n--- Update Task ---");
        long id = readLong("Enter task ID: ");

        try {
            Task existing = facade.findTaskById(id);
            System.out.println("\nCurrent details:");
            System.out.println(existing);

            System.out.println("\nEnter new details:");
            String taskName = readString("Enter task name: ");
            long employeeId = readLong("Enter employee ID: ");
            String deadline = readString("Enter deadline (YYYY-MM-DD): ");
            String priority = readString("Enter priority (HIGH/MEDIUM/LOW): ").toUpperCase();
            String status = readString("Enter status (TODO/IN_PROGRESS/DONE): ").toUpperCase();

            Task updated = facade.updateTask(id, taskName, employeeId, deadline, priority, status);
            System.out.println("\nTask updated successfully!");
            System.out.println(updated);
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void processDeleteTask() {
        System.out.println("\n--- Delete Task ---");
        long id = readLong("Enter task ID: ");

        try {
            Task task = facade.findTaskById(id);
            System.out.println("\nTask to delete:");
            System.out.println(task);

            String confirm = readString("Confirm deletion (yes/no): ");
            if (confirm.equalsIgnoreCase("yes")) {
                facade.deleteTask(id);
                System.out.println("\nTask deleted successfully!");
            } else {
                System.out.println("\nDeletion cancelled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void processFilterByStatus() {
        System.out.println("\n--- Filter by Status ---");
        String status = readString("Enter status (TODO/IN_PROGRESS/DONE): ").toUpperCase();

        List<Task> tasks = facade.findTasksByStatus(status);

        if (tasks.isEmpty()) {
            System.out.println("\nNo tasks found with status: " + status);
        } else {
            System.out.println("\nTasks with status " + status + ":");
            for (Task task : tasks) {
                System.out.println(task);
            }
            System.out.println("\nTotal: " + tasks.size() + " tasks");
        }
    }

    private void displaySystemStatistics() {
        System.out.println("\n========== SYSTEM STATISTICS ==========");
        long empCount = facade.countEmployees();
        long taskCount = facade.countTasks();

        System.out.println("Total Employees: " + empCount);
        System.out.println("Total Tasks: " + taskCount);

        if (empCount > 0) {
            System.out.println("\nEmployees by Department:");
            List<Employee> allEmps = facade.findAllEmployeesByOrderByName();
            allEmps.stream()
                    .map(Employee::getDepartment)
                    .distinct()
                    .forEach(dept -> {
                        long count = facade.findEmployeesByDepartment(dept).size();
                        System.out.println("  " + dept + ": " + count);
                    });
        }

        if (taskCount > 0) {
            System.out.println("\nTasks by Status:");
            for (String status : new String[]{"TODO", "IN_PROGRESS", "DONE"}) {
                long count = facade.findTasksByStatus(status).size();
                System.out.println("  " + status + ": " + count);
            }
        }

        System.out.println("========================================");
    }

    private void loadInitialData() {
        System.out.println("Initializing system with sample data...\n");

        facade.saveEmployee("David Amo", "d.amo@techcorp.com", "Engineering", 85000);
        facade.saveEmployee("Rayane Debbarh", "r.debbarh@techcorp.com", "Human Resources", 90000);
        facade.saveEmployee("Hiba Mitchell", "s.mitchell@techcorp.com", "Sales", 78000);
        facade.saveEmployee("Barboza Brown", "j.brown@techcorp.com", "Marketing", 74000);
        facade.saveEmployee("Robert Williams", "r.williams@techcorp.com", "Engineering", 82000);

        facade.saveTask("Implement authentication module", 1L, "2025-01-30", "HIGH", "IN_PROGRESS");
        facade.saveTask("Conduct employee training session", 2L, "2025-02-05", "MEDIUM", "TODO");
        facade.saveTask("Prepare quarterly sales report", 3L, "2025-01-28", "HIGH", "TODO");
        facade.saveTask("Launch social media campaign", 4L, "2025-01-22", "MEDIUM", "DONE");
        facade.saveTask("Code review for new features", 5L, "2025-01-25", "HIGH", "IN_PROGRESS");

        System.out.println("System ready.\n");
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private long readLong(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextLong()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        long value = scanner.nextLong();
        scanner.nextLine();
        return value;
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
              }

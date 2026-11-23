package com.company.employeetaskmanager.service;

import com.company.employeetaskmanager.model.Task;
import com.company.employeetaskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    public Task createTask(String taskName, Long employeeId, String deadline, String priority, String status) {
        if (taskName == null || taskName.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be empty");
        }

        if (employeeId == null || employeeId <= 0) {
            throw new IllegalArgumentException("Invalid employee ID");
        }

        if (deadline == null || deadline.trim().isEmpty()) {
            throw new IllegalArgumentException("Deadline cannot be empty");
        }

        if (!priority.equals("HIGH") && !priority.equals("MEDIUM") && !priority.equals("LOW")) {
            throw new IllegalArgumentException("Priority must be HIGH, MEDIUM, or LOW");
        }

        if (!status.equals("TODO") && !status.equals("IN_PROGRESS") && !status.equals("DONE")) {
            throw new IllegalArgumentException("Status must be TODO, IN_PROGRESS, or DONE");
        }

        Task task = new Task(taskName, employeeId, deadline, priority, status);
        return repository.save(task);
    }

    public Task updateTask(Long id, String taskName, Long employeeId, String deadline, String priority, String status) {
        Task existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));

        if (taskName == null || taskName.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be empty");
        }

        if (employeeId == null || employeeId <= 0) {
            throw new IllegalArgumentException("Invalid employee ID");
        }

        if (deadline == null || deadline.trim().isEmpty()) {
            throw new IllegalArgumentException("Deadline cannot be empty");
        }

        if (!priority.equals("HIGH") && !priority.equals("MEDIUM") && !priority.equals("LOW")) {
            throw new IllegalArgumentException("Priority must be HIGH, MEDIUM, or LOW");
        }

        if (!status.equals("TODO") && !status.equals("IN_PROGRESS") && !status.equals("DONE")) {
            throw new IllegalArgumentException("Status must be TODO, IN_PROGRESS, or DONE");
        }

        existing.setTaskName(taskName);
        existing.setEmployeeId(employeeId);
        existing.setDeadline(deadline);
        existing.setPriority(priority);
        existing.setStatus(status);

        return repository.save(existing);
    }

    public boolean deleteTask(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Task not found with ID: " + id);
        }
        repository.deleteById(id);
        return true;
    }

    public Task getTaskById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
    }

    public long getTaskCount() {
        return repository.count();
    }

    public List<Task> getAllTasksSorted() {
        return repository.findAllByOrderByTaskNameAsc();
    }

    public List<Task> getTasksByStatus(String status) {
        return repository.findByStatus(status);
    }
}
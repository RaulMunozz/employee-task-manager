package com.company.employeetaskmanager.service;

import com.company.employeetaskmanager.model.Task;
import com.company.employeetaskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    private static final Set<String> PRIORITIES = Set.of("HIGH", "MEDIUM", "LOW");
    private static final Set<String> STATUSES = Set.of("TODO", "IN_PROGRESS", "DONE");
    private static final int MAX_TASK_NAME_LENGTH = 200;

    @Autowired
    private TaskRepository repository;

    public Task createTask(String taskName, Long employeeId, String deadline, String priority, String status) {
        validate(taskName, employeeId, deadline, priority, status);
        return repository.save(new Task(taskName.trim(), employeeId, deadline.trim(), priority, status));
    }

    public Task updateTask(Long id, String taskName, Long employeeId, String deadline, String priority, String status) {
        Task existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));

        validate(taskName, employeeId, deadline, priority, status);
        existing.setTaskName(taskName.trim());
        existing.setEmployeeId(employeeId);
        existing.setDeadline(deadline.trim());
        existing.setPriority(priority);
        existing.setStatus(status);

        return repository.save(existing);
    }

    private void validate(String taskName, Long employeeId, String deadline, String priority, String status) {
        if (taskName == null || taskName.trim().isEmpty()
                || taskName.trim().length() > MAX_TASK_NAME_LENGTH) {
            throw new IllegalArgumentException("Task name cannot be empty or longer than 200 characters");
        }
        if (employeeId == null || employeeId <= 0) {
            throw new IllegalArgumentException("Invalid employee ID");
        }
        if (deadline == null || deadline.trim().isEmpty()) {
            throw new IllegalArgumentException("Deadline cannot be empty");
        }
        try {
            LocalDate.parse(deadline.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Deadline must be a valid date in YYYY-MM-DD format");
        }
        if (priority == null || !PRIORITIES.contains(priority)) {
            throw new IllegalArgumentException("Priority must be HIGH, MEDIUM, or LOW");
        }
        if (status == null || !STATUSES.contains(status)) {
            throw new IllegalArgumentException("Status must be TODO, IN_PROGRESS, or DONE");
        }
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

package com.example.mega.service.impl;

import com.example.mega.db.domain.TaskEntity;
import com.example.mega.db.repository.TaskRepository;
import com.example.mega.dto.TaskCreateDto;
import com.example.mega.dto.TaskUpdateDto;
import com.example.mega.exceptions.TaskNotFoundException;
import com.example.mega.service.TaskService;
import com.example.mega.utils.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EmailService emailService;


    @Override
    public TaskEntity createTask(TaskCreateDto taskCreateDto) {
        TaskEntity taskEntity = TaskEntity.builder()
                .title(taskCreateDto.getTitle())
                .description(taskCreateDto.getDescription())
                .email(taskCreateDto.getEmail())
                .completed(false)
                .build();

        TaskEntity createdTask = taskRepository.save(taskEntity);
        log.info("Task created: {}", createdTask);
        emailService.sendTask(createdTask.getEmail(), createdTask);
        log.info("Task sent to: {}", createdTask.getEmail());


        return createdTask;
    }

    @Override
    @Cacheable(value = "tasks", key = "'allTasks'")
    public List<TaskEntity> getAllTasks() {
        log.info("Fetching all tasks");
        List<TaskEntity> tasks = taskRepository.findAll();
        log.info("Found {} tasks", tasks.size());
        return tasks;
    }

    @Override
    public TaskEntity getTaskById(Long id) {
        log.info("Fetching Task by id: {}", id);
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> {
            log.warn("Task with id {} not found", id);
            return new TaskNotFoundException(id);
        });
        log.info("Task found: {}", task);
        return task;
    }

    @Override
    public TaskEntity updateTask(Long id, TaskUpdateDto taskUpdateDto) {
        log.info("Updating task with id: {}", id);
        TaskEntity existingTask = getTaskById(id);

        TaskEntity updatedTask = TaskEntity.builder()
                .id(existingTask.getId())
                .title(taskUpdateDto.getTitle())
                .description(taskUpdateDto.getDescription())
                .email(existingTask.getEmail())
                .completed(taskUpdateDto.getCompleted())
                .build();


        TaskEntity savedTask = taskRepository.save(updatedTask);
        log.info("Task updated: {}", updatedTask);

        return updatedTask;
    }

    @Override
    public void deleteTaskById(Long id) {
        log.info("Deleting task with id: {}", id);
        TaskEntity taskToDelete = getTaskById(id);
        taskRepository.delete(taskToDelete);
        log.info("Task with id {} deleted", id);
    }
}

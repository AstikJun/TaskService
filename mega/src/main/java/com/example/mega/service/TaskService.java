package com.example.mega.service;

import com.example.mega.db.domain.TaskEntity;
import com.example.mega.dto.TaskCreateDto;
import com.example.mega.dto.TaskUpdateDto;

import java.util.List;

public interface TaskService {
     TaskEntity createTask(TaskCreateDto taskCreateDTO);

    List<TaskEntity> getAllTasks();

     TaskEntity getTaskById(Long id);

     TaskEntity updateTask(Long id, TaskUpdateDto taskUpdateDTO);

    void deleteTaskById(Long id);

}


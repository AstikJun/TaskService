package com.example.mega.controller;

import com.example.mega.db.domain.TaskEntity;
import com.example.mega.dto.TaskCreateDto;
import com.example.mega.dto.TaskResponseDto;
import com.example.mega.dto.TaskUpdateDto;
import com.example.mega.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/create")
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreateDto taskCreateDTO) {
        TaskEntity createdTask = taskService.createTask(taskCreateDTO);
        TaskResponseDto responseDTO = convertToResponseDTO(createdTask);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskEntity> tasks = taskService.getAllTasks();
        List<TaskResponseDto> responseDTOs = tasks.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        TaskEntity task = taskService.getTaskById(id);
        TaskResponseDto responseDTO = convertToResponseDTO(task);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateDto taskUpdateDTO) {
        TaskEntity updatedTask = taskService.updateTask(id, taskUpdateDTO);
        TaskResponseDto responseDTO = convertToResponseDTO(updatedTask);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private TaskResponseDto convertToResponseDTO(TaskEntity task) {
        return new TaskResponseDto(task.getId(), task.getTitle(), task.getDescription(), task.getEmail(), task.isCompleted());
    }


}

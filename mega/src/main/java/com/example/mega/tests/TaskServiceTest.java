package com.example.mega.tests;

import com.example.mega.db.domain.TaskEntity;
import com.example.mega.db.repository.TaskRepository;
import com.example.mega.dto.TaskCreateDto;
import com.example.mega.dto.TaskUpdateDto;
import com.example.mega.exceptions.TaskNotFoundException;
import com.example.mega.service.impl.TaskServiceImpl;
import com.example.mega.utils.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {


    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private EmailService emailService;

    private TaskEntity task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        task = new TaskEntity();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
    }


    @Test
    void createTask_ShouldReturnCreatedTask() {
        TaskCreateDto taskCreateDto = new TaskCreateDto();
        taskCreateDto.setTitle("Test Task");
        taskCreateDto.setDescription("Test Description");
        taskCreateDto.setCompleted(false);
        taskCreateDto.setEmail("test@example.com");

        TaskEntity task = new TaskEntity();
        task.setTitle(taskCreateDto.getTitle());
        task.setDescription(taskCreateDto.getDescription());
        task.setCompleted(taskCreateDto.isCompleted());
        task.setEmail(taskCreateDto.getEmail());

        when(taskRepository.save(task)).thenReturn(task);

        TaskEntity createdTask = taskService.createTask(taskCreateDto);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("Test Description", createdTask.getDescription());
        assertEquals("test@example.com", createdTask.getEmail());
        assertFalse(createdTask.isCompleted());

        verify(taskRepository, times(1)).save(task);
        verify(emailService, times(1)).sendTask(eq("test@example.com"), any(TaskEntity.class));
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        List<TaskEntity> tasks = taskService.getAllTasks();

        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskEntity foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals(1L, foundTask.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(1L);
        });
    }

    @Test
    void updateTask_ShouldUpdateTask_WhenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        TaskUpdateDto taskUpdateDto = new TaskUpdateDto();
        taskUpdateDto.setTitle("Updated Task");
        taskUpdateDto.setDescription("Updated Description");
        taskUpdateDto.setCompleted(true);

        TaskEntity result = taskService.updateTask(1L, taskUpdateDto);

        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertTrue(result.isCompleted());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTaskById(1L);

        verify(taskRepository, times(1)).delete(task);
    }
}
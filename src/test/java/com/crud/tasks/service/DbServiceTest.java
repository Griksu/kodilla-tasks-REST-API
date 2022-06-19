package com.crud.tasks.service;

import com.crud.tasks.controller.TaskNotFoundException;
import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TDD: DB Service Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DbServiceTest {

    private static int testCounter = 0;

    @BeforeAll
    public static void beforeAllTests() {
        System.out.println("This is the beginning of tests");
    }

    @AfterAll
    public static void afterAllTests() {
        System.out.println("All tests are finished");
    }

    @BeforeEach
    public void beforeEachTest() {
        testCounter++;
        System.out.println("Preparing to execute test #" + testCounter);
    }

    @InjectMocks
    private DbService dbService;

    @Mock
    private TaskRepository taskRepository;

    @DisplayName("Test for: getting all Tasks")
    @Test
    @Order(1)
    public void testGetAllTasks() {
        //Given
        Task task1 = new Task(1L, "Task 1", "Task 1 description");
        Task task2 = new Task(2L, "Task 2", "Task 2 description");
        List<Task> taskList = List.of(task1, task2);

        when(taskRepository.findAll()).thenReturn(taskList);

        //When
        List<Task> taskListFromRepo = dbService.getAllTasks();

        //Then
        assertEquals(2, taskListFromRepo.size());
    }

    @DisplayName("Test for: getting Task by Id")
    @Test
    @Order(2)
    public void testGetTaskById() throws TaskNotFoundException {
        //Given
        Task task3 = new Task(3L, "Task 3", "Task 3 description");
        Long task3Id = task3.getId();

        when(taskRepository.findById(task3Id)).thenReturn(Optional.of(task3));

        //When
        Task taskGetFromRepo = dbService.getTaskById(task3Id);

        //Then
        assertEquals("Task 3", taskGetFromRepo.getTitle());
    }

    @DisplayName("Test for: saving Task")
    @Test
    @Order(3)
    public void testSaveTask() {
        //Given
        Task task4 = new Task(4L, "Task 4", "Task 4 description");

        when(taskRepository.save(task4)).thenReturn(task4);

        //When
        Task taskSavedFromRepo = dbService.saveTask(task4);

        //Then
        assertEquals("Task 4 description", taskSavedFromRepo.getContent());
    }

    @DisplayName("Test for: deleting Task")
    @Test
    @Order(4)
    public void testDeleteTaskById() {
        //Given
        Task task5 = new Task(5L, "Task 5", "Task 5 description");
        Long task5Id = task5.getId();

        //When
        dbService.deleteTaskById(task5Id);

        //Then
        verify(taskRepository, times(1)).deleteById(task5Id);
    }
}

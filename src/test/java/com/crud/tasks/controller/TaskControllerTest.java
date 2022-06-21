package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(TaskController.class)
@DisplayName("TDD: Task Controller Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskControllerTest {

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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService dbService;

    @MockBean
    private TaskMapper taskMapper;

    @DisplayName("Test for: getting all Tasks")
    @Test
    @Order(1)
    public void shouldFetchTasks() throws Exception {
        //Given
        List<Task> taskList =
                List.of(new Task(1L, "Task", "Task description"));
        List<TaskDto> taskDtoList =
                List.of(new TaskDto(1L, "Task", "Task description"));

        when(dbService.getAllTasks()).thenReturn(taskList);
        when(taskMapper.mapToTaskDtoList(taskList)).thenReturn(taskDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("Task")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", Matchers.is("Task description")));
    }

    @DisplayName("Test for: getting Task by Id")
    @Test
    @Order(2)
    public void shouldFetchTask() throws Exception {
        //Given
        Task task = new Task(1L, "Task", "Task description");
        TaskDto taskDto = new TaskDto(1L, "Task", "Task description");

        when(dbService.getTaskById(task.getId())).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Task")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("Task description")));
    }

    @DisplayName("Test for: deleting Task")
    @Test
    @Order(3)
    public void shouldDeleteTask() throws Exception {
        //Given
        Task task = new Task(1L, "Task", "Task description");
        TaskDto taskDto = new TaskDto(1L, "Task", "Task description");

        when(dbService.getTaskById(task.getId())).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);
        dbService.deleteTaskById(taskDto.getId());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @DisplayName("Test for: updating Task")
    @Test
    @Order(4)
    public void shouldUpdateTask() throws Exception {
        //Given
        Task task = new Task(1L, "Task", "Task description");
        TaskDto taskDto = new TaskDto(1L, "Task", "Task description");
        Task savedTask = new Task(1L, "Task", "Task updated");
        TaskDto savedTaskDto = new TaskDto(1L, "Task", "Task updated");

        when(taskMapper.mapToTask(taskDto)).thenReturn(task);
        when(dbService.saveTask(task)).thenReturn(savedTask);
        when(taskMapper.mapToTaskDto(savedTask)).thenReturn(savedTaskDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(savedTaskDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test for: creating Task")
    @Test
    @Order(5)
    public void shouldCreateTask() throws Exception {
        //Given
        Task task = new Task(1L, "Task", "Task description");
        TaskDto taskDto = new TaskDto(1L, "Task", "Task description");

        when(taskMapper.mapToTask(taskDto)).thenReturn(task);
        dbService.saveTask(task);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(task);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}
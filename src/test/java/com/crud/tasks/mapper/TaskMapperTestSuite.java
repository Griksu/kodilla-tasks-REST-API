package com.crud.tasks.mapper;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("TDD: Task Mapper Test Suite")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskMapperTestSuite {

    private static int testCounter = 0;

    @BeforeAll
    public static void beforeAllTests() {
        System.out.println("This is the beginning of Task Mapper tests");
    }

    @AfterAll
    public static void afterAllTests() {
        System.out.println("All tests in Task Mapper are finished");
    }

    @BeforeEach
    public void beforeEachTest() {
        testCounter++;
        System.out.println("Preparing to execute test #" + testCounter);
    }

    @Autowired
    private TaskMapper taskMapper = new TaskMapper();

    @DisplayName("Test for: mapping TaskDto to Task")
    @Test
    @Order(1)
    public void testMapToTask() {
        //Given
        TaskDto taskDto = new TaskDto(1L, "Task 1",
                "Task 1 content");
        //When
        Task task = taskMapper.mapToTask(taskDto);
        //Then
        assertThat(task).isNotNull();
        assertEquals(1L, task.getId());
        assertThat(task.getTitle()).isEqualTo("Task 1");
    }

    @DisplayName("Test for: mapping Task to TaskDto")
    @Test
    @Order(2)
    public void testMapToTaskDto() {
        //Given
        Task task = new Task(2L, "Task 2", "Task 2 content");
        //When
        TaskDto taskDto = taskMapper.mapToTaskDto(task);
        //Then
        assertThat(taskDto).isNotNull();
        assertEquals(2L, taskDto.getId());
        assertThat(taskDto.getContent()).isEqualTo("Task 2 content");
    }

    @DisplayName("Test for: mapping TaskList to TaskDtoList")
    @Test
    @Order(3)
    public void testMapToTaskDtoList() {
        //Given
       List<Task> taskList = List.of(
                new Task(1L, "T1", "T1 cont"),
                new Task(2L, "T2", "T2 cont"),
                new Task(3L, "T3", "T3 cont"));
        //When
        List<TaskDto> taskDtoList = taskMapper.mapToTaskDtoList(taskList);
        //Then
        assertThat(taskDtoList).isNotNull();
        assertThat(taskDtoList.size()).isEqualTo(3);
        assertThat(taskDtoList.get(2).getContent()).isEqualTo("T3 cont");
        assertThat(taskDtoList.get(0).getId()).isEqualTo(1L);
    }
}

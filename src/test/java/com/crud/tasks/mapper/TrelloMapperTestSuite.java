package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("TDD: Trello Mappers Test Suite")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrelloMapperTestSuite {

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
    private TrelloMapper trelloMapper = new TrelloMapper();

    @DisplayName("Test for: mapping Boards to BoardsDto")
    @Test
    @Order(1)
    public void testMapToBoardsDto() {
        //Given
        TrelloList trelloList1 = new TrelloList("00", "My List", true);
        TrelloList trelloList2 = new TrelloList("01", "Your List", false);
        TrelloList trelloList3 = new TrelloList("02", "Our List", true);
        TrelloList trelloList4 = new TrelloList("03", "Their List", false);
        List<TrelloList> trelloListsFirst = new ArrayList<>();
        List<TrelloList> trelloListsSecond = new ArrayList<>();
        trelloListsFirst.add(trelloList1);
        trelloListsFirst.add(trelloList2);
        trelloListsSecond.add(trelloList3);
        trelloListsSecond.add(trelloList4);
        TrelloBoard trelloBoard1 = new TrelloBoard("001", "My Board", trelloListsFirst);
        TrelloBoard trelloBoard2 = new TrelloBoard("002", "Your Board", trelloListsSecond);
        List<TrelloBoard> trelloBoardsList = new ArrayList<>();
        trelloBoardsList.add(trelloBoard1);
        trelloBoardsList.add(trelloBoard2);
        //When
        List<TrelloBoardDto> trelloBoardsDtoList = trelloMapper.mapToBoardsDto(trelloBoardsList);
        //Then
        assertEquals(2, trelloBoardsDtoList.size());
        assertTrue(trelloBoardsDtoList.get(0).getLists().get(0).isClosed());
        assertEquals("Your Board", trelloBoardsDtoList.get(1).getName());
        assertEquals(2, trelloBoardsDtoList.get(1).getLists().size());
    }

    @DisplayName("Test for: mapping BoardsDto to Boards")
    @Test
    @Order(2)
    public void testMapToBoards() {
        //Given
        TrelloListDto trelloListDto1 = new TrelloListDto("10", "List 1", false);
        TrelloListDto trelloListDto2 = new TrelloListDto("11", "List 2", true);
        TrelloListDto trelloListDto3 = new TrelloListDto("12", "List 3", false);
        TrelloListDto trelloListDto4 = new TrelloListDto("13", "List 4", false);
        TrelloListDto trelloListDto5 = new TrelloListDto("14", "List 5", false);
        List<TrelloListDto> trelloListDtoListRed = new ArrayList<>();
        List<TrelloListDto> trelloListDtoListBlue = new ArrayList<>();
        trelloListDtoListRed.add(trelloListDto1);
        trelloListDtoListRed.add(trelloListDto3);
        trelloListDtoListRed.add(trelloListDto4);
        trelloListDtoListRed.add(trelloListDto5);
        trelloListDtoListBlue.add(trelloListDto2);
        TrelloBoardDto trelloBoardDtoOngoing = new TrelloBoardDto("101", "Ongoing Tasks Board", trelloListDtoListRed);
        TrelloBoardDto trelloBoardDtoDone = new TrelloBoardDto("102", "Done Tasks Board", trelloListDtoListBlue);
        List<TrelloBoardDto> trelloBoardsDtoList = new ArrayList<>();
        trelloBoardsDtoList.add(trelloBoardDtoOngoing);
        trelloBoardsDtoList.add(trelloBoardDtoDone);
        //When
        List<TrelloBoard> trelloBoardsList = trelloMapper.mapToBoards(trelloBoardsDtoList);
        //Then
        assertEquals(2, trelloBoardsList.size());
        assertEquals(4, trelloBoardsList.get(0).getLists().size());
        assertFalse(trelloBoardsList.get(0).getLists().get(3).isClosed());
        assertEquals("102", trelloBoardsList.get(1).getId());
    }

    @DisplayName("Test for: mapping List to ListDto")
    @Test
    @Order(3)
    public void testMapToListDto() {
        //Given
        TrelloList trelloList1 = new TrelloList("00", "My List", true);
        TrelloList trelloList2 = new TrelloList("01", "Your List", false);
        TrelloList trelloList3 = new TrelloList("02", "Our List", true);
        TrelloList trelloList4 = new TrelloList("03", "Their List", false);
        List<TrelloList> trelloLists = new ArrayList<>();
        trelloLists.add(trelloList1);
        trelloLists.add(trelloList2);
        trelloLists.add(trelloList3);
        trelloLists.add(trelloList4);
        //When
        List<TrelloListDto> trelloListsDto = trelloMapper.mapToListDto(trelloLists);
        int trelloListsDtoSize = trelloListsDto.size();
        //then
        assertEquals(4, trelloListsDtoSize);
        assertEquals(trelloLists.get(3).getId(), trelloListsDto.get(3).getId());
        assertEquals("Our List", trelloListsDto.get(2).getName());
        assertTrue(trelloListsDto.get(0).isClosed());
    }

    @DisplayName("Test for: mapping ListDto to List")
    @Test
    @Order(4)
    public void testMapToList() {
        //Given
        TrelloListDto trelloListDto1 = new TrelloListDto("11", "ToDoList", true);
        TrelloListDto trelloListDto2 = new TrelloListDto("12", "Working List", false);
        TrelloListDto trelloListDto3 = new TrelloListDto("13", "Done List", false);
        List<TrelloListDto> trelloListsDto = new ArrayList<>();
        trelloListsDto.add(trelloListDto1);
        trelloListsDto.add(trelloListDto2);
        trelloListsDto.add(trelloListDto3);
        //When
        List<TrelloList> trelloLists = trelloMapper.mapToList(trelloListsDto);
        int trelloListSize = trelloLists.size();
        //Then
        assertEquals(3, trelloListSize);
        assertEquals(trelloListsDto.get(0).getId(), trelloLists.get(0).getId());
        assertEquals("Working List", trelloLists.get(1).getName());
        assertFalse(trelloLists.get(2).isClosed());
    }

    @DisplayName("Test for: mapping Card to CardDto")
    @Test
    @Order(5)
    public void testMapToCardDto() {
        //Given
        TrelloCard trelloCard = new TrelloCard("Card", "Card description",
                "top", "L-12");
        //When
        TrelloCardDto trelloCardDto = trelloMapper.mapToCardDto(trelloCard);
        //Then
        assertEquals("Card", trelloCardDto.getName());
        assertEquals("L-12", trelloCardDto.getListId());
    }

    @DisplayName("Test for: mapping CardDto to Card")
    @Test
    @Order(6)
    public void testMapToCard() {
        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("Card DTO",
                "Card DTO description", "bottom", "L-dto-12");
        //When
        TrelloCard trelloCard = trelloMapper.mapToCard(trelloCardDto);
        //Then
        assertEquals("Card DTO", trelloCard.getName());
        assertEquals("bottom", trelloCard.getPos());
    }
}

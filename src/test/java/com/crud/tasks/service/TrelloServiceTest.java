package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.*;
import com.crud.tasks.trello.client.TrelloClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrelloServiceTest {

    @InjectMocks
    private TrelloService trelloService;

    @Mock
    private TrelloClient trelloClient;

    @Mock
    private SimpleEmailService simpleEmailService;

    @Mock
    private AdminConfig adminConfig;

    @Test
    public void testFetchTrelloBoards() {
        //Given
        List<TrelloListDto> trelloListDtoList =
                List.of(new TrelloListDto("1", "test List", true));

        List<TrelloBoardDto> trelloBoardDtoList =
                List.of(new TrelloBoardDto("1", "test Board", trelloListDtoList));

        when(trelloClient.getTrelloBoards()).thenReturn(trelloBoardDtoList);

        //When
        List<TrelloBoardDto> trelloBoardsDto = trelloService.fetchTrelloBoards();

        //Then
        assertEquals("test Board", trelloBoardsDto.get(0).getName());
    }

    @Test
    public void testCreateTrelloCard() {
        //Given
        TrelloCardDto cardDto = new TrelloCardDto(
                "Card", "Card description", "top", "1L");
        CreatedTrelloCardDto createdCardDto = new CreatedTrelloCardDto(
                "123", "Card", "short URL");

        when(trelloClient.createNewCard(cardDto)).thenReturn(createdCardDto);

        //When
        CreatedTrelloCardDto createdTrelloCardDto = trelloService.createTrelloCard(cardDto);

        //Then
        assertEquals("123", createdTrelloCardDto.getId());

    }
}

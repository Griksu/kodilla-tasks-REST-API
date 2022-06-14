package com.crud.tasks.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Data
public class TrelloCard {
    private String name;
    private String description;
    private String pos;
    private String listId;
}

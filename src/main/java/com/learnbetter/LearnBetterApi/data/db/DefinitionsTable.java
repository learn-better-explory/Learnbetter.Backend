package com.learnbetter.LearnBetterApi.data.db;

import com.learnbetter.LearnBetterApi.data.WordRemoveListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "definitions_table")
@EntityListeners(value=WordRemoveListener.class)
@Getter
@Setter
@ToString
public class DefinitionsTable {

    @Id
    private UUID tableId;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "owner")
    private User owner;
    private String tableName;
    private String tableDescription;
    private int definitionsCount;

    @Transient
    private List<WordDefinition> words;

    public DefinitionsTable(User owner, String tableName, String tableDescription) {
        this.tableId = generateNewUuid();
        this.owner = owner;
        this.tableName = tableName;
        this.tableDescription = tableDescription;
        this.definitionsCount = 0;
        this.words = new ArrayList<>();
    }

    public DefinitionsTable(UUID tableId, String tableDescription, User owner, String tableName, int definitionsCount) {
        this.tableId = tableId;
        this.tableDescription = tableDescription;
        this.owner = owner;
        this.tableName = tableName;
        this.definitionsCount = definitionsCount;
        this.words = new ArrayList<>();
    }

    public DefinitionsTable() {
        this.tableId = generateNewUuid();
        this.words = new ArrayList<>();
    }

    private UUID generateNewUuid(){
        return UUID.randomUUID();
    }

    public void addWordDefinition(WordDefinition wordDefinition){
        this.words.add(wordDefinition);
        this.definitionsCount++;
        wordDefinition.setWordId(definitionsCount);
    }

    public void removeWordDefinition(WordDefinition wordDefinition){
        this.words.remove(wordDefinition);
        int wordId = wordDefinition.getWordId();
        if (wordId != this.definitionsCount) {
            for (int i = wordId; i < this.definitionsCount; i++) {
                this.words.get(i).setWordId(i);
            }
        }
        this.definitionsCount--;

    }

    public void incrementDefinitionsCount(){
        this.definitionsCount++;
    }


}

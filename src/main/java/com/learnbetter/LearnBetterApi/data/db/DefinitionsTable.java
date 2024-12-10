package com.learnbetter.LearnBetterApi.data.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "definitions_table")
@ToString
public class DefinitionsTable {

    @Id
    private UUID tableId;
    @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "owner")
    private User owner;
    @Setter
    private String tableName;
    @Setter
    private String tableDescription;
    private int definitionsCount;

    @OneToMany(mappedBy = "defTable", fetch = FetchType.EAGER)
    private List<WordDefinition> words;

    public DefinitionsTable(User owner, String tableName, String tableDescription) {
        this.tableId = generateNewUuid();
        this.owner = owner;
        this.tableName = tableName;
        this.tableDescription = tableDescription;
        this.words = new ArrayList<>();
        this.definitionsCount = 0;
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
        this.definitionsCount = 0;
    }

    private UUID generateNewUuid(){
        return UUID.randomUUID();
    }


    @JsonIgnore
    public int getDefinitionsCountAndIncrement(){
        return this.definitionsCount++;
    }

    public void decrementDefinitionsCount(){
        this.definitionsCount--;
    }

}

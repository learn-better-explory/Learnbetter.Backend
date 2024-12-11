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
    @Column( name = "owner")
    private long ownerId;
    @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "owner", insertable = false, updatable = false)
    private User owner;
    @Setter
    private String tableName;
    @Setter
    private String tableDescription;

    @OneToMany(mappedBy = "defTable", fetch = FetchType.EAGER)
    private List<WordDefinition> words;

    public DefinitionsTable(User owner, String tableName, String tableDescription) {
        this.tableId = generateNewUuid();
        this.owner = owner;
        this.tableName = tableName;
        this.tableDescription = tableDescription;
        this.words = new ArrayList<>();
    }

    public DefinitionsTable(UUID tableId, String tableDescription, User owner, String tableName, int definitionsCount) {
        this.tableId = tableId;
        this.tableDescription = tableDescription;
        this.owner = owner;
        this.tableName = tableName;
        this.words = new ArrayList<>();
    }

    private UUID generateNewUuid(){
        return UUID.randomUUID();
    }

    public int getDefinitionsCount(){
        return this.words.size();
    }

}

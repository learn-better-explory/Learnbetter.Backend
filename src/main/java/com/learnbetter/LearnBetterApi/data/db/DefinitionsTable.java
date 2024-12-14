package com.learnbetter.LearnBetterApi.data.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * This class represents a table which holds all the words and their
 * definitions from which user can learn and play games. This is the 
 * most basic entity which allows user to create words and interact with<br>
 * Every table's id is a random UUID which is generated at construction. <br>
 * Every table also stores their name, description and an owner - a {@link User} which
 * has every right to every operation on this table. It also holds from a relation in the
 * {@link WordDefinition} table every word definition that it contains.
 */
@Getter
@Entity
@Table(name = "definitions_table")
@ToString
public class DefinitionsTable {

    @Id
    private UUID tableId;
    @Column( name = "owner")
    private long ownerId;
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

    @SuppressWarnings("unused")
    public DefinitionsTable(){
        this.tableId = generateNewUuid();
        this.words = new ArrayList<>();
    }

    /**
     * Creates a new instance of the {@link DefinitionsTable}. <br>
     * It generates a random id for this table.
     * @param owner The user which has all the rights for this table. It is the user 
     *              who created the table.
     * @param tableName The name of the table.
     * @param tableDescription The description of this table.
     * 
     */
    public DefinitionsTable(User owner, String tableName, String tableDescription) {
        this.tableId = generateNewUuid();
        this.owner = owner;
        this.tableName = tableName;
        this.tableDescription = tableDescription;
        this.words = new ArrayList<>();
    }

    public DefinitionsTable(UUID tableId, String tableDescription, User owner, String tableName) {
        this.tableId = tableId;
        this.tableDescription = tableDescription;
        this.owner = owner;
        this.tableName = tableName;
        this.words = new ArrayList<>();
    }

    /**
     * Generates a new unique UUID.
     */
    private UUID generateNewUuid(){
        return UUID.randomUUID();
    }

    @SuppressWarnings("unused")
    public void setOwner(User owner){
        this.owner = owner;
        this.ownerId = owner.getId();
    }

    /**
     * Returns the amount of words defined in this table.
     * @return Amount of word defined.
     */
    public int getDefinitionsCount(){
        return this.getWords().size();
    }

}

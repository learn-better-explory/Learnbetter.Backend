package com.learnbetter.LearnBetterApi.data.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * This entity represents a word and its definition which is stored in a
 * {@link DefinitionsTable}. This class is referenced by a relation in the table
 * to make it automatically add and remove them.  This class stores its order in the table, 
 * which means at which position is it located in the edit view. It also stored
 * the original table that it is in. Every word entity can only be in one table class, 
 * if you want to create the same word for two different tables they have to be duplicated.
 */
@Getter
@Entity
@Table(name = "word_definition")
@IdClass(WordDefPK.class)
@NoArgsConstructor
public class WordDefinition {

    @Id
    @SequenceGenerator(name = "id_word_generator", sequenceName = "id_word_generator",  allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "id_word_generator")
    private Integer wordId;
    @Id
    @Column(name = "table_id")
    private UUID tableId;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "table_id", insertable = false, updatable = false)
    private DefinitionsTable defTable;
    private String word;
    @Setter
    private int orderInTable;
    @Setter
    private String description;

    /**
     * Creates a new instance of a word definition.
     * @param defTable The table that it resides in.
     * @param word The word that is stored in this definition. It can have max length of 40.
     * @param description The definition of this word.
     */
    public WordDefinition(DefinitionsTable defTable, String word, String description) {
        this.tableId = defTable.getTableId();
        this.defTable = defTable;
        this.word = word;
        this.description = description;
        this.orderInTable = defTable.getDefinitionsCount();
    }

    /**
     * Sets the word for this definition. <br>
     * <b>The word cannot be longer than 40 chars if it is going
     * to be cut to the length of 40</b>
     */
    public void setWord(String word){
        this.word = word.length() > 40 ? word.substring(0,40) : word;
    }


    public void setDefTable(DefinitionsTable defTable){
        this.defTable = defTable;
        this.tableId = defTable.getTableId();
        this.orderInTable = defTable.getDefinitionsCount();
    }

    @Override
    public String toString(){
        return "WordDefinition[wordId=" + this.wordId + ", defTable="
                + defTable.getTableName() + ", word=" + word + ", description=" + description + "]";
    }

}

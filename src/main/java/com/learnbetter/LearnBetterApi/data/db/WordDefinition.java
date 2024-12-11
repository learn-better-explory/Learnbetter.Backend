package com.learnbetter.LearnBetterApi.data.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

    public WordDefinition(DefinitionsTable defTable, String word, String description) {
        this.tableId = defTable.getTableId();
        this.defTable = defTable;
        this.word = word;
        this.description = description;
        this.orderInTable = defTable.getDefinitionsCount();
    }

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

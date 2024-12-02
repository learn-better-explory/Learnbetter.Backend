package com.learnbetter.LearnBetterApi.data.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "word_definition")
@IdClass(WordDefPK.class)
@Getter
@Setter
@NoArgsConstructor
public class WordDefinition {

    @Id
    private Integer wordId;
    @Id
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "def_table")
    private DefinitionsTable defTable;
    private String word;
    private String description;

    public WordDefinition(DefinitionsTable defTable, String word, String description) {
        this.defTable = defTable;
        this.word = word;
        this.description = description;
        this.defTable.addWordDefinition(this);
    }

    public void setWord(String word){
        this.word = word.length() > 40 ? word.substring(0,40) : word;
    }

    @Override
    public String toString(){
        return "WordDefinition[wordId=" + this.wordId + ", defTable="
                + defTable.getTableName() + ", word=" + word + ", description=" + description + "]";
    }

}

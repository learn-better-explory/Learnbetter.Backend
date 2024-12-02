package com.learnbetter.LearnBetterApi.data.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class WordDefPK implements Serializable {

    private Integer wordId;
    private DefinitionsTable defTable;


    @Override
    public int hashCode() {
        return Objects.hash(wordId, defTable);
    }
}


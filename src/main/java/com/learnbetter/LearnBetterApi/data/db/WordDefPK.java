package com.learnbetter.LearnBetterApi.data.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class WordDefPK implements Serializable {

    private Integer wordId;
    private UUID tableId;


    @Override
    public int hashCode() {
        return Objects.hash(wordId, tableId);
    }
}


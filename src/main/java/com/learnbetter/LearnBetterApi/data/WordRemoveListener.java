package com.learnbetter.LearnBetterApi.data;


import com.learnbetter.LearnBetterApi.data.db.WordDefinition;
import jakarta.persistence.PreRemove;

public class WordRemoveListener {

    @PreRemove
    public void wordPreRemove(WordDefinition word){
        word.getDefTable().removeWordDefinition(word);
        System.out.println("Removed lololo");
    }
}

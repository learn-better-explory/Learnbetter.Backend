package com.learnbetter.LearnBetterApi.services;

import com.learnbetter.LearnBetterApi.data.db.DefinitionsTable;
import com.learnbetter.LearnBetterApi.data.db.User;
import com.learnbetter.LearnBetterApi.data.db.WordDefinition;
import com.learnbetter.LearnBetterApi.data.repositories.DefinitionsTableRepo;
import com.learnbetter.LearnBetterApi.data.repositories.WordDefinitionsRepo;
import com.learnbetter.LearnBetterApi.exceptions.TitleTooLongException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TableService {

    private DefinitionsTableRepo definitionsTableRepo;
    private WordDefinitionsRepo wordsRepo;

    @Autowired
    public TableService(DefinitionsTableRepo definitionsTableRepo, WordDefinitionsRepo wordsRepo){
        this.definitionsTableRepo = definitionsTableRepo;
        this.wordsRepo = wordsRepo;
    }

    public List<DefinitionsTable> getAll(){
        return definitionsTableRepo.findAll();
    }

    public List<DefinitionsTable> getUserDefinitionsTables(User user){
        return definitionsTableRepo.findByOwner(user);
    }

    public DefinitionsTable getDefinitionsTableFromId(UUID uuid){
        return definitionsTableRepo.findById(uuid).orElse(null);
    }

    public void addDefinitionsTableUser(DefinitionsTable table){
        if(table.getTableName().length() > 100){
            throw new TitleTooLongException("The title for the table " + table.getTableId() + " is too long!");
        }

        definitionsTableRepo.save(table);
    }

    public void addWordDefinition(WordDefinition wordDefinition){
        wordsRepo.save(wordDefinition);
    }
    //TODO: finish this
    public void removeWordDefinition(WordDefinition wordDefinition){
        DefinitionsTable definitionsTable = wordDefinition.getDefTable();
        int oldId = wordDefinition.getWordId();

        wordsRepo.delete(wordDefinition);

        if(oldId == definitionsTable.getDefinitionsCount()){
            definitionsTable.decrementDefinitionsCount();
        }

    }


}

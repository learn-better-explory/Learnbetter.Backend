package com.learnbetter.LearnBetterApi.services;

import com.learnbetter.LearnBetterApi.data.db.DefinitionsTable;
import com.learnbetter.LearnBetterApi.data.db.User;
import com.learnbetter.LearnBetterApi.data.db.WordDefinition;
import com.learnbetter.LearnBetterApi.data.repositories.DefinitionsTableRepo;
import com.learnbetter.LearnBetterApi.data.repositories.WordDefinitionsRepo;
import com.learnbetter.LearnBetterApi.exceptions.DefinitionWordNotExists;
import com.learnbetter.LearnBetterApi.exceptions.TitleTooLongException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void updateWordDefinition(int orderTable, UUID tableId,WordDefinition newWordDefinition){
        WordDefinition wordDefinition = wordsRepo.findByOrderInTableAndTableId(orderTable, tableId);
        if(wordDefinition == null){
            throw new DefinitionWordNotExists();
        }

        if(newWordDefinition.getWord() != null) wordDefinition.setWord(newWordDefinition.getWord());
        if(newWordDefinition.getDescription() != null) wordDefinition.setDescription(newWordDefinition.getDescription());
        wordsRepo.save(wordDefinition);
    }

    public void removeWordDefinition(UUID tableId ,int orderTable){
        WordDefinition wordDefinition;
        wordDefinition = wordsRepo.findByOrderInTableAndTableId(orderTable, tableId);

        if(wordDefinition == null){
            throw new DefinitionWordNotExists();
        }

        DefinitionsTable definitionsTable = wordDefinition.getDefTable();
        int oldOrder = wordDefinition.getOrderInTable();

        wordsRepo.delete(wordDefinition);

        definitionsTable.getWords().remove(wordDefinition);
        if(oldOrder != definitionsTable.getDefinitionsCount()){
            for(int i=oldOrder; i < definitionsTable.getWords().size(); i++){
                WordDefinition word = definitionsTable.getWords().get(i);
                word.setOrderInTable(word.getOrderInTable()-1);
                wordsRepo.save(word);
            }
        }

    }


}

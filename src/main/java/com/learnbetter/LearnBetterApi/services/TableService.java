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

/**
 * This service is responsible for handling the {@link DefinitionsTable} relationship
 * with database. It should be used for getting the tables from database, adding them, deleting and
 * updating. <br>
 * It also handles the {@link WordDefiniton} relationship with tables. It manages their order in the table
 * while deleting or updating. 
 */
@Service
public class TableService {

    private DefinitionsTableRepo definitionsTableRepo;
    private WordDefinitionsRepo wordsRepo;
    private UserService userService;

    @Autowired
    public TableService(DefinitionsTableRepo definitionsTableRepo, WordDefinitionsRepo wordsRepo, UserService userService){
        this.definitionsTableRepo = definitionsTableRepo;
        this.wordsRepo = wordsRepo;
        this.userService = userService;
    }

    /**
     * Gets all the {@link DefinitionsTable}s that the user with the provided userId
     * owns.
     * @param userId The id of the user.
     * @return All of the definition tables that the user is the owner of.
     */
    public List<DefinitionsTable> getUserDefinitionsTables(long userId){
        return definitionsTableRepo.findByOwner(userService.getUser(userId));
    }

    /**
     * Gets the definition table that has the uuid provided.
     * @param uuid The id of the table to get.
     * @return The definition table with the id or null if none found.
     */
    public DefinitionsTable getDefinitionsTableFromId(UUID uuid){
        return definitionsTableRepo.findById(uuid).orElse(null);
    }

    /**
     * Adds the provided definitions table to the database and sets the owner 
     * as the user of the id provided.
     * @param userId The user id to set the owner to.
     * @param table The table to add to the database.
     */
    public void addDefinitionsTableUser(long userId ,DefinitionsTable table){
        if(table.getTableName().length() > 100){
            throw new TitleTooLongException("The title for the table " + table.getTableId() + " is too long!");
        }
        User user = userService.getUser(userId);
        table.setOwner(user);

        definitionsTableRepo.save(table);
    }

    /**
     * Removes the definitions table provided and all of its word definitions
     * from the database.
     * @param definitionsTable The table to delete
     */
    public void removeDefinitionsTable(DefinitionsTable definitionsTable){
        List<WordDefinition> tableWords = definitionsTable.getWords();
        wordsRepo.deleteAll(tableWords);

        definitionsTableRepo.delete(definitionsTable);
    }

    /**
     * Updates the definitions table name and description to the name and description
     * of the new table provided.
     * @param tableId The id of the table to update.
     * @param newTable The new table to update the old table to.
     */
    public void updateDefinitionsTable(UUID tableId ,DefinitionsTable newTable){
        DefinitionsTable oldTable = definitionsTableRepo.getReferenceById(tableId);
        oldTable.setTableName(newTable.getTableName());
        oldTable.setTableDescription(newTable.getTableDescription());

        definitionsTableRepo.save(oldTable);
    }
    
    /**
     * Adds a new word definition to the database and table specified in the
     * {@link WordDefinition#getDefTable}.
     * @param WordDefinition The word to add.
     */
    public void addWordDefinition(WordDefinition wordDefinition){
        wordsRepo.save(wordDefinition);
    }

    /**
     * Updates the word definition saved in the database to the new one provided. <br>
     * This means that it updates only the word and the definition of word in the instance.
     * @param orderTable The position of the word in the table
     * @param tableId The id of the table that the word is from
     * @param newWordDefinition The word to update the saved word to.
     * @throws DefinitionWordNotExists If the function couldn't find the word definition in the database by
     *                                 its order and the tableId. 
     */
    public void updateWordDefinition(int orderTable, UUID tableId,WordDefinition newWordDefinition){
        WordDefinition wordDefinition = wordsRepo.findByOrderInTableAndTableId(orderTable, tableId);
        if(wordDefinition == null){
            throw new DefinitionWordNotExists();
        }

        if(newWordDefinition.getWord() != null) wordDefinition.setWord(newWordDefinition.getWord());
        if(newWordDefinition.getDescription() != null) wordDefinition.setDescription(newWordDefinition.getDescription());
        wordsRepo.save(wordDefinition);
    }
    
    /**
     * Removes the word definition from the specified table . <br>
     * This means it removes it from the database and the list in the table.
     * While removing it from the list it changes the order in table variable
     * for every word to make them have the right number. For example if we had
     * a list of word definitions with their order numbers like:
     * <pre>{@code [0,1,2,3,4]}</pre>
     * When we would delete the second one this function would rearrange
     * the numbers to be in sequence, so for this example it would write:
     * <pre> {@code [0,1,2,3]} </pre>
     * @param tableId The id of the tble to remove the word definiton from
     * @param orderTable The position of the word in the table.
     * @throws DefinitionWordNotExists If the function couldn't find the word definition in the database by
     *                                 its order and the tableId. 
     */
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

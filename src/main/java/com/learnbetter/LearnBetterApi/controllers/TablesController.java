package com.learnbetter.LearnBetterApi.controllers;

import com.learnbetter.LearnBetterApi.LearnBetterApiApplication;
import com.learnbetter.LearnBetterApi.data.db.DefinitionsTable;
import com.learnbetter.LearnBetterApi.data.db.WordDefinition;
import com.learnbetter.LearnBetterApi.exceptions.DefinitionsTableException;
import com.learnbetter.LearnBetterApi.exceptions.DoesntHavePermission;
import com.learnbetter.LearnBetterApi.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This controller specifies endpoints related to {@link DefinitionsTable} deleting,
 * updating, getting and creating with the same being to {@link WordDefinition}.
 * @see TableService
 */
@RestController
@PreAuthorize("#id == principal.id")
@RequestMapping(LearnBetterApiApplication.API_PATH)
public class TablesController {


    private TableService tableService;

    @Autowired
    public TablesController(TableService tableService){
        this.tableService = tableService;
    }

    /**
     * Gets all the tables that the user owns.
     * @param id Id of the user to get the tables for.
     */
    @GetMapping("/{id}/tables")
    @ResponseStatus(HttpStatus.OK)
    public List<DefinitionsTable> getDefinitionsTablesUser(@PathVariable long id){
        return tableService.getUserDefinitionsTables(id);
    }

    /**
     * Gets the table with the specfied id.
     * @param id The id of the owner of the table
     * @param tableId The id of the table.
     * @throws DefinitionsTableException When the table is not found. Returns http code 400
     * @throws DoesntHavePermission If the user is not the owner of the table. Returns http code 403
     */
    @GetMapping("/{id}/{tableId}")
    @ResponseStatus(HttpStatus.OK)
    public DefinitionsTable getDefinitionTable(@PathVariable long id, @PathVariable UUID tableId){
        DefinitionsTable definitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, definitionsTable);

        return definitionsTable;
    }

    /**
     * Adds the specified table to the database using the 
     * {@link TableService#addDefinitionsTableUser(long, DefinitionsTable)}.
     * @param id The id of the user
     * @param definitionsTable The table to add
     * @return A map of the table uuid where the key is "tableId".
     */
    @PostMapping("/{id}/addTable")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, UUID> addDefinitionsTable(@PathVariable long id, @RequestBody DefinitionsTable definitionsTable){
        tableService.addDefinitionsTableUser(id, definitionsTable);

        return Map.of("tableId", definitionsTable.getTableId());
    }

    /**
     * Deletes the provided table from the database.
     * @param id The id of the owner of the table
     * @param tableId The id of the table.
     * @throws DefinitionsTableException When the table is not found. Returns http code 400
     * @throws DoesntHavePermission If the user is not the owner of the table. Returns http code 403
     */
    @DeleteMapping("/{id}/{tableId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDefinitionsTable(@PathVariable long id, @PathVariable UUID tableId){
        DefinitionsTable definitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, definitionsTable);

        tableService.removeDefinitionsTable(definitionsTable);
    }

    /**
     * Updates the title and description of the provided table with the changed data.
     * @param id The id of the owner of the table
     * @param tableId The id of the table.
     * @throws DefinitionsTableException When the table is not found. Returns http code 400
     * @throws DoesntHavePermission If the user is not the owner of the table. Returns http code 403
     */
    @PutMapping("/{id}/{tableId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateDefinitionsTable(@PathVariable long id, @PathVariable UUID tableId, @RequestBody DefinitionsTable definitionsTable){
        DefinitionsTable checkDefinitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, checkDefinitionsTable);

        tableService.updateDefinitionsTable(tableId, definitionsTable);
    }

    /**
     * Adds a new {@link WordDefinition} to the specified table.
     * @param id The id of the owner of the table
     * @param tableId The id of the table
     * @param wordDefinition The word to add
     * @throws DefinitionsTableException When the table is not found. Returns http code 400
     * @throws DoesntHavePermission If the user is not the owner of the table. Returns http code 403 
     */
    @PostMapping("/{id}/{tableId}/addDefWord")
    @ResponseStatus(HttpStatus.CREATED)
    public void addWordDefinition(@PathVariable long id, @PathVariable UUID tableId, @RequestBody WordDefinition wordDefinition){
        DefinitionsTable definitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, definitionsTable);

        wordDefinition.setDefTable(definitionsTable);

        tableService.addWordDefinition(wordDefinition);
    }

    /**
     * Removes the specified {@link WordDefinition} from the specified table.
     * @param id The id of the owner of the table
     * @param tableId The id of the table
     * @param orderTable The position of the word in the table
     * @throws DefinitionsTableException When the table is not found. Returns http code 400
     * @throws DoesntHavePermission If the user is not the owner of the table. Returns http code 403 
     */
    @DeleteMapping("/{id}/{tableId}/{orderTable}")
    @ResponseStatus(HttpStatus.OK)
    public void removeWordDefinition(@PathVariable long id, @PathVariable UUID tableId, @PathVariable int orderTable){
        DefinitionsTable definitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, definitionsTable);

        tableService.removeWordDefinition(tableId, orderTable);
    }   
/**
     * Updates the specified {@link WordDefinition} with the changed values.
     * @param id The id of the owner of the table
     * @param tableId The id of the table
     * @param orderTable The position of the word in the table
     * @param wordDefinition The new word to change.
     * @throws DefinitionsTableException When the table is not found. Returns http code 400
     * @throws DoesntHavePermission If the user is not the owner of the table. Returns http code 403 
     */
    @PutMapping("/{id}/{tableId}/{orderTable}")
    @ResponseStatus(HttpStatus.OK)
    public void updateWordDefinition(@PathVariable long id, @PathVariable UUID tableId, @PathVariable int orderTable, @RequestBody WordDefinition wordDefinition){
        DefinitionsTable definitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, definitionsTable);

        tableService.updateWordDefinition(orderTable,tableId,wordDefinition);
    }

    private void checkDefTableCorrect(long id, DefinitionsTable definitionsTable){
        if(definitionsTable == null){
            throw new DefinitionsTableException();
        }
        if(definitionsTable.getOwner().getId() != id){
            throw new DoesntHavePermission();
        }
    }



}

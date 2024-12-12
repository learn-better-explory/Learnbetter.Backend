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

@RestController
@PreAuthorize("#id == principal.id")
@RequestMapping(LearnBetterApiApplication.API_PATH)
public class TablesController {


    private TableService tableService;

    @Autowired
    public TablesController(TableService tableService){
        this.tableService = tableService;
    }

    @GetMapping("/{id}/tables")
    @ResponseStatus(HttpStatus.OK)
    public List<DefinitionsTable> getDefinitionsTablesUser(@PathVariable long id){
        return tableService.getUserDefinitionsTables(id);
    }

    @GetMapping("/{id}/{tableId}")
    @ResponseStatus(HttpStatus.OK)
    public DefinitionsTable getDefinitionTable(@PathVariable long id, @PathVariable UUID tableId){
        DefinitionsTable definitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, definitionsTable);

        return definitionsTable;
    }

    @PostMapping("/{id}/addTable")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, UUID> addDefinitionsTable(@PathVariable long id, @RequestBody DefinitionsTable definitionsTable){
        tableService.addDefinitionsTableUser(id, definitionsTable);

        return Map.of("tableId", definitionsTable.getTableId());
    }

    @DeleteMapping("/{id}/{tableId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDefinitionsTable(@PathVariable long id, @PathVariable UUID tableId){
        DefinitionsTable definitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, definitionsTable);

        tableService.removeDefinitionsTable(definitionsTable);
    }

    @PutMapping("/{id}/{tableId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateDefinitionsTable(@PathVariable long id, @PathVariable UUID tableId, @RequestBody DefinitionsTable definitionsTable){
        DefinitionsTable checkDefinitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, checkDefinitionsTable);

        tableService.updateDefinitionsTable(tableId, definitionsTable);
    }

    @PostMapping("/{id}/{tableId}/addDefWord")
    @ResponseStatus(HttpStatus.CREATED)
    public void addWordDefinition(@PathVariable long id, @PathVariable UUID tableId, @RequestBody WordDefinition wordDefinition){
        DefinitionsTable definitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, definitionsTable);

        wordDefinition.setDefTable(definitionsTable);

        tableService.addWordDefinition(wordDefinition);
    }



    @DeleteMapping("/{id}/{tableId}/{orderTable}")
    @ResponseStatus(HttpStatus.OK)
    public void removeWordDefinition(@PathVariable long id, @PathVariable UUID tableId, @PathVariable int orderTable){
        DefinitionsTable definitionsTable = tableService.getDefinitionsTableFromId(tableId);
        checkDefTableCorrect(id, definitionsTable);

        tableService.removeWordDefinition(tableId, orderTable);
    }

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

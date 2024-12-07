package com.learnbetter.LearnBetterApi.controllers;

import com.learnbetter.LearnBetterApi.LearnBetterApiApplication;
import com.learnbetter.LearnBetterApi.data.db.DefinitionsTable;
import com.learnbetter.LearnBetterApi.data.db.User;
import com.learnbetter.LearnBetterApi.data.db.WordDefinition;
import com.learnbetter.LearnBetterApi.data.repositories.UserRepo;
import com.learnbetter.LearnBetterApi.exceptions.UserNotFoundException;
import com.learnbetter.LearnBetterApi.services.TableService;
import com.learnbetter.LearnBetterApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(LearnBetterApiApplication.API_PATH)
public class TablesController {


    private TableService tableService;
    private UserService userService;

    @Autowired
    public TablesController(TableService tableService, UserService userService){
        this.tableService = tableService;
        this.userService = userService;
    }

    @GetMapping("/tables")
    public List<DefinitionsTable> getAllTables(){
        return tableService.getAll();
    }

    @GetMapping("/{id}/tables")
    public List<DefinitionsTable> getDefinitionsTablesUser(@PathVariable long id){
        User user = getUserFromId(id);

        return tableService.getUserDefinitionsTables(user);
    }

    @PreAuthorize("#id == principal.id")
    @PostMapping("/{id}/addTable")
    public DefinitionsTable addDefinitionsTable(@PathVariable long id, @RequestBody DefinitionsTable definitionsTable){
        User user = getUserFromId(id);

        definitionsTable.setOwner(user);

        tableService.addDefinitionsTableUser(user, definitionsTable);
        return definitionsTable;
    }

    @PreAuthorize("#id == principal.id")
    @PostMapping("/{id}/{tableId}/addDefWord")
    public void addWordDefinition(@PathVariable long id, @PathVariable UUID tableId, @RequestBody WordDefinition wordDefinition){
        User user = getUserFromId(id);
        DefinitionsTable definitionsTable = tableService.getDefinitionsTableFromId(tableId);

        if(definitionsTable == null){
            throw new UserNotFoundException("");
        }
        if(!definitionsTable.getOwner().equals(user)){
            throw new UserNotFoundException("");
        }
        wordDefinition.setDefTable(definitionsTable);

        tableService.addWordDefinition(wordDefinition);
    }

    private User getUserFromId(long id){
        User user = userService.getUser(id);
        if(user == null){
            throw new UserNotFoundException("User with id " + id + " not found!");
        }

        return user;

    }

}

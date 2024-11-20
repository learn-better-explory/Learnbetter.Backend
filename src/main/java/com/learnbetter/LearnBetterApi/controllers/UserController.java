package com.learnbetter.LearnBetterApi.controllers;

import com.learnbetter.LearnBetterApi.data.UserPrincipal;
import com.learnbetter.LearnBetterApi.data.db.User;
import com.learnbetter.LearnBetterApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "hello")
    public String testes(){
        return "world";
    }

    @PostMapping(value = "/register")
    public User register(@RequestBody User user){
        return userService.addUser(user);
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable long id, Principal principal){
        return userService.getUser(id);
    }

}

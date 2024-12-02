package com.learnbetter.LearnBetterApi.controllers;

import com.learnbetter.LearnBetterApi.LearnBetterApiApplication;
import com.learnbetter.LearnBetterApi.data.db.User;
import com.learnbetter.LearnBetterApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = LearnBetterApiApplication.API_PATH)
public class LoginController {


    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping(value = "/users-test")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String login(@RequestBody User user){
        return userService.loginUser(user);
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user){
        return userService.addUser(user);
    }

}

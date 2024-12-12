package com.learnbetter.LearnBetterApi.controllers;

import com.learnbetter.LearnBetterApi.LearnBetterApiApplication;
import com.learnbetter.LearnBetterApi.data.db.User;
import com.learnbetter.LearnBetterApi.exceptions.UserNotFoundException;
import com.learnbetter.LearnBetterApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = LearnBetterApiApplication.API_PATH)
public class LoginController {


    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping(value = "/users-test")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> login(@RequestBody User user){
        User realUser = userService.getUserByUsername(user.getUsername());
        if(realUser == null){
            throw new UserNotFoundException("User not found!");
        }

        return Map.of("token",userService.loginUser(user), "id" , realUser.getId());
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user){
        return userService.addUser(user);
    }

}

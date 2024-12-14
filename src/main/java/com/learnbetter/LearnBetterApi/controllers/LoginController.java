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

/**
 * This is a controller which defines all the endpoints
 * for actions related to user managing. It allows requests
 * to get the JWT token of a user and to create a new user.
 */
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

    /**
     * Logins in the specified user using the {@link UserService#loginUser(User)} method.
     * @param user The user to get the JWT token for.
     * @return A map of the JWT token of the specified user with the key "token" and
     *         the id of the user which has logged in with the key "id".
     */
    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> login(@RequestBody User user){
        User realUser = userService.getUserByUsername(user.getUsername());
        if(realUser == null){
            throw new UserNotFoundException("User not found!");
        }

        return Map.of("token",userService.loginUser(user), "id" , realUser.getId());
    }

    /**
     * Registers (adds) a new user to the database using the {@link UserService#addUser(User)}
     * method.
     * @return The registered user.
     */
    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user){
        return userService.addUser(user);
    }

}

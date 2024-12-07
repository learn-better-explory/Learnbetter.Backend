package com.learnbetter.LearnBetterApi.services;


import com.learnbetter.LearnBetterApi.configs.SecurityConfig;
import com.learnbetter.LearnBetterApi.data.db.User;
import com.learnbetter.LearnBetterApi.data.db.UserStatus;
import com.learnbetter.LearnBetterApi.data.repositories.UserRepo;
import com.learnbetter.LearnBetterApi.data.repositories.UserStatusRepo;
import com.learnbetter.LearnBetterApi.exceptions.PasswordDoesntMatchException;
import com.learnbetter.LearnBetterApi.exceptions.UserAlreadyExistsException;
import com.learnbetter.LearnBetterApi.exceptions.UserNotFoundException;
import com.learnbetter.LearnBetterApi.exceptions.WrongRegisterDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepo userRepo;
    private UserStatusRepo userStatusRepo;
    private SecurityConfig securityConfig;
    private JwtService jwtService;
    private MyUserDetailsService userDetailsService;

    @Autowired
    public UserService(UserRepo userRepo,
                       UserStatusRepo userStatusRepo,
                       SecurityConfig securityConfig,
                       JwtService jwtService,
                       MyUserDetailsService userDetailsService){
        this.userRepo = userRepo;
        this.userStatusRepo = userStatusRepo;
        this.securityConfig = securityConfig;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @PostAuthorize("returnObject.getUsername() == authentication.name")
    public User getUser(long id){
        return userRepo.findById(id).orElse(null);
    }

    public String loginUser(User user){
        User foundUser = userRepo.findByUsername(user.getUsername());
        if(foundUser == null)
            throw new UserNotFoundException("The user with name " + user.getUsername() + " not found!");
        if(!securityConfig.getPasswordEncoder().matches(user.getPassword(), foundUser.getPassword()))
            throw new PasswordDoesntMatchException("The password for the user " + user.getId() + " isn't correct!");

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        return jwtService.generateToken(userDetails);
    }



    public User addUser(User user){
        if(user == null)
            return null;
        if(!verifyUserData(user))
            throw new WrongRegisterDataException("The details of " + user + " are incorrect!");

        if(userRepo.findByEmailOrUsername(user.getEmail(), user.getUsername()) != null)
            throw new UserAlreadyExistsException("User with id " + user.getId() + " already exists!");

        UserStatus userStatus = new UserStatus();
        user.setStatus(userStatus);
        user.setPassword(securityConfig.getPasswordEncoder().encode(user.getPassword()));
        user.setStreak(0);
        userStatusRepo.save(userStatus);
        userRepo.save(user);
        return user;
    }

    public List<User> getUsers(){
        return userRepo.findAll();
    }

    private boolean verifyUserData(User user){
        if(user.getUsername().contains(" ")) return false;
        if(!user.getEmail().contains("@")) return false;

        return user.getEmail().split("@")[1].contains(".");
    }


}

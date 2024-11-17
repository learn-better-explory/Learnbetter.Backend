package com.learnbetter.LearnBetterApi.services;

import com.learnbetter.LearnBetterApi.configs.SecurityConfig;
import com.learnbetter.LearnBetterApi.data.db.User;
import com.learnbetter.LearnBetterApi.data.db.UserStatus;
import com.learnbetter.LearnBetterApi.data.repositories.UserRepo;
import com.learnbetter.LearnBetterApi.data.repositories.UserStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserStatusRepo userStatusRepo;
    @Autowired
    private SecurityConfig securityConfig;

    public User getUser(long id){
        return userRepo.findById(id).orElse(null);
    }

    public User addUser(User user){
        UserStatus userStatus = new UserStatus();
        user.setStatus(userStatus);
        user.setPassword(securityConfig.getPasswordEncoder().encode(user.getPassword()));
        userRepo.save(user);
        userStatusRepo.save(userStatus);
        return user;
    }


}

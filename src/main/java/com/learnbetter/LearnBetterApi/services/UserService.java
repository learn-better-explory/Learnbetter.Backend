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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service. is responsible for logging, registering users and getting them. <br>
 * It only interacts with the database to get the {@link User} objects from it, 
 * the authorization part of this application logic is handled in {@link JwtService}.
 * @see JwtService
 */
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

    /**
     * Gets the user with the id provided from the database. <br>
     * @param id The id to get the user with.
     * @throws UserNotFoundException When it could not find a user with the specified
     *                               id. 
     */
    @PostAuthorize("returnObject.getUsername() == authentication.name")
    public User getUser(long id){
        User user = userRepo.findById(id).orElse(null);
        if(user == null){
            throw new UserNotFoundException("User with id " + id + " not found!");
        }

        return user;
    }

    /**
     * Gets the user from the database with the specified username.
     * @param username The username to get the user for.
     * @return THe user with the specified username or null if none found.
     */
    public User getUserByUsername(String username){
        User user;
        try{
            user = userRepo.findByUsername(username);
        }catch (EntityNotFoundException e){
            return null;
        }
        return user;
    }

    /**
     * Gets the user from the database and checks if it exists
     * and if the passwords match. After that it generates a unique JWT token
     * to identify the user using {@link JwtService#generateToken(UserDetails)}.
     * @param user The user to log in to generate the token for.
     * @return The generated JWT token which identifies the user in the system.
     * @throws PasswordDoesntMatchException When the password for the provided user
     *                                      does not match the password for the user got from
     *                                      the database.
     * @see JwtService
     */
    public String loginUser(User user){
        User foundUser = getUserByUsername(user.getUsername());
        if(!securityConfig.getPasswordEncoder().matches(user.getPassword(), foundUser.getPassword()))
            throw new PasswordDoesntMatchException("The password for the user " + user.getId() + " isn't correct!");

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        return jwtService.generateToken(userDetails);
    }


    /**
     * Adds (registers) a new user to the database. 
     * This functions checks if the user already exists and if the data
     * is formated correctly to ensure integrity.
     * @param user The user to add to the database
     * @return The created user.
     * @throws WrongRegisterDataException When the data of user provided is incorrect. This means:<br>
     *                                    The email does not have the @ sign or . the url part <br>
     *                                    The username contains spaces. Returns http code 400
     * @throws UserAlreadyExistsException When the password of the passed user does not match the password
     *                                    of the user saved in the database. Returns http code 403
     */
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

    /**
     * Gets all users. (Probably going to remove it soon)
     */
    public List<User> getUsers(){
        return userRepo.findAll();
    }

    /**
     * Verifies the user data, it checks the username and email. <br>
     * For more detail check out {@link UserService#addUser(User)}
     */
    private boolean verifyUserData(User user){
        if(user.getUsername().contains(" ")) return false;
        if(!user.getEmail().contains("@")) return false;

        return user.getEmail().split("@")[1].contains(".");
    }


}

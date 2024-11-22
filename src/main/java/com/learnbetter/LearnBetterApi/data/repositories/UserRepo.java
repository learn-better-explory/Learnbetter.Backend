package com.learnbetter.LearnBetterApi.data.repositories;

import com.learnbetter.LearnBetterApi.data.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByEmailOrUsername(String email, String username);

}

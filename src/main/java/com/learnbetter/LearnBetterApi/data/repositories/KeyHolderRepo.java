package com.learnbetter.LearnBetterApi.data.repositories;

import com.learnbetter.LearnBetterApi.data.db.SecretKeyHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeyHolderRepo extends org.springframework.data.repository.Repository<SecretKeyHolder, String> {

    @Query("SELECT kh.getKey() FROM SecretKeyHolder kh")
    Optional<String> getKey();

    @Query("INSERT INTO SecretKeyHolder VALUES (?1)")
    void addKey(String key);
}

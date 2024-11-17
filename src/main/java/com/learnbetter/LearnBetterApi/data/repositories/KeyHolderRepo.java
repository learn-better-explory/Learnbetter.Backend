package com.learnbetter.LearnBetterApi.data.repositories;

import com.learnbetter.LearnBetterApi.data.db.SecretKeyHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyHolderRepo extends org.springframework.data.repository.Repository<SecretKeyHolder, String> {

    @Query("SELECT kh.getKey() FROM SecretKeyHolder kh")
    String getKey();
}

package com.learnbetter.LearnBetterApi.data.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class KeyHolderRepo {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public Optional<String> getKey(){
        String keyS;
        try {
            keyS = (String) entityManager
                    .createNativeQuery("SELECT key FROM key_holder")
                    .getSingleResult();
        }catch (NoResultException e){
            keyS = null;
        }

        return Optional.ofNullable(keyS);
    }

    @Transactional
    public void addKey(String key){
        entityManager.createNativeQuery("INSERT INTO key_holder (\"key\") VALUES (?)")
                .setParameter(1, key)
                .executeUpdate();
    }



}

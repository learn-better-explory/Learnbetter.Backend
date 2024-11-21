package com.learnbetter.LearnBetterApi.services;

import com.learnbetter.LearnBetterApi.data.repositories.KeyHolderRepo;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Service
public class JwtService {

    @Autowired
    private KeyHolderRepo keyRepo;
    private String secret;

    public JwtService() throws NoSuchAlgorithmException {
        Optional<String> keyO = keyRepo.getKey();
        String key;
        if(keyO.isEmpty()){
            key = generateSecret();
            keyRepo.addKey(key);
        }else{
            key = keyO.get();
        }
    }

    private String generateSecret() throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(KeyGenerator.getInstance("hmacSHA256").generateKey().getEncoded());
    }


}

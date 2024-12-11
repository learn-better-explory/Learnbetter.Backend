package com.learnbetter.LearnBetterApi.services;

import com.learnbetter.LearnBetterApi.data.repositories.KeyHolderRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    private String secret;

    @Autowired
    public JwtService(KeyHolderRepo keyRepo) throws NoSuchAlgorithmException {
        Optional<String> keyO = keyRepo.getKey();
        String key;
        if(keyO.isEmpty()){
            key = generateSecret();
            keyRepo.addKey(key);
        }else{
            key = keyO.get();
        }

        secret = key;

    }


    public boolean verify(String token, UserDetails userDetails){
        Claims claims = getClaims(token);

        return claims.getSubject().equals(userDetails.getUsername()) && claims.getIssuedAt().before(new Date());
    }

    public String getUsername(String token){
        Claims claims = getClaims(token);

        return claims.getSubject();
    }

    public String generateToken(UserDetails userDetails){
        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .signWith(getKey())
                .compact();
    }

    private Claims getClaims(String token){
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
    }

    private String generateSecret() throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(KeyGenerator.getInstance("HmacSHA256").generateKey().getEncoded());
    }


}

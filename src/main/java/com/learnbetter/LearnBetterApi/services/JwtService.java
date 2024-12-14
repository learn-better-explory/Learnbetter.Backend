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

/**
 * This service is responsible for generating and verifying JWT token in this project. <br>
 * It gets a secret key from the database using {@link KeyHolderRepo} and if it doesn't exist it generates new and 
 * saves it to the database. Later from this secret key it generates keys that are you for token generation
 * and verification. Thanks to this system tokens are saved between sessions and different system launches.
 */
@Service
public class JwtService {

    private String secret;

    /**
     * Creates a new JwtService. 
     * @param keyRepo The repository to get the stored key from.
     */
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

    /**
     * Verifies the given key using the user details provided. <br>
     * It decrypts the key and checks if the username of it is the same as the username of user details
     * and if the issued at date is before the current date.
     * @param token The token to verify
     * @param userDetails The user details to verify against.
     * @return If the token is valid and for this user or not.
     */
    public boolean verify(String token, UserDetails userDetails){
        Claims claims = getClaims(token);

        return claims.getSubject().equals(userDetails.getUsername()) && claims.getIssuedAt().before(new Date());
    }

    /**
     * Gets the username from the encrypted token.
     * @param token The token to get the username from.
     * @return The username encrypted in this JWT token.
     */
    public String getUsername(String token){
        Claims claims = getClaims(token);

        return claims.getSubject();
    }

    /**
     * Generates a new JWT token based on the user details provided. <br>
     * This function encrypts the username from the user details and sets the
     * issued at date to current date.
     * @param userDetails The user details to generate the JWT token for.
     * @return A new JWT token which identifies this user.
     */
    public String generateToken(UserDetails userDetails){
        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .signWith(getKey())
                .compact();
    }

    /**
     * Returns the {@link Claims} decrypted from the JWT token provided.
     * @param token The token the get the claims from.
     * @return The decrypted {@link Claims}.
     */
    private Claims getClaims(String token){
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Gets the key from the secret stored in this class. <br>
     * It uses the hmacSha algorithm to encode it.
     * @return The generated key.
     */
    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
    }

    /**
     * Generates the secret stored in this class. <br>
     * @return The secret string to generate the key from.
     */
    private String generateSecret() throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(KeyGenerator.getInstance("HmacSHA256").generateKey().getEncoded());
    }


}

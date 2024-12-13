package com.learnbetter.LearnBetterApi.data.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class holds the secret that is used in the JwtService
 * to generate the keys.
 * @see JwtService
 */
@Entity
@Table(name = "key_holder")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecretKeyHolder {
    @Id
    private String key;
}

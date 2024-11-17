package com.learnbetter.LearnBetterApi.data.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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

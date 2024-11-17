package com.learnbetter.LearnBetterApi.data.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @SequenceGenerator(name = "id_generator", sequenceName = "id_generator",  allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "id_generator")
    @Column(name = "user_id")
    private long id;
    private String username;
    private String password;
    private String email;
    private int streak;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "us_id")
    private UserStatus status;


    public User(String username, String password, String email, int streak, UserStatus status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.streak = streak;
        this.status = status;
    }
}

package com.learnbetter.LearnBetterApi.data.db;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents a user which is registered in the system. <br>
 * This entity is a representation for more of the non-authentication work
 * that is used for the app. Except the obvious data it holds a streak of every user
 * for the tables.
 */
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
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "us_id")
    private UserStatus status;

    /**
     * Creates a new user.
     * @param username The username of this user.
     * @param password The password (encrypted) of this user.
     * @param email The email of this user.
     * @param streak The streak of this user.
     * @param status A status of this user. Hold data for the authorization part.
     */
    public User(String username, String password, String email, int streak, UserStatus status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.streak = streak;
        this.status = status;
    }
}

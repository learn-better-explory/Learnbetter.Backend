package com.learnbetter.LearnBetterApi.data.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Holds the status of a user, this means basic account data
 * like:
 * <ul>
 *  <li> Is the account expired </li>
 *  <li> Is this account locked</li>
 *  <li> Are their credentials (username, password) expired </li>
 *  <li> Is this account enabled</li>
 * </ul>
 */
@Entity
@Table(name = "user_status")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserStatus {

    @Id
    @SequenceGenerator(name = "us_generator", sequenceName = "us_generator", allocationSize = 1)
    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "us_generator")
    @Column(name = "us_id")
    private long id;
    private boolean isExpired;
    private boolean isLocked;
    private boolean isCredentialsExpired;
    private boolean isEnabled;

    public UserStatus(){
        this.isExpired = false;
        this.isLocked = false;
        this.isEnabled = true;
        this.isCredentialsExpired = false;
    }

    public UserStatus(boolean isExpired, boolean isLocked, boolean isEnabled, boolean isCredentialsExpired) {
        this.isExpired = isExpired;
        this.isLocked = isLocked;
        this.isEnabled = isEnabled;
        this.isCredentialsExpired = isCredentialsExpired;
    }
}

package com.learnbetter.LearnBetterApi.data.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

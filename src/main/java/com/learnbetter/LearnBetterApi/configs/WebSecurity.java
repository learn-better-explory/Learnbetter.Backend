package com.learnbetter.LearnBetterApi.configs;

import com.learnbetter.LearnBetterApi.data.UserPrincipal;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

@Configuration
public class WebSecurity {

    public boolean check(Authentication authentication, long id){

        if(authentication.getPrincipal() instanceof UserPrincipal userPrincipal){
            return userPrincipal.getUser().getId() == id;
        }

        return false;
    }

}

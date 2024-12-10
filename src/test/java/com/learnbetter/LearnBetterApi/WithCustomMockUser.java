package com.learnbetter.LearnBetterApi;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithCustomMockUser {

    String username() default "Test";

    String password() default "Password";

    String email() default "test@example.com";

    long id() default 1;
}

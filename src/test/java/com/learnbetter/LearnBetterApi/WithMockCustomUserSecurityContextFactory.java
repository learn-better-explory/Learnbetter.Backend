package com.learnbetter.LearnBetterApi;

import com.learnbetter.LearnBetterApi.data.UserPrincipal;
import com.learnbetter.LearnBetterApi.data.db.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    /**
     * Create a {@link SecurityContext} given an Annotation.
     *
     * @return the {@link SecurityContext} to use. Cannot be null.
     */
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser mockUser) {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        UserPrincipal userPrincipal = new UserPrincipal(new User(mockUser.id(), mockUser.username(), mockUser.password(), mockUser.email(), 0, null));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

        securityContext.setAuthentication(authToken);

        return securityContext;
    }
}

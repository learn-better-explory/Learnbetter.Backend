package com.learnbetter.LearnBetterApi.configs;

import com.learnbetter.LearnBetterApi.services.JwtService;
import com.learnbetter.LearnBetterApi.services.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private ApplicationContext applicationContext;
    private JwtService jwtService;

    @Autowired
    public JwtFilter(JwtService jwtService, ApplicationContext applicationContext){
        this.jwtService = jwtService;
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authString = request.getHeader("Authorization");
        if(authString != null && authString.contains("Bearer")){
            String token = authString.substring(7);
            String username = jwtService.getUsername(token);

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null ){
                UserDetails userDetails = applicationContext.getBean(MyUserDetailsService.class).loadUserByUsername(username);
                if(jwtService.verify(token, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

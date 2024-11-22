package com.learnbetter.LearnBetterApi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnbetter.LearnBetterApi.configs.SecurityConfig;
import com.learnbetter.LearnBetterApi.controllers.LoginController;
import com.learnbetter.LearnBetterApi.data.UserPrincipal;
import com.learnbetter.LearnBetterApi.data.db.User;
import com.learnbetter.LearnBetterApi.data.repositories.KeyHolderRepo;
import com.learnbetter.LearnBetterApi.data.repositories.UserRepo;
import com.learnbetter.LearnBetterApi.data.repositories.UserStatusRepo;
import com.learnbetter.LearnBetterApi.services.JwtService;
import com.learnbetter.LearnBetterApi.services.MyUserDetailsService;
import com.learnbetter.LearnBetterApi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class LoginControllerTests {

    @Mock
    UserService userService;

    @Mock
    private UserRepo userRepo;
    @Mock
    private UserStatusRepo statusRepo;
    @Mock
    private KeyHolderRepo keyHolderRepo;
    @Mock
    private MyUserDetailsService userDetailsService;

    private SecurityConfig securityConfig;
    private MockMvc mockMvc;

    @BeforeEach()
    public void setUp() throws NoSuchAlgorithmException {
        this.securityConfig = new SecurityConfig();
        setupMocks();

        JwtService jwtService = new JwtService(keyHolderRepo);
        userService = new UserService(userRepo, statusRepo, securityConfig, jwtService, userDetailsService);
        LoginController loginController = new LoginController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(new GlobalControllerExceptionHandler())
                .build();
    }

    private void setupMocks(){
        User user = new User("Test", securityConfig.getPasswordEncoder().encode("TestPassword"), "test@example.com", 0, null);

        userRepo = mock();
        when(userRepo.findByEmailOrUsername("popularemail@example.com", "PopularName"))
                .thenReturn(new User("PopularName", "TestPassword", "popularemail@example.com", 0, null));
        when(userRepo.findByUsername("Test")).thenReturn(user);

        userDetailsService = mock();
        when(userDetailsService.loadUserByUsername("Test")).thenReturn(new UserPrincipal(user));

        keyHolderRepo = mock(KeyHolderRepo.class);
        when(keyHolderRepo.getKey()).thenReturn(Optional.of("uSkpMDXa2+khZ+xuLxrqU2rYGBh8C4V/fY42/nWeuxM="));

    }

    @Test
    @DisplayName("Adding correct user. Should return code 201")
    public void testAddingCorrectUser() throws Exception {
        User user = new User("New", "NewPassword", "new@example.com", 0, null);

        mockMvc.perform(getPostRequestForUser("/api/v1/register", user))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test registration with user which already exists. Should return code 400")
    public void testRegisterExistingUser() throws Exception{
        User user = new User("PopularName", "TestPassword", "popularemail@example.com", 0, null);

        mockMvc.perform(getPostRequestForUser("/api/v1/register", user))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test username with spaces. Should return code 400")
    public void testAddingIncorrectUsernameUser() throws Exception{
        User user = new User("Test incorrect", "TestPassword", "example@example.com", 0, null);

        mockMvc.perform(getPostRequestForUser("/api/v1/register", user))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test email without @ in the email. Should return code 400")
    public void testAddingIncorrectEmailAtSign() throws Exception{
        User user = new User("Test", "TestPassword", "example", 0, null);

        mockMvc.perform(getPostRequestForUser("/api/v1/register", user))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test email without dot in the website part of email. Should return code 400")
    public void testAddingIncorrectWebsiteEmail() throws Exception{
        User user = new User("Test", "TestPassword", "example@examplecom", 0, null);

        mockMvc.perform(getPostRequestForUser("/api/v1/register", user))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Tests correct login. Should return code 202")
    public void testCorrectLogin() throws Exception{
        User user = new User("Test", "TestPassword", "test@example.com", 0, null);

        mockMvc.perform(getPostRequestForUser("/api/v1/login", user))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("Tests login with no existing user in the database. Should return code 400")
    public void testNoExistingLogin() throws Exception{
        User user = new User("I_Dont_Exist", "Non_Existing_Password", "noexist@noexist.com", 0, null);

        mockMvc.perform(getPostRequestForUser("/api/v1/login", user))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Tests login with incorrect password. Should return code 403")
    public void testWrongPassword() throws Exception{
        User user =  new User("Test", "WrongPassword", "test@example.com", 0, null);

        mockMvc.perform(getPostRequestForUser("/api/v1/login", user))
                .andExpect(status().isForbidden());
    }

    private MockHttpServletRequestBuilder getPostRequestForUser(String path, User user){
        return MockMvcRequestBuilders.post(path)
                .content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

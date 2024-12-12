package com.learnbetter.LearnBetterApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnbetter.LearnBetterApi.data.UserPrincipal;
import com.learnbetter.LearnBetterApi.data.db.DefinitionsTable;
import com.learnbetter.LearnBetterApi.data.db.User;
import com.learnbetter.LearnBetterApi.data.db.WordDefinition;
import com.learnbetter.LearnBetterApi.data.repositories.WordDefinitionsRepo;
import com.learnbetter.LearnBetterApi.services.TableService;
import com.learnbetter.LearnBetterApi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration()
public class DefinitionsTableTests {

    @MockBean
    TableService tableService;
    @MockBean
    WordDefinitionsRepo wordDefinitionsRepo;
    @MockBean
    UserService userService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithCustomMockUser
    public void testAddCorrectTable_shouldReturn201() throws Exception {
        User user = new User(1, "Test", "Password", "test@example.com", 0, null);
        DefinitionsTable definitionsTable = new DefinitionsTable(null,
                "Testes", "This is a test table");
        when(this.userService.getUser(anyLong())).thenReturn(user);

        mockMvc.perform(
                createPostRequest("/api/v1/1/addTable", definitionsTable)
        ).andExpect(status().isCreated());
    }

    @Test
    @WithCustomMockUser
    public void testNoPermissionAddTable_shouldReturn403() throws Exception {
        DefinitionsTable definitionsTable = new DefinitionsTable(null, "Another test table", "Yeah idk");

        mockMvc.perform(
                createPostRequest("/api/v1/2/addTable", definitionsTable)
        ).andExpect(status().isForbidden());
    }

//    @Test
//    @WithCustomMockUser
//    public void testTooLongTitle_shouldReturn400() throws Exception{
//        DefinitionsTable definitionsTable = new DefinitionsTable(null, "This is a very long title which should not be allowed to exist " +
//                "in this application because you should use the description for that", "Yeah idk");
//
//        Mockito.doCallRealMethod().when(tableService).addDefinitionsTableUser(definitionsTable);
//
//        mockMvc.perform(
//                createPostRequest("/api/v1/1/addTable", definitionsTable)
//        ).andExpect(status().isBadRequest());
//    }

    @Test
    @WithCustomMockUser
    public void testCorrectAddDefWord_shouldReturn201() throws Exception {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID uuid = UUID.randomUUID();
        DefinitionsTable definitionsTable = new DefinitionsTable(uuid, "Test desc", userPrincipal.getUser(), "Test table");

        given(this.tableService.getDefinitionsTableFromId(uuid)).willReturn(definitionsTable);

        WordDefinition wordDefinition = new WordDefinition(definitionsTable, "Test", "This is a test word");

        mockMvc.perform(createPostRequest("/api/v1/1/"+ uuid + "/addDefWord", wordDefinition))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomMockUser
    public void testAddDefWordWithNoTable_shouldReturn400() throws Exception {
        UUID uuid = UUID.randomUUID();
        DefinitionsTable definitionsTable = new DefinitionsTable(uuid, "Test desc", null, "Test table");

        WordDefinition wordDefinition = new WordDefinition(definitionsTable, "TEst", "This is a test word");

        Mockito.doCallRealMethod().when(tableService).addWordDefinition(wordDefinition);

        mockMvc.perform(createPostRequest("/api/v1/1/"+uuid + "/addDefWord", wordDefinition))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomMockUser
    public void testAddDefWordWithNoPermissions_shouldReturn403() throws Exception{
        UUID uuid = UUID.randomUUID();
        DefinitionsTable definitionsTable = new DefinitionsTable(uuid, "Test desc", new User(2, "Test", "Password", "test@example.com", 0, null), "Test table");
        given(this.tableService.getDefinitionsTableFromId(uuid)).willReturn(definitionsTable);

        mockMvc.perform(
                createPostRequest("/api/v1/2/"+uuid + "/addDefWord", definitionsTable)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithCustomMockUser
    public void testDeleteDefWord_shouldReturn200() throws Exception {
        UUID uuid = UUID.randomUUID();
        DefinitionsTable definitionsTable = mock();
        mockTable(uuid, definitionsTable);
        WordDefinition wordDefinition = new WordDefinition(definitionsTable, "Test", "Testes");

        when(wordDefinitionsRepo.findByOrderInTableAndTableId(anyInt(), any())).thenReturn(wordDefinition);

        doReturn(List.of(wordDefinition)).when(definitionsTable).getWords();

        doReturn(definitionsTable).when(this.tableService).getDefinitionsTableFromId(uuid);

        mockMvc.perform(
                delete("/api/v1/1/" + uuid + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @WithCustomMockUser
    public void testDeleteDefWordNoWord_shouldReturn400() throws Exception {
        UUID uuid = UUID.randomUUID();
        DefinitionsTable definitionsTable = mock();
        mockTable(uuid, definitionsTable);
        WordDefinition wordDefinition = new WordDefinition(definitionsTable, "Test", "Testes");

        when(wordDefinitionsRepo.findByOrderInTableAndTableId(anyInt(), any())).thenReturn(null);

        doReturn(List.of(wordDefinition)).when(definitionsTable).getWords();

        doReturn(definitionsTable).when(this.tableService).getDefinitionsTableFromId(uuid);

        mockMvc.perform(
                delete("/api/v1/1/" + uuid + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @WithCustomMockUser
    public void testUpdateDefWordCorrect_shouldReturn200() throws Exception {
        UUID uuid = UUID.randomUUID();
        DefinitionsTable definitionsTable = mock();
        mockTable(uuid, definitionsTable);

        WordDefinition wordDefinition = new WordDefinition(definitionsTable, "Test", "Testes");
        when(wordDefinitionsRepo.findByOrderInTableAndTableId(anyInt(), any())).thenReturn(null);

        doReturn(List.of(wordDefinition)).when(definitionsTable).getWords();
        doReturn(definitionsTable).when(this.tableService).getDefinitionsTableFromId(uuid);
        when(wordDefinitionsRepo.findByOrderInTableAndTableId(anyInt(), any())).thenReturn(wordDefinition);

        mockMvc.perform(
                delete("/api/v1/1/" + uuid + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @WithCustomMockUser
    public void testUpdateDefWordNoWord_shouldReturn400() throws Exception {
        UUID uuid = UUID.randomUUID();
        DefinitionsTable definitionsTable = mock();
        mockTable(uuid, definitionsTable);

        WordDefinition wordDefinition = new WordDefinition(definitionsTable, "Test", "Testes");
        when(wordDefinitionsRepo.findByOrderInTableAndTableId(anyInt(), any())).thenReturn(null);

        doReturn(List.of(wordDefinition)).when(definitionsTable).getWords();
        doReturn(definitionsTable).when(this.tableService).getDefinitionsTableFromId(uuid);

        mockMvc.perform(
                delete("/api/v1/1/" + uuid + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    private void mockTable(UUID uuid , DefinitionsTable mock){
        when(mock.getTableId()).thenReturn(uuid);
        when(mock.getOwner()).thenReturn(new User(1, "Test", "Password", "szgiemza@gmail.com", 0, null));
        when(mock.getDefinitionsCount()).thenReturn(0);
    }


    private MockHttpServletRequestBuilder createPostRequest(String path, Object object){
        return post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(object));
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

package com.teya.tiny_bank.web;

import com.teya.tiny_bank.dto.UserDto;
import com.teya.tiny_bank.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void create() throws Exception {
        final String name = "Optimus";
        final String surname = "Prime";

        UserDto result = new UserDto();
        UUID userId = UUID.randomUUID();
        result.setId(userId);
        result.setName(name);
        result.setSurname(surname);

        when(userService.create(any(UserDto.class))).thenReturn(result);

        final String json = "{\"name\": \"" + name + "\", \"surname\":\"" + surname + "\"}";

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.surname").value(surname));
    }

    @Test
    public void deactivate() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(delete("/api/v1/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getById() throws Exception {
        final String name = "Bruce";
        final String surname = "Wayne";

        UserDto userDto = new UserDto();
        UUID userId = UUID.randomUUID();
        userDto.setId(userId);
        userDto.setName(name);
        userDto.setSurname(surname);

        when(userService.getById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.surname").value(surname));
    }

    @Test
    public void createAccountForUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        when(userService.createAccountForUser(userId)).thenReturn(accountId);

        mockMvc.perform(post("/api/v1/user/account")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\""+ accountId + "\""));
    }
}
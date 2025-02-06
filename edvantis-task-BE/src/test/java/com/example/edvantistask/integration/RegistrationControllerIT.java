package com.example.edvantistask.integration;


import com.example.edvantistask.dto.RegistrationRequest;
import com.example.edvantistask.model.UserAccount;
import com.example.edvantistask.repository.UserAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RegistrationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    public void setup() {
        userAccountRepository.deleteAll();
    }

    @Test
    public void testRegisterNewUser() throws Exception {
        RegistrationRequest request = new RegistrationRequest("testuser", "testpassword");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        UserAccount savedUser = userAccountRepository.findByUsername("testuser").orElse(null);
        assertNotNull(savedUser, "User should be created");
        assertNotEquals("testpassword", savedUser.getPassword(), "Password should be encoded");
        assertEquals("ROLE_USER", savedUser.getRole());
    }

    @Test
    public void testRegisterDuplicateUser() throws Exception {
        RegistrationRequest request = new RegistrationRequest("duplicateUser", "password123");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("User already exists")));
    }
}

package com.example.edvantistask.integration;

import com.example.edvantistask.command.EmergencyCallCreateCommand;
import com.example.edvantistask.command.EmergencyCallUpdateCommand;
import com.example.edvantistask.dto.EmergencyCallDTO;
import com.example.edvantistask.model.EmergencyCall;
import com.example.edvantistask.model.IncidentType;
import com.example.edvantistask.model.Status;
import com.example.edvantistask.repository.EmergencyCallRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"WRITE"})
class EmergencyCallControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmergencyCallRepository emergencyCallRepository;

    @Test
    void shouldCreateEmergencyCallUsingCreateCommand() throws Exception {
        // given
        var createCommand = new EmergencyCallCreateCommand(
                "John Doe",
                "123 Main St",
                IncidentType.FIRE,
                Status.OPEN
        );
        String createJson = objectMapper.writeValueAsString(createCommand);
        long countBefore = emergencyCallRepository.count();

        // when
        String createResponse = mockMvc.perform(post("/api/emergency-calls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.callerName", is("John Doe")))
                .andReturn().getResponse().getContentAsString();

        EmergencyCallDTO created = objectMapper.readValue(createResponse, EmergencyCallDTO.class);

        // then
        long countAfter = emergencyCallRepository.count();
        assertTrue(countAfter > countBefore);
        EmergencyCall entity = emergencyCallRepository.findById(created.id()).orElse(null);
        assertNotNull(entity);
        assertEquals("John Doe", entity.getCallerName());
    }

    @Test
    void shouldGetEmergencyCallAfterCreation() throws Exception {
        // given
        var createCommand = new EmergencyCallCreateCommand(
                "John Doe",
                "123 Main St",
                IncidentType.FIRE,
                Status.OPEN
        );
        String createJson = objectMapper.writeValueAsString(createCommand);
        String createResponse = mockMvc.perform(post("/api/emergency-calls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        EmergencyCallDTO created = objectMapper.readValue(createResponse, EmergencyCallDTO.class);
        Long id = created.id();

        // when & then
        mockMvc.perform(get("/api/emergency-calls/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.callerName", is("John Doe")));
    }

    @Test
    void shouldUpdateEmergencyCallUsingUpdateCommand() throws Exception {
        // given
        var createCommand = new EmergencyCallCreateCommand(
                "John Doe",
                "123 Main St",
                IncidentType.FIRE,
                Status.OPEN
        );
        String createJson = objectMapper.writeValueAsString(createCommand);
        String createResponse = mockMvc.perform(post("/api/emergency-calls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        EmergencyCallDTO created = objectMapper.readValue(createResponse, EmergencyCallDTO.class);
        Long id = created.id();

        // when
        var updateCommand = new EmergencyCallUpdateCommand(
                "Jane Doe",
                "456 Park Ave",
                IncidentType.ACCIDENT,
                Status.CLOSED
        );
        String updateJson = objectMapper.writeValueAsString(updateCommand);
        String updateResponse = mockMvc.perform(put("/api/emergency-calls/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.callerName", is("Jane Doe")))
                .andExpect(jsonPath("$.position", is("456 Park Ave")))
                .andExpect(jsonPath("$.incidentType", is("ACCIDENT")))
                .andExpect(jsonPath("$.status", is("CLOSED")))
                .andReturn().getResponse().getContentAsString();
        EmergencyCallDTO updated = objectMapper.readValue(updateResponse, EmergencyCallDTO.class);

        // then
        EmergencyCall entity = emergencyCallRepository.findById(updated.id()).orElse(null);
        assertNotNull(entity);
        assertEquals("Jane Doe", entity.getCallerName());
        assertEquals("456 Park Ave", entity.getPosition());
        assertEquals(IncidentType.ACCIDENT, entity.getIncidentType());
        assertEquals(Status.CLOSED, entity.getStatus());
    }

    @Test
    void shouldDeleteEmergencyCall() throws Exception {
        // given
        var createCommand = new EmergencyCallCreateCommand(
                "John Doe",
                "123 Main St",
                IncidentType.FIRE,
                Status.OPEN
        );
        String createJson = objectMapper.writeValueAsString(createCommand);
        String createResponse = mockMvc.perform(post("/api/emergency-calls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        EmergencyCallDTO created = objectMapper.readValue(createResponse, EmergencyCallDTO.class);
        Long id = created.id();

        // when
        mockMvc.perform(delete("/api/emergency-calls/{id}", id))
                .andExpect(status().isNoContent());

        // then
        assertFalse(emergencyCallRepository.findById(id).isPresent());
    }

    @Test
    void shouldReturnAllEmergencyCallsWhenFilteredByIncidentType() throws Exception {
        // given
        var call1 = new EmergencyCallCreateCommand("Alice", "Street A", IncidentType.CRIME, Status.OPEN);
        var call2 = new EmergencyCallCreateCommand("Bob", "Street B", IncidentType.CRIME, Status.OPEN);
        var call3 = new EmergencyCallCreateCommand("Charlie", "Street C", IncidentType.FIRE, Status.CLOSED);

        mockMvc.perform(post("/api/emergency-calls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(call1)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/emergency-calls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(call2)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/emergency-calls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(call3)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/emergency-calls")
                        .param("incidentType", "FIRE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.content[0].incidentType", is("FIRE")));
    }

    @Test
    void shouldReturnValidationErrorsWhenInputIsInvalid() throws Exception {
        // given
        var invalidCommand = new EmergencyCallCreateCommand("", null, null, null);
        String invalidJson = objectMapper.writeValueAsString(invalidCommand);

        // when/then
        mockMvc.perform(post("/api/emergency-calls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation error")))
                .andExpect(jsonPath("$.errors", not(empty())));
    }

    @Test
    void shouldReturnUnauthorizedWhenInvalidCredentialsProvided() throws Exception {
        // when/then
        mockMvc.perform(get("/api/emergency-calls")
                        .with(httpBasic("invalidUser", "invalidPassword")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "wrongRoleUser", roles = {"OTHER"})
    void shouldReturnForbiddenWhenUserHasInvalidRole() throws Exception {
        // when/then
        mockMvc.perform(get("/api/emergency-calls"))
                .andExpect(status().isForbidden());
    }
}

package com.example.edvantistask;

import com.example.edvantistask.command.EmergencyCallCreateCommand;
import com.example.edvantistask.command.EmergencyCallUpdateCommand;
import com.example.edvantistask.dto.EmergencyCallDTO;
import com.example.edvantistask.exception.ResourceNotFoundException;
import com.example.edvantistask.model.EmergencyCall;
import com.example.edvantistask.model.IncidentType;
import com.example.edvantistask.model.Status;
import com.example.edvantistask.repository.EmergencyCallRepository;
import com.example.edvantistask.service.EmergencyCallServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmergencyCallServiceTest {

    @Mock
    private EmergencyCallRepository repository;

    @InjectMocks
    private EmergencyCallServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateEmergencyCallUsingCreateCommandWhenValidDataProvided() {
        // given:
        EmergencyCallCreateCommand createCommand = new EmergencyCallCreateCommand(
                "New Caller",
                "New Address",
                IncidentType.FIRE,
                Status.OPEN
        );
        EmergencyCall createdEntity = createCommand.toEntity();
        createdEntity.setId(1L);
        when(repository.saveAndFlush(any(EmergencyCall.class))).thenReturn(createdEntity);

        // when:
        EmergencyCallDTO result = service.createEmergencyCall(createCommand);

        // then:
        assertNotNull(result.id());
        assertEquals("New Caller", result.callerName());
        verify(repository, times(1)).saveAndFlush(any(EmergencyCall.class));
    }

    @Test
    void shouldThrowResourceNotFoundWhenEmergencyCallDoesNotExist() {
        // given:
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when/then:
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.getEmergencyCall(1L));
        assertTrue(exception.getMessage().contains("Emergency Call not found with id: 1"));
    }

    @Test
    void shouldUpdateEmergencyCallUsingUpdateCommandWhenValidDataProvided() {
        // given:
        EmergencyCall existing = new EmergencyCall();
        existing.setId(1L);
        existing.setCallerName("Old Caller");
        existing.setPosition("Old Address");
        existing.setIncidentType(IncidentType.FIRE);
        existing.setStatus(Status.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        EmergencyCallUpdateCommand updateCommand = new EmergencyCallUpdateCommand(
                "Updated Caller",
                "Updated Address",
                IncidentType.ACCIDENT,
                Status.CLOSED
        );
        EmergencyCall updatedEntity = new EmergencyCall();
        updatedEntity.setId(1L);
        updatedEntity.setCallerName("Updated Caller");
        updatedEntity.setPosition("Updated Address");
        updatedEntity.setIncidentType(IncidentType.ACCIDENT);
        updatedEntity.setStatus(Status.CLOSED);
        when(repository.saveAndFlush(existing)).thenReturn(updatedEntity);

        // when:
        EmergencyCallDTO result = service.updateEmergencyCall(1L, updateCommand);

        // then:
        assertEquals("Updated Caller", result.callerName());
        assertEquals("Updated Address", result.position());
        assertEquals(IncidentType.ACCIDENT, result.incidentType());
        assertEquals(Status.CLOSED, result.status());
    }

    @Test
    void shouldDeleteEmergencyCallWhenIdExists() {
        // given:
        EmergencyCall existing = new EmergencyCall();
        existing.setId(1L);
        existing.setCallerName("Test Caller");
        existing.setPosition("Test Address");
        existing.setIncidentType(IncidentType.CRIME);
        existing.setStatus(Status.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(repository).delete(existing);

        // when:
        service.deleteEmergencyCall(1L);

        // then:
        verify(repository, times(1)).delete(existing);
    }

    @Test
    void shouldReturnAllEmergencyCallsWhenFilteredByIncidentType() {
        // given:
        EmergencyCall call1 = new EmergencyCall();
        call1.setId(1L);
        call1.setCallerName("A");
        call1.setPosition("Addr A");
        call1.setIncidentType(IncidentType.FIRE);
        call1.setStatus(Status.OPEN);

        EmergencyCall call2 = new EmergencyCall();
        call2.setId(2L);
        call2.setCallerName("B");
        call2.setPosition("Addr B");
        call2.setIncidentType(IncidentType.FIRE);
        call2.setStatus(Status.OPEN);

        List<EmergencyCall> list = List.of(call1, call2);
        Page<EmergencyCall> page = new PageImpl<>(list);
        when(repository.findByIncidentType(IncidentType.FIRE, PageRequest.of(0, 10)))
                .thenReturn(page);

        // when:
        Page<EmergencyCallDTO> result = service.getAllEmergencyCalls(IncidentType.FIRE, PageRequest.of(0, 10));

        // then:
        assertEquals(2, result.getTotalElements());
        verify(repository, times(1)).findByIncidentType(IncidentType.FIRE, PageRequest.of(0, 10));
    }
}

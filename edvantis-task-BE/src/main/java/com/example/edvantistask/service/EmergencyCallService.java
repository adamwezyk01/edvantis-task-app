package com.example.edvantistask.service;

import com.example.edvantistask.command.EmergencyCallCreateCommand;
import com.example.edvantistask.dto.EmergencyCallDTO;
import com.example.edvantistask.command.EmergencyCallUpdateCommand;
import com.example.edvantistask.model.IncidentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmergencyCallService {
    EmergencyCallDTO createEmergencyCall(EmergencyCallCreateCommand command);
    EmergencyCallDTO updateEmergencyCall(Long id, EmergencyCallUpdateCommand command);
    void deleteEmergencyCall(Long id);
    EmergencyCallDTO getEmergencyCall(Long id);
    Page<EmergencyCallDTO> getAllEmergencyCalls(IncidentType incidentType, Pageable pageable);
}

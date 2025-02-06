package com.example.edvantistask.service;

import com.example.edvantistask.command.EmergencyCallCreateCommand;
import com.example.edvantistask.command.EmergencyCallUpdateCommand;
import com.example.edvantistask.dto.EmergencyCallDTO;
import com.example.edvantistask.exception.ResourceNotFoundException;
import com.example.edvantistask.model.EmergencyCall;
import com.example.edvantistask.model.IncidentType;
import com.example.edvantistask.repository.EmergencyCallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmergencyCallServiceImpl implements EmergencyCallService {

    private final EmergencyCallRepository repository;

    @Override
    @Transactional
    public EmergencyCallDTO createEmergencyCall(EmergencyCallCreateCommand command) {
        EmergencyCall emergencyCall = command.toEntity();
        EmergencyCall savedCall = repository.saveAndFlush(emergencyCall);
        return EmergencyCallDTO.fromEntity(savedCall);
    }

    @Override
    @Transactional
    public EmergencyCallDTO updateEmergencyCall(Long id, EmergencyCallUpdateCommand command) {
        EmergencyCall existingCall = getEmergencyCallById(id);
        command.applyUpdate(existingCall);
        EmergencyCall updatedCall = repository.saveAndFlush(existingCall);
        return EmergencyCallDTO.fromEntity(updatedCall);
    }

    @Override
    @Transactional
    public void deleteEmergencyCall(Long id) {
        EmergencyCall existingCall = getEmergencyCallById(id);
        repository.delete(existingCall);
    }

    @Override
    @Transactional(readOnly = true)
    public EmergencyCallDTO getEmergencyCall(Long id) {
        EmergencyCall emergencyCall = getEmergencyCallById(id);
        return EmergencyCallDTO.fromEntity(emergencyCall);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmergencyCallDTO> getAllEmergencyCalls(IncidentType incidentType, Pageable pageable) {
        Page<EmergencyCall> page;
        if (incidentType != null) {
            page = repository.findByIncidentType(incidentType, pageable);
        } else {
            page = repository.findAll(pageable);
        }
        return page.map(EmergencyCallDTO::fromEntity);
    }

    private EmergencyCall getEmergencyCallById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Emergency Call not found with id: %d", id)));
    }
}

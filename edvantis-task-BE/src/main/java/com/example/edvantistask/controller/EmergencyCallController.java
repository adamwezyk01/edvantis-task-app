package com.example.edvantistask.controller;

import com.example.edvantistask.command.EmergencyCallCreateCommand;
import com.example.edvantistask.dto.EmergencyCallDTO;
import com.example.edvantistask.command.EmergencyCallUpdateCommand;
import com.example.edvantistask.model.IncidentType;
import com.example.edvantistask.service.EmergencyCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emergency-calls")
@RequiredArgsConstructor
public class EmergencyCallController {

    private final EmergencyCallService service;

    @PostMapping
    public ResponseEntity<EmergencyCallDTO> createEmergencyCall(@Validated @RequestBody EmergencyCallCreateCommand command) {
        EmergencyCallDTO created = service.createEmergencyCall(command);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyCallDTO> getEmergencyCall(@PathVariable Long id) {
        EmergencyCallDTO dto = service.getEmergencyCall(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmergencyCallDTO> updateEmergencyCall(@PathVariable Long id,
                                                                @Validated @RequestBody EmergencyCallUpdateCommand command) {
        EmergencyCallDTO updated = service.updateEmergencyCall(id, command);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergencyCall(@PathVariable Long id) {
        service.deleteEmergencyCall(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<EmergencyCallDTO>> getAllEmergencyCalls(
            @RequestParam(value = "incidentType", required = false) IncidentType incidentType,
            Pageable pageable) {
        Page<EmergencyCallDTO> page = service.getAllEmergencyCalls(incidentType, pageable);
        return ResponseEntity.ok(page);
    }
}

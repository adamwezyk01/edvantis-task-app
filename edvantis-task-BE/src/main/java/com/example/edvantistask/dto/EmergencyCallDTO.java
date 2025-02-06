package com.example.edvantistask.dto;

import com.example.edvantistask.model.EmergencyCall;
import com.example.edvantistask.model.IncidentType;
import com.example.edvantistask.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmergencyCallDTO(
        Long id,
        @NotBlank(message = "Caller name is required") String callerName,
        @NotBlank(message = "Position is required") String position,
        @NotNull(message = "Incident type is required") IncidentType incidentType,
        @NotBlank(message = "Status is required") Status status
) {
    public static EmergencyCallDTO fromEntity(EmergencyCall call) {
        return new EmergencyCallDTO(
                call.getId(),
                call.getCallerName(),
                call.getPosition(),
                call.getIncidentType(),
                call.getStatus()
        );
    }
}

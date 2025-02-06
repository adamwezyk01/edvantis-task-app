package com.example.edvantistask.command;

import com.example.edvantistask.model.EmergencyCall;
import com.example.edvantistask.model.IncidentType;
import com.example.edvantistask.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmergencyCallCreateCommand(
        @NotBlank(message = "Caller name is required") String callerName,
        @NotBlank(message = "Position is required") String position,
        @NotNull(message = "Incident type is required") IncidentType incidentType,
        @NotNull(message = "Status is required") Status status
) {
    public EmergencyCall toEntity() {
        EmergencyCall call = new EmergencyCall();
        call.setCallerName(this.callerName());
        call.setPosition(this.position());
        call.setIncidentType(this.incidentType());
        call.setStatus(this.status());
        return call;
    }
}

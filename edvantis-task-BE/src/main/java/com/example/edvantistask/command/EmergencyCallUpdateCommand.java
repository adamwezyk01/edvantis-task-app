package com.example.edvantistask.command;

import com.example.edvantistask.model.EmergencyCall;
import com.example.edvantistask.model.IncidentType;
import com.example.edvantistask.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmergencyCallUpdateCommand(
    @NotBlank(message = "Caller name is required") String callerName,
    @NotBlank(message = "Position is required") String position,
    @NotNull(message = "Incident type is required") IncidentType incidentType,
    @NotNull(message = "Status is required") Status status
) {

    public EmergencyCall applyUpdate(EmergencyCall existingCall) {
        existingCall.setCallerName(this.callerName());
        existingCall.setPosition(this.position());
        existingCall.setIncidentType(this.incidentType());
        existingCall.setStatus(this.status());
        return existingCall;
    }
}

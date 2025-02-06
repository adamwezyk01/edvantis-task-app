package com.example.edvantistask.repository;

import com.example.edvantistask.model.EmergencyCall;
import com.example.edvantistask.model.IncidentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyCallRepository extends JpaRepository<EmergencyCall, Long> {
    Page<EmergencyCall> findByIncidentType(IncidentType incidentType, Pageable pageable);
}

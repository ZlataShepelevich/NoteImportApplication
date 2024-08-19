package com.app.noteimportapplication.repositories;

import com.app.noteimportapplication.entities.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
    Optional<PatientProfile> findByOldClientGuidContainingAndStatusIdIn(String oldClientGuid, List<Short> statusIds);
}
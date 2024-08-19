package com.app.noteimportapplication.repositories;

import com.app.noteimportapplication.entities.PatientNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientNoteRepository extends JpaRepository<PatientNote, Long> {
    Optional<PatientNote> findByGuid(String guid);
}


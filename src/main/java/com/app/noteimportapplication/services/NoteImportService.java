package com.app.noteimportapplication.services;

import com.app.noteimportapplication.entities.CompanyUser;
import com.app.noteimportapplication.entities.PatientNote;
import com.app.noteimportapplication.entities.PatientProfile;
import com.app.noteimportapplication.models.Client;
import com.app.noteimportapplication.models.Note;
import com.app.noteimportapplication.repositories.CompanyUserRepository;
import com.app.noteimportapplication.repositories.PatientNoteRepository;
import com.app.noteimportapplication.repositories.PatientProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class NoteImportService {

    private final CompanyUserRepository companyUserRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final PatientNoteRepository patientNoteRepository;
    private static final Logger logger = LoggerFactory.getLogger(NoteImportService.class);

    @Value("${old.system.url}")
    private String oldSystemUrl;

    @Value("${note.import.dateFrom}")
    private String dateFrom;

    @Value("${note.import.dateTo}")
    private String dateTo;

    private final RestTemplate restTemplate;

    @Autowired
    public NoteImportService(CompanyUserRepository companyUserRepository,
                             PatientProfileRepository patientProfileRepository,
                             PatientNoteRepository patientNoteRepository,
                             RestTemplate restTemplate) {
        this.companyUserRepository = companyUserRepository;
        this.patientProfileRepository = patientProfileRepository;
        this.patientNoteRepository = patientNoteRepository;
        this.restTemplate = restTemplate;
    }

    // Method to fetch clients from old system
    private List<Client> fetchClientsFromOldSystem() {
        try {
            String url = oldSystemUrl + "/clients";
            ResponseEntity<Client[]> response = restTemplate.postForEntity(url, null, Client[].class);
            Client[] clients = response.getBody();
            return clients != null ? Arrays.asList(clients) : List.of();
        } catch (Exception e) {
            logger.error("Error fetching clients from old system", e);
            return List.of();
        }
    }

    // Method to fetch notes for a specific client
    private List<Note> fetchNotesForClient(Client client) {
        try {
            String url = oldSystemUrl + "/notes";

            NotesRequestPayload payload = new NotesRequestPayload();
            payload.setAgency(client.getAgency());
            payload.setClientGuid(client.getGuid());
            payload.setDateFrom(dateFrom);
            payload.setDateTo(dateTo);

            ResponseEntity<Note[]> response = restTemplate.postForEntity(url, payload, Note[].class);
            Note[] notes = response.getBody();
            return notes != null ? Arrays.asList(notes) : List.of();
        } catch (Exception e) {
            logger.error("Error fetching notes for client: " + client.getGuid(), e);
            return List.of();
        }
    }

    // Inner class for the request payload
    private static class NotesRequestPayload {
        private String agency;
        private String clientGuid;
        private String dateFrom;
        private String dateTo;

        public String getAgency() {
            return agency;
        }

        public void setAgency(String agency) {
            this.agency = agency;
        }

        public String getClientGuid() {
            return clientGuid;
        }

        public void setClientGuid(String clientGuid) {
            this.clientGuid = clientGuid;
        }

        public String getDateFrom() {
            return dateFrom;
        }

        public void setDateFrom(String dateFrom) {
            this.dateFrom = dateFrom;
        }

        public String getDateTo() {
            return dateTo;
        }

        public void setDateTo(String dateTo) {
            this.dateTo = dateTo;
        }
    }

    // Main import logic
    @Transactional
    public void importNotes() {
        try {
            List<Client> clients = fetchClientsFromOldSystem();

            for (Client client : clients) {
                // Find matching patient in the new system
                Optional<PatientProfile> patientOpt = patientProfileRepository.findByOldClientGuidContainingAndStatusIdIn(
                        client.getGuid(), Arrays.asList((short) 200, (short) 210, (short) 230));

                if (patientOpt.isPresent()) {
                    List<Note> notes = fetchNotesForClient(client);

                    for (Note note : notes) {
                        Optional<PatientNote> existingNoteOpt = patientNoteRepository.findByGuid(note.getGuid());

                        if (existingNoteOpt.isPresent()) {
                            PatientNote existingNote = existingNoteOpt.get();

                            // Update logic based on last modification date
                            if (note.getModifiedDateTime().isAfter(existingNote.getLastModifiedDateTime())) {
                                updateNoteDetails(existingNote, note);
                                patientNoteRepository.save(existingNote);
                            }
                        } else {
                            System.out.println(note);
                            createNewNoteForPatient(patientOpt.get(), note);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred during note import: ", e);
        }
    }

    private void createNewNoteForPatient(PatientProfile patient, Note note) {
        PatientNote patientNote = new PatientNote();
        patientNote.setPatient(patient);

        if (note.getDatetime() != null) {
            patientNote.setCreatedDateTime(note.getDatetime());
        } else {
            throw new IllegalArgumentException("Note's createdDateTime cannot be null");
        }

        if (note.getModifiedDateTime() != null) {
            patientNote.setLastModifiedDateTime(note.getModifiedDateTime());
        } else {
            throw new IllegalArgumentException("Note's modifiedDateTime cannot be null");
        }

        patientNote.setNote(note.getComments());
        patientNote.setGuid(note.getGuid());
        // Set user who created and last modified the note
        CompanyUser user = companyUserRepository.findByLogin(note.getLoggedUser())
                .orElseGet(() -> createUser(note.getLoggedUser()));
        patientNote.setCreatedByUser(user);
        patientNote.setLastModifiedByUser(user);
        patientNoteRepository.save(patientNote);
    }
    private CompanyUser createUser(String login) {
        CompanyUser user = new CompanyUser();
        user.setLogin(login);
        return companyUserRepository.save(user);
    }

    private void updateNoteDetails(PatientNote existingNote, Note note) {
        existingNote.setLastModifiedDateTime(note.getModifiedDateTime());
        existingNote.setNote(note.getComments());
        CompanyUser lastModifiedByUser = companyUserRepository.findByLogin(note.getLoggedUser())
                .orElseGet(() -> createUser(note.getLoggedUser()));
        existingNote.setLastModifiedByUser(lastModifiedByUser);
    }
}


package com.app.noteimportapplication.services;

import com.app.noteimportapplication.entities.PatientNote;
import com.app.noteimportapplication.models.Client;
import com.app.noteimportapplication.repositories.PatientNoteRepository;
import com.app.noteimportapplication.repositories.PatientProfileRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NoteImportServiceTest {

    @Mock
    private PatientProfileRepository patientProfileRepository;

    @Mock
    private PatientNoteRepository patientNoteRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NoteImportService noteImportService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testImportNotes_NoClients() {
        lenient().when(restTemplate.postForEntity(anyString(), any(), eq(Client[].class)))
                .thenReturn(ResponseEntity.ok(new Client[]{}));

        noteImportService.importNotes();

        verify(patientProfileRepository, never()).findByOldClientGuidContainingAndStatusIdIn(anyString(), anyList());
        verify(patientNoteRepository, never()).save(any(PatientNote.class));
    }

    @Test
    public void testFetchClientsFromOldSystem_Exception() {
        lenient().when(restTemplate.postForEntity(anyString(), any(), eq(Client[].class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        noteImportService.importNotes();

        verify(patientProfileRepository, never()).findByOldClientGuidContainingAndStatusIdIn(anyString(), anyList());
    }
}

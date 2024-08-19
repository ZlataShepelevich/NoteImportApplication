package com.app.noteimportapplication;

import com.app.noteimportapplication.services.NoteImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class NoteImportApplication {
    final NoteImportService noteImportService;

    @Autowired
    public NoteImportApplication(NoteImportService noteImportService) {
        this.noteImportService = noteImportService;
    }

    public static void main(String[] args) {
        SpringApplication.run(NoteImportApplication.class, args);
    }

    @Scheduled(cron = "0 15 */2 * * *")
    public void runImportTask() {
        noteImportService.importNotes();
    }
}

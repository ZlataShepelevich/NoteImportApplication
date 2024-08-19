package com.app.noteimportapplication.repositories;

import com.app.noteimportapplication.entities.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {
    Optional<CompanyUser> findByLogin(String login);
}

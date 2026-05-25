package com.example.itsupport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration globale de l'application.
 * Contient les déclarations de beans généraux comme le PasswordEncoder.
 */
@Configuration
public class AppConfig {

    // Déclare le bean PasswordEncoder pour crypter les mots de passe des
    // utilisateurs.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

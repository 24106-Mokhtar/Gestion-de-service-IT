package com.example.itsupport.dto;

import com.example.itsupport.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO représentant les données d'un Ticket renvoyées à l'utilisateur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private String creatorUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

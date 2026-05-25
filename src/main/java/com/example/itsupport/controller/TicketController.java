package com.example.itsupport.controller;

import com.example.itsupport.dto.TicketDTO;
import com.example.itsupport.entity.Status;
import com.example.itsupport.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Contrôleur REST gérant le cycle de vie des tickets de support.
 */
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(
            @RequestBody TicketDTO ticketDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        TicketDTO createdTicket = ticketService.createTicket(
                ticketDTO.getTitle(),
                ticketDTO.getDescription(),
                userDetails.getUsername());
        return ResponseEntity.ok(createdTicket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        TicketDTO ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getTickets(@AuthenticationPrincipal UserDetails userDetails) {
        List<TicketDTO> tickets = ticketService.getTicketsForUser(userDetails.getUsername());
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketDTO> updateTicketStatus(
            @PathVariable Long id,
            @RequestParam Status status) {

        TicketDTO updatedTicket = ticketService.updateTicketStatus(id, status);
        return ResponseEntity.ok(updatedTicket);
    }
}

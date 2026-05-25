package com.example.itsupport.service;

import com.example.itsupport.dto.TicketDTO;
import com.example.itsupport.entity.Role;
import com.example.itsupport.entity.Status;
import com.example.itsupport.entity.Ticket;
import com.example.itsupport.entity.User;
import com.example.itsupport.exception.ResourceNotFoundException;
import com.example.itsupport.repository.TicketRepository;
import com.example.itsupport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service gérant la logique métier des tickets de support.
 */
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Transactional
    public TicketDTO createTicket(String title, String description, String username) {
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec le nom : " + username));

        Ticket ticket = Ticket.builder()
                .title(title)
                .description(description)
                .status(Status.OPEN)
                .creator(creator)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDTO(savedTicket);
    }

    public TicketDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket non trouvé avec id: " + id));
        return convertToDTO(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getTicketsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec le nom : " + username));

        List<Ticket> tickets;
        if (user.getRole() == Role.ADMIN) {
            tickets = ticketRepository.findAll();
        } else {
            tickets = ticketRepository.findByCreator(user);
        }

        return tickets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketDTO updateTicketStatus(Long ticketId, Status newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket non trouvé avec l'id : " + ticketId));

        ticket.setStatus(newStatus);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return convertToDTO(updatedTicket);
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        return TicketDTO.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .creatorUsername(ticket.getCreator().getUsername())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}

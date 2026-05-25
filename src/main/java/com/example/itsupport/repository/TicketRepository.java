package com.example.itsupport.repository;

import com.example.itsupport.entity.Ticket;
import com.example.itsupport.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour gérer l'entité Ticket en base de données.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreator(User creator);
    List<Ticket> findByCreatorUsername(String username);
}

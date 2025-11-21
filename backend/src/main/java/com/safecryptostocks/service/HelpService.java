package com.safecryptostocks.service;

import com.safecryptostocks.model.Help;
import com.safecryptostocks.repository.HelpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HelpService {

    @Autowired
    private HelpRepository helpRepository;

    // ---------- Create / Save a help ticket ----------
    public Help createTicket(Help help) {
        help.setCreatedAt(LocalDateTime.now()); // ✅ use camelCase
        help.setStatus("Open");
        return helpRepository.save(help);
    }

    // ---------- Get ticket by ID ----------
    public Help getTicketById(Long id) {
        return helpRepository.findById(id).orElse(null);
    }

    // ---------- Update ticket (status, response) ----------
    public Help updateTicket(Help help) {
        help.setUpdatedAt(LocalDateTime.now()); // ✅ use camelCase
        return helpRepository.save(help);
    }

    // ---------- Get all tickets for a specific user ----------
    public List<Help> getTicketsByUserId(Long userId) {
        return helpRepository.findByUserId(userId);
    }

    // ---------- Get all tickets (for admin view, optional) ----------
    public List<Help> getAllTickets() {
        return helpRepository.findAll();
    }
}

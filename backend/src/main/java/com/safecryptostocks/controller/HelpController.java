package com.safecryptostocks.controller;

import com.safecryptostocks.model.Help;
import com.safecryptostocks.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/help")
@CrossOrigin(origins = "http://localhost:3000") // Allow React frontend
public class HelpController {

    @Autowired
    private HelpService helpService;

    // ---------- POST API: /api/help/create ----------
    @PostMapping("/create")
    public ResponseEntity<?> createHelpTicket(@RequestBody Help help) {
        try {
            // Set default fields
            help.setCreatedAt(LocalDateTime.now());
            help.setStatus("Open");

            Help savedTicket = helpService.createTicket(help);
            return ResponseEntity.ok(savedTicket);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to submit ticket: " + e.getMessage());
        }
    }

    // ---------- GET API: /api/help/user/{userId} ----------
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Help>> getUserTickets(@PathVariable Long userId) {
        List<Help> tickets = helpService.getTicketsByUserId(userId);
        return ResponseEntity.ok(tickets);
    }

    // ---------- Optional: GET ticket by ID ----------
    @GetMapping("/{id}")
    public ResponseEntity<Help> getTicketById(@PathVariable Long id) {
        Help ticket = helpService.getTicketById(id);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------- Optional: Update ticket (status, response) ----------
    @PutMapping("/update")
    public ResponseEntity<Help> updateTicket(@RequestBody Help help) {
        help.setUpdatedAt(LocalDateTime.now());
        Help updatedTicket = helpService.updateTicket(help);
        return ResponseEntity.ok(updatedTicket);
    }
}

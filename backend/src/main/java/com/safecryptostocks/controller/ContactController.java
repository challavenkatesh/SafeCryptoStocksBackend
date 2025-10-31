package com.safecryptostocks.controller;

import com.safecryptostocks.model.Contact;
import com.safecryptostocks.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")

@CrossOrigin(origins = "http://localhost:3000")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Create a new contact
    @PostMapping("/create")
    public ResponseEntity<String> createContact(@RequestBody Contact contact) {
        try {
            contactService.saveContact(contact);
            return ResponseEntity.ok("Contact saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving contact: " + e.getMessage());
        }
    }

    // Optional: Get all contacts
    @GetMapping("/all")
    public ResponseEntity<?> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }
}

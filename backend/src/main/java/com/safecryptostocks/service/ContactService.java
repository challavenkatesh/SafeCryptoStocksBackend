package com.safecryptostocks.service;

import com.safecryptostocks.model.Contact;
import com.safecryptostocks.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    // ---------------- Save a new contact ----------------
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    // ---------------- Get all contacts ----------------
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    // ---------------- Get a contact by ID (optional) ----------------
    public Contact getContactById(Long id) {
        Optional<Contact> contact = contactRepository.findById(id);
        return contact.orElse(null);
    }

    // ---------------- Delete a contact (optional) ----------------
    public boolean deleteContact(Long id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

package com.example.demo.service;

import com.example.demo.models.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(String id) {
        return clientRepository.findById(id);
    }

    public Client addClient(Client client) {
        client.setId(null);
        return clientRepository.save(client);
    }

    public Optional<Client> updateClient(String id, Client updated) {
        return clientRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setEmail(updated.getEmail());
            existing.setPhone(updated.getPhone());
            existing.setAddress(updated.getAddress());
            return clientRepository.save(existing);
        });
    }

    public boolean deleteClient(String id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
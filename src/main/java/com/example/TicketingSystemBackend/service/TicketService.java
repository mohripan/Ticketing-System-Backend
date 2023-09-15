package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.Ticket;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByUser(user);
    }

    public List<Ticket> getTicketsByAssignedTo(Integer assignedTo) {
        return ticketRepository.findByAssignedTo(assignedTo);
    }

    public List<Ticket> getTicketsByDepartment(Department department) {
        return ticketRepository.findByTicketTagDepartment(department);
    }
}

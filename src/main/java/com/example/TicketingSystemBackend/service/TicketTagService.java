package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.dto.TicketTagDTO;
import com.example.TicketingSystemBackend.dto.TicketTagShowDTO;
import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.TicketTag;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.DepartmentRepository;
import com.example.TicketingSystemBackend.repository.TicketTagRepository;
import com.example.TicketingSystemBackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketTagService {
    @Autowired
    private TicketTagRepository ticketTagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<TicketTagShowDTO> getAllTicketTags(Optional<Integer> departmentId) {
        List<TicketTag> ticketTags;

        if(departmentId.isPresent()) {
            ticketTags = ticketTagRepository.findByDepartment_DepartmentID(departmentId.get());
        } else {
            ticketTags = ticketTagRepository.findAll();
        }

        return ticketTags.stream()
                .map(TicketTag::showToDTO)
                .collect(Collectors.toList());
    }

    public TicketTag createTicketTag(TicketTagDTO ticketTagDTO, User user) {
        TicketTag ticketTag = new TicketTag();

        ticketTag.setTagName(ticketTagDTO.getTagName());
        ticketTag.setDescription(ticketTagDTO.getDescription());

        Department department = departmentRepository.findById(ticketTagDTO.getDepartmentID()).orElseThrow(() -> new RuntimeException("Department not found"));
        ticketTag.setDepartment(department);

        ticketTag.setUser(user);

        ticketTag.setCreatedDate(LocalDateTime.now());

        return ticketTagRepository.save(ticketTag);
    }

    public TicketTag editTicketTag(Integer ticketTagID, TicketTagDTO ticketTagDTO, User authenticatedUser) {
        TicketTag existingTicketTag = ticketTagRepository.findById(ticketTagID)
                .orElseThrow(() -> new EntityNotFoundException("Ticket tag not found"));

        existingTicketTag.setTagName(ticketTagDTO.getTagName());
        existingTicketTag.setDescription(ticketTagDTO.getDescription());

        Department department = departmentRepository.findById(ticketTagDTO.getDepartmentID())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        existingTicketTag.setDepartment(department);

        existingTicketTag.setUser(authenticatedUser);

        return ticketTagRepository.save(existingTicketTag);
    }

    public void deleteTicketTag(Integer tagID) {
        ticketTagRepository.deleteById(tagID);
    }
}

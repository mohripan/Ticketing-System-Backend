package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.dto.DepartmentListDTO;
import com.example.TicketingSystemBackend.dto.DepartmentResponseDTO;
import com.example.TicketingSystemBackend.dto.DepartmentSingleViewDTO;
import com.example.TicketingSystemBackend.dto.UserListResponseDTO;
import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.DepartmentRepository;
import com.example.TicketingSystemBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<DepartmentResponseDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream().map(department -> {
            List<UserListResponseDTO> userDTOs = department.getUsers().stream().map(user -> new UserListResponseDTO(
                    user.getUserID(),
                    user.getEmail(),
                    user.getUserName(),
                    user.getName()
            )).collect(Collectors.toList());

            return new DepartmentResponseDTO(
                    department.getDepartmentID(),
                    department.getDepartmentName(),
                    userDTOs
            );
        }).collect(Collectors.toList());
    }

    public Optional<DepartmentSingleViewDTO> getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .map(department -> new DepartmentSingleViewDTO(department.getDepartmentID(), department.getDepartmentName()));
    }

    public DepartmentListDTO getUsersByDepartmentAndRoleId(Integer departmentId) {
        List<User> users = userRepository.findByDepartmentDepartmentIDAndRoleRoleId(departmentId, 2);

        List<UserListResponseDTO> userDTOs = users.stream().map(user -> new UserListResponseDTO(
                user.getUserID(),
                user.getEmail(),
                user.getUserName(),
                user.getName()
        )).collect(Collectors.toList());

        return new DepartmentListDTO(userDTOs);
    }

    public Department updateDepartment(Integer id, Department department) {
        if(departmentRepository.existsById(id)) {
            department.setDepartmentID(id);
            return departmentRepository.save(department);
        }

        return null;
    }

    public void deleteDepartment(Integer id) {
        departmentRepository.deleteById(id);
    }
}

package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.Permission;
import com.example.TicketingSystemBackend.model.Role;
import com.example.TicketingSystemBackend.repository.PermissionRepository;
import com.example.TicketingSystemBackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Integer id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(Integer id) {
        roleRepository.deleteById(id);
    }

    public Role associateRoleWithPermissions(Integer roleId, List<Integer> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if(permissions.size() != permissionIds.size()) {
            throw new RuntimeException("Some permissions were not found");
        }

        role.setPermissions(permissions);

        return roleRepository.save(role);
    }
}

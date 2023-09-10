package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.Permission;
import com.example.TicketingSystemBackend.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission getPermissionById(Integer id) {
        return permissionRepository.findById(id).orElse(null);
    }

    public List<Permission> getPermissionByIds(List<Integer> ids) {
        return permissionRepository.findAllById(ids);
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Permission permission) {
        if(!permissionRepository.existsById(permission.getPermissionId())) {
            throw new RuntimeException("Permission not found");
        }
        return permissionRepository.save(permission);
    }

    public void deletePermission(Integer id) {
        if(!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permission not found");
        }
        permissionRepository.deleteById(id);
    }
}

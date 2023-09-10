package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.model.Permission;
import com.example.TicketingSystemBackend.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    public Permission getPermissionById(@PathVariable Integer id) {
        Permission permission = permissionService.getPermissionById(id);
        if(permission == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found");
        }
        return permission;
    }

    @PostMapping
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionService.createPermission(permission);
    }

    @PutMapping("/{id}")
    public Permission updatePermission(@PathVariable Integer id, @RequestBody Permission permission) {
        permission.setPermissionId(id);
        return permissionService.updatePermission(permission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Integer id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}

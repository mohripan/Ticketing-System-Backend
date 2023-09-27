package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.model.Permission;
import com.example.TicketingSystemBackend.model.Role;
import com.example.TicketingSystemBackend.service.PermissionService;
import com.example.TicketingSystemBackend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PreAuthorize("hasAuthority('VIEW_SINGLE_USER')")
    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }

    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    @PostMapping("/{id}")
    public Role updateRole(@PathVariable Integer id, @RequestBody Role role) {
        role.setRoleId(id);
        return roleService.updateRole(role);
    }

    @PreAuthorize("hasAuthority('ASSOCIATE_ROLE')")
    @PostMapping("/{roleId}/permissions")
    public Role associateWithPermissions(@PathVariable Integer roleId, @RequestBody List<Integer> permissionIds) {
        return roleService.associateRoleWithPermissions(roleId, permissionIds);
    }

    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
    }
}

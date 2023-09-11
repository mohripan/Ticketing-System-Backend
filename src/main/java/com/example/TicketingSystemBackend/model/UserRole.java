package com.example.TicketingSystemBackend.model;

public enum UserRole {
    MANAGER(1, "ROLE_MANAGER");

    private final int roleId;
    private final String roleStr;

    UserRole(int roleId, String roleStr) {
        this.roleId = roleId;
        this.roleStr = roleStr;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleStr() {
        return roleStr;
    }

    public static UserRole fromRoleId(int roleId) {
        for (UserRole role : values()) {
            if (role.getRoleId() == roleId) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role found for ID: " + roleId);
    }
}

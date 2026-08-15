package com.spms.userservice.dto;

import com.spms.userservice.entity.Role;
import com.spms.userservice.entity.User;
import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private LocalDateTime createdAt;
    private boolean active;

    public static UserResponse from(User u) {
        UserResponse r = new UserResponse();
        r.id = u.getId();
        r.name = u.getName();
        r.email = u.getEmail();
        r.phone = u.getPhone();
        r.role = u.getRole();
        r.createdAt = u.getCreatedAt();
        r.active = u.isActive();
        return r;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Role getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return active; }
}

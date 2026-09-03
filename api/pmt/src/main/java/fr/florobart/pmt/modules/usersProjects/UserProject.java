package fr.florobart.pmt.modules.usersProjects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_projects")
public class UserProject {

    @EmbeddedId
    private UserProjectId id = new UserProjectId();

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public UserProject() {
    }

    public UserProject(UserProjectId id, Role role) {
        this.id = id;
        this.role = role;
    }

    public UserProjectId getId() {
        return id;
    }

    public void setId(UserProjectId id) {
        this.id = id;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
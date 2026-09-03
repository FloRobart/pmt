package fr.florobart.pmt.modules.usersProjects;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, UserProjectId> {

    // Navigue vers id.userId
    List<UserProject> findByIdUserId(Long userId);

    // Navigue vers id.projectId
    List<UserProject> findByIdProjectId(Long projectId);
}

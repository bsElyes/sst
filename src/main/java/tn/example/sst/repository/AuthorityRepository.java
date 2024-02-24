package tn.example.sst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.example.sst.domain.Role;

/**
 * Spring Data JPA repository for the {@link Role} entity.
 */
public interface AuthorityRepository extends JpaRepository<Role, String> {}

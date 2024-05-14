package com.personal.jobportal.jobportalproject.repository;
import com.personal.jobportal.jobportalproject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UserRepository extends JpaRepository<Users, Integer> {
    public Users findByUsername(String username);
    Optional<Users> findByEmail(String email);
}

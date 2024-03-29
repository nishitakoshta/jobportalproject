package com.personal.jobportal.jobportalproject.repository;
import com.personal.jobportal.jobportalproject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<Users, Integer> {
    public Users findByUsername(String username);
}

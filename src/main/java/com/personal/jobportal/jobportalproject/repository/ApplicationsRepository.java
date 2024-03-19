package com.personal.jobportal.jobportalproject.repository;
import com.personal.jobportal.jobportalproject.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ApplicationsRepository extends JpaRepository<Applications, Integer> {
}

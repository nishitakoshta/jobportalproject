package com.personal.jobportal.jobportalproject.repository;
import com.personal.jobportal.jobportalproject.entity.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface JobRepository extends JpaRepository<Jobs, Integer> {
    @Query(value = "SELECT * FROM tfidf_scores where term = ? and field_type = ? and job_id = ?", nativeQuery = true)
    List<Object[]> checkTermExistence(String term, String fieldType, Integer jobId);
}

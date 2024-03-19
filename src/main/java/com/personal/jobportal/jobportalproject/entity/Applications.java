package com.personal.jobportal.jobportalproject.entity;
import com.personal.jobportal.jobportalproject.dto.CoverLetterAnswerDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
@Table(name = "job_applications")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Applications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private int applicationId;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Jobs job;
    @ManyToOne
    @JoinColumn(name = "job_seeker_id")
    private Users jobSeeker;
    @ElementCollection
    @Column(name = "cover_letter")
    private List<CoverLetterAnswerDTO> coverLetter;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'Pending'")
    private int status;
    @Column(name = "created_on", columnDefinition = "TIMESTAMP DEFAULT '2024-03-04 00:00:01'")
    @CreationTimestamp
    private LocalDateTime appliedOn;
}

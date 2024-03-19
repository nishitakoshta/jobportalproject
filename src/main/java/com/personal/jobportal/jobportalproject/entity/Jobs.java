package com.personal.jobportal.jobportalproject.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
@Table(name = "job_list")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private int jobId;
    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Users employer;
    @Column(name = "job_title", nullable = false,length = 500)
    private String title;
    @Column(name = "job_description", nullable = false, length = 5000)
    private String description;
    @Column(name = "requirements", nullable = false, length = 5000)
    private List<String> requirements;
    @Column(name = "job_location", nullable = false, length = 500)
    private String location;
    @Column(name = "category", nullable = false, length = 500)
    private String category;
    @Column(name = "created_on", columnDefinition = "TIMESTAMP DEFAULT '2024-03-04 00:00:01'")
    @CreationTimestamp
    private LocalDateTime createdOn;
}

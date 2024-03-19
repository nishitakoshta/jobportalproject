package com.personal.jobportal.jobportalproject.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(name = "user_name", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "role", nullable = false)
    private Integer role;
    @Column(name = "created_on", columnDefinition = "TIMESTAMP DEFAULT '2024-01-22 00:00:01'")
    @CreationTimestamp
    private LocalDateTime createdOn;
    @Column(name = "updated_on", columnDefinition = "TIMESTAMP DEFAULT '2024-01-22 00:00:01'")
    @UpdateTimestamp
    private LocalDateTime updatedOn;
}

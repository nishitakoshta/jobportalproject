package com.personal.jobportal.jobportalproject.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "question_master")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_Id")
    private int questionId;
    @Column(name = "question")
    private String question;
}

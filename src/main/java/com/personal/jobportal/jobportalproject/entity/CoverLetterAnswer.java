package com.personal.jobportal.jobportalproject.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "cover_letter_answer")
@AllArgsConstructor
@NoArgsConstructor
public class CoverLetterAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cover_letter_id")
    private int coverLetterId;
    @OneToOne
    @JoinColumn(name = "question_id")
    private QuestionMaster questionId;
    @Column(name = "answer", length = 2500)
    private String answer;
}

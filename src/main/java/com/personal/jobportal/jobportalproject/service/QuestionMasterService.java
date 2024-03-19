package com.personal.jobportal.jobportalproject.service;
import com.personal.jobportal.jobportalproject.entity.QuestionMaster;

import java.util.List;
public interface QuestionMasterService {
    QuestionMaster addQuestion(QuestionMaster question);
    QuestionMaster updateQuestion(int questionId, QuestionMaster question);
    void deleteQuestion(int questionId);
    QuestionMaster getQuestionById(int questionId);
    List<QuestionMaster> getAllQuestion();
}

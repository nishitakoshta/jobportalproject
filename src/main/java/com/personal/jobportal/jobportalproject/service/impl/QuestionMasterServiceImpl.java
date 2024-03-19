package com.personal.jobportal.jobportalproject.service.impl;
import com.personal.jobportal.jobportalproject.entity.QuestionMaster;
import com.personal.jobportal.jobportalproject.repository.QuestionMasterRepository;
import com.personal.jobportal.jobportalproject.service.QuestionMasterService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class QuestionMasterServiceImpl implements QuestionMasterService {
    @Autowired
    private QuestionMasterRepository questionMasterRepository;
    @Override
    public QuestionMaster addQuestion(QuestionMaster question){
        QuestionMaster addQuestion = new QuestionMaster();
        addQuestion.setQuestion(question.getQuestion());
       return questionMasterRepository.save(addQuestion);
    }
    @Override
    public QuestionMaster updateQuestion(int questionId, QuestionMaster question){
        QuestionMaster existingQuestion = questionMasterRepository.findById(questionId)
                .orElseThrow(EntityNotFoundException ::new);
        existingQuestion.setQuestion(question.getQuestion());
        return questionMasterRepository.save(existingQuestion);
    }
    @Override
    public void deleteQuestion(int questionId){
        questionMasterRepository.findById(questionId)
                        .orElseThrow(EntityNotFoundException::new);
        questionMasterRepository.deleteById(questionId);
    }
    @Override
    public QuestionMaster getQuestionById(int questionId){
        QuestionMaster question = questionMasterRepository.findById(questionId)
                .orElseThrow(EntityNotFoundException::new);
        return question;
    }
    @Override
    public List<QuestionMaster> getAllQuestion(){
        return questionMasterRepository.findAll();
    }
}

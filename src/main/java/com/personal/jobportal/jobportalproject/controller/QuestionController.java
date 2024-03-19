package com.personal.jobportal.jobportalproject.controller;
import com.personal.jobportal.jobportalproject.entity.QuestionMaster;
import com.personal.jobportal.jobportalproject.service.QuestionMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("api/v1/questions")
public class QuestionController {
    @Autowired
    private QuestionMasterService questionMasterService;
    @PostMapping
    public QuestionMaster addQuestion(QuestionMaster questionMaster){
        try {
            return questionMasterService.addQuestion(questionMaster);
        }catch (Exception e){
            throw e;
        }
    }
    @PutMapping("/{questionId}")
    public QuestionMaster updateQuestion(@RequestParam int questionId, @RequestBody QuestionMaster questionMaster){
        try {
            return questionMasterService.updateQuestion(questionId, questionMaster);
        }catch (Exception e){
            throw e;
        }
    }
    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(@RequestParam int questionId){
        try {
            questionMasterService.deleteQuestion(questionId);
            Map<String, Object> message = new HashMap<>();
            message.put("Message", "Question deleted successfully!!");
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }catch (Exception e){
            throw e;
        }
    }
    @GetMapping
    public List<QuestionMaster> getAllQuestions(){
        try{
            return questionMasterService.getAllQuestion();
        }catch (Exception e){
            throw e;
        }
    }
    @GetMapping("/{questionId}")
    public QuestionMaster getQuestionById(@RequestParam int questionId){
        try{
            return questionMasterService.getQuestionById(questionId);
        }catch (Exception e){
            throw e;
        }
    }
}

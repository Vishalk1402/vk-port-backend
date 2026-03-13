package com.main.portbackend.controller;

import com.main.portbackend.dto.AskRequest;
import com.main.portbackend.dto.JobMatchRequest;
import com.main.portbackend.dto.ProjectExplainRequest;
import com.main.portbackend.service.AIService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin("*")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody AskRequest request) {

        String answer = aiService.askAI(request.getQuestion());

        return Map.of("answer", answer);
    }

    @PostMapping("/job-match")
    public Map<String, String> analyzeJob(@RequestBody JobMatchRequest request) {

        String result = aiService.analyzeJobMatch(request.getJobDescription());

        return Map.of("result", result);
    }

    @PostMapping("/explain-project")
    public Map<String, String> explainProject(@RequestBody ProjectExplainRequest request) {

        String result = aiService.explainProject(request.getProjectDescription(),request.getProjectSkills());

        return Map.of("result", result);
    }
}
package com.main.portbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Autowired
    private PortfolioService portfolioService;

    private final WebClient webClient = WebClient.create();

    private final ObjectMapper mapper = new ObjectMapper();

    public String askAI(String question) {

        String portfolioContext = portfolioService.getPortfolioContext();

        Map<String, Object> request = Map.of(
                "model", "openai/gpt-oss-20b",
                "messages", List.of(

                        Map.of(
                                "role", "system",
                                "content", """
                                        You are an AI assistant for Vishal Koli's portfolio.
                                        
                                        Use the provided portfolio data to Answer questions about Vishal's portfolio.
                                        
                                            IMPORTANT:
                                              - Use a clean conversational format.
                                              - Do NOT use tables or markdown tables.
                                              - Use bullet points or numbered lists.
                                              - Keep responses clear and readable for a website UI.
                                        """
                        ),

                        Map.of(
                                "role", "system",
                                "content", portfolioContext
                        ),

                        Map.of(
                                "role", "user",
                                "content", question
                        )
                )
        );
        String response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = mapper.readTree(response);

            JsonNode choices = root.path("choices");

            if (choices.isArray() && choices.size() > 0) {
                return choices.get(0)
                        .path("message")
                        .path("content")
                        .asText();
            }

            return "No response from AI.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing AI response.";
        }
    }

    public String analyzeJobMatch(String jobDescription) {

        String prompt = """
                You are an AI career assistant.
                
                Analyze how well Vishal Koli matches the following job description.
                
                Candidate Profile:
                Name: Vishal Koli
                Role: Software Engineer at Siemens
                Skills: Java, Spring Boot, React, JavaScript, SQL, MongoDB, Docker, REST APIs.
                
                Return response in this format:
                
                Match Score: %
                
                Matching Skills:
                - skill1
                - skill2
                
                Missing Skills:
                - skill1
                - skill2
                
                Job Description:
                """ + jobDescription;

        Map<String, Object> request = Map.of(
                "model", "openai/gpt-oss-20b",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        String response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = mapper.readTree(response);

            JsonNode choices = root.path("choices");

            if (choices.isArray() && choices.size() > 0) {
                return choices.get(0)
                        .path("message")
                        .path("content")
                        .asText();
            }

            return "No response from AI.";

        } catch (Exception e) {
            return "Error processing AI response.";
        }
    }

    public String explainProject(String projectDescription, String[] projectSkills) {

        String prompt = """
                You are explaining a developer's project for a portfolio.
                
                IMPORTANT RULES:
                - Only use the information provided in the project description.
                - Do NOT invent technologies, frameworks, or features.
                - Do NOT assume anything not mentioned.
                - If something is not mentioned, do not include it.
                
                Return explanation in this format:
                
                ## Project Overview
                Explain the project briefly.
                
                ## Technologies Mentioned
                List only technologies explicitly mentioned.
                
                ## Key Features
                Describe only features from the description.
                
                Project Description:
                """ + projectDescription + """
                
                Technologies Used:
                """ + Arrays.toString(projectSkills);

        Map<String, Object> request = Map.of(
                "model", "openai/gpt-oss-20b",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        String response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = mapper.readTree(response);

            JsonNode choices = root.path("choices");

            if (choices.isArray() && choices.size() > 0) {
                return choices.get(0)
                        .path("message")
                        .path("content")
                        .asText();
            }

            return "No response from AI.";

        } catch (Exception e) {
            return "Error processing AI response.";
        }
    }
}
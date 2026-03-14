package com.main.portbackend.dto;

import java.util.Map;

public class AskRequest {

    private String question;
    private Map<String,Object> context;

    public String getQuestion() {
        return question;
    }

    public Map<String,Object> getContext() {
        return context;
    }
}
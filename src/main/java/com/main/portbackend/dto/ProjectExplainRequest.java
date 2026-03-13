package com.main.portbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectExplainRequest {
    private String projectDescription;
    private  String[] projectSkills;

}

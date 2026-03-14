package com.main.portbackend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Service
public class PortfolioService {

    private final ObjectMapper mapper = new ObjectMapper();
    private JsonNode portfolio;

    @PostConstruct
    public void loadPortfolio() throws Exception {

        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("portfolio.json");

        portfolio = mapper.readTree(is);
    }

    public String getPortfolioContext() {
        return portfolio.toPrettyString();
    }
}
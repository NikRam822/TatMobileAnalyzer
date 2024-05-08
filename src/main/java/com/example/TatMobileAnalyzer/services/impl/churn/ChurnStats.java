package com.example.TatMobileAnalyzer.services.impl.churn;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ChurnStats {
    private Map<String, List<Map<String, JsonNode>>> authorStats;
    private Map<String, Integer> overall;
    private Map<String, Map<String, Integer>> generalResult;
    private Map<String, Double> churn;
}


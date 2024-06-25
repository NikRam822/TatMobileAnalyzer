package com.tat.mobile.analyzer.services.impl.churn;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;


public record ChurnStat(Map<String, List<Map<String, JsonNode>>> authorStats,
                        java.util.Map<String, Integer> overall,
                        Map<String, Map<Integer, String>> general,
                        Map<String, Map<String, Integer>> generalResult,
                        Map<String, Double> churn) {

}

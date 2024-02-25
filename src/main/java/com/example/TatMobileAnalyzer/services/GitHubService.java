package com.example.TatMobileAnalyzer.services;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public interface GitHubService {

    HttpResponse<String> sendGetRequest(String apiUrl);

    List<Map<String, Object>> readJsonToList(String data);

    Map<String, Object> readJsonToMap(String data);
}

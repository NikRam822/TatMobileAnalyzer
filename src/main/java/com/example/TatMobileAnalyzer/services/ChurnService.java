package com.example.TatMobileAnalyzer.services;

import java.util.Date;
import java.util.Map;

public interface ChurnService {
    Map<String, Object> getStatisticChurn(Date since, Date until, Long projectId, String branc);
}

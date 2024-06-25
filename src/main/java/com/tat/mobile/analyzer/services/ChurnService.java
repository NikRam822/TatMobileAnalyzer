package com.tat.mobile.analyzer.services;

import java.util.Date;
import java.util.Map;

public interface ChurnService {
    Map<String, Object> getStatisticChurn(Date since, Date until, Long projectId);
}

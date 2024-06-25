package com.tat.mobile.analyzer.services.impl.cocomo;

import com.tat.mobile.analyzer.dto.CocomoCalculateDto;
import com.tat.mobile.analyzer.dto.CocomoDto;
import com.tat.mobile.analyzer.services.CocomoService;
import org.springframework.stereotype.Service;

@Service
public class CocomoServiceImpl implements CocomoService {
    @Override
    public CocomoCalculateDto calculateCocomoMetrics(CocomoDto cocomoDto) {

        CocomoCoefficients cocomoCoefficients = new CocomoCoefficients(cocomoDto.getProjectType());
        CocomoCalculateDto cocomoCalculateDto = new CocomoCalculateDto();
        cocomoCalculateDto.setPersonMonths((double) Math.round(cocomoCoefficients.a() * Math.pow(cocomoDto.getKloc(), cocomoCoefficients.b()) * 100) / 100);
        cocomoCalculateDto.setMonths((double) Math.round(cocomoCoefficients.c() * Math.pow(cocomoCalculateDto.getPersonMonths(), cocomoCoefficients.d()) * 100) / 100);
        cocomoCalculateDto.setPersonnel((double) Math.round(cocomoCalculateDto.getPersonMonths() / cocomoCalculateDto.getMonths() * 100) / 100);

        cocomoCalculateDto.setLaborIntensityWithRisk(
                Math.round(cocomoCalculateDto.getPersonMonths() *
                        cocomoDto.getReliability() *
                        cocomoDto.getDatabaseSize() *
                        cocomoDto.getProductComplexity() *
                        cocomoDto.getPerformance() *
                        cocomoDto.getMemoryLimit() *
                        cocomoDto.getUnstableEnvironment() *
                        cocomoDto.getRecoveryTime() *
                        cocomoDto.getAnalyticalSkills() *
                        cocomoDto.getDevelopmentSkills() *
                        cocomoDto.getDevelopmentExperience() *
                        cocomoDto.getVirtualMachinesExperience() *
                        cocomoDto.getLanguageExperience() *
                        cocomoDto.getDevelopmentTools() *
                        cocomoDto.getDevelopmentMethods() *
                        cocomoDto.getDevelopmentSchedule() * cocomoCoefficients.risk() * 100.0) / 100.0
        );

        return cocomoCalculateDto;
    }
}


package com.tat.mobile.analyzer.services.impl.cocomo;

public record CocomoCoefficients(double a, double b, double c, double d,double risk) {
    public CocomoCoefficients(ProjectType projectType) {
        this(getCoefficients(projectType).a, getCoefficients(projectType).b,
                getCoefficients(projectType).c, getCoefficients(projectType).d, getCoefficients(projectType).risk);
    }

    private static CocomoCoefficients getCoefficients(ProjectType projectType) {
        return switch (projectType) {
            case ORGANIC -> new CocomoCoefficients(2.4, 1.05, 2.5, 0.38,1.3333);
            case SEMIDETACH -> new CocomoCoefficients(3.0, 1.12, 2.5, 0.35,1);
            case EMBEDDED -> new CocomoCoefficients(3.6, 1.2, 2.5, 0.32,0.7777);
        };
    }
}

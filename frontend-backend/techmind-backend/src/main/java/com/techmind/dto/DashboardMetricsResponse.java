package com.techmind.dto;

public record DashboardMetricsResponse(
        long totalDocumentos,
        double precisionPromedio,
        long latenciaPromedioMs,
        String categoriaDominante,
        String ociStatus,
        String dockerStatus,
        MlHealthStatus mlHealth
) {
    public record MlHealthStatus(
            String status,
            boolean modelLoaded,
            String version
    ) {}
}

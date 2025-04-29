package com.practica1.dto;

import com.practica1.dto.common.ReportFormat;
import com.practica1.dto.common.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReportDto {
    @NotNull(message = "Agrege a la peticion el tipo de reporte.")
    private ReportType reportType;

    @NotNull(message = "Agrege a la peticion el formato deseado.")
    private ReportFormat reportFormat;

    public ReportDto() {
    }

    public ReportDto(ReportType reportType, ReportFormat reportFormat) {
        this.reportType = reportType;
        this.reportFormat = reportFormat;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportFormat getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(ReportFormat reportFormat) {
        this.reportFormat = reportFormat;
    }
}

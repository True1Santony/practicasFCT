package com.practica1.dto;

import com.practica1.dto.common.ReportFormat;
import com.practica1.dto.common.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportDto {

    @NotNull(message = "Agrege a la peticion el tipo de reporte.")
    private ReportType reportType;

    @NotNull(message = "Agrege a la peticion el formato deseado.")
    private ReportFormat reportFormat;

}

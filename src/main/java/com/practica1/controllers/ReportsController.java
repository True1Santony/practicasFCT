package com.practica1.controllers;

import com.practica1.dto.ReportDto;
import com.practica1.service.report.ReportService;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/report")
public class ReportsController {

    private final ReportService reportService;

    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping()
    public ResponseEntity<byte[]> generateReport(@Valid @RequestBody ReportDto reportDto) throws JRException {

        byte[] reportBytes = reportService.generateReport(reportDto);

        String contentType;
        String fileExtension;

        switch (reportDto.getReportFormat()) {
            case PDF:
                contentType = MediaType.APPLICATION_PDF_VALUE;
                fileExtension = ".pdf";
                break;
            case HTML:
                contentType = MediaType.TEXT_HTML_VALUE;
                fileExtension = ".html";
                break;
            case XLSX:
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileExtension = ".xlsx";
                break;
            default:
                throw new IllegalArgumentException("Formato no soportado: " + reportDto.getReportFormat());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportDto.getReportType() + fileExtension)
                .contentType(MediaType.parseMediaType(contentType))
                .body(reportBytes);


    }
}

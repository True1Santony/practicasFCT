package com.practica1.service.report;

import com.practica1.base.DatabaseConnection;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
public class ReportService {

    private final DatabaseConnection databaseConnection;

    public byte[] generateReport() throws JRException {

        InputStream reportStream = this.getClass().getResourceAsStream("/reports/VEHICLE.jasper");

        Map<String, Object> parameters = new HashMap<>();

        try (Connection connection = databaseConnection.getConnection()) {

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, connection);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            return out.toByteArray();

        } catch (SQLException e) {
            throw new RuntimeException("Error al generar el reporte: " + e.getMessage(), e);
        }
    }
}

package com.practica1;

import com.practica1.base.DatabaseConnection;
import com.practica1.service.report.ReportService;
import net.sf.jasperreports.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private JasperPrint jasperPrint;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() throws SQLException {
        when(databaseConnection.getConnection()).thenReturn(connection);
    }

    @Test
    void generateReport_successful() throws Exception {
        InputStream reportStream = getClass().getResourceAsStream("/reports/VEHICLE.jasper");
        assertNotNull(reportStream, "El archivo VEHICLE.jasper debe estar en resources/reports");

        try (MockedStatic<JasperFillManager> fillMock = mockStatic(JasperFillManager.class);
             MockedStatic<JasperExportManager> exportMock = mockStatic(JasperExportManager.class)) {

            fillMock.when(() -> JasperFillManager.fillReport(any(InputStream.class), anyMap(), eq(connection)))
                    .thenReturn(jasperPrint);

            exportMock.when(() -> JasperExportManager.exportReportToPdfStream(eq(jasperPrint), any(ByteArrayOutputStream.class)))
                    .thenAnswer(invocation -> {
                        ByteArrayOutputStream out = invocation.getArgument(1);
                        out.write("PDF content".getBytes());
                        return null;
                    });

            byte[] result = reportService.generateReport();

            assertNotNull(result);
            assertTrue(result.length > 0);
            assertEquals("PDF content", new String(result));
        }
    }

    @Test
    void generateReport_sqlException() throws SQLException {
        when(databaseConnection.getConnection()).thenThrow(new SQLException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.generateReport());
        assertTrue(exception.getMessage().contains("Error al generar el reporte"));
    }
}

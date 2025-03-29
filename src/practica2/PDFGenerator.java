/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica2;

/**
 *
 * @author Christopher
 */
import Model.Datos;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.JFreeChart;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import org.jfree.chart.ChartUtils;


public class PDFGenerator {
 private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font TABLE_HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

    public static void generarReporte(Datos modelo, String algoritmo, String velocidad, 
                                   String direccion, JFreeChart chart, JFreeChart chartDesordenado,
                                   String nombreEstudiante, String carnet) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "Reporte_Ordenamiento_" + timestamp + ".pdf";

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            // 1. Portada
            agregarPortada(document, nombreEstudiante, carnet);

            // 2. Información del proceso
            document.newPage();
            agregarSeccionProceso(document, modelo, algoritmo, velocidad, direccion);

            // 3. Datos estadísticos
            agregarEstadisticasProceso(document, modelo);

            // 4. Datos originales
            document.newPage();
            agregarDatosOriginales(document, modelo);
         document.newPage();
        agregarGrafica(document, chartDesordenado, "Gráfica de Datos Desordenados");

            // 5. Gráfica
            document.newPage();
            agregarGrafica(document, chart, "Gráfica de Datos Ordenados");

            document.close();
        } catch (Exception e) {
            throw new DocumentException("Error al generar PDF: " + e.getMessage());
        }
    }

    private static void agregarPortada(Document document, String nombre, String carnet) throws DocumentException {
        Paragraph universidad = new Paragraph("UNIVERSIDAD DE SAN CARLOS DE GUATEMALA", TITLE_FONT);
        universidad.setAlignment(Element.ALIGN_CENTER);
        universidad.setSpacingAfter(20f);
        document.add(universidad);

        Paragraph escuela = new Paragraph("Escuela de Ciencias y Sistemas\nIPC1 - Sección D", HEADER_FONT);
        escuela.setAlignment(Element.ALIGN_CENTER);
        escuela.setSpacingAfter(30f);
        document.add(escuela);

        Paragraph titulo = new Paragraph("REPORTE DE ORDENAMIENTO DE DATOS", 
            new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(50f);
        document.add(titulo);

        Paragraph estudiante = new Paragraph("Generado por:\n" + nombre + "\nCarné: " + carnet, 
            new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        estudiante.setAlignment(Element.ALIGN_CENTER);
        estudiante.setSpacingAfter(30f);
        document.add(estudiante);

        Paragraph fecha = new Paragraph("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), NORMAL_FONT);
        fecha.setAlignment(Element.ALIGN_CENTER);
        document.add(fecha);
    }

    private static void agregarSeccionProceso(Document document, Datos modelo, String algoritmo, 
                                            String velocidad, String direccion) throws DocumentException {
        Paragraph titulo = new Paragraph("INFORMACIÓN DEL PROCESO", HEADER_FONT);
        titulo.setSpacingAfter(20f);
        document.add(titulo);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(90);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(30f);

        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(new BaseColor(70, 130, 180));
        headerCell.setPadding(5);
        headerCell.setPhrase(new Phrase("PARÁMETRO", TABLE_HEADER_FONT));
        table.addCell(headerCell);
        headerCell.setPhrase(new Phrase("VALOR", TABLE_HEADER_FONT));
        table.addCell(headerCell);

        agregarFilaTabla(table, "Algoritmo utilizado", algoritmo);
        agregarFilaTabla(table, "Velocidad", velocidad);
        agregarFilaTabla(table, "Dirección", direccion);
        agregarFilaTabla(table, "Total de elementos", String.valueOf(modelo.getConteo().length));

        document.add(table);
    }

    private static void agregarEstadisticasProceso(Document document, Datos modelo) throws DocumentException {
        Paragraph titulo = new Paragraph("ESTADÍSTICAS DEL PROCESO", HEADER_FONT);
        titulo.setSpacingAfter(20f);
        document.add(titulo);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(60);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(30f);

        agregarFilaTabla(table, "Pasos totales", String.valueOf(modelo.getPasos()));
        agregarFilaTabla(table, "Comparaciones", String.valueOf(modelo.getComparaciones()));
        agregarFilaTabla(table, "Intercambios", String.valueOf(modelo.getIntercambios()));
        agregarFilaTabla(table, "Tiempo total", formatTime(modelo.getTiempoEjecucion()));

        document.add(table);
    }

    private static void agregarDatosOriginales(Document document, Datos modelo) throws DocumentException {
        Paragraph titulo = new Paragraph("DATOS ORIGINALES", HEADER_FONT);
        titulo.setSpacingAfter(20f);
        document.add(titulo);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(90);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(20f);

        // Encabezados
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(new BaseColor(70, 130, 180));
        headerCell.setPadding(5);
        headerCell.setPhrase(new Phrase(modelo.getEjeX(), TABLE_HEADER_FONT));
        table.addCell(headerCell);
        headerCell.setPhrase(new Phrase(modelo.getEjeY(), TABLE_HEADER_FONT));
        table.addCell(headerCell);

        // Datos
        String[] categorias = modelo.getCategoriaOriginal();
        int[] valores = modelo.getConteoOriginal();
        for (int i = 0; i < categorias.length; i++) {
            table.addCell(crearCeldaNormal(categorias[i]));
            table.addCell(crearCeldaNormal(String.valueOf(valores[i])));
        }

        document.add(table);
    }

    private static void agregarGrafica(Document document, JFreeChart chart, String titulo) throws DocumentException {
        Paragraph paragraph = new Paragraph(titulo, HEADER_FONT);
        paragraph.setSpacingAfter(20f);
        document.add(paragraph);

        try {
            byte[] chartImage = ChartUtils.encodeAsPNG(chart.createBufferedImage(600, 400));
            Image image = Image.getInstance(chartImage);
            image.setAlignment(Image.ALIGN_CENTER);
            document.add(image);
        } catch (Exception e) {
            throw new DocumentException("Error al agregar gráfica: " + e.getMessage());
        }
    }

    private static PdfPCell crearCeldaNormal(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, NORMAL_FONT));
        cell.setPadding(5);
        return cell;
    }

    private static void agregarFilaTabla(PdfPTable table, String parametro, String valor) {
        table.addCell(crearCeldaNormal(parametro));
        table.addCell(crearCeldaNormal(valor));
    }

    private static String formatTime(long millis) {
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        long milliseconds = millis % 1000;
        return String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Christopher
 */
import Model.Datos;
import View.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import practica2.PDFGenerator;


public class Controlador {
    
   private Datos modelo;
    private View vista;

    public Controlador(Datos modelo, View vista) {
        this.modelo = modelo;
        this.vista = vista;
        configurarListeners();
    }

    private void configurarListeners() {
        vista.setBuscarListener(new BuscarListener());
        vista.setOrdenarListener(new OrdenarListener());
        vista.setGenerarPDFListener(new GenerarPDFListener());
    
        
        modelo.setListener(new Datos.OrdenamientoListener() {
    @Override
    public void onPasoEjecutado(int[] datosActuales, int pasoActual, long tiempoTranscurrido,
            int indice1, int indice2, String accion) {
        
        // Actualizar la vista con los datos del paso actual
        vista.actualizarProceso(
            vista.getAlgoritmo(),
            vista.getVelocidad(),
            vista.isAscendente() ? "Ascendente" : "Descendente",
            pasoActual,
            modelo.getComparaciones(),
            modelo.getIntercambios(),
            tiempoTranscurrido,
            indice1,
            indice2,
            accion
        );
        
        // Forzar la actualización de la gráfica con los datos ordenados
        vista.mostrarDatos(modelo.getCategoria(), modelo.getConteo());
    }
});
    }

    class BuscarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File f) {
                    return f.getName().toLowerCase().endsWith(".ipcd1") || f.isDirectory();
                }
                public String getDescription() {
                    return "Archivos IPC1 (*.ipcd1)";
                }
            });

            if (fileChooser.showOpenDialog(vista) == JFileChooser.APPROVE_OPTION) {
                vista.getRutaArchivoField().setText(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    modelo.cargarDatos(fileChooser.getSelectedFile().getAbsolutePath());
                    vista.mostrarDatos(modelo.getCategoria(), modelo.getConteo());
                    vista.mostrarExito("Datos cargados correctamente");
                } catch (Exception ex) {
                    vista.mostrarError("Error al cargar archivo: " + ex.getMessage());
                }
            }
        }
    }

    class OrdenarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                try {
                    String algoritmo = vista.getAlgoritmo();
                    String velocidad = vista.getVelocidad();
                    boolean ascendente = vista.isAscendente();

                    modelo.setVelocidad(velocidad);
                    vista.btnGenerarPDF.setEnabled(true);

                    switch (algoritmo) {
                        case "Burbuja": modelo.ordenarBurbuja(ascendente); break;
                        case "Inserción": modelo.ordenarInsercion(ascendente); break;
                        case "Selección": modelo.ordenarSeleccion(ascendente); break;
                        case "QuickSort": modelo.ordenarQuickSort(ascendente); break;
                        case "MergeSort": modelo.ordenarMergeSort(ascendente); break;
                        case "ShellSort": modelo.ordenarShellSort(ascendente); break;
                    }

                    vista.mostrarExito("Ordenamiento completado");
                } catch (Exception ex) {
                    vista.mostrarError("Error durante el ordenamiento: " + ex.getMessage());
                }
            }).start();
        }
    }

    class GenerarPDFListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JFreeChart chart = vista.getChart();
            // Crear una copia de la gráfica desordenada usando los datos originales
            DefaultCategoryDataset datasetDesordenado = new DefaultCategoryDataset();
            String[] categoriasOriginales = modelo.getCategoriaOriginal();
            int[] conteoOriginal = modelo.getConteoOriginal();
            for (int i = 0; i < categoriasOriginales.length; i++) {
                datasetDesordenado.addValue(conteoOriginal[i], "Valor", categoriasOriginales[i]);
            }
            JFreeChart chartDesordenado = ChartFactory.createBarChart(
                "Datos Desordenados", 
                modelo.getEjeX(), 
                modelo.getEjeY(), 
                datasetDesordenado,
                PlotOrientation.VERTICAL, true, true, false
            );

            PDFGenerator.generarReporte(
                modelo,
                vista.getAlgoritmo(),
                vista.getVelocidad(),
                vista.isAscendente() ? "Ascendente" : "Descendente",
                chart,
                chartDesordenado,
                "Christopher Monroy",
                "202400860"
            );
            vista.mostrarExito("Reporte PDF generado exitosamente");
        } catch (Exception ex) {
            vista.mostrarError("Error al generar PDF: " + ex.getMessage());
        }
    }
}
}
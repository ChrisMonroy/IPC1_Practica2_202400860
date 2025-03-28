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
import com.itextpdf.text.DocumentException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jfree.chart.JFreeChart;
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
        
        modelo.setOrdenarListener((datos, paso, comp, interc, tiempo, idx1, idx2, accion) -> {
            vista.actualizarProceso(
                vista.getAlgoritmo(),
                vista.getVelocidad(),
                vista.isAscendente() ? "Ascendente" : "Descendente",
                paso,
                comp,
                interc,
                tiempo,
                idx1,
                idx2,
                accion
            );
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
                    vista.btnGenerarPDF.setEnabled(false);

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
                
                PDFGenerator.generarReporte(
                    modelo,
                    vista.getAlgoritmo(),
                    vista.getVelocidad(),
                    vista.isAscendente() ? "Ascendente" : "Descendente",
                    chart,
                    "Tu Nombre",  // Cambiar por tu nombre
                    "202500000"   // Cambiar por tu carné
                );
                
                vista.mostrarExito("Reporte PDF generado exitosamente");
            } catch (Exception ex) {
                vista.mostrarError("Error al generar PDF: " + ex.getMessage());
            }
        }
    }
}
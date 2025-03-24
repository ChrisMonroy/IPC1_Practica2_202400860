/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author Christopher
 */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
public class GenerarGrafica {
     public static JPanel generarGraficaBarras(String[] categorias, int[] conteos, String titulo) {
        // Crear un dataset para el gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < categorias.length; i++) {
            dataset.addValue(conteos[i], "Conteo", categorias[i]);
        }

        // Crear el gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
                titulo,  
                "Categorías",  // Etiqueta del eje X
                "Conteo",  // Etiqueta del eje Y
                dataset, 
                PlotOrientation.VERTICAL,  
                true,  
                true, 
                false  
        );

        // Crear un panel para mostrar el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));  // Tamaño del panel

        return chartPanel;
    }
}
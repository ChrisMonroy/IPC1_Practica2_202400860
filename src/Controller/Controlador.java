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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;


public class Controlador {
    
   private Datos modelo;
    private View vista;

    public Controlador(Datos modelo, View vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    
    public void procesarArchivo(String ruta, String algoritmo, boolean ascendente, String velocidad) {
        // Leer el archivo y cargar los datos en el modelo
        modelo.LeerArchivos(ruta);

        // Seleccionar el algoritmo de ordenamiento basado en la elección del usuario
        switch (algoritmo) {
            case "Bubble Sort":
                modelo.ordenarBurbuja(ascendente);
                break;
            case "Insert Sort":
                modelo.ordenarInsercion(ascendente);
                break;
            case "Select Sort":
                modelo.ordenarSeleccion(ascendente);
                break;
            case "Merge Sort":
                modelo.ordenarMezcla(ascendente);
                break;
            case "Quicksort":
                modelo.ordenarRapido(ascendente);
                break;
            case "Shellsort":
                modelo.ordenarShell(ascendente);
                break;
            default:
                vista.mostrarError("Algoritmo no reconocido");
                return;
        }

       vista.mostrarResultados(modelo.getCategoria(), modelo.getConteo());
       vista.mostrarGrafica(modelo.getCategoria(), modelo.getConteo(), "Resultados del Ordenamiento");
    }
}
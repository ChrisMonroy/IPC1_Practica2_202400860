/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package practica2;

import Controller.Controlador;
import Model.Datos;
import View.View;

/**
 *
 * @author Christopher
 */
public class Practica2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Datos modelo = new Datos();
        View vista = new View();
        Controlador controlador = new Controlador(modelo, vista);
        vista.setVisible(true);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Christopher
 */
public abstract class Ordenamiento {
     protected String[] categorias;
    protected int[] conteos;
    protected int cantidadDatos;
    protected int pasos;
    protected long tiempoInicio;
    protected JFreeChart chartOriginal;
    protected JFreeChart chartOrdenado;

    public void setDatos(String[] categorias, int[] conteos, int cantidadDatos) {
        this.categorias = categorias;
        this.conteos = conteos;
        this.cantidadDatos = cantidadDatos;
        this.pasos = 0;
    }

    protected void intercambiar(int i, int j) {
        int temp = conteos[i];
        conteos[i] = conteos[j];
        conteos[j] = temp;

        String tempCat = categorias[i];
        categorias[i] = categorias[j];
        categorias[j] = tempCat;

        pasos++;
    }

    protected boolean debeIntercambiar(int a, int b, boolean ascendente) {
        return (ascendente && a > b) || (!ascendente && a < b);
    }

    // Bubble Sort
    public void ordenarBurbuja(int velocidad, boolean ascendente) throws InterruptedException {
        tiempoInicio = System.currentTimeMillis();
        for (int i = 0; i < cantidadDatos - 1; i++) {
            for (int j = 0; j < cantidadDatos - i - 1; j++) {
                if (debeIntercambiar(conteos[j], conteos[j + 1], ascendente)) {
                    intercambiar(j, j + 1);
                    Thread.sleep(velocidad);
                }
            }
        }
    }

    // Selection Sort
    public void ordenarSeleccion(int velocidad, boolean ascendente) throws InterruptedException {
        tiempoInicio = System.currentTimeMillis();
        for (int i = 0; i < cantidadDatos - 1; i++) {
            int extremo = i;
            for (int j = i + 1; j < cantidadDatos; j++) {
                if (debeIntercambiar(conteos[extremo], conteos[j], ascendente)) {
                    extremo = j;
                }
            }
            if (extremo != i) {
                intercambiar(i, extremo);
                Thread.sleep(velocidad);
            }
        }
    }

    // Insertion Sort
    public void ordenarInsercion(int velocidad, boolean ascendente) throws InterruptedException {
        tiempoInicio = System.currentTimeMillis();
        for (int i = 1; i < cantidadDatos; i++) {
            int clave = conteos[i];
            String claveCat = categorias[i];
            int j = i - 1;

            while (j >= 0 && debeIntercambiar(conteos[j], clave, ascendente)) {
                conteos[j + 1] = conteos[j];
                categorias[j + 1] = categorias[j];
                j--;
                pasos++;
                Thread.sleep(velocidad);
            }
            conteos[j + 1] = clave;
            categorias[j + 1] = claveCat;
        }
    }

    // Merge Sort
    public void ordenarMezcla(int velocidad, boolean ascendente) throws InterruptedException {
        tiempoInicio = System.currentTimeMillis();
        mezclaOrdenar(0, cantidadDatos - 1, velocidad, ascendente);
    }

    private void mezclaOrdenar(int izquierda, int derecha, int velocidad, boolean ascendente) throws InterruptedException {
        if (izquierda < derecha) {
            int medio = (izquierda + derecha) / 2;
            mezclaOrdenar(izquierda, medio, velocidad, ascendente);
            mezclaOrdenar(medio + 1, derecha, velocidad, ascendente);
            mezclar(izquierda, medio, derecha, velocidad, ascendente);
        }
    }

    private void mezclar(int izquierda, int medio, int derecha, int velocidad, boolean ascendente) throws InterruptedException {
        int n1 = medio - izquierda + 1;
        int n2 = derecha - medio;

        int[] L = new int[n1];
        String[] LCategorias = new String[n1];
        int[] R = new int[n2];
        String[] RCategorias = new String[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = conteos[izquierda + i];
            LCategorias[i] = categorias[izquierda + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = conteos[medio + 1 + j];
            RCategorias[j] = categorias[medio + 1 + j];
        }

        int i = 0, j = 0, k = izquierda;
        while (i < n1 && j < n2) {
            if ((ascendente && L[i] <= R[j]) || (!ascendente && L[i] >= R[j])) {
                conteos[k] = L[i];
                categorias[k] = LCategorias[i];
                i++;
            } else {
                conteos[k] = R[j];
                categorias[k] = RCategorias[j];
                j++;
            }
            pasos++;
            Thread.sleep(velocidad);
            k++;
        }

        while (i < n1) {
            conteos[k] = L[i];
            categorias[k] = LCategorias[i];
            i++;
            k++;
            pasos++;
            Thread.sleep(velocidad);
        }

        while (j < n2) {
            conteos[k] = R[j];
            categorias[k] = RCategorias[j];
            j++;
            k++;
            pasos++;
            Thread.sleep(velocidad);
        }
    }

    // Quick Sort
    public void ordenarRapido(int velocidad, boolean ascendente) throws InterruptedException {
        tiempoInicio = System.currentTimeMillis();
        quickSort(0, cantidadDatos - 1, velocidad, ascendente);
    }

    private void quickSort(int bajo, int alto, int velocidad, boolean ascendente) throws InterruptedException {
        if (bajo < alto) {
            int pi = particion(bajo, alto, velocidad, ascendente);
            quickSort(bajo, pi - 1, velocidad, ascendente);
            quickSort(pi + 1, alto, velocidad, ascendente);
        }
    }

    private int particion(int bajo, int alto, int velocidad, boolean ascendente) throws InterruptedException {
        int pivote = conteos[alto];
        int i = (bajo - 1);

        for (int j = bajo; j < alto; j++) {
            if (debeIntercambiar(pivote, conteos[j], !ascendente)) {
                i++;
                intercambiar(i, j);
                Thread.sleep(velocidad);
            }
        }
        intercambiar(i + 1, alto);
        return i + 1;
    }

    public int getPasos() {
        return pasos;
    }

    public long getTiempoTranscurrido() {
        return System.currentTimeMillis() - tiempoInicio;
    }

    public void setChartOriginal(JFreeChart chart) {
        this.chartOriginal = chart;
    }

    public void setChartOrdenado(JFreeChart chart) {
        this.chartOrdenado = chart;
    }

    public JFreeChart getChartOriginal() {
        return chartOriginal;
    }

    public JFreeChart getChartOrdenado() {
        return chartOrdenado;
    }
}
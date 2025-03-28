/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Christopher
 */
public class Datos {
   private String[] categoria;
    private int[] conteo;
    private String[] categoriasOriginales;
    private int[] conteoOriginal;
    private int pasos;
    private String velocidad;
    private String ejeX;
    private String ejeY;
    private long tiempoInicio;
    private OrdenamientoListener listener;
    
    public interface OrdenamientoListener {
        void onPasoEjecutado(int[] datosActuales, int pasoActual, long tiempoTranscurrido, 
                           int indice1, int indice2, String accion);
    }
    
    public void setListener(OrdenamientoListener listener) {
        this.listener = listener;
    }

    public void cargarDatos(String rutaArchivo) throws IOException {
        if (!rutaArchivo.toLowerCase().endsWith(".ipcd1")) {
            throw new IOException("Extensión de archivo no permitida para sección D. Use .ipcd1");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String encabezados = br.readLine();
            if (encabezados == null) {
                throw new IOException("El archivo está vacío");
            }

            String[] partesEncabezado = encabezados.split(",");
            if (partesEncabezado.length < 2) {
                throw new IOException("Formato de archivo inválido. Debe contener al menos 2 columnas");
            }

            ejeX = partesEncabezado[0].trim();
            ejeY = partesEncabezado[1].trim();

            int lineas = 0;
            BufferedReader contador = new BufferedReader(new FileReader(rutaArchivo));
            contador.readLine();
            while (contador.readLine() != null) lineas++;
            contador.close();

            categoria = new String[lineas];
            conteo = new int[lineas];
            categoriasOriginales = new String[lineas];
            conteoOriginal = new int[lineas];

            for (int i = 0; i < lineas; i++) {
                String linea = br.readLine();
                if (linea == null) break;

                String[] partes = linea.split(",");
                if (partes.length < 2) {
                    throw new IOException("Error en línea " + (i + 2) + ": Formato incorrecto");
                }

                categoria[i] = partes[0].trim();
                categoriasOriginales[i] = categoria[i];
                
                try {
                    conteo[i] = Integer.parseInt(partes[1].trim());
                    conteoOriginal[i] = conteo[i];
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Error en línea " + (i + 2) + ": El valor debe ser numérico");
                }
            }
        } catch (FileNotFoundException e) {
            throw new IOException("Archivo no encontrado: " + rutaArchivo);
        }
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    private void pausar() {
        try {
            switch(velocidad) {
                case "Alta": Thread.sleep(100); break;
                case "Media": Thread.sleep(500); break;
                case "Baja": Thread.sleep(1000); break;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Algoritmos de ordenamiento
    public void ordenarBurbuja(boolean ascendente) {
        pasos = 0;
        tiempoInicio = System.currentTimeMillis();
        for (int i = 0; i < conteo.length - 1; i++) {
            for (int j = 0; j < conteo.length - i - 1; j++) {
                if ((ascendente && conteo[j] > conteo[j + 1]) || (!ascendente && conteo[j] < conteo[j + 1])) {
                    intercambiar(j, j + 1);
                    notificarPaso(j, j + 1, "Intercambio");
                }
            }
        }
    }

    public void ordenarInsercion(boolean ascendente) {
        pasos = 0;
        tiempoInicio = System.currentTimeMillis();
        for (int i = 1; i < conteo.length; i++) {
            int key = conteo[i];
            String keyCat = categoria[i];
            int j = i - 1;
            while (j >= 0 && ((ascendente && conteo[j] > key) || (!ascendente && conteo[j] < key))) {
                conteo[j + 1] = conteo[j];
                categoria[j + 1] = categoria[j];
                j--;
                pasos++;
                notificarPaso(j, i, "Desplazamiento");
            }
            conteo[j + 1] = key;
            categoria[j + 1] = keyCat;
        }
    }

    public void ordenarSeleccion(boolean ascendente) {
        pasos = 0;
        tiempoInicio = System.currentTimeMillis();
        for (int i = 0; i < conteo.length - 1; i++) {
            int extremo = i;
            for (int j = i + 1; j < conteo.length; j++) {
                if ((ascendente && conteo[j] < conteo[extremo]) || (!ascendente && conteo[j] > conteo[extremo])) {
                    extremo = j;
                }
                pasos++;
                notificarPaso(j, extremo, "Comparación");
            }
            intercambiar(i, extremo);
        }
    }

    public void ordenarQuickSort(boolean ascendente) {
        pasos = 0;
        tiempoInicio = System.currentTimeMillis();
        quickSort(0, conteo.length - 1, ascendente);
    }

    private void quickSort(int inicio, int fin, boolean ascendente) {
        if (inicio < fin) {
            int indiceParticion = particion(inicio, fin, ascendente);
            quickSort(inicio, indiceParticion - 1, ascendente);
            quickSort(indiceParticion + 1, fin, ascendente);
        }
    }

    private int particion(int inicio, int fin, boolean ascendente) {
        int pivote = conteo[fin];
        int i = inicio - 1;
        for (int j = inicio; j < fin; j++) {
            if ((ascendente && conteo[j] <= pivote) || (!ascendente && conteo[j] >= pivote)) {
                i++;
                intercambiar(i, j);
                notificarPaso(i, j, "Partición");
            }
        }
        intercambiar(i + 1, fin);
        return i + 1;
    }

    public void ordenarMergeSort(boolean ascendente) {
        pasos = 0;
        tiempoInicio = System.currentTimeMillis();
        mergeSort(0, conteo.length - 1, ascendente);
    }

    private void mergeSort(int inicio, int fin, boolean ascendente) {
        if (inicio < fin) {
            int medio = (inicio + fin) / 2;
            mergeSort(inicio, medio, ascendente);
            mergeSort(medio + 1, fin, ascendente);
            merge(inicio, medio, fin, ascendente);
        }
    }

    private void merge(int inicio, int medio, int fin, boolean ascendente) {
        int n1 = medio - inicio + 1;
        int n2 = fin - medio;

        int[] L = new int[n1];
        int[] R = new int[n2];
        String[] Lcat = new String[n1];
        String[] Rcat = new String[n2];

        System.arraycopy(conteo, inicio, L, 0, n1);
        System.arraycopy(conteo, medio + 1, R, 0, n2);
        System.arraycopy(categoria, inicio, Lcat, 0, n1);
        System.arraycopy(categoria, medio + 1, Rcat, 0, n2);

        int i = 0, j = 0, k = inicio;
        while (i < n1 && j < n2) {
            if ((ascendente && L[i] <= R[j]) || (!ascendente && L[i] >= R[j])) {
                conteo[k] = L[i];
                categoria[k] = Lcat[i];
                i++;
            } else {
                conteo[k] = R[j];
                categoria[k] = Rcat[j];
                j++;
            }
            pasos++;
            notificarPaso(k, k, "Merge");
            k++;
        }

        while (i < n1) {
            conteo[k] = L[i];
            categoria[k] = Lcat[i];
            i++;
            k++;
            pasos++;
            notificarPaso(k, k, "Merge");
        }

        while (j < n2) {
            conteo[k] = R[j];
            categoria[k] = Rcat[j];
            j++;
            k++;
            pasos++;
            notificarPaso(k, k, "Merge");
        }
    }

    public void ordenarShellSort(boolean ascendente) {
        pasos = 0;
        tiempoInicio = System.currentTimeMillis();
        int n = conteo.length;
        for (int gap = n/2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = conteo[i];
                String tempCat = categoria[i];
                int j;
                for (j = i; j >= gap && ((ascendente && conteo[j - gap] > temp) || (!ascendente && conteo[j - gap] < temp)); j -= gap) {
                    conteo[j] = conteo[j - gap];
                    categoria[j] = categoria[j - gap];
                    pasos++;
                    notificarPaso(j, j - gap, "Shell Sort");
                }
                conteo[j] = temp;
                categoria[j] = tempCat;
            }
        }
    }

    private void intercambiar(int i, int j) {
        int temp = conteo[i];
        conteo[i] = conteo[j];
        conteo[j] = temp;

        String tempCat = categoria[i];
        categoria[i] = categoria[j];
        categoria[j] = tempCat;

        pasos++;
    }

    private void notificarPaso(int idx1, int idx2, String accion) {
        if (listener != null) {
            listener.onPasoEjecutado(
                conteo.clone(),
                pasos,
                System.currentTimeMillis() - tiempoInicio,
                idx1,
                idx2,
                accion
            );
        }
        pausar();
    }

    // Getters
    public String[] getCategoria() { return categoria; }
    public int[] getConteo() { return conteo; }
    public String[] getCategoriaOriginal() { return categoriasOriginales; }
    public int[] getConteoOriginal() { return conteoOriginal; }
    public String getEjeX() { return ejeX; }
    public String getEjeY() { return ejeY; }
    public int getPasos() { return pasos; }
    public long getTiempoEjecucion() { return System.currentTimeMillis() - tiempoInicio; }
    public int getMinimo() {
        int min = conteo[0];
        for (int val : conteo) if (val < min) min = val;
        return min;
    }
    public int getMaximo() {
        int max = conteo[0];
        for (int val : conteo) if (val > max) max = val;
        return max;
    }

    public String[] getCategoriasOriginales() {
        return categoriasOriginales;
    }

    public void setCategoriasOriginales(String[] categoriasOriginales) {
        this.categoriasOriginales = categoriasOriginales;
    }

    public long getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(long tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }
    
}
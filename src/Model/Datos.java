/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Christopher
 */
public class Datos {
    private String categoria[];
    private int conteo[];

    public Datos(String categoria[], int conteo[]) {
        this.categoria = categoria;
        this.conteo = conteo;
    }

    public String[] getCategoria() {
        return categoria;
    }

    public void setCategoria(String[] categoria) {
        this.categoria = categoria;
    }

    public int[] getConteo() {
        return conteo;
    }

    public void setConteo(int[] conteo) {
        this.conteo = conteo;
    } 
    
    public void LeerArchivos(String ruta) {
    try (BufferedReader lector = new BufferedReader(new FileReader(ruta))) {
        String fila;
        int contador = 0;
        String encabezado = lector.readLine(); // Leer la primera línea (encabezado)
        System.out.println(encabezado);

        while ((fila = lector.readLine()) != null) {
            // Dividir la línea en partes usando la coma como separador
            String[] datos = fila.split(",");

            if (datos.length < 2) {
                continue; // Si no hay suficientes datos, continuar con la siguiente línea
            }

            String nombreJuego = datos[0];
            int cantidad = Integer.parseInt(datos[1]);

            System.out.println("Juego: " + nombreJuego + " Cantidad: " + cantidad);
            contador++;
        }

        System.out.println("Todos los juegos leidos: " + contador);
    } catch (IOException e) {
        System.out.println("Error al leer el archivo");
        e.printStackTrace();
    }
}
    
    public void ordenarBurbuja(boolean cambio) {
        for (int i = 0; i < conteo.length - 1; i++) {
            for (int j = 0; j < conteo.length - i - 1; j++) {
                if ((cambio && conteo[j] > conteo[j + 1]) || (!cambio && conteo[j] < conteo[j + 1])) {
                    // Intercambiar conteos
                    int temp = conteo[j];
                    conteo[j] = conteo[j + 1];
                    conteo[j + 1] = temp;

                    // Intercambiar categorías
                    String tempCategoria = categoria[j];
                    categoria[j] = categoria[j + 1];
                    categoria[j + 1] = tempCategoria;
                }
            }
        }
    }

    // Método de ordenamiento por Inserción (ascendente o descendente)
    public void ordenarInsercion(boolean cambio) {
        for (int i = 1; i < conteo.length; i++) {
            int clave = conteo[i];
            String claveCategoria = categoria[i];
            int j = i - 1;

            while (j >= 0 && ((cambio && conteo[j] > clave) || (!cambio && conteo[j] < clave))) {
                conteo[j + 1] = conteo[j];
                categoria[j + 1] = categoria[j];
                j--;
            }
            conteo[j + 1] = clave;
            categoria[j + 1] = claveCategoria;
        }
    }

    // Método de ordenamiento por Selección (ascendente o descendente)
    public void ordenarSeleccion(boolean cambio) {
        for (int i = 0; i < conteo.length - 1; i++) {
            int indiceMinMax = i;
            for (int j = i + 1; j < conteo.length; j++) {
                if ((cambio && conteo[j] < conteo[indiceMinMax]) || (!cambio && conteo[j] > conteo[indiceMinMax])) {
                    indiceMinMax = j;
                }
            }
            // Intercambiar conteos
            int temp = conteo[indiceMinMax];
            conteo[indiceMinMax] = conteo[i];
            conteo[i] = temp;

            // Intercambiar categorías
            String tempCategoria = categoria[indiceMinMax];
            categoria[indiceMinMax] = categoria[i];
            categoria[i] = tempCategoria;
        }
    }

    // Método de ordenamiento por Mezcla (ascendente o descendente)
    public void ordenarMezcla(boolean cambio) {
        mezclaOrdenar(0, conteo.length - 1, cambio);
    }

    private void mezclaOrdenar(int izquierda, int derecha, boolean cambio) {
        if (izquierda < derecha) {
            int medio = (izquierda + derecha) / 2;
            mezclaOrdenar(izquierda, medio, cambio);
            mezclaOrdenar(medio + 1, derecha, cambio);
            mezclar(izquierda, medio, derecha, cambio);
        }
    }

    private void mezclar(int izquierda, int medio, int derecha, boolean cambio) {
        int n1 = medio - izquierda + 1;
        int n2 = derecha - medio;

        int[] LConteos = new int[n1];
        String[] LCategorias = new String[n1];
        int[] RConteos = new int[n2];
        String[] RCategorias = new String[n2];

        // Copiar datos a los arreglos temporales
        for (int i = 0; i < n1; i++) {
            LConteos[i] = conteo[izquierda + i];
            LCategorias[i] = categoria[izquierda + i];
        }
        for (int j = 0; j < n2; j++) {
            RConteos[j] = conteo[medio + 1 + j];
            RCategorias[j] = categoria[medio + 1 + j];
        }

        // Mezclar los arreglos temporales
        int i = 0, j = 0, k = izquierda;
        while (i < n1 && j < n2) {
            if ((cambio && LConteos[i] <= RConteos[j]) || (!cambio && LConteos[i] >= RConteos[j])) {
                conteo[k] = LConteos[i];
                categoria[k] = LCategorias[i];
                i++;
            } else {
                conteo[k] = RConteos[j];
                categoria[k] = RCategorias[j];
                j++;
            }
            k++;
        }

        // Copiar los elementos restantes de LConteos y LCategorias
        while (i < n1) {
            conteo[k] = LConteos[i];
            categoria[k] = LCategorias[i];
            i++;
            k++;
        }

        // Copiar los elementos restantes de RConteos y RCategorias
        while (j < n2) {
            conteo[k] = RConteos[j];
            categoria[k] = RCategorias[j];
            j++;
            k++;
        }
    }

    // Método de ordenamiento Rápido (Quicksort) (ascendente o descendente)
    public void ordenarRapido(boolean cambio) {
        rapidoOrdenar(0, conteo.length - 1, cambio);
    }

    private void rapidoOrdenar(int bajo, int alto, boolean cambio) {
        if (bajo < alto) {
            int pi = particionar(bajo, alto, cambio);
            rapidoOrdenar(bajo, pi - 1, cambio);
            rapidoOrdenar(pi + 1, alto, cambio);
        }
    }

    private int particionar(int bajo, int alto, boolean cambio) {
        int pivote = conteo[alto];
        int i = (bajo - 1);
        for (int j = bajo; j < alto; j++) {
            if ((cambio && conteo[j] <= pivote) || (!cambio && conteo[j] >= pivote)) {
                i++;
                // Intercambiar conteos
                int temp = conteo[i];
                conteo[i] = conteo[j];
                conteo[j] = temp;

                // Intercambiar categorías
                String tempCategoria = categoria[i];
                categoria[i] = categoria[j];
                categoria[j] = tempCategoria;
            }
        }
        // Intercambiar conteos
        int temp = conteo[i + 1];
        conteo[i + 1] = conteo[alto];
        conteo[alto] = temp;

        // Intercambiar categorías
        String tempCategoria = categoria[i + 1];
        categoria[i + 1] = categoria[alto];
        categoria[alto] = tempCategoria;

        return i + 1;
    }

    // Método de ordenamiento Shell (ascendente o descendente)
    public void ordenarShell(boolean cambio) {
        int n = conteo.length;
        for (int intervalo = n / 2; intervalo > 0; intervalo /= 2) {
            for (int i = intervalo; i < n; i++) {
                int temp = conteo[i];
                String tempCategoria = categoria[i];
                int j;
                for (j = i; j >= intervalo && ((cambio && conteo[j - intervalo] > temp) || (!cambio && conteo[j - intervalo] < temp)); j -= intervalo) {
                    conteo[j] = conteo[j - intervalo];
                    categoria[j] = categoria[j - intervalo];
                }
                conteo[j] = temp;
                categoria[j] = tempCategoria;
            }
        }
    }
    private static void pausar(String velocidad) {
        try {
            switch (velocidad) {
                case "Baja":
                    Thread.sleep(1000);  // 500 ms de pausa
                    break;
                case "Media":
                    Thread.sleep(500);  // 250 ms de pausa
                    break;
                case "Alta":
                    Thread.sleep(100);  // 100 ms de pausa
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
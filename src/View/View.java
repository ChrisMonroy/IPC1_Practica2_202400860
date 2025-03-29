/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import java.awt.event.ActionListener;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;



/**
 *
 * @author Christopher
 */
public class View extends javax.swing.JFrame {
     private JTextField txtArchivo;
    public JButton btnBuscar;
    public JButton btnOrdenar;
    public JButton btnGenerarPDF;
    private JComboBox<String> cbAlgoritmo, cbVelocidad, cbDireccion;
    private JLabel lblAlgoritmo, lblVelocidad, lblDireccion, lblEstado;
    private JLabel lblPasos, lblComparaciones, lblIntercambios, lblTiempo;
    private JProgressBar progressBar;
    private ChartPanel chartPanel;
    private DefaultCategoryDataset dataset;

    public View() {
        setTitle("USAC - Ordenamiento de Datos");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponent();
    }

    private void initComponent() {
        // Panel superior
        JPanel pnlSuperior = new JPanel(new GridLayout(3, 1));

        // Panel de búsqueda
        JPanel pnlBusqueda = new JPanel();
        txtArchivo = new JTextField(30);
        btnBuscar = new JButton("Buscar Archivo .ipcd1");
        pnlBusqueda.add(new JLabel("Archivo:"));
        pnlBusqueda.add(txtArchivo);
        pnlBusqueda.add(btnBuscar);

        // Panel de configuración
        JPanel pnlConfig = new JPanel();
        cbAlgoritmo = new JComboBox<>(new String[]{"Burbuja", "Inserción", "Selección", "QuickSort", "MergeSort", "ShellSort"});
        cbDireccion = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        cbVelocidad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        btnOrdenar = new JButton("Ordenar Datos");
        btnGenerarPDF = new JButton("Generar PDF");
        btnGenerarPDF.setEnabled(true);

        pnlConfig.add(new JLabel("Algoritmo:"));
        pnlConfig.add(cbAlgoritmo);
        pnlConfig.add(new JLabel("Dirección:"));
        pnlConfig.add(cbDireccion);
        pnlConfig.add(new JLabel("Velocidad:"));
        pnlConfig.add(cbVelocidad);
        pnlConfig.add(btnOrdenar);
        pnlConfig.add(btnGenerarPDF);

        // Panel de información
        JPanel pnlInfo = new JPanel(new GridLayout(2, 4));
        lblAlgoritmo = new JLabel("Algoritmo: -");
        lblVelocidad = new JLabel("Velocidad: -");
        lblDireccion = new JLabel("Dirección: -");
        lblEstado = new JLabel("Estado: Esperando...");
        lblPasos = new JLabel("Pasos: 0");
        lblComparaciones = new JLabel("Comparaciones: 0");
        lblIntercambios = new JLabel("Intercambios: 0");
        lblTiempo = new JLabel("Tiempo: 00:00:000");
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        pnlInfo.add(lblAlgoritmo);
        pnlInfo.add(lblVelocidad);
        pnlInfo.add(lblDireccion);
        pnlInfo.add(lblEstado);
        pnlInfo.add(lblPasos);
        pnlInfo.add(lblComparaciones);
        pnlInfo.add(lblIntercambios);
        pnlInfo.add(lblTiempo);

        pnlSuperior.add(pnlBusqueda);
        pnlSuperior.add(pnlConfig);
        pnlSuperior.add(pnlInfo);

        // Panel central con gráfica
        dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart(
            "Datos", "Categoría", "Valor", dataset,
            PlotOrientation.VERTICAL, true, true, false
        );
        chartPanel = new ChartPanel(chart);
        JPanel pnlGrafica = new JPanel(new BorderLayout());
        pnlGrafica.add(chartPanel, BorderLayout.CENTER);

        // Configuración principal
        setLayout(new BorderLayout());
        add(pnlSuperior, BorderLayout.NORTH);
        add(pnlGrafica, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);
    }

    public void mostrarDatos(String[] categorias, int[] valores) {
        dataset.clear();
        for (int i = 0; i < categorias.length; i++) {
            dataset.addValue(valores[i], "Valor", categorias[i]);
        }
    }

    public void actualizarProceso(String algoritmo, String velocidad, String direccion,
            int pasos, int comparaciones, int intercambios,
            long tiempo, int idx1, int idx2, String accion) {
        
        SwingUtilities.invokeLater(() -> {
            lblAlgoritmo.setText("Algoritmo: " + algoritmo);
            lblVelocidad.setText("Velocidad: " + velocidad);
            lblDireccion.setText("Dirección: " + direccion);
            lblEstado.setText("Estado: " + accion);
            lblPasos.setText("Pasos: " + pasos);
            lblComparaciones.setText("Comparaciones: " + comparaciones);
            lblIntercambios.setText("Intercambios: " + intercambios);
            lblTiempo.setText("Tiempo: " + formatTime(tiempo));

            // Actualizar progreso
            int totalEstimado = (int) Math.pow(dataset.getRowCount(), 2);
            int progreso = (int) ((double) pasos / totalEstimado * 100);
            progressBar.setValue(Math.min(progreso, 100));

        });
    }

    private String formatTime(long millis) {
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        long milliseconds = millis % 1000;
        return String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
    }

    // Getters
    public String getRutaArchivo() { return txtArchivo.getText(); }
    public JTextField getRutaArchivoField() { return txtArchivo; }
    public String getAlgoritmo() { return (String) cbAlgoritmo.getSelectedItem(); }
    public String getVelocidad() { return (String) cbVelocidad.getSelectedItem(); }
    public boolean isAscendente() { return cbDireccion.getSelectedIndex() == 0; }
    public JFreeChart getChart() { return chartPanel.getChart(); }

    // Setters para listeners
    public void setBuscarListener(ActionListener listener) { btnBuscar.addActionListener(listener); }
    public void setOrdenarListener(ActionListener listener) { btnOrdenar.addActionListener(listener); }
    public void setGenerarPDFListener(ActionListener listener) { btnGenerarPDF.addActionListener(listener); }

    // Métodos de mensajes
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
/*
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextField1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jButton1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jButton1.setText("Buscar");

        jComboBox1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bubble Sort", "Insert Sort", "Select Sort", "Merge Sort", "Quicksort", "Shellsort" }));

        jComboBox2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ascendente", "Descendente" }));

        jComboBox3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alta", "Media", "Baja" }));

        jButton2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton2.setText("Aceptar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 699, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 251, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables


}

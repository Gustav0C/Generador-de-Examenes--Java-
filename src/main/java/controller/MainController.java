package controller;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Desktop;
import java.net.URL;

import java.util.Random;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void generarExamenes(ActionEvent event) {
        // Generar cuadro de dialogo para seleccionar la cantidad de exámenes a generar
        int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de exámenes a generar:"));

        for (int k = 0; k < cantidad; k++) {
            InputStream inputStream = getClass().getResourceAsStream("/Plantilla.docx");
            if (inputStream == null) {
                System.out.println("Error: No se encontró la plantilla.");
                return;
            }

            try (
                    // Cargar la plantilla original
                    XWPFDocument document = new XWPFDocument(inputStream)) {

                // Generar una letra aleatoria para el tema (A-Z)
                char tema = (char) ('A' + new Random().nextInt(26));

                // Reemplazar [TEMA] en la plantilla (si es necesario)
                for (XWPFParagraph paragraph : document.getParagraphs()) {
                    String text = paragraph.getText();
                    if (text != null && text.contains("[TEMA]")) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            String runText = run.getText(0);
                            if (runText != null && runText.contains("[TEMA]")) {
                                run.setText(runText.replace("[TEMA]", String.valueOf(tema)), 0);
                            }
                        }
                    }
                }
                // Crear directorio de salida si no existe
                File outputDir = new File("C:/Users/Admin/dev/Generador de Examenes (Java)/src/main/resources/output");
                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                }

                // Guardar el documento DOCX
                String outputWordPath = "C:/Users/Admin/dev/Generador de Examenes (Java)/src/main/resources/output/Examen_Tema_"
                        + tema + ".docx";
                try (FileOutputStream outputWordStream = new FileOutputStream(outputWordPath)) {
                    document.write(outputWordStream);
                }
                // abrir y cerrar ventana de dialogo
                JOptionPane.showMessageDialog(null, "Examen generado exitosamente: Examen_Tema_" + tema + ".docx");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al generar el examen: " + e.getMessage());
            }
        }
    }

    @FXML
    void verExamenes(ActionEvent event) {
        try {
            // Ruta de la carpeta que contiene los exámenes
            String outputDir = "C:/Users/Admin/dev/Generador de Examenes (Java)/src/main/resources/output";
            File directorio = new File(outputDir);

            // Verificar si la carpeta existe
            if (!directorio.exists()) {
                System.out.println("La carpeta de exámenes no existe. Se creará una nueva.");
                directorio.mkdirs();
            }

            // Abrir la carpeta con el explorador de archivos predeterminado del sistema
            Desktop.getDesktop().open(directorio);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al abrir la carpeta de exámenes: " + e.getMessage());
        }
    }

    @FXML
    void initialize() {
        // Inicialización del controlador
        System.out.println("Controlador inicializado");
    }
}
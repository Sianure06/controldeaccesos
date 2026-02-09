/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.controldeaccesos;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.mycompany.controldeaccesos.ui.SplashScreen;
import com.mycompany.controldeaccesos.ui.VentanaPrincipal;

public class Controldeaccesos {

    public static void main(String[] args) {
        // Configura la visualizacion del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Mostrar splash screen y luego la ventana principal
        SwingUtilities.invokeLater(() -> {
            try {
                // Crear ventana principal (pero no mostrarla aún)
                VentanaPrincipal ventana = new VentanaPrincipal();

                // Mostrar splash y luego la ventana principal
                SplashScreen.mostrarYContinuar(ventana);
            } catch (Exception e) {
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Error fatal al iniciar la aplicación:\n" + e.getMessage(),
                        "Error de Inicio",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

package com.mycompany.controldeaccesos.ui;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.*;

public class GestorIcono {
public static void aplicarIcono(Window ventana) { // Cambiar JFrame por Window
    try {
        ImageIcon icono = new ImageIcon(GestorIcono.class.getResource("/icons/lock.png"));
        if (ventana instanceof JFrame) {
            ((JFrame) ventana).setIconImage(icono.getImage());
        } else if (ventana instanceof JDialog) {
            ((JDialog) ventana).setIconImage(icono.getImage());
        }
    } catch (Exception e) {
        System.err.println("Error al cargar icono: " + e.getMessage());
    }
}
}
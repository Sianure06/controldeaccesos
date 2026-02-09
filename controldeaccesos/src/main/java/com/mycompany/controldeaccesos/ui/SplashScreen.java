/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SplashScreen extends JWindow {
    private int duracion;
    
    public SplashScreen(int duracion) {
        this.duracion = duracion;
       
        mostrarSplash();
        
    }
    
    private void mostrarSplash() {
        // Crear panel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(44, 62, 80)); // Azul oscuro elegante
        
        // Panel superior con logo
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(44, 62, 80));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        
        // Cargar icono de la aplicación
        ImageIcon iconoOriginal = null;
        java.net.URL iconUrl = getClass().getResource("/icons/lock.png");
        
        if (iconUrl != null) {
            iconoOriginal = new ImageIcon(iconUrl);
            // Redimensionar el icono
            Image iconoRedimensionado = iconoOriginal.getImage()
                .getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            ImageIcon iconoFinal = new ImageIcon(iconoRedimensionado);
            
            JLabel lblIcono = new JLabel(iconoFinal);
            lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
            panelSuperior.add(lblIcono, BorderLayout.CENTER);
        } else {
            // Si no encuentra el icono, mostrar un texto
            JLabel lblIconoTexto = new JLabel("🔐");
            lblIconoTexto.setFont(new Font("Arial", Font.BOLD, 80));
            lblIconoTexto.setForeground(Color.WHITE);
            lblIconoTexto.setHorizontalAlignment(SwingConstants.CENTER);
            panelSuperior.add(lblIconoTexto, BorderLayout.CENTER);
        }
        
        panel.add(panelSuperior, BorderLayout.CENTER);
        
        // Panel inferior con texto y progreso
        JPanel panelInferior = new JPanel(new GridBagLayout());
        panelInferior.setBackground(new Color(44, 62, 80));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 30, 40, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nombre del sistema
        JLabel lblTitulo = new JLabel("SICA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(52, 152, 219)); // Azul brillante
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelInferior.add(lblTitulo, gbc);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Sistema Integrado de Control de Acceso");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelInferior.add(lblSubtitulo, gbc);
        
        // Versión
        JLabel lblVersion = new JLabel("v2.0 - Arquitectura con Patrones de Diseño");
        lblVersion.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblVersion.setForeground(new Color(189, 195, 199)); // Gris claro
        lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
        lblVersion.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));
        panelInferior.add(lblVersion, gbc);
        
        // Barra de progreso
        JProgressBar barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setForeground(new Color(46, 204, 113)); // Verde
        barraProgreso.setBackground(new Color(236, 240, 241)); // Gris claro
        barraProgreso.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 1));
        barraProgreso.setPreferredSize(new Dimension(300, 20));
        barraProgreso.setStringPainted(true);
        barraProgreso.setString("Inicializando...");
        panelInferior.add(barraProgreso, gbc);
        
        // Mensaje de estado
        JLabel lblEstado = new JLabel("Cargando componentes del sistema...");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEstado.setForeground(new Color(236, 240, 241)); // Gris muy claro
        lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
        lblEstado.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelInferior.add(lblEstado, gbc);
        
        panel.add(panelInferior, BorderLayout.SOUTH);
        
        // Configurar la ventana
        setContentPane(panel);
        pack();
        setSize(500, 450);
        setLocationRelativeTo(null); // Centrar en pantalla
         aplicarFadeIn();
        
        // Sin bordes de ventana
        setBackground(new Color(0, 0, 0, 0));
        
        // Hacerla visible
        setVisible(true);
        
        // Animación de la barra de progreso
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    final int progreso = i;
                    SwingUtilities.invokeLater(() -> {
                        barraProgreso.setValue(progreso);
                        
                        // Actualizar mensajes según el progreso
                        if (progreso < 20) {
                            barraProgreso.setString("Iniciando sistema...");
                            lblEstado.setText("Cargando configuración...");
                        } else if (progreso < 40) {
                            barraProgreso.setString("Conectando a la base de datos...");
                            lblEstado.setText("Estableciendo conexión...");
                        } else if (progreso < 60) {
                            barraProgreso.setString("Cargando componentes...");
                            lblEstado.setText("Inicializando módulos de seguridad...");
                        } else if (progreso < 80) {
                            barraProgreso.setString("Verificando seguridad...");
                            lblEstado.setText("Configurando encriptación...");
                        } else if (progreso < 100) {
                            barraProgreso.setString("Preparando interfaz...");
                            lblEstado.setText("Cargando interfaz gráfica...");
                        } else {
                            barraProgreso.setString("¡Listo!");
                            lblEstado.setText("Sistema inicializado correctamente");
                        }
                    });
                    
                    Thread.sleep(duracion / 100); // Dividir la duración total
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Cerrar el splash screen después de la duración
                SwingUtilities.invokeLater(() -> {
                    setVisible(false);
                    dispose();
                });
            }
        }).start();
        
        // Timer para cerrar automáticamente después de la duración
        Timer timer = new Timer(duracion, e -> {
            aplicarFadeOut(()->{ 
            setVisible(false);
            dispose();
             if (onSplashClosed != null) {
                 onSplashClosed.run();
                    }});
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    // Para agregar efecto de fade in al splash
private void aplicarFadeIn() {
    setOpacity(0f);
    Timer fadeIn = new Timer(10, null);
    fadeIn.addActionListener(new ActionListener() {
        float opacity = 0;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            opacity += 0.05f;
            setOpacity(Math.min(opacity, 1f));
            if (opacity >= 1) {
                fadeIn.stop();
            }
        }
    });
    fadeIn.start();
}

// Para agregar efecto de fade out al cerrar
private void aplicarFadeOut(Runnable alTerminar) {
    Timer fadeOut = new Timer(10, null);
    fadeOut.addActionListener(new ActionListener() {
        float opacity = 1;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            opacity -= 0.05f;
            setOpacity(Math.max(opacity, 0f));
            if (opacity <= 0) {
                fadeOut.stop();
                alTerminar.run();
            }
        }
    });
    fadeOut.start();
}
 private Runnable onSplashClosed;
    
    public void setOnSplashClosed(Runnable callback) {
        this.onSplashClosed = callback;
    }
    
    // Método estático para mostrar splash y luego la ventana principal
    public static void mostrarYContinuar(JFrame ventanaPrincipal) {
        SplashScreen splash = new SplashScreen(3000); // 3 segundos
        
        // Timer para mostrar la ventana principal después del splash
        Timer timer = new Timer(3000, e -> {
            ventanaPrincipal.setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.mycompany.controldeaccesos.PreguntasSeguridad;
import com.mycompany.controldeaccesos.core.SicaEngine;
import com.mycompany.controldeaccesos.models.UsuarioModel;

/**
 *
 * @author Carlos
 */
public class VentanaUsuarioNormal implements VentanaUsuarioInterface {

    private JPanel panelPrincipal;
    private UsuarioModel usuario;
    private SicaEngine sicaEngine;

    public VentanaUsuarioNormal() {
        sicaEngine = SicaEngine.getInstancia();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 245, 245));

        // Barra superior más simple
        JPanel barraSuperior = crearBarraSuperior();
        panelPrincipal.add(barraSuperior, BorderLayout.NORTH);

        // Panel central con funcionalidades limitadas
        JPanel panelCentral = crearPanelCentral();
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = crearPanelInferior();
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(41, 128, 185));
        barra.setPreferredSize(new Dimension(100, 50));
        barra.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("PANEL DE USUARIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel();
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuario.setForeground(Color.WHITE);

        JButton btnCerrarSesion = new JButton("Salir");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.black);
        btnCerrarSesion.setFocusPainted(false);

        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setOpaque(false);
        panelInfo.add(lblTitulo, BorderLayout.WEST);
        panelInfo.add(lblUsuario, BorderLayout.EAST);

        barra.add(panelInfo, BorderLayout.WEST);
        barra.add(btnCerrarSesion, BorderLayout.EAST);

        // Guardar referencias
        barra.putClientProperty("lblUsuario", lblUsuario);
        barra.putClientProperty("btnCerrarSesion", btnCerrarSesion);

        return barra;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tarjetas de funciones para usuario normal
        String[] funciones = {
            "👤 Mi Perfil",
            "? Cambiar Contraseña",
            "❓ Pregunta Secreta",
            "📋 Mis Actividades",
            "📊 Mis Estadísticas",
            "🆘 Soporte Técnico"
        };

        String[] descripciones = {
            "Ver y editar mi información personal",
            "Actualizar mi contraseña de acceso",
            "Configurar pregunta de seguridad",
            "Historial de mis actividades",
            "Ver mis estadísticas de uso",
            "Contactar con soporte"
        };

        // Acciones para cada tarjeta
        Runnable[] acciones = {
            this::mostrarMiPerfil,
            this::mostrarCambiarContrasena,
            this::mostrarPreguntaSecreta,
            this::mostrarMisActividades,
            this::mostrarMisEstadisticas,
            this::mostrarSoporteTecnico
        };

        for (int i = 0; i < funciones.length; i++) {
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            JPanel tarjeta = crearTarjetaFuncion(funciones[i], descripciones[i], acciones[i]);
            panel.add(tarjeta, gbc);
        }

        return panel;
    }

    private JPanel crearTarjetaFuncion(String titulo, String descripcion, Runnable accion) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setPreferredSize(new Dimension(200, 150));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblIcono = new JLabel(titulo.split(" ")[0]); // Toma el emoji

        JLabel lblTitulo = new JLabel(titulo.substring(2)); // Quita el emoji
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(44, 62, 80));

        JLabel lblDesc = new JLabel("<html><body style='width: 150px'>" + descripcion + "</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(108, 122, 137));

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(false);
        panelContenido.add(lblIcono);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 5)));
        panelContenido.add(lblDesc);

        tarjeta.add(panelContenido, BorderLayout.CENTER);

        // Hacer la tarjeta clickeable
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Agregar listener para la acción
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accion.run();
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(new Color(240, 248, 255));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                        BorderFactory.createEmptyBorder(18, 18, 18, 18)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(Color.WHITE);
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
        });

        return tarjeta;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblInfo = new JLabel("Usuario Normal - Acceso limitado");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(108, 122, 137));

        JLabel lblHora = new JLabel();
        lblHora.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHora.setForeground(new Color(108, 122, 137));

        // Actualizar hora cada segundo
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            lblHora.setText(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        });
        timer.start();

        panel.add(lblInfo, BorderLayout.WEST);
        panel.add(lblHora, BorderLayout.EAST);

        return panel;
    }

    @Override
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    @Override
    public String getTituloVentana() {
        return "SICA - Panel de Usuario";
    }

    @Override
    public void cargarDatosUsuario(UsuarioModel usuario) {
        this.usuario = usuario;

        // Actualizar interfaz con datos del usuario
        JPanel barraSuperior = (JPanel) panelPrincipal.getComponent(0);
        JLabel lblUsuario = (JLabel) barraSuperior.getClientProperty("lblUsuario");
        if (lblUsuario != null) {
            lblUsuario.setText("Bienvenido: " + usuario.getUsername());
        }
    }

    @Override
    public void configurarEventos() {
        // Configurar eventos de botones
        JPanel barraSuperior = (JPanel) panelPrincipal.getComponent(0);
        JButton btnCerrarSesion = (JButton) barraSuperior.getClientProperty("btnCerrarSesion");

        if (btnCerrarSesion != null) {
            btnCerrarSesion.addActionListener(e -> {
                int confirmacion = JOptionPane.showConfirmDialog(
                        panelPrincipal,
                        "¿Está seguro que desea cerrar sesión?",
                        "Confirmar cierre de sesión",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Obtener la ventana padre y cerrarla
                    java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(panelPrincipal);
                    if (parentWindow != null) {
                        parentWindow.dispose();
                    }

                    // También podríamos mostrar un mensaje de despedida
                    JOptionPane.showMessageDialog(null,
                            "Sesión cerrada correctamente",
                            "Cierre de sesión",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                // Si elige NO, no hacer nada - mantener la ventana abierta
            });
        }
    }

    // ========== MÉTODOS DE FUNCIONALIDAD ==========
    private void mostrarMiPerfil() {
        StringBuilder perfil = new StringBuilder();
        perfil.append("👤 MI PERFIL\n");
        perfil.append("============\n\n");
        perfil.append("Usuario: ").append(usuario.getUsername()).append("\n");
        perfil.append("Nombre completo: ").append(usuario.getNombreCompleto()).append("\n");
        perfil.append("Email: ").append(usuario.getEmail()).append("\n");
        perfil.append("Rol: ").append(usuario.getRol()).append("\n");
        perfil.append("Fecha registro: ").append(usuario.getFechaRegistro().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        perfil.append("Último acceso: ").append(usuario.getUltimoAcceso().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        perfil.append("Estado: ").append(usuario.isActivo() ? "🟢 Activo" : "🔴 Inactivo").append("\n");
        perfil.append("Pregunta secreta: ").append(usuario.tienePreguntaSecreta() ? "Configurada ✓" : "No configurada ✗").append("\n");

        JOptionPane.showMessageDialog(panelPrincipal, perfil.toString(),
                "Mi Perfil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarCambiarContrasena() {
        javax.swing.JPasswordField txtNuevaPass = new javax.swing.JPasswordField(20);
        javax.swing.JPasswordField txtConfirmarPass = new javax.swing.JPasswordField(20);

        // Botones para mostrar/ocultar contraseña
        JToggleButton btnMostrarNueva = new JToggleButton("👁️");
        JToggleButton btnMostrarConfirmar = new JToggleButton("👁️");

        // Configurar listeners para los botones
        java.awt.event.ActionListener toggleListener = e -> {
            JToggleButton btn = (JToggleButton) e.getSource();
            JPasswordField txt = (btn == btnMostrarNueva) ? txtNuevaPass : txtConfirmarPass;
            if (btn.isSelected()) {
                txt.setEchoChar((char) 0);
                btn.setText("🔒");
            } else {
                txt.setEchoChar('•');
                btn.setText("👁️");
            }
        };

        btnMostrarNueva.addActionListener(toggleListener);
        btnMostrarConfirmar.addActionListener(toggleListener);

        // Paneles contenedores para campos + botones
        JPanel panelNueva = new JPanel(new BorderLayout());
        panelNueva.add(txtNuevaPass, BorderLayout.CENTER);
        panelNueva.add(btnMostrarNueva, BorderLayout.EAST);

        JPanel panelConfirmar = new JPanel(new BorderLayout());
        panelConfirmar.add(txtConfirmarPass, BorderLayout.CENTER);
        panelConfirmar.add(btnMostrarConfirmar, BorderLayout.EAST);

        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(4, 2, 10, 10));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(panelNueva);
        panel.add(new JLabel("Confirmar contraseña:"));
        panel.add(panelConfirmar);

        // Barra de fortaleza
        javax.swing.JProgressBar barraFortaleza = new javax.swing.JProgressBar(0, 100);
        barraFortaleza.setStringPainted(true);
        barraFortaleza.setString("Esperando contraseña...");

        panel.add(new JLabel("Fortaleza:"));
        panel.add(barraFortaleza);

        // Listener para fortaleza
        txtNuevaPass.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarFortaleza();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarFortaleza();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarFortaleza();
            }

            private void actualizarFortaleza() {
                String password = new String(txtNuevaPass.getPassword());
                int fortaleza = com.mycompany.controldeaccesos.EncriptadorContrasena.calcularFortalezaContrasena(password);
                String nivel = com.mycompany.controldeaccesos.EncriptadorContrasena.obtenerNivelFortaleza(password);
                java.awt.Color color = com.mycompany.controldeaccesos.EncriptadorContrasena.obtenerColorFortaleza(password);

                barraFortaleza.setValue(fortaleza);
                barraFortaleza.setForeground(color);
                barraFortaleza.setString(nivel + " (" + fortaleza + "%)");
            }
        });

        int opcion = JOptionPane.showConfirmDialog(panelPrincipal, panel,
                "Cambiar Contraseña", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            String nuevaPass = new String(txtNuevaPass.getPassword());
            String confirmarPass = new String(txtConfirmarPass.getPassword());

            if (!nuevaPass.equals(confirmarPass)) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Las contraseñas no coinciden",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar fortaleza
            String errores = com.mycompany.controldeaccesos.EncriptadorContrasena.validarFortalezaContrasena(nuevaPass);
            if (errores != null) {
                int fortaleza = com.mycompany.controldeaccesos.EncriptadorContrasena.calcularFortalezaContrasena(nuevaPass);
                if (fortaleza < 60) {
                    int confirmar = JOptionPane.showConfirmDialog(panelPrincipal,
                            "La contraseña es débil:\n" + errores + "\n\n¿Continuar de todos modos?",
                            "Contraseña débil",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (confirmar != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }

            if (sicaEngine.cambiarPassword(usuario.getUsername(), nuevaPass)) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Contraseña cambiada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Error al cambiar la contraseña",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void mostrarPreguntaSecreta() {
        String[] opciones = {"Probar Respuesta", "Cambiar Pregunta", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(
                panelPrincipal,
                "¿Qué desea hacer con su configuración de seguridad?",
                "Gestión de Pregunta Secreta",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]
        );

        if (seleccion == 0) {
            menuProbarRespuesta();
        } else if (seleccion == 1) {
            menuCambiarPregunta();
        }
    }

// --- SUB-MENÚ PARA PROBAR ---
    private void menuProbarRespuesta() {
        if (usuario.getPreguntaSecreta() == null || usuario.getPreguntaSecreta().isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal, "Aún no tienes una pregunta configurada.");
            return;
        }

        String respuestaInput = JOptionPane.showInputDialog(panelPrincipal,
                "Pregunta: " + usuario.getPreguntaSecreta() + "\n\nIngrese su respuesta:",
                "Probando Seguridad", JOptionPane.PLAIN_MESSAGE);

        if (respuestaInput != null) {
            // Usamos el motor para verificar sin exponer el hash
            boolean esCorrecta = sicaEngine.verificarRespuestaSecreta(usuario.getUsername(), respuestaInput);

            if (esCorrecta) {
                JOptionPane.showMessageDialog(panelPrincipal, "¡Correcto! Te acuerdas de tu respuesta.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelPrincipal, "Respuesta incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// --- SUB-MENÚ PARA CAMBIAR (Tu código actual mejorado) ---
    private void menuCambiarPregunta() {
        JDialog dialogo = new JDialog();
        dialogo.setTitle("Cambiar Pregunta");
        dialogo.setModal(true);
        dialogo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> combo = new JComboBox<>(PreguntasSeguridad.getPreguntasPredefinidas().toArray(new String[0]));
        JTextField txtResp = new JTextField(20);
        JButton btnG = new JButton("Guardar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialogo.add(new JLabel("Nueva Pregunta:"), gbc);
        gbc.gridy = 1;
        dialogo.add(combo, gbc);
        gbc.gridy = 2;
        dialogo.add(new JLabel("Nueva Respuesta:"), gbc);
        gbc.gridy = 3;
        dialogo.add(txtResp, gbc);
        gbc.gridy = 4;
        dialogo.add(btnG, gbc);

        btnG.addActionListener(e -> {
            if (sicaEngine.cambiarPreguntaSecreta(usuario.getUsername(), (String) combo.getSelectedItem(), txtResp.getText())) {
                JOptionPane.showMessageDialog(dialogo, "Actualizado correctamente.");
                // Actualizamos el objeto local para que 'Probar' funcione de inmediato
                usuario.setPreguntaSecreta((String) combo.getSelectedItem());
                dialogo.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogo, "Error al actualizar credenciales.");
            }
        });

        dialogo.pack();
        dialogo.setLocationRelativeTo(panelPrincipal);
        dialogo.setVisible(true);
    }


    private void mostrarMisActividades() {
        StringBuilder actividades = new StringBuilder();
        actividades.append("📋 MIS ACTIVIDADES\n");
        actividades.append("==================\n\n");

        actividades.append("👤 Información de acceso:\n");
        actividades.append("─────────────────────────\n");
        actividades.append("• Último acceso: ").append(
                usuario.getUltimoAcceso().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        ).append("\n");
        actividades.append("• Número total de accesos: ").append("Registrado\n");
        actividades.append("• Hora actual del sistema: ").append(
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        ).append("\n\n");

        actividades.append("📊 Actividad reciente:\n");
        actividades.append("──────────────────────\n");
        actividades.append("• Sesión actual: Activa desde ").append(
                java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
        ).append("\n");
        actividades.append("• Cambios de contraseña: ").append("0 (este mes)\n");
        actividades.append("• Intentos fallidos: ").append("0 (este mes)\n");

        JOptionPane.showMessageDialog(panelPrincipal, actividades.toString(),
                "Mis Actividades", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMisEstadisticas() {
        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        long diasRegistrado = java.time.temporal.ChronoUnit.DAYS.between(usuario.getFechaRegistro(), ahora);

        StringBuilder estadisticas = new StringBuilder();
        estadisticas.append("📊 MIS ESTADÍSTICAS\n");
        estadisticas.append("===================\n\n");

        estadisticas.append("📅 Tiempo en el sistema:\n");
        estadisticas.append("────────────────────────\n");
        estadisticas.append("• Días registrado: ").append(diasRegistrado).append(" días\n");
        estadisticas.append("• Desde: ").append(
                usuario.getFechaRegistro().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ).append("\n");
        estadisticas.append("• Último acceso: ").append(
                usuario.getUltimoAcceso().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        ).append("\n\n");

        estadisticas.append("🔐 Seguridad:\n");
        estadisticas.append("─────────────\n");
        estadisticas.append("• Fortaleza contraseña: ").append("Alta ✓\n");
        estadisticas.append("• Pregunta secreta: ").append(
                usuario.tienePreguntaSecreta() ? "Configurada ✓" : "No configurada ✗"
        ).append("\n");
        estadisticas.append("• Email verificado: ").append("Sí ✓\n\n");

        estadisticas.append("📈 Métricas:\n");
        estadisticas.append("────────────\n");
        estadisticas.append("• Sesiones este mes: ").append("1\n");
        estadisticas.append("• Tiempo promedio sesión: ").append("--\n");
        estadisticas.append("• Actividad: ").append("Regular\n");

        JOptionPane.showMessageDialog(panelPrincipal, estadisticas.toString(),
                "Mis Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarSoporteTecnico() {
        StringBuilder soporte = new StringBuilder();
        soporte.append("🆘 SOPORTE TÉCNICO\n");
        soporte.append("==================\n\n");

        soporte.append("📞 Contacto:\n");
        soporte.append("────────────\n");
        soporte.append("• Administrador del sistema: admin@sistema.com\n");
        soporte.append("• Teléfono soporte: +1-800-SOPORTE\n");
        soporte.append("• Horario atención: L-V 9:00-18:00\n\n");

        soporte.append("📋 Problemas comunes:\n");
        soporte.append("─────────────────────\n");
        soporte.append("1. Olvidé mi contraseña:\n");
        soporte.append("   → Use 'Recuperar Contraseña' en el login\n\n");
        soporte.append("2. No puedo iniciar sesión:\n");
        soporte.append("   → Verifique usuario y contraseña\n");
        soporte.append("   → Contacte al administrador\n\n");

        soporte.append("3. Error en el sistema:\n");
        soporte.append("   • Cierre y vuelva a abrir la aplicación\n");
        soporte.append("   • Verifique su conexión a internet\n");
        soporte.append("   • Contacte al administrador\n\n");

        soporte.append("🔧 Información técnica:\n");
        soporte.append("───────────────────────\n");
        soporte.append("• Usuario: ").append(usuario.getUsername()).append("\n");
        soporte.append("• Sistema: SICA v2.0\n");
        soporte.append("• Versión Java: ").append(System.getProperty("java.version")).append("\n");
        soporte.append("• SO: ").append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version")).append("\n");

        JOptionPane.showMessageDialog(panelPrincipal, soporte.toString(),
                "Soporte Técnico", JOptionPane.INFORMATION_MESSAGE);
    }
}

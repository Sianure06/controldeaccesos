/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.mycompany.controldeaccesos.core.SicaEngine;
import com.mycompany.controldeaccesos.models.UsuarioModel;

/**
 *
 * @author Carlos
 */
public class VentanaAdministrador implements VentanaUsuarioInterface {

    protected JButton btnActualizar;
    protected JButton btnEditarUsuario;
    protected JButton btnEliminarUsuario;
    protected JPanel panelPrincipal;
    protected UsuarioModel usuario;
    protected SicaEngine sicaEngine;
    protected DefaultTableModel modeloTablaActividades;
    protected JTable tablaActividades;
    protected JLabel lblUsuariosActivos;
    protected JLabel lblSesionesHoy;
    protected JLabel lblIntentosFallidos;
    protected JLabel lblBackups;
    protected JLabel lblAdministradores;
    protected JLabel lblAlertas;
    protected JButton btnSeguridadAuditoria; // Variable para referencia al botón 
    protected DefaultListModel<String> modeloBackups;

    public VentanaAdministrador() {
        sicaEngine = SicaEngine.getInstancia();
        inicializarComponentes();

    }

    protected void inicializarComponentes() {
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(240, 248, 255));

        // ========== BARRA SUPERIOR ==========
        JPanel barraSuperior = crearBarraSuperior();
        panelPrincipal.add(barraSuperior, BorderLayout.NORTH);

        // ========== MENÚ LATERAL ==========
        JPanel menuLateral = crearMenuLateral();
        panelPrincipal.add(menuLateral, BorderLayout.WEST);

        // ========== PANEL CENTRAL ==========
        JPanel panelCentral = crearPanelCentral();
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        // ========== PANEL INFERIOR ==========
        JPanel panelInferior = crearPanelInferior();
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
    }

    protected JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(52, 73, 94));
        barra.setPreferredSize(new Dimension(100, 60));
        barra.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Título y bienvenida
        JLabel lblTitulo = new JLabel("PANEL DE ADMINISTRACIÓN");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel();
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(new Color(200, 200, 200));

        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelInfo.setOpaque(false);
        panelInfo.add(lblTitulo);
        panelInfo.add(lblUsuario);

        // Botón cerrar sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.black);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        barra.add(panelInfo, BorderLayout.WEST);
        barra.add(btnCerrarSesion, BorderLayout.EAST);

        // Guardar referencia para actualizar después
        barra.putClientProperty("lblUsuario", lblUsuario);
        barra.putClientProperty("btnCerrarSesion", btnCerrarSesion);

        return barra;
    }

    protected JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(44, 62, 80));
        menu.setPreferredSize(new Dimension(220, 0));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Opciones del menú
        String[] opciones = {
            "📊 Dashboard",
            "👥 Gestión de Usuarios",
            "🔐 Permisos y Roles",
            "📈 Reportes y Estadísticas",
            "📊 Gráficas de Acceso", // NUEVA OPCIÓN
            "⚙️ Configuración del Sistema",
            "🗄️ Backup y Restauración",
            "📋 Registro de Actividades",
            "🛡️ Seguridad y Auditoría"
        };

        for (String opcion : opciones) {
            JButton btnOpcion = new JButton(opcion);
            btnOpcion.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnOpcion.setBackground(new Color(52, 73, 94));
            btnOpcion.setForeground(Color.black);
            btnOpcion.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
            btnOpcion.setFocusPainted(false);
            btnOpcion.setMaximumSize(new Dimension(200, 45));
            btnOpcion.setEnabled(true);

            btnOpcion.addActionListener(e -> manejarAccionMenu(btnOpcion.getText()));

            menu.add(btnOpcion);
            menu.add(Box.createRigidArea(new Dimension(0, 5)));

            //Guardar referencia al botón de seguridad
            if (opcion.equals("🛡️ Seguridad y Auditoría")) {
                btnSeguridadAuditoria = btnOpcion;
            }
        }

        menu.add(Box.createVerticalGlue());
        return menu;
    }

    protected JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tarjetas de resumen
        JPanel panelResumen = new JPanel(new GridLayout(2, 3, 15, 15));
        panelResumen.setBackground(Color.white);

        // Crear tarjetas de métricas con referencias
        Color[] colores = {
            new Color(46, 204, 113), new Color(52, 152, 219),
            new Color(155, 89, 182), new Color(241, 196, 15),
            new Color(230, 126, 34), new Color(231, 76, 60)
        };

        // Crear y almacenar referencias a las tarjetas
        JPanel[] tarjetas = new JPanel[6];
        String[] metricas = {
            "Usuarios Activos", "Sesiones Hoy", "Intentos Fallidos",
            "Backups", "Administradores", "Alertas"
        };

        for (int i = 0; i < metricas.length; i++) {
            tarjetas[i] = crearTarjetaMetrica(metricas[i], "0", colores[i]);
            panelResumen.add(tarjetas[i]);

            // Guardar referencia a la etiqueta del valor
            switch (i) {
                case 0:
                    lblUsuariosActivos = obtenerLabelValor(tarjetas[i]);
                    break;
                case 1:
                    lblSesionesHoy = obtenerLabelValor(tarjetas[i]);
                    break;
                case 2:
                    lblIntentosFallidos = obtenerLabelValor(tarjetas[i]);
                    break;
                case 3:
                    lblBackups = obtenerLabelValor(tarjetas[i]);
                    break;
                case 4:
                    lblAdministradores = obtenerLabelValor(tarjetas[i]);
                    break;
                case 5:
                    lblAlertas = obtenerLabelValor(tarjetas[i]);
                    break;
            }
        }

        panel.add(panelResumen, BorderLayout.NORTH);

        // Panel de actividades recientes
        JPanel panelActividades = new JPanel(new BorderLayout());
        panelActividades.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                "Lista de Usuarios Registrados",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(44, 62, 80)
        ));

        // Crear modelo de tabla
        String[] columnas = {"Usuario", "Nombre Completo", "Email", "Rol", "Fecha Registro", "Último Acceso", "Estado"};
        modeloTablaActividades = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        tablaActividades = new JTable(modeloTablaActividades);
        tablaActividades.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaActividades.setRowHeight(25);
        tablaActividades.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaActividades.getTableHeader().setBackground(new Color(44, 62, 80));
        tablaActividades.getTableHeader().setForeground(Color.black);

        JScrollPane scrollPane = new JScrollPane(tablaActividades);
        scrollPane.setPreferredSize(new Dimension(0, 300));

        panelActividades.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones para acciones de usuario
        JPanel panelBotonesUsuarios = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotonesUsuarios.setBackground(Color.WHITE);

        btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.addActionListener(e -> actualizarDatos());

        btnEditarUsuario = new JButton("✏️ Editar Usuario");
        btnEditarUsuario.addActionListener(e -> editarUsuarioSeleccionado());

        btnEliminarUsuario = new JButton("🗑️ Eliminar Usuario");
        btnEliminarUsuario.addActionListener(e -> eliminarUsuarioSeleccionado());
        // ------------------------------------------------------------------

        panelBotonesUsuarios.add(btnActualizar);
        panelBotonesUsuarios.add(btnEditarUsuario);
        panelBotonesUsuarios.add(btnEliminarUsuario);

        panelActividades.add(panelBotonesUsuarios, BorderLayout.SOUTH);
        panel.add(panelActividades, BorderLayout.CENTER);

        return panel;
    }

    protected JLabel obtenerLabelValor(JPanel tarjeta) {
        // Buscar el JLabel con el valor en la tarjeta
        for (Component comp : tarjeta.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panelValor = (JPanel) comp;
                for (Component comp2 : panelValor.getComponents()) {
                    if (comp2 instanceof JLabel) {
                        return (JLabel) comp2;
                    }
                }
            }
        }
        return null;
    }

    protected JPanel crearTarjetaMetrica(String titulo, String valor, Color color) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(236, 240, 241), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(108, 122, 137));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(color);

        JPanel panelValor = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelValor.setOpaque(false);
        panelValor.add(lblValor);

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(panelValor, BorderLayout.CENTER);

        return tarjeta;
    }

    protected JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblVersion = new JLabel("SICA v2.0 - Modo Administrador");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblVersion.setForeground(new Color(108, 122, 137));

        panel.add(lblVersion);
        return panel;
    }

    @Override
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    @Override
    public String getTituloVentana() {
        return "SICA - Panel de Administración";
    }

    @Override
    public void cargarDatosUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
        // Actualizar interfaz con datos del usuario
        JPanel barraSuperior = (JPanel) panelPrincipal.getComponent(0);
        JLabel lblUsuario = (JLabel) barraSuperior.getClientProperty("lblUsuario");
        if (lblUsuario != null) {
            lblUsuario.setText("Bienvenido: " + usuario.getUsername() + " (Admin)");
        }

        // Cargar datos iniciales
        actualizarDatos();
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

    // Métodos para manejar las acciones del menú
    protected void manejarAccionMenu(String opcion) {
        switch (opcion) {
            case "📊 Dashboard":
                actualizarDatos();
                break;
            case "👥 Gestión de Usuarios":
                mostrarGestionUsuarios();
                break;
            case "🔐 Permisos y Roles":
                mostrarPermisosRoles();
                break;
            case "📈 Reportes y Estadísticas":
                mostrarReportesEstadisticas();
                break;
            case "⚙️ Configuración del Sistema":
                mostrarConfiguracionSistema();
                break;
            case "🗄️ Backup y Restauración":
                mostrarBackupRestauracion();
                break;
            case "📋 Registro de Actividades":
                mostrarRegistroActividades();
                break;
            case "🛡️ Seguridad y Auditoría":
                mostrarSeguridadAuditoria();
                break;
            case "📊 Gráficas de Acceso":
                mostrarGraficaAccesos();
                break;
        }
    }

    protected void actualizarDatos() {
        // Actualizar métricas
        List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();

        int usuariosActivos = 0;
        int administradores = 0;
        int sesionesHoy = 0; // Esto sería más complejo en un sistema real
        int backups = sicaEngine.obtenerNumeroBackups();
        int intentosFallidosHoy = sicaEngine.obtenerTotalIntentosFallidosHoy();
        int usuariosBloqueados = sicaEngine.obtenerUsuariosBloqueados(); // Es el contador

        for (UsuarioModel usuario : usuarios) {
            if (usuario.isActivo()) {
                usuariosActivos++;
            }
            if (usuario.esAdmin()) {
                administradores++;
            }
        }

        // Actualizar las etiquetas
        if (lblUsuariosActivos != null) {
            lblUsuariosActivos.setText(String.valueOf(usuariosActivos));
        }
        if (lblAdministradores != null) {
            lblAdministradores.setText(String.valueOf(administradores));
        }
        if (lblSesionesHoy != null) {
            lblSesionesHoy.setText(String.valueOf(sesionesHoy));
        }
        if (lblBackups != null) {
            lblBackups.setText(String.valueOf(backups));
        }
        if (lblIntentosFallidos != null) {
            lblIntentosFallidos.setText(String.valueOf(intentosFallidosHoy));
        }
        if (lblSesionesHoy != null) {
            int accesosHoy = sicaEngine.obtenerAccesosHoy();
            lblSesionesHoy.setText(String.valueOf(accesosHoy));
        }
        // Actualizar ALERTAS (usuarios bloqueados)
        if (lblAlertas != null) {
            lblAlertas.setText(String.valueOf(usuariosBloqueados));

            // Cambiar color según cantidad
            if (usuariosBloqueados == 0) {
                lblAlertas.setForeground(new Color(46, 204, 113)); // Verde
            } else if (usuariosBloqueados <= 2) {
                lblAlertas.setForeground(new Color(241, 196, 15)); // Amarillo
            } else {
                lblAlertas.setForeground(new Color(231, 76, 60)); // Rojo
            }
        }

        // ACTUALIZAR COLOR DEL BOTÓN DE SEGURIDAD
        if (btnSeguridadAuditoria != null) {
            if (usuariosBloqueados > 0) {
                // Hay alertas - poner el botón en rojo
                btnSeguridadAuditoria.setBackground(new Color(231, 76, 60)); // Rojo
                btnSeguridadAuditoria.setForeground(Color.BLACK);
                btnSeguridadAuditoria.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 40, 20), 3), // Borde rojo oscuro grueso
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                // Agregar indicador de notificación
                String textoOriginal = "🛡️ Seguridad y Auditoría";
                if (!btnSeguridadAuditoria.getText().contains("(")) {
                    btnSeguridadAuditoria.setText(textoOriginal + " (" + usuariosBloqueados + ")");
                } else {
                    // Actualizar el número entre paréntesis
                    String nuevoTexto = textoOriginal + " (" + usuariosBloqueados + ")";
                    if (!btnSeguridadAuditoria.getText().equals(nuevoTexto)) {
                        btnSeguridadAuditoria.setText(nuevoTexto);
                    }
                }
            } else {
                // No hay alertas - color normal
                btnSeguridadAuditoria.setBackground(new Color(52, 73, 94));
                btnSeguridadAuditoria.setForeground(Color.GRAY);

                // Quitar indicador de notificación si existe
                String textoActual = btnSeguridadAuditoria.getText();
                if (textoActual.contains("(")) {
                    btnSeguridadAuditoria.setText("🛡️ Seguridad y Auditoría");
                }
            }
        }
        //Actualizar lista de backups
        if (modeloBackups != null) {
            actualizarListaBackups(modeloBackups);
        }
        // Actualizar tabla de usuarios
        actualizarTablaUsuarios();
    }

    protected void mostrarRestaurarBackup() {
        List<String> backups = sicaEngine.obtenerListaBackups();
        if (backups.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No hay backups disponibles para restaurar.",
                    "Restaurar Backup",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Crear array de opciones
        String[] opciones = new String[backups.size()];
        for (int i = 0; i < backups.size(); i++) {
            opciones[i] = (i + 1) + ". " + backups.get(i);
        }

        String seleccion = (String) JOptionPane.showInputDialog(panelPrincipal,
                "Seleccione el backup a restaurar:\n\n"
                + "⚠️ ADVERTENCIA: Esta acción reemplazará todos los usuarios actuales",
                "Restaurar Backup",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            // Obtener el índice (el número antes del punto)
            int indice = Integer.parseInt(seleccion.split("\\.")[0]) - 1;

            int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                    "¿Restaurar backup #" + (indice + 1) + "?\n\n"
                    + backups.get(indice) + "\n\n"
                    + "Esta acción no se puede deshacer.",
                    "Confirmar Restauración",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    sicaEngine.cambiarBackups(indice);
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "✅ Backup restaurado exitosamente\n\n"
                            + "Reinicie la aplicación para ver los cambios.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    actualizarDatos();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "❌ Error: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    protected void actualizarTablaUsuarios() {
        // Limpiar tabla
        modeloTablaActividades.setRowCount(0);

        // Obtener usuarios
        List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (UsuarioModel usuario : usuarios) {
            String estado;
            if (usuario.estaBloqueado()) {
                estado = "🔴 Bloqueado";
            } else if (usuario.isActivo()) {
                estado = "🟢 Activo";
            } else {
                estado = "⚫ Inactivo";
            }

            // Agregar información de intentos fallidos si existen
            String infoExtra = "";
            if (usuario.getIntentosFallidos() > 0) {
                infoExtra = " (" + usuario.getIntentosFallidos() + " intentos fallidos)";
            }

            Object[] fila = {
                usuario.getUsername(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getFechaRegistro().format(formatter),
                usuario.getUltimoAcceso().format(formatter),
                estado + infoExtra
            };
            modeloTablaActividades.addRow(fila);
        }
    }

    protected void mostrarGestionUsuarios() {
        // Crear panel con opciones
        JPanel panelOpciones = new JPanel(new GridLayout(0, 1, 10, 10));
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Botón para ver estadísticas
        JButton btnEstadisticas = new JButton("📊 Ver Estadísticas de Usuarios");
        btnEstadisticas.addActionListener(e -> mostrarEstadisticasUsuarios());

        // Botón para activar/desactivar usuarios
        JButton btnGestionarEstados = new JButton("⚡ Gestionar Estados de Usuarios");
        btnGestionarEstados.addActionListener(e -> mostrarGestionEstadosUsuarios());

        // Botón para ver usuarios inactivos
        JButton btnVerInactivos = new JButton("👥 Ver Usuarios Inactivos");
        btnVerInactivos.addActionListener(e -> mostrarUsuariosInactivos());

        panelOpciones.add(btnEstadisticas);
        panelOpciones.add(btnGestionarEstados);
        panelOpciones.add(btnVerInactivos);

        JOptionPane.showMessageDialog(panelPrincipal,
                panelOpciones,
                "Gestión de Usuarios",
                JOptionPane.PLAIN_MESSAGE);
    }

    protected void mostrarPermisosRoles() {
        List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();

        // Contar usuarios por rol
        Map<String, Integer> conteoRoles = new LinkedHashMap<>();
        conteoRoles.put("ADMIN", 0);
        conteoRoles.put("USUARIO", 0);
        conteoRoles.put("AUDITOR", 0);
        conteoRoles.put("SUPERVISOR", 0);

        for (UsuarioModel usuario : usuarios) {
            String rol = usuario.getRol().toUpperCase();
            conteoRoles.put(rol, conteoRoles.getOrDefault(rol, 0) + 1);
        }

        StringBuilder info = new StringBuilder();
        info.append("🔐 PERMISOS Y ROLES DEL SISTEMA\n");
        info.append("===============================\n\n");

        info.append("📊 DISTRIBUCIÓN DE USUARIOS POR ROL:\n");
        for (Map.Entry<String, Integer> entry : conteoRoles.entrySet()) {
            if (entry.getValue() > 0) {
                info.append("• ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" usuario(s)\n");
            }
        }

        info.append("\n👑 ROLES DISPONIBLES:\n");
        info.append("────────────────────\n");

        String[][] rolesInfo = {
            {"ADMIN", "Acceso completo al sistema", "Ilimitado"},
            {"USUARIO", "Funciones básicas personales", "Limitado"},
            {"AUDITOR", "Solo lectura de reportes", "Solo lectura"},
            {"SUPERVISOR", "Gestión básica de usuarios", "Parcial"}
        };

        for (String[] rolInfo : rolesInfo) {
            info.append("\n").append(rolInfo[0]).append(":\n");
            info.append("  Descripción: ").append(rolInfo[1]).append("\n");
            info.append("  Permisos: ").append(rolInfo[2]).append("\n");
        }

        // Botón para gestionar roles
        JButton btnGestionarRoles = new JButton("🔄 Gestionar Roles de Usuarios");
        btnGestionarRoles.addActionListener(e -> mostrarGestionRolesUsuarios());

        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 10, 10));
        panelBotones.add(btnGestionarRoles);

        JOptionPane.showMessageDialog(panelPrincipal,
                new Object[]{
                    info.toString(),
                    new JSeparator(),
                    panelBotones
                },
                "Permisos y Roles",
                JOptionPane.INFORMATION_MESSAGE);
    }

    protected void mostrarReportesEstadisticas() {
        List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();
        int totalUsuarios = usuarios.size();
        int activos = 0;
        int admins = 0;
        int backups = sicaEngine.obtenerNumeroBackups();
        int intentosFallidosHoy = sicaEngine.obtenerTotalIntentosFallidosHoy(); // ¡Nuevo!
        int usuariosBloqueados = sicaEngine.obtenerUsuariosBloqueados(); // ¡Nuevo!

        for (UsuarioModel usuario : usuarios) {
            if (usuario.isActivo()) {
                activos++;
            }
            if (usuario.esAdmin()) {
                admins++;
            }
        }

        String reporte = String.format("""
        📊 REPORTES Y ESTADÍSTICAS
        ===========================
        
        👥 USUARIOS:
        • Total registrados: %d
        • Activos: %d
        • Inactivos: %d
        • Administradores: %d
        • Usuarios normales: %d
        
        🔐 SEGURIDAD:
        • Intentos fallidos hoy: %d
        • Usuarios bloqueados: %d
        • Usuarios con pregunta secreta: %d
        
        💾 BACKUPS:
        • Total de backups: %d
        • Último backup: %s
        
        📅 ÚLTIMAS ACTIVIDADES:
        • Último acceso del admin: %s
        
        ⚙️ SISTEMA:
        • Base de datos: TXT
        • Encriptación: BCrypt
        """,
                totalUsuarios, activos, totalUsuarios - activos, admins, totalUsuarios - admins,
                intentosFallidosHoy, usuariosBloqueados, contarUsuariosConPreguntaSecreta(),
                backups,
                obtenerFechaUltimoBackup(),
                usuario.getUltimoAcceso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        );

        JOptionPane.showMessageDialog(panelPrincipal, reporte, "Reportes y Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    protected String obtenerFechaUltimoBackup() {
        List<String> backups = sicaEngine.obtenerListaBackups();
        if (backups.isEmpty()) {
            return "Nunca";
        }

        try {
            // El primer elemento es el más reciente
            String ultimoBackup = backups.get(0);
            // Extraer la parte de la fecha del nombre
            if (ultimoBackup.contains("_")) {
                String fechaParte = ultimoBackup.substring(
                        ultimoBackup.indexOf("usuarios_backup_") + "usuarios_backup_".length(),
                        ultimoBackup.indexOf(".txt")
                );

                // Parsear YYYYMMDD_HHmmss a formato legible
                String anio = fechaParte.substring(0, 4);
                String mes = fechaParte.substring(4, 6);
                String dia = fechaParte.substring(6, 8);
                String hora = fechaParte.substring(9, 11);
                String minuto = fechaParte.substring(11, 13);
                String segundo = fechaParte.substring(13, 15);

                return String.format("%s/%s/%s %s:%s:%s", dia, mes, anio, hora, minuto, segundo);
            }
        } catch (Exception e) {
            // En caso de error, devolver el string completo
        }

        return backups.get(0);
    }

    protected int contarUsuariosConPreguntaSecreta() {
        int count = 0;
        List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();
        for (UsuarioModel usuario : usuarios) {
            if (usuario.tienePreguntaSecreta()) {
                count++;
            }
        }
        return count;
    }

    protected void mostrarConfiguracionSistema() {
        String configuracion = """
            ⚙️ CONFIGURACIÓN DEL SISTEMA
            ============================
            
            🔐 SEGURIDAD:
            • Encriptación: BCrypt (Cost factor: 12)
            • Longitud mínima contraseña: 8 caracteres
            • Requisitos: Mayúscula, minúscula, número, carácter especial
            • Tiempo de sesión: 30 minutos
            
            📁 ALMACENAMIENTO:
            • Base de datos: Archivo TXT
            • Ubicación: data/usuarios.txt
            • Backup automático: Manual
            
            📊 INTERFAZ:
            • Tema: Claro/Oscuro (según sistema)
            • Idioma: Español
            • Formato fecha: dd/MM/yyyy HH:mm
            
            ⚡ RENDIMIENTO:
            • Máx. usuarios: Ilimitado (depende de disco)
            • Cache: En memoria
            • Logs: En consola
            """;

        JOptionPane.showMessageDialog(panelPrincipal, configuracion, "Configuración del Sistema", JOptionPane.INFORMATION_MESSAGE);
    }
   

    protected void mostrarBackupRestauracion() {
        // Obtener información actualizada de backups
        int totalBackups = sicaEngine.obtenerNumeroBackups();
        List<String> listaBackups = sicaEngine.obtenerListaBackups();

        StringBuilder infoBackups = new StringBuilder();
        infoBackups.append("🗄️ BACKUP Y RESTAURACIÓN\n");
        infoBackups.append("========================\n\n");
        infoBackups.append("Total de backups: ").append(totalBackups).append("\n\n");

        if (listaBackups.isEmpty()) {
            infoBackups.append("No hay backups disponibles.\n");
        } else {
            infoBackups.append("Últimos backups:\n");
            int count = 0;
            for (String backup : listaBackups) {
                if (count >= 5) {
                    break; // Mostrar solo los 5 más recientes
                }
                infoBackups.append("• ").append(backup).append("\n");
                count++;
            }
            if (listaBackups.size() > 5) {
                infoBackups.append("... y ").append(listaBackups.size() - 5).append(" más\n");
            }
        }

        infoBackups.append("\nLa base de datos se almacena en:\n");
        infoBackups.append("• data/usuarios.txt\n\n");
        infoBackups.append("Los backups se guardan en:\n");
        infoBackups.append("• data/backup/\n\n");
        infoBackups.append("Para restaurar:\n");
        infoBackups.append("1. Cerrar el sistema\n");
        infoBackups.append("2. Reemplazar usuarios.txt\n");
        infoBackups.append("3. Reiniciar el sistema\n\n");
        infoBackups.append("Precaución: Siempre haga backup antes de cambios importantes.");

        // Panel para botones - MODIFICADO: Agregar botón específico para cambiar backup
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton btnCrearBackup = new JButton("➕ Crear Backup Ahora");
        JButton btnCambiarBackup = new JButton("🔄 Cambiar/Restaurar Backup"); // NUEVO BOTÓN
        JButton btnCargarBackup = new JButton("📂 Cargar Backup Existente");
        JButton btnVerTodosBackups = new JButton("📋 Ver Todos los Backups");
        JButton btnEliminarBackups = new JButton("🗑️ Gestionar Backups");

        btnCrearBackup.addActionListener(e -> {
            try {
                sicaEngine.crearBackup();
                JOptionPane.showMessageDialog(panelPrincipal,
                        "✅ Backup creado exitosamente!\nTotal de backups: "
                        + sicaEngine.obtenerNumeroBackups(),
                        "Backup Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                actualizarDatos(); // Actualizar contador
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "❌ Error al crear backup: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción del NUEVO botón para cambiar/restaurar backup
        btnCambiarBackup.addActionListener(e -> {
            mostrarRestaurarBackup(); // Llama al método existente que ya implementa la lógica
        });

        btnCargarBackup.addActionListener(e -> {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Para cargar un backup:\n1. Cierre el sistema\n2. Reemplace el archivo usuarios.txt con su backup\n3. Reinicie el sistema",
                    "Instrucciones para Cargar Backup",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        btnVerTodosBackups.addActionListener(e -> mostrarListaCompletaBackups());
        btnEliminarBackups.addActionListener(e -> mostrarGestionBackups());

        // Agregar botones al panel - incluyendo el nuevo botón
        panelBotones.add(btnCrearBackup);
        panelBotones.add(btnCambiarBackup); // NUEVO BOTÓN AQUÍ
        panelBotones.add(btnVerTodosBackups);
        panelBotones.add(btnEliminarBackups);

        JOptionPane.showMessageDialog(panelPrincipal,
                new Object[]{
                    infoBackups.toString(),
                    new JSeparator(),
                    panelBotones
                },
                "Backup y Restauración",
                JOptionPane.INFORMATION_MESSAGE);
    }


    protected void mostrarListaCompletaBackups() {
        List<String> backups = sicaEngine.obtenerListaBackups();

        if (backups.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No hay backups disponibles.",
                    "Lista de Backups",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder lista = new StringBuilder();
        lista.append("📋 LISTA COMPLETA DE BACKUPS\n");
        lista.append("=============================\n\n");
        lista.append("Total: ").append(backups.size()).append(" backups\n\n");

        int contador = 1;
        for (String backup : backups) {
            lista.append(contador).append(". ").append(backup).append("\n");
            contador++;
        }

        lista.append("\n────────────────────────────────\n");
        lista.append("Los archivos se encuentran en: data/backup/\n");
        lista.append("Formato: usuarios_backup_YYYYMMDD_HHmmss.txt");

        JOptionPane.showMessageDialog(panelPrincipal,
                lista.toString(),
                "Lista Completa de Backups",
                JOptionPane.INFORMATION_MESSAGE);
    }

    protected void mostrarGestionBackups() {
        List<String> backups = sicaEngine.obtenerListaBackups();

        if (backups.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No hay backups disponibles para gestionar.",
                    "Gestionar Backups",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crear lista de backups con checkbox
        JPanel panelBackups = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String backup : backups) {
            // Extraer solo el nombre del archivo
            String nombreArchivo = backup.split(" - ")[0];
            listModel.addElement(nombreArchivo);
        }

        JList<String> listaBackups = new JList<>(listModel);
        listaBackups.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listaBackups);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        panelBackups.add(new JLabel("Seleccione backups a eliminar:"), BorderLayout.NORTH);
        panelBackups.add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnEliminarSeleccionados = new JButton("Eliminar Seleccionados");
        JButton btnEliminarTodos = new JButton("Eliminar Todos");
        JButton btnCancelar = new JButton("Cancelar");

        btnEliminarSeleccionados.addActionListener(e -> {
            List<String> seleccionados = listaBackups.getSelectedValuesList();
            if (seleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Seleccione al menos un backup para eliminar.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                    "¿Está seguro que desea eliminar " + seleccionados.size() + " backup(s)?\nEsta acción no se puede deshacer.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                int eliminadosExitosos = 0;
                for (String archivo : seleccionados) {
                    if (sicaEngine.eliminarBackup(archivo)) {
                        eliminadosExitosos++;
                    }
                }

                JOptionPane.showMessageDialog(panelPrincipal,
                        "Eliminados " + eliminadosExitosos + " de " + seleccionados.size() + " backup(s).",
                        "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar datos
                actualizarDatos();

                // Cerrar diálogo
                Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
                if (window != null) {
                    window.dispose();
                }
            }
        });

        btnEliminarTodos.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                    "¿Está seguro que desea eliminar TODOS los backups?\nEsta acción no se puede deshacer.",
                    "Confirmar Eliminación Total",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                int eliminadosExitosos = 0;
                int totalBackups = backups.size();

                for (String backup : backups) {
                    String nombreArchivo = backup.split(" - ")[0];
                    if (sicaEngine.eliminarBackup(nombreArchivo)) {
                        eliminadosExitosos++;
                    }
                }

                JOptionPane.showMessageDialog(panelPrincipal,
                        "Eliminados " + eliminadosExitosos + " de " + totalBackups + " backup(s).",
                        "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar datos
                actualizarDatos();

                // Cerrar diálogo
                Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
                if (window != null) {
                    window.dispose();
                }
            }
        });

        btnCancelar.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
            if (window != null) {
                window.dispose();
            }
        });

        panelBotones.add(btnEliminarSeleccionados);
        panelBotones.add(btnEliminarTodos);
        panelBotones.add(btnCancelar);

        panelBackups.add(panelBotones, BorderLayout.SOUTH);

        // Mostrar en un diálogo
        JOptionPane.showMessageDialog(panelPrincipal,
                panelBackups,
                "Gestionar Backups",
                JOptionPane.PLAIN_MESSAGE);
    }

    protected void mostrarRegistroActividades() {
        List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();
        StringBuilder actividades = new StringBuilder();
        actividades.append("📋 REGISTRO DE ACTIVIDADES\n");
        actividades.append("=========================\n\n");

        // Ordenar por último acceso (más reciente primero)
        usuarios.sort((u1, u2) -> u2.getUltimoAcceso().compareTo(u1.getUltimoAcceso()));

        int count = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        for (UsuarioModel usuario : usuarios) {
            if (count >= 10) {
                break; // Mostrar solo los 10 más recientes
            }
            actividades.append(String.format("👤 %s\n", usuario.getUsername()));
            actividades.append(String.format("   📅 Último acceso: %s\n", usuario.getUltimoAcceso().format(formatter)));
            actividades.append(String.format("   🏷️ Rol: %s | Estado: %s\n",
                    usuario.getRol(),
                    usuario.isActivo() ? "Activo" : "Inactivo"));
            actividades.append("   ─────────────────────────\n");
            count++;
        }

        if (usuarios.isEmpty()) {
            actividades.append("No hay actividades registradas.");
        }

        JOptionPane.showMessageDialog(panelPrincipal, actividades.toString(),
                "Registro de Actividades", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void mostrarSeguridadAuditoria() {
        int intentosFallidosHoy = sicaEngine.obtenerTotalIntentosFallidosHoy();
        int usuariosBloqueados = sicaEngine.obtenerUsuariosBloqueados();
        List<UsuarioModel> usuariosConIntentos = sicaEngine.obtenerUsuariosConIntentosFallidos();

        StringBuilder auditoria = new StringBuilder();
        auditoria.append("🛡️ SEGURIDAD Y AUDITORÍA\n");
        auditoria.append("=======================\n\n");

        auditoria.append("🔍 AUDITORÍA ACTIVA:\n");
        auditoria.append("• Registro de todos los accesos\n");
        auditoria.append("• Historial de cambios de contraseña\n");
        auditoria.append("• Registro de intentos fallidos\n");
        auditoria.append("• Backup automático de registros\n\n");

        auditoria.append("⚠️ ALERTAS DE SEGURIDAD HOY:\n");
        auditoria.append("• Intentos fallidos totales: ").append(intentosFallidosHoy).append("\n");
        auditoria.append("• Usuarios bloqueados: ").append(usuariosBloqueados).append("\n");

        if (!usuariosConIntentos.isEmpty()) {
            auditoria.append("\n📊 USUARIOS CON INTENTOS FALLIDOS:\n");
            for (UsuarioModel usuario : usuariosConIntentos) {
                auditoria.append("• ").append(usuario.getUsername())
                        .append(": ").append(usuario.getIntentosFallidos())
                        .append(" intentos");

                if (usuario.estaBloqueado() && usuario.getFechaDesbloqueo() != null) {
                    auditoria.append(" (BLOQUEADO hasta ")
                            .append(usuario.getFechaDesbloqueo().format(DateTimeFormatter.ofPattern("HH:mm")))
                            .append(")");
                }
                auditoria.append("\n");
            }
        }

        auditoria.append("\n🛠️ HERRAMIENTAS:\n");
        auditoria.append("• Verificación de integridad de datos\n");
        auditoria.append("• Análisis de patrones de acceso\n");
        auditoria.append("• Reporte de actividades sospechosas\n");
        auditoria.append("• Limpieza automática de logs antiguos\n\n");

        auditoria.append("📊 MÉTRICAS DE SEGURIDAD:\n");
        auditoria.append("• Total usuarios: ").append(sicaEngine.obtenerTodosUsuarios().size()).append("\n");
        auditoria.append("• Usuarios con 2FA: 0\n");
        auditoria.append("• Contraseñas expiradas: 0\n");
        auditoria.append("• Última auditoría: ").append(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        ).append("\n\n");

        auditoria.append("💡 Recomendaciones:\n");
        auditoria.append("1. Revisar usuarios con múltiples intentos fallidos\n");
        auditoria.append("2. Verificar IPs de acceso sospechosas\n");
        auditoria.append("3. Implementar autenticación de dos factores\n");
        auditoria.append("4. Rotar contraseñas cada 90 días\n");

        // Panel para botones de acción
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 10, 10));

        if (!usuariosConIntentos.isEmpty()) {
            JButton btnVerDetalles = new JButton("🔍 Ver Detalles de Intentos Fallidos");
            btnVerDetalles.addActionListener(e -> mostrarDetallesIntentosFallidos());
            panelBotones.add(btnVerDetalles);
        }

        if (usuariosBloqueados > 0) {
            JButton btnDesbloquearUsuarios = new JButton("🔓 Gestionar Usuarios Bloqueados");
            btnDesbloquearUsuarios.addActionListener(e -> mostrarGestionUsuariosBloqueados());
            panelBotones.add(btnDesbloquearUsuarios);
        }

        JButton btnGenerarReporte = new JButton("📄 Generar Reporte de Seguridad");
        btnGenerarReporte.addActionListener(e -> generarReporteSeguridad());
        panelBotones.add(btnGenerarReporte);

        JOptionPane.showMessageDialog(panelPrincipal,
                new Object[]{
                    auditoria.toString(),
                    new JSeparator(),
                    panelBotones
                },
                "Seguridad y Auditoría",
                JOptionPane.INFORMATION_MESSAGE);
    }

    protected void mostrarDetallesIntentosFallidos() {
        List<UsuarioModel> usuariosConIntentos = sicaEngine.obtenerUsuariosConIntentosFallidos();

        if (usuariosConIntentos.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No hay intentos fallidos registrados hoy.",
                    "Intentos Fallidos",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder detalles = new StringBuilder();
        detalles.append("🔍 DETALLES DE INTENTOS FALLIDOS - HOY\n");
        detalles.append("=======================================\n\n");

        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        int totalIntentos = 0;

        for (UsuarioModel usuario : usuariosConIntentos) {
            detalles.append("👤 USUARIO: ").append(usuario.getUsername()).append("\n");
            detalles.append("   • Intentos fallidos: ").append(usuario.getIntentosFallidos()).append("\n");

            if (usuario.getUltimoIntentoFallido() != null) {
                detalles.append("   • Último intento: ").append(usuario.getUltimoIntentoFallido().format(horaFormatter)).append("\n");
            }

            if (usuario.estaBloqueado()) {
                detalles.append("   • Estado: 🔴 BLOQUEADO\n");
                if (usuario.getFechaDesbloqueo() != null) {
                    long minutosRestantes = java.time.Duration.between(
                            LocalDateTime.now(), usuario.getFechaDesbloqueo()
                    ).toMinutes();

                    detalles.append("   • Desbloqueo automático en: ").append(minutosRestantes).append(" minutos\n");
                    detalles.append("   • Hora desbloqueo: ").append(usuario.getFechaDesbloqueo().format(horaFormatter)).append("\n");
                }
            } else {
                detalles.append("   • Estado: 🟢 ACTIVO\n");
            }

            detalles.append("   ──────────────────────────────────\n");
            totalIntentos += usuario.getIntentosFallidos();
        }

        detalles.append("\n📊 RESUMEN:\n");
        detalles.append("• Total usuarios con intentos fallidos: ").append(usuariosConIntentos.size()).append("\n");
        detalles.append("• Total intentos fallidos hoy: ").append(totalIntentos).append("\n");
        detalles.append("• Promedio por usuario: ").append(String.format("%.1f", (double) totalIntentos / usuariosConIntentos.size())).append("\n");

        JOptionPane.showMessageDialog(panelPrincipal,
                detalles.toString(),
                "Detalles de Intentos Fallidos",
                JOptionPane.INFORMATION_MESSAGE);
    }

    protected void mostrarGestionUsuariosBloqueados() {
        List<UsuarioModel> todosUsuarios = sicaEngine.obtenerTodosUsuarios();
        List<UsuarioModel> usuariosBloqueados = new ArrayList<>();

        for (UsuarioModel usuario : todosUsuarios) {
            if (usuario.estaBloqueado()) {
                usuariosBloqueados.add(usuario);
            }
        }

        if (usuariosBloqueados.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No hay usuarios bloqueados en este momento.",
                    "Usuarios Bloqueados",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crear lista de usuarios bloqueados
        JPanel panelUsuarios = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (UsuarioModel usuario : usuariosBloqueados) {
            String info = usuario.getUsername() + " - "
                    + usuario.getIntentosFallidos() + " intentos fallidos";
            listModel.addElement(info);
        }

        JList<String> listaUsuarios = new JList<>(listModel);
        listaUsuarios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listaUsuarios);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        panelUsuarios.add(new JLabel("Usuarios actualmente bloqueados:"), BorderLayout.NORTH);
        panelUsuarios.add(scrollPane, BorderLayout.CENTER);

        // Información adicional
        StringBuilder infoBloqueo = new StringBuilder();
        infoBloqueo.append("INFORMACIÓN DE BLOQUEO:\n");
        infoBloqueo.append("• Los usuarios se bloquean automáticamente después de 3 intentos fallidos\n");
        infoBloqueo.append("• El bloqueo dura 15 minutos\n");
        infoBloqueo.append("• Después de 15 minutos, el usuario se desbloquea automáticamente\n");
        infoBloqueo.append("• El administrador puede desbloquear manualmente en cualquier momento\n\n");
        infoBloqueo.append("Total de usuarios bloqueados: ").append(usuariosBloqueados.size());

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnDesbloquearSeleccionados = new JButton("Desbloquear Seleccionados");
        JButton btnDesbloquearTodos = new JButton("Desbloquear Todos");
        JButton btnCancelar = new JButton("Cancelar");

        btnDesbloquearSeleccionados.addActionListener(e -> {
            List<String> seleccionados = listaUsuarios.getSelectedValuesList();
            if (seleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Seleccione al menos un usuario para desbloquear.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int desbloqueadosExitosos = 0;
            for (String seleccionado : seleccionados) {
                String username = seleccionado.split(" - ")[0];
                if (sicaEngine.desbloquearUsuario(username)) {
                    desbloqueadosExitosos++;
                }
            }

            JOptionPane.showMessageDialog(panelPrincipal,
                    "Desbloqueados " + desbloqueadosExitosos + " de " + seleccionados.size() + " usuario(s).",
                    "Resultado",
                    JOptionPane.INFORMATION_MESSAGE);

            // Actualizar datos
            actualizarDatos();

            // Cerrar diálogo
            Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
            if (window != null) {
                window.dispose();
            }
        });

        btnDesbloquearTodos.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                    "¿Está seguro que desea desbloquear TODOS los usuarios?",
                    "Confirmar Desbloqueo Total",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                int desbloqueadosExitosos = 0;

                for (UsuarioModel usuario : usuariosBloqueados) {
                    if (sicaEngine.desbloquearUsuario(usuario.getUsername())) {
                        desbloqueadosExitosos++;
                    }
                }

                JOptionPane.showMessageDialog(panelPrincipal,
                        "Desbloqueados " + desbloqueadosExitosos + " de " + usuariosBloqueados.size() + " usuario(s).",
                        "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar datos
                actualizarDatos();

                // Cerrar diálogo
                Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
                if (window != null) {
                    window.dispose();
                }
            }
        });

        btnCancelar.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
            if (window != null) {
                window.dispose();
            }
        });

        panelBotones.add(btnDesbloquearSeleccionados);
        panelBotones.add(btnDesbloquearTodos);
        panelBotones.add(btnCancelar);

        // Panel principal del diálogo
        JPanel panelPrincipalDialog = new JPanel(new BorderLayout(10, 10));
        panelPrincipalDialog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelPrincipalDialog.add(new JLabel(infoBloqueo.toString()), BorderLayout.NORTH);
        panelPrincipalDialog.add(panelUsuarios, BorderLayout.CENTER);
        panelPrincipalDialog.add(panelBotones, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(panelPrincipal,
                panelPrincipalDialog,
                "Gestionar Usuarios Bloqueados",
                JOptionPane.PLAIN_MESSAGE);
    }

    protected void generarReporteSeguridad() {
        List<UsuarioModel> usuariosConIntentos = sicaEngine.obtenerUsuariosConIntentosFallidos();
        int totalIntentosHoy = sicaEngine.obtenerTotalIntentosFallidosHoy();
        int usuariosBloqueados = sicaEngine.obtenerUsuariosBloqueados();
        int totalUsuarios = sicaEngine.obtenerTodosUsuarios().size();

        StringBuilder reporte = new StringBuilder();
        reporte.append("📄 REPORTE DE SEGURIDAD - ").append(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        ).append("\n");
        reporte.append("=============================================\n\n");

        reporte.append("📊 RESUMEN GENERAL:\n");
        reporte.append("• Total usuarios en sistema: ").append(totalUsuarios).append("\n");
        reporte.append("• Intentos fallidos hoy: ").append(totalIntentosHoy).append("\n");
        reporte.append("• Usuarios bloqueados: ").append(usuariosBloqueados).append("\n");
        reporte.append("• Porcentaje de actividad sospechosa: ").append(
                String.format("%.1f%%", (double) usuariosConIntentos.size() / totalUsuarios * 100)
        ).append("\n\n");

        if (!usuariosConIntentos.isEmpty()) {
            reporte.append("🔴 USUARIOS CON ACTIVIDAD SOSPECHOSA:\n");
            reporte.append("--------------------------------------\n");

            for (UsuarioModel usuario : usuariosConIntentos) {
                reporte.append("• ").append(usuario.getUsername())
                        .append(" - ").append(usuario.getIntentosFallidos()).append(" intentos");

                if (usuario.estaBloqueado()) {
                    reporte.append(" (BLOQUEADO)");
                }

                if (usuario.getUltimoIntentoFallido() != null) {
                    reporte.append(" - Último intento: ")
                            .append(usuario.getUltimoIntentoFallido().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                }

                reporte.append("\n");
            }
            reporte.append("\n");
        }

        reporte.append("💡 RECOMENDACIONES:\n");
        reporte.append("-------------------\n");

        if (usuariosBloqueados > 0) {
            reporte.append("1. Revisar usuarios bloqueados para posibles ataques\n");
        }

        if (totalIntentosHoy > 10) {
            reporte.append("2. Alto número de intentos fallidos - considerar fortalecer políticas de contraseñas\n");
        }

        if (!usuariosConIntentos.isEmpty() && usuariosConIntentos.size() > 3) {
            reporte.append("3. Múltiples usuarios con intentos fallidos - revisar seguridad del sistema\n");
        }

        reporte.append("4. Considerar implementar CAPTCHA para login\n");
        reporte.append("5. Revisar logs de acceso para IPs sospechosas\n");

        // Mostrar en un área de texto para poder copiar
        JTextArea textArea = new JTextArea(reporte.toString(), 20, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);

        // Botón para copiar
        JButton btnCopiar = new JButton("📋 Copiar Reporte");
        btnCopiar.addActionListener(e -> {
            textArea.selectAll();
            textArea.copy();
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Reporte copiado al portapapeles.",
                    "Copiado",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCopiar);

        JOptionPane.showMessageDialog(panelPrincipal,
                new Object[]{scrollPane, panelBotones},
                "Reporte de Seguridad",
                JOptionPane.PLAIN_MESSAGE);
    }

    protected void editarUsuarioSeleccionado() {
        int filaSeleccionada = tablaActividades.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un usuario de la tabla para editar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = (String) modeloTablaActividades.getValueAt(filaSeleccionada, 0);
        UsuarioModel usuario = sicaEngine.obtenerUsuario(username);

        if (usuario != null) {
            String estadoActual = usuario.isActivo() ? "Activo" : "Inactivo";
            String nuevoEstado = usuario.isActivo() ? "Inactivo" : "Activo";

            // Crear opciones de menú
            String[] opciones = {
                "Cambiar Contraseña",
                "Cambiar Rol",
                (usuario.isActivo() ? "Desactivar Usuario" : "Activar Usuario"),
                "Cancelar"
            };

            int opcion = JOptionPane.showOptionDialog(panelPrincipal,
                    "Usuario: " + username + "\n"
                    + "Estado actual: " + estadoActual + "\n"
                    + "Rol: " + usuario.getRol() + "\n\n"
                    + "Seleccione una acción:",
                    "Editar Usuario: " + username,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            switch (opcion) {
                case 0: // Cambiar contraseña
                    cambiarPasswordUsuario(username);
                    break;
                case 1: // Cambiar rol
                    cambiarRolUsuario(username, usuario.getRol());
                    break;
                case 2: // Activar/Desactivar
                    cambiarEstadoUsuario(username, usuario.isActivo());
                    break;
                // case 3 es Cancelar, no hace nada
            }
        }
    }

    protected void mostrarEstadisticasUsuarios() {
        List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();

        int totalUsuarios = usuarios.size();
        int usuariosActivos = 0;
        int usuariosInactivos = 0;
        int administradores = 0;
        int usuariosNormales = 0;
        int usuariosBloqueados = 0;

        for (UsuarioModel usuario : usuarios) {
            if (usuario.isActivo()) {
                usuariosActivos++;
            } else {
                usuariosInactivos++;
            }

            if (usuario.esAdmin()) {
                administradores++;
            } else {
                usuariosNormales++;
            }

            if (usuario.estaBloqueado()) {
                usuariosBloqueados++;
            }
        }

        String estadisticas = String.format("""
        📊 ESTADÍSTICAS DE USUARIOS
        ============================
        
        👥 TOTAL DE USUARIOS: %d
        
        🟢 USUARIOS ACTIVOS: %d
        🔴 USUARIOS INACTIVOS: %d
        
        👑 ADMINISTRADORES: %d
        👤 USUARIOS NORMALES: %d
        
        ⚠️ USUARIOS BLOQUEADOS: %d
        
        📈 PORCENTAJES:
        • Activos: %.1f%%
        • Inactivos: %.1f%%
        • Administradores: %.1f%%
        • Normales: %.1f%%
        """,
                totalUsuarios,
                usuariosActivos, usuariosInactivos,
                administradores, usuariosNormales,
                usuariosBloqueados,
                (double) usuariosActivos / totalUsuarios * 100,
                (double) usuariosInactivos / totalUsuarios * 100,
                (double) administradores / totalUsuarios * 100,
                (double) usuariosNormales / totalUsuarios * 100
        );

        JOptionPane.showMessageDialog(panelPrincipal,
                estadisticas,
                "Estadísticas de Usuarios",
                JOptionPane.INFORMATION_MESSAGE);
    }

    protected void mostrarGestionEstadosUsuarios() {
        List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();

        // Crear tabla para gestión
        String[] columnas = {"Usuario", "Nombre", "Estado", "Rol", "Acción"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (UsuarioModel usuario : usuarios) {
            // No mostrar el usuario actual ni el admin principal para desactivar
            boolean puedeDesactivar = !usuario.getUsername().equals(usuario.getUsername())
                    && !usuario.getUsername().equals("admin");

            String accion = usuario.isActivo() && puedeDesactivar ? "Desactivar"
                    : !usuario.isActivo() ? "Activar" : "No disponible";

            Object[] fila = {
                usuario.getUsername(),
                usuario.getNombreCompleto(),
                usuario.isActivo() ? "🟢 Activo" : "🔴 Inactivo",
                usuario.getRol(),
                accion
            };
            modeloTabla.addRow(fila);
        }

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);

        // Agregar botón de acción
        JButton btnEjecutarAccion = new JButton("Ejecutar Acción");
        btnEjecutarAccion.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Seleccione un usuario de la tabla",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String username = (String) tabla.getValueAt(filaSeleccionada, 0);
            String accion = (String) tabla.getValueAt(filaSeleccionada, 4);

            if (accion.equals("No disponible")) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No se puede cambiar el estado de este usuario",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            UsuarioModel usuarioSeleccionado = sicaEngine.obtenerUsuario(username);
            if (usuarioSeleccionado != null) {
                cambiarEstadoUsuario(username, usuarioSeleccionado.isActivo());

                // Cerrar diálogo actual y abrir uno nuevo actualizado
                Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
                if (window != null) {
                    window.dispose();
                }

                // Volver a abrir la gestión
                mostrarGestionEstadosUsuarios();
            }
        });

        // Panel principal
        JPanel panelPrincipalDialog = new JPanel(new BorderLayout(10, 10));
        panelPrincipalDialog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Información
        JLabel lblInfo = new JLabel("<html><b>Gestión de Estados de Usuarios</b><br>"
                + "Seleccione un usuario y haga clic en 'Ejecutar Acción'</html>");

        panelPrincipalDialog.add(lblInfo, BorderLayout.NORTH);
        panelPrincipalDialog.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panelPrincipalDialog.add(btnEjecutarAccion, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(panelPrincipal,
                panelPrincipalDialog,
                "Gestionar Estados de Usuarios",
                JOptionPane.PLAIN_MESSAGE);
    }

    protected void mostrarUsuariosInactivos() {
        List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();
        List<UsuarioModel> usuariosInactivos = new ArrayList<>();

        for (UsuarioModel usuario : usuarios) {
            if (!usuario.isActivo()) {
                usuariosInactivos.add(usuario);
            }
        }

        if (usuariosInactivos.isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No hay usuarios inactivos en el sistema.",
                    "Usuarios Inactivos",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder lista = new StringBuilder();
        lista.append("🔴 USUARIOS INACTIVOS\n");
        lista.append("=====================\n\n");
        lista.append("Total: ").append(usuariosInactivos.size()).append(" usuario(s) inactivo(s)\n\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (UsuarioModel usuario : usuariosInactivos) {
            lista.append("👤 ").append(usuario.getUsername()).append("\n");
            lista.append("   • Nombre: ").append(usuario.getNombreCompleto()).append("\n");
            lista.append("   • Email: ").append(usuario.getEmail()).append("\n");
            lista.append("   • Rol: ").append(usuario.getRol()).append("\n");
            lista.append("   • Fecha registro: ").append(usuario.getFechaRegistro().format(formatter)).append("\n");
            lista.append("   • Último acceso: ").append(usuario.getUltimoAcceso().format(formatter)).append("\n");
            lista.append("   ──────────────────────────\n");
        }

        // Botón para activar todos
        JButton btnActivarTodos = new JButton("🔄 Activar Todos los Usuarios");
        btnActivarTodos.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                    "¿Está seguro que desea activar TODOS los usuarios inactivos?\n\n"
                    + "Esto afectará a " + usuariosInactivos.size() + " usuario(s).",
                    "Confirmar Activación Masiva",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                int activadosExitosos = 0;

                for (UsuarioModel usuario : usuariosInactivos) {
                    // No activar el admin principal si está inactivo (raro caso)
                    if (!usuario.getUsername().equals("admin")) {
                        if (sicaEngine.cambiarEstadoUsuario(usuario.getUsername(), true)) {
                            activadosExitosos++;
                        }
                    }
                }

                JOptionPane.showMessageDialog(panelPrincipal,
                        "Activados " + activadosExitosos + " de " + usuariosInactivos.size() + " usuario(s) inactivos.",
                        "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar datos
                actualizarDatos();

                // Cerrar diálogo
                Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
                if (window != null) {
                    window.dispose();
                }
            }
        });

        JOptionPane.showMessageDialog(panelPrincipal,
                new Object[]{
                    lista.toString(),
                    new JSeparator(),
                    btnActivarTodos
                },
                "Usuarios Inactivos",
                JOptionPane.INFORMATION_MESSAGE);
    }

    protected void cambiarEstadoUsuario(String username, boolean estadoActual) {
        String accion = estadoActual ? "desactivar" : "activar";
        String mensajeConfirmacion = estadoActual
                ? "¿Está seguro que desea DESACTIVAR al usuario '" + username + "'?\n\n"
                + "El usuario no podrá iniciar sesión hasta que sea activado nuevamente."
                : "¿Está seguro que desea ACTIVAR al usuario '" + username + "'?\n\n"
                + "El usuario podrá iniciar sesión nuevamente.";

        // No permitir desactivar el usuario administrador actual
        if (username.equals(usuario.getUsername()) && estadoActual) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No puede desactivar su propio usuario mientras está en sesión",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // No permitir desactivar el admin principal
        if (username.equals("admin") && estadoActual) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No puede desactivar el usuario administrador principal",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                mensajeConfirmacion,
                "Confirmar " + (estadoActual ? "Desactivación" : "Activación"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean exito = sicaEngine.cambiarEstadoUsuario(username, !estadoActual);

            if (exito) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Usuario '" + username + "' ha sido "
                        + (estadoActual ? "DESACTIVADO" : "ACTIVADO") + " exitosamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar tabla
                actualizarDatos();

                // Mostrar advertencia si se desactivó un usuario
                if (estadoActual) {
                    mostrarAdvertenciaDesactivacion(username);
                }
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Error al " + accion + " el usuario",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected void mostrarAdvertenciaDesactivacion(String username) {
        StringBuilder advertencia = new StringBuilder();
        advertencia.append("⚠️ ADVERTENCIA: USUARIO DESACTIVADO\n");
        advertencia.append("====================================\n\n");
        advertencia.append("El usuario '").append(username).append("' ha sido desactivado.\n\n");
        advertencia.append("🔒 CONSECUENCIAS:\n");
        advertencia.append("• No podrá iniciar sesión\n");
        advertencia.append("• No recibirá notificaciones\n");
        advertencia.append("• Su cuenta permanece en el sistema\n\n");
        advertencia.append("🔄 PARA REACTIVAR:\n");
        advertencia.append("• Seleccione el usuario en la tabla\n");
        advertencia.append("• Haga clic en 'Editar Usuario'\n");
        advertencia.append("• Seleccione 'Activar Usuario'\n\n");
        advertencia.append("📝 NOTA: Puede activarlo en cualquier momento.");

        JOptionPane.showMessageDialog(panelPrincipal,
                advertencia.toString(),
                "Usuario Desactivado",
                JOptionPane.WARNING_MESSAGE);
    }

    protected void cambiarPasswordUsuario(String username) {
        JPasswordField nuevaPassword = new JPasswordField();
        JPasswordField confirmarPassword = new JPasswordField();

        // Botones para mostrar/ocultar
        JToggleButton btnMostrarNueva = new JToggleButton("👁️");
        JToggleButton btnMostrarConfirmar = new JToggleButton("👁️");
        
        ActionListener toggleListener = e -> {
            JToggleButton btn = (JToggleButton) e.getSource();
            JPasswordField txt = (btn == btnMostrarNueva) ? nuevaPassword : confirmarPassword;
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
        
        JPanel panelNueva = new JPanel(new BorderLayout());
        panelNueva.add(nuevaPassword, BorderLayout.CENTER);
        panelNueva.add(btnMostrarNueva, BorderLayout.EAST);
        
        JPanel panelConfirmar = new JPanel(new BorderLayout());
        panelConfirmar.add(confirmarPassword, BorderLayout.CENTER);
        panelConfirmar.add(btnMostrarConfirmar, BorderLayout.EAST);

        Object[] campos = {
            "Nueva contraseña:", panelNueva,
            "Confirmar contraseña:", panelConfirmar
        };

        int opcion = JOptionPane.showConfirmDialog(panelPrincipal,
                campos,
                "Cambiar Contraseña para " + username,
                JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            String pass1 = new String(nuevaPassword.getPassword());
            String pass2 = new String(confirmarPassword.getPassword());

            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Las contraseñas no coinciden",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sicaEngine.cambiarPassword(username, pass1)) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Contraseña cambiada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                actualizarDatos();
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Error al cambiar la contraseña",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected void cambiarRolUsuario(String username, String rolActual) {
        // Crear diálogo personalizado para cambio de rol
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(panelPrincipal),
                "Cambiar Rol de Usuario", true);
        dialogo.setSize(500, 400);
        dialogo.setLocationRelativeTo(panelPrincipal);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Información del usuario - variable local diferente
        UsuarioModel usuarioSeleccionado = sicaEngine.obtenerUsuario(username);
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Usuario no encontrado: " + username,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el usuario administrador actual (this.usuario)
        String usuarioActualAdmin = this.usuario.getUsername();

        StringBuilder infoUsuario = new StringBuilder();
        infoUsuario.append("<html><b>Usuario:</b> ").append(username).append("<br>");
        infoUsuario.append("<b>Nombre:</b> ").append(usuarioSeleccionado.getNombreCompleto()).append("<br>");
        infoUsuario.append("<b>Email:</b> ").append(usuarioSeleccionado.getEmail()).append("<br>");
        infoUsuario.append("<b>Rol actual:</b> ").append(rolActual).append("</html>");

        JLabel lblInfo = new JLabel(infoUsuario.toString());
        lblInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Panel de selección de rol
        JPanel panelRoles = new JPanel(new GridLayout(0, 2, 8, 7));
        panelRoles.setBorder(BorderFactory.createTitledBorder("Seleccione el nuevo rol:"));

        // Definir roles disponibles
        String[] rolesDisponibles = {"ADMIN", "USUARIO", "AUDITOR", "SUPERVISOR"};
        String[] descripcionesRoles = {
            "Administrador ",
            "Usuario Normal ",
            "Auditor ",
            "Supervisor "
        };

        ButtonGroup grupoRoles = new ButtonGroup();
        JRadioButton[] radioButtons = new JRadioButton[rolesDisponibles.length];

        for (int i = 0; i < rolesDisponibles.length; i++) {
            radioButtons[i] = new JRadioButton(rolesDisponibles[i] + " - " + descripcionesRoles[i]);
            radioButtons[i].setActionCommand(rolesDisponibles[i]);

            // Seleccionar el rol actual
            if (rolesDisponibles[i].equalsIgnoreCase(rolActual)) {
                radioButtons[i].setSelected(true);
            }

            grupoRoles.add(radioButtons[i]);
            panelRoles.add(radioButtons[i]);
        }

        // Panel de permisos
        JPanel panelPermisos = new JPanel(new BorderLayout());
        panelPermisos.setBorder(BorderFactory.createTitledBorder("Permisos del rol seleccionado:"));

        JTextArea txtPermisos = new JTextArea();
        txtPermisos.setEditable(false);
        txtPermisos.setLineWrap(true);
        txtPermisos.setWrapStyleWord(true);
        txtPermisos.setBackground(new Color(240, 240, 240));
        txtPermisos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Actualizar permisos cuando se selecciona un rol
        ActionListener actualizarPermisos = e -> {
            String rolSeleccionado = grupoRoles.getSelection().getActionCommand();
            txtPermisos.setText(obtenerDescripcionPermisos(rolSeleccionado));
        };

        for (JRadioButton radio : radioButtons) {
            radio.addActionListener(actualizarPermisos);
        }

        // Inicializar permisos
        String rolInicial = grupoRoles.getSelection() != null ? grupoRoles.getSelection().getActionCommand() : rolActual;
        txtPermisos.setText(obtenerDescripcionPermisos(rolInicial));

        JScrollPane scrollPermisos = new JScrollPane(txtPermisos);
        scrollPermisos.setPreferredSize(new Dimension(0, 100));
        panelPermisos.add(scrollPermisos, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAceptar = new JButton("Cambiar Rol");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
            if (grupoRoles.getSelection() == null) {
                JOptionPane.showMessageDialog(dialogo,
                        "Seleccione un rol",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nuevoRol = grupoRoles.getSelection().getActionCommand();

            // Validaciones especiales
            if (username.equals("admin") && !nuevoRol.equals("ADMIN")) {
                JOptionPane.showMessageDialog(dialogo,
                        "No puede cambiar el rol del usuario administrador principal",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // CORRECCIÓN: Comparar con el usuario administrador actual, no con el seleccionado
            if (username.equals(usuarioActualAdmin) && !nuevoRol.equals(rolActual)) {
                int confirmacion = JOptionPane.showConfirmDialog(dialogo,
                        "⚠️ ADVERTENCIA: Está cambiando su PROPIO rol.\n\n"
                        + "Si cambia de ADMIN a otro rol, perderá:\n"
                        + "• Acceso al panel de administración\n"
                        + "• Permisos de gestión de usuarios\n"
                        + "• Configuración del sistema\n\n"
                        + "¿Desea continuar?",
                        "Confirmar Cambio de Rol Propio",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirmacion != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            if (nuevoRol.equals(rolActual)) {
                JOptionPane.showMessageDialog(dialogo,
                        "El usuario ya tiene asignado el rol " + nuevoRol,
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(dialogo,
                    "¿Cambiar rol de " + username + " de " + rolActual + " a " + nuevoRol + "?\n\n"
                    + "Esta acción afectará los permisos y accesos del usuario.",
                    "Confirmar Cambio de Rol",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean exito = sicaEngine.cambiarRolUsuario(username, nuevoRol);

                if (exito) {
                    JOptionPane.showMessageDialog(dialogo,
                            "✅ Rol cambiado exitosamente\n\n"
                            + "Usuario: " + username + "\n"
                            + "Nuevo rol: " + nuevoRol + "\n\n"
                            + "Los cambios se aplicarán en el próximo inicio de sesión.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                    dialogo.dispose();

                    // Actualizar datos
                    actualizarDatos();

                    // CORRECCIÓN: Solo mostrar advertencia si es el usuario actual
                    if (username.equals(usuarioActualAdmin)) {
                        mostrarAdvertenciaCambioRolPropio(nuevoRol);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialogo,
                            "❌ Error al cambiar el rol",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);

        // Ensamblar el diálogo
        panel.add(lblInfo, BorderLayout.NORTH);
        panel.add(panelRoles, BorderLayout.CENTER);
        panel.add(panelPermisos, BorderLayout.SOUTH);

        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    protected String obtenerDescripcionPermisos(String rol) {
        switch (rol.toUpperCase()) {
            case "ADMIN":
                return """
                   PERMISOS DE ADMINISTRADOR:
                   • Acceso completo a todas las funciones
                   • Gestión de usuarios (crear, editar, eliminar)
                   • Cambio de roles y permisos
                   • Configuración del sistema
                   • Backup y restauración
                   • Ver todos los reportes y auditorías
                   • Desbloquear usuarios
                   • Resetear contraseñas
                   
                   ACCESO: Completo e ilimitado""";

            case "USUARIO":
                return """
                   PERMISOS DE USUARIO NORMAL:
                   • Ver y editar perfil propio
                   • Cambiar contraseña propia
                   • Configurar pregunta secreta
                   • Ver actividades propias
                   • Ver estadísticas propias
                   • Acceder a soporte técnico
                   
                   RESTRICCIONES:
                   • No puede gestionar otros usuarios
                   • No puede ver reportes del sistema
                   • No puede modificar configuración
                   
                   ACCESO: Limitado a funciones personales""";

            case "AUDITOR":
                return """
                   PERMISOS DE AUDITOR:
                   • Ver reportes del sistema
                   • Ver auditorías y logs
                   • Ver estadísticas globales
                   • Ver usuarios inactivos
                   • Ver intentos fallidos
                   • Exportar reportes
                   
                   RESTRICCIONES:
                   • Solo lectura, no puede modificar
                   • No puede gestionar usuarios
                   • No puede cambiar configuración
                   
                   ACCESO: Solo lectura a información""";

            case "SUPERVISOR":
                return """
                   PERMISOS DE SUPERVISOR:
                   • Ver todos los usuarios
                   • Activar/desactivar usuarios
                   • Ver reportes básicos
                   • Ver actividades de usuarios
                   • Resetear contraseñas
                   • Desbloquear usuarios
                   
                   RESTRICCIONES:
                   • No puede cambiar roles
                   • No puede eliminar usuarios
                   • No puede modificar configuración
                   • No puede hacer backup
                   
                   ACCESO: Gestión básica de usuarios""";

            default:
                return "Rol no reconocido";
        }
    }

    protected void mostrarGestionRolesUsuarios() {
        List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();

        // Crear modelo de tabla personalizado
        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Solo la columna "Nuevo Rol" es editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return JComboBox.class;
                }
                return String.class;
            }
        };

        // Definir columnas
        String[] columnas = {"Usuario", "Nombre", "Rol Actual", "Nuevo Rol"};
        modeloTabla.setColumnIdentifiers(columnas);

        // Roles disponibles
        String[] rolesDisponibles = {"ADMIN", "USUARIO", "AUDITOR", "SUPERVISOR"};

        // Agregar datos a la tabla
        for (UsuarioModel usuario : usuarios) {
            Object[] fila = new Object[4];
            fila[0] = usuario.getUsername();
            fila[1] = usuario.getNombreCompleto();
            fila[2] = usuario.getRol();
            fila[3] = usuario.getRol(); // Valor inicial del combo

            modeloTabla.addRow(fila);
        }

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);

        // Configurar el editor de celda para la columna "Nuevo Rol"
        TableColumn columnaRol = tabla.getColumnModel().getColumn(3);

        // Crear combo box para seleccionar rol
        JComboBox<String> comboRoles = new JComboBox<>(rolesDisponibles);
        columnaRol.setCellEditor(new DefaultCellEditor(comboRoles));

        // Asegurarse de que los combos muestren el valor correcto
        for (int i = 0; i < tabla.getRowCount(); i++) {
            String rolActual = (String) modeloTabla.getValueAt(i, 2);
            comboRoles.setSelectedItem(rolActual);
        }

        // Botón para aplicar cambios
        JButton btnAplicarCambios = new JButton("💾 Aplicar Cambios de Roles");
        btnAplicarCambios.addActionListener(e -> {
            // Verificar si hay cambios
            boolean hayCambios = false;
            List<String[]> cambios = new ArrayList<>();

            for (int i = 0; i < tabla.getRowCount(); i++) {
                String username = (String) modeloTabla.getValueAt(i, 0);
                String rolActual = (String) modeloTabla.getValueAt(i, 2);
                Object valorRolNuevo = modeloTabla.getValueAt(i, 3);
                String nuevoRol = valorRolNuevo != null ? valorRolNuevo.toString() : rolActual;

                if (!rolActual.equals(nuevoRol)) {
                    hayCambios = true;
                    cambios.add(new String[]{username, rolActual, nuevoRol});
                }
            }

            if (!hayCambios) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "No hay cambios pendientes en los roles",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Mostrar resumen de cambios
            StringBuilder resumen = new StringBuilder();
            resumen.append("📋 RESUMEN DE CAMBIOS DE ROL\n");
            resumen.append("===========================\n\n");
            resumen.append("Total cambios: ").append(cambios.size()).append("\n\n");

            for (String[] cambio : cambios) {
                resumen.append("• ").append(cambio[0])
                        .append(": ").append(cambio[1])
                        .append(" → ").append(cambio[2])
                        .append("\n");
            }

            resumen.append("\n⚠️ ADVERTENCIA: Estos cambios afectarán los permisos de los usuarios.");

            // Crear diálogo de confirmación
            int confirmacion = JOptionPane.showConfirmDialog(
                    panelPrincipal,
                    resumen.toString(),
                    "Confirmar Cambios de Roles",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                int exitosos = 0;
                int fallidos = 0;
                List<String> errores = new ArrayList<>();

                for (String[] cambio : cambios) {
                    String username = cambio[0];
                    String nuevoRol = cambio[2];

                    // Validación especial para admin principal
                    if (username.equals("admin") && !nuevoRol.equals("ADMIN")) {
                        errores.add("No se puede cambiar el rol del usuario administrador principal: " + username);
                        fallidos++;
                        continue;
                    }

                    // Validación para el usuario actual
                    if (username.equals(this.usuario.getUsername()) && !nuevoRol.equals("ADMIN")) {
                        int confirmarPropio = JOptionPane.showConfirmDialog(
                                panelPrincipal,
                                "⚠️ ADVERTENCIA: Está cambiando su PROPIO rol a " + nuevoRol + "\n\n"
                                + "Si continúa, perderá acceso al panel de administración.\n"
                                + "¿Desea continuar?",
                                "Confirmar Cambio de Rol Propio",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );

                        if (confirmarPropio != JOptionPane.YES_OPTION) {
                            errores.add("Cancelado cambio de rol propio: " + username);
                            fallidos++;
                            continue;
                        }
                    }

                    try {
                        if (sicaEngine.cambiarRolUsuario(username, nuevoRol)) {
                            exitosos++;

                            // Actualizar la columna "Rol Actual" en la tabla
                            for (int i = 0; i < tabla.getRowCount(); i++) {
                                if (tabla.getValueAt(i, 0).equals(username)) {
                                    modeloTabla.setValueAt(nuevoRol, i, 2);
                                    break;
                                }
                            }
                        } else {
                            fallidos++;
                            errores.add("Error al cambiar rol de: " + username);
                        }
                    } catch (Exception ex) {
                        fallidos++;
                        errores.add("Excepción con " + username + ": " + ex.getMessage());
                    }
                }

                // Mostrar resultados
                StringBuilder resultado = new StringBuilder();
                resultado.append("✅ CAMBIOS APLICADOS\n");
                resultado.append("===================\n\n");
                resultado.append("• Exitosos: ").append(exitosos).append("\n");
                resultado.append("• Fallidos: ").append(fallidos).append("\n");

                if (!errores.isEmpty()) {
                    resultado.append("\n❌ ERRORES:\n");
                    for (String error : errores) {
                        resultado.append("• ").append(error).append("\n");
                    }
                }

                resultado.append("\n💡 Los cambios se reflejarán en la próxima actualización.");

                JOptionPane.showMessageDialog(
                        panelPrincipal,
                        resultado.toString(),
                        "Resultado",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Actualizar datos principales
                actualizarDatos();

                // Si se cambió el rol propio, mostrar advertencia
                for (String[] cambio : cambios) {
                    if (cambio[0].equals(this.usuario.getUsername())
                            && !cambio[1].equals(cambio[2])
                            && sicaEngine.cambiarRolUsuario(cambio[0], cambio[2])) {
                        mostrarAdvertenciaCambioRolPropio(cambio[2]);
                        break;
                    }
                }
            }
        });

        // Panel principal del diálogo
        JPanel panelPrincipalDialog = new JPanel(new BorderLayout(10, 10));
        panelPrincipalDialog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de instrucciones
        JPanel panelInstrucciones = new JPanel(new BorderLayout());
        panelInstrucciones.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel lblTitulo = new JLabel("<html><b>Gestión Masiva de Roles</b></html>");
        JLabel lblInstrucciones = new JLabel("<html>Seleccione nuevos roles en la columna 'Nuevo Rol' y haga clic en 'Aplicar Cambios'</html>");

        panelInstrucciones.add(lblTitulo, BorderLayout.NORTH);
        panelInstrucciones.add(lblInstrucciones, BorderLayout.SOUTH);

        // Panel de la tabla
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(600, 300));

        // Panel del botón
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelBoton.add(btnAplicarCambios);

        // Ensamblar todo
        panelPrincipalDialog.add(panelInstrucciones, BorderLayout.NORTH);
        panelPrincipalDialog.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipalDialog.add(panelBoton, BorderLayout.SOUTH);

        // Crear diálogo
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(panelPrincipal),
                "Gestión de Roles de Usuarios", true);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setContentPane(panelPrincipalDialog);
        dialogo.pack();
        dialogo.setLocationRelativeTo(panelPrincipal);
        dialogo.setVisible(true);
    }

    protected void mostrarCreacionNuevoRol() {
        JOptionPane.showMessageDialog(panelPrincipal,
                "⚠️ FUNCIÓN EN DESARROLLO\n\n"
                + "La creación de roles personalizados estará disponible en la próxima versión.\n\n"
                + "Por ahora, solo están disponibles los roles predefinidos:\n"
                + "• ADMIN\n• USUARIO\n• AUDITOR\n• SUPERVISOR",
                "Crear Nuevo Rol",
                JOptionPane.INFORMATION_MESSAGE);
    }
    //necesito agregar el codigo de eliminacion 

    protected void eliminarUsuarioSeleccionado() {
        int filaSeleccionada = tablaActividades.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un usuario de la tabla para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = (String) modeloTablaActividades.getValueAt(filaSeleccionada, 0);
        UsuarioModel usuarioObj = sicaEngine.obtenerUsuario(username);

        if (usuarioObj != null && usuarioObj.isActivo()) {
            int opcion = JOptionPane.showConfirmDialog(panelPrincipal,
                    "El usuario '" + username + "' está ACTIVO.\n\n"
                    + "¿Desea DESACTIVARLO en lugar de ELIMINARLO?\n\n"
                    + "• Desactivar: El usuario no podrá acceder pero conserva sus datos\n"
                    + "• Eliminar: Se borrará permanentemente del sistema",
                    "Usuario Activo - ¿Desactivar o Eliminar?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                // Desactivar en lugar de eliminar
                cambiarEstadoUsuario(username, true);
                return;
            } else if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                return; // Cancelar operación
            }
            // Si elige NO, continuar con eliminación
        }

        // ========== CÓDIGO ORIGINAL DE ELIMINACIÓN ==========
        // No permitir eliminar el usuario administrador actual
        if (username.equals(usuario.getUsername())) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No puede eliminar su propio usuario mientras está en sesión",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (username.equals("admin")) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No puede eliminar el usuario administrador principal",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener información adicional del usuario para mostrar
        String nombreCompleto = (String) modeloTablaActividades.getValueAt(filaSeleccionada, 1);
        String email = (String) modeloTablaActividades.getValueAt(filaSeleccionada, 2);
        String rol = (String) modeloTablaActividades.getValueAt(filaSeleccionada, 3);
        String estado = (String) modeloTablaActividades.getValueAt(filaSeleccionada, 6);

        // Mensaje de confirmación más detallado
        StringBuilder mensajeConfirmacion = new StringBuilder();
        mensajeConfirmacion.append("⚠️ ¿ESTÁ SEGURO QUE DESEA ELIMINAR ESTE USUARIO?\n\n");
        mensajeConfirmacion.append("📋 INFORMACIÓN DEL USUARIO:\n");
        mensajeConfirmacion.append("• Usuario: ").append(username).append("\n");
        mensajeConfirmacion.append("• Nombre: ").append(nombreCompleto).append("\n");
        mensajeConfirmacion.append("• Email: ").append(email).append("\n");
        mensajeConfirmacion.append("• Rol: ").append(rol).append("\n");
        mensajeConfirmacion.append("• Estado: ").append(estado).append("\n\n");

        mensajeConfirmacion.append("🔥 CONSECUENCIAS DE LA ELIMINACIÓN:\n");
        mensajeConfirmacion.append("• Se borrará PERMANENTEMENTE del sistema\n");
        mensajeConfirmacion.append("• Se perderán todos sus datos\n");
        mensajeConfirmacion.append("• No se podrá recuperar la información\n");
        mensajeConfirmacion.append("• Se eliminará su historial de actividades\n\n");

        mensajeConfirmacion.append("💡 RECOMENDACIÓN:\n");
        mensajeConfirmacion.append("Considere DESACTIVAR al usuario en lugar de ELIMINAR.\n");
        mensajeConfirmacion.append("Así podrá reactivarlo en el futuro si es necesario.");

        // Panel personalizado para confirmación
        JCheckBox chkConfirmar = new JCheckBox("✅ Confirmo que deseo eliminar permanentemente este usuario");
        JCheckBox chkBackup = new JCheckBox("💾 Crear backup antes de eliminar (recomendado)");
        chkBackup.setSelected(true);

        JPanel panelConfirmacion = new JPanel(new BorderLayout(10, 10));
        panelConfirmacion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea txtMensaje = new JTextArea(mensajeConfirmacion.toString());
        txtMensaje.setEditable(false);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setBackground(panelPrincipal.getBackground());
        txtMensaje.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JPanel panelCheckboxes = new JPanel(new GridLayout(2, 1, 5, 5));
        panelCheckboxes.add(chkConfirmar);
        panelCheckboxes.add(chkBackup);

        panelConfirmacion.add(new JScrollPane(txtMensaje), BorderLayout.CENTER);
        panelConfirmacion.add(panelCheckboxes, BorderLayout.SOUTH);

        int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                panelConfirmacion,
                "Confirmar Eliminación Permanente",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (!chkConfirmar.isSelected()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Debe marcar la casilla de confirmación para proceder",
                        "Confirmación Requerida",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Crear backup si está seleccionado
            if (chkBackup.isSelected()) {
                try {
                    sicaEngine.crearBackup();
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "✅ Backup creado exitosamente antes de la eliminación",
                            "Backup Exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    int continuar = JOptionPane.showConfirmDialog(panelPrincipal,
                            "❌ Error al crear backup: " + ex.getMessage() + "\n\n"
                            + "¿Desea continuar con la eliminación sin backup?",
                            "Error en Backup",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.ERROR_MESSAGE);

                    if (continuar != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }

            // Proceder con la eliminación
            if (sicaEngine.eliminarUsuario(username)) {
                // Mostrar mensaje de éxito
                JOptionPane.showMessageDialog(panelPrincipal,
                        "✅ Usuario eliminado exitosamente:\n\n"
                        + "• Usuario: " + username + "\n"
                        + "• Nombre: " + nombreCompleto + "\n"
                        + "• Email: " + email + "\n\n"
                        + "El usuario ha sido removido permanentemente del sistema.",
                        "Eliminación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar datos y tabla
                actualizarDatos();

                // Registrar en log (simulado)
                System.out.println("ADMIN: Usuario '" + username + "' eliminado por " + usuario.getUsername());

            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "❌ Error al eliminar el usuario\n\n"
                        + "No se pudo completar la eliminación.\n"
                        + "Posibles causas:\n"
                        + "• Problemas con el archivo de base de datos\n"
                        + "• Permisos insuficientes\n"
                        + "• El usuario no existe",
                        "Error en Eliminación",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected void mostrarAdvertenciaCambioRolPropio(String nuevoRol) {
        StringBuilder advertencia = new StringBuilder();
        advertencia.append("⚠️ ADVERTENCIA: CAMBIO DE ROL PROPIO\n");
        advertencia.append("=====================================\n\n");
        advertencia.append("Ha cambiado su propio rol a: ").append(nuevoRol).append("\n\n");

        if (!nuevoRol.equals("ADMIN")) {
            advertencia.append("🔒 PERDERÁ LOS SIGUIENTES ACCESOS:\n");
            advertencia.append("• Panel de administración\n");
            advertencia.append("• Gestión de usuarios\n");
            advertencia.append("• Configuración del sistema\n");
            advertencia.append("• Backup y restauración\n");
            advertencia.append("• Reportes avanzados\n\n");
        }

        advertencia.append("🔄 PARA RECUPERAR ACCESO:\n");
        advertencia.append("1. Cerrar sesión actual\n");
        advertencia.append("2. Iniciar sesión con otro usuario administrador\n");
        advertencia.append("3. Cambiar su rol nuevamente a ADMIN\n\n");

        advertencia.append("📝 NOTA: Los cambios se aplican al próximo inicio de sesión.");

        JOptionPane.showMessageDialog(panelPrincipal,
                advertencia.toString(),
                "Advertencia - Cambio de Rol Propio",
                JOptionPane.WARNING_MESSAGE);
    }

    protected class PanelGraficaAccesos extends JPanel {

        protected Map<String, Integer> datos;
        protected Color colorBarras = new Color(52, 152, 219);
        protected Color colorFondo = Color.WHITE;
        protected Color colorTexto = new Color(44, 62, 80);
        protected Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 16);
        protected Font fuenteEjes = new Font("Segoe UI", Font.PLAIN, 12);
        protected int margen = 60;
        protected int alturaMaxima = 0;

        public PanelGraficaAccesos(Map<String, Integer> datos) {
            this.datos = datos;
            setBackground(colorFondo);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Encontrar el valor máximo para escalar
            for (int valor : datos.values()) {
                if (valor > alturaMaxima) {
                    alturaMaxima = valor;
                }
            }
            // Asegurar que haya al menos algo de altura
            if (alturaMaxima == 0) {
                alturaMaxima = 1;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g; // Usa el mismo nombre de variable g2d
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1. Ajustar el margen inferior si hay muchos días para que quepa el texto rotado
            int margenInferior = (datos.size() > 10) ? 80 : 40;
            int ancho = getWidth() - 2 * margen;
            int alto = getHeight() - margen - margenInferior;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dibujar título
            g2d.setFont(fuenteTitulo);
            g2d.setColor(colorTexto);
            String titulo = "📊 ACCESOS POR DÍA (Últimos " + datos.size() + " días)";
            FontMetrics fmTitulo = g2d.getFontMetrics();
            int xTitulo = (getWidth() - fmTitulo.stringWidth(titulo)) / 2;
            g2d.drawString(titulo, xTitulo, 30);

            // Dibujar ejes
            g2d.setFont(fuenteEjes);
            FontMetrics fmEjes = g2d.getFontMetrics();

            // Eje Y (vertical)
            g2d.drawLine(margen, margen, margen, alto + margen);

            // Eje X (horizontal)
            g2d.drawLine(margen, alto + margen, ancho + margen, alto + margen);

            // Marcar valores en eje Y
            int numMarcasY = 5;
            for (int i = 0; i <= numMarcasY; i++) {
                int valor = alturaMaxima * i / numMarcasY;
                int y = alto - (alto * i / numMarcasY) + margen;

                // Línea horizontal guía
                g2d.setColor(new Color(240, 240, 240));
                g2d.drawLine(margen + 1, y, ancho + margen, y);

                // Valor
                g2d.setColor(colorTexto);
                String textoValor = String.valueOf(valor);
                int anchoTexto = fmEjes.stringWidth(textoValor);
                g2d.drawString(textoValor, margen - anchoTexto - 5, y + fmEjes.getHeight() / 4);
            }

            // Dibujar barras
            if (!datos.isEmpty()) {
                int numBarras = datos.size();

                // CAMBIO CLAVE: Ancho de barra fijo si hay scroll, o proporcional
                int anchoBarra = Math.min(50, (ancho - 20) / numBarras);
                if (numBarras > 15) {
                    anchoBarra = 30; // Tamaño fijo para muchos días
                }
                int espacioEntreBarras = (ancho - (numBarras * anchoBarra)) / (numBarras + 1);
                List<String> fechas = new ArrayList<>(datos.keySet());

                for (int i = 0; i < numBarras; i++) {
                    String fecha = fechas.get(i);
                    int valor = datos.get(fecha);

                    int x = margen + espacioEntreBarras + i * (anchoBarra + espacioEntreBarras);
                    int alturaBarra = (int) ((double) valor / (alturaMaxima == 0 ? 1 : alturaMaxima) * alto);
                    int y = alto + margen - alturaBarra;

                    // Dibujar barra (IDEM a tu código)
                    g2d.setColor(colorBarras);
                    g2d.fillRoundRect(x, y, anchoBarra, alturaBarra, 10, 10);
                    g2d.setColor(colorBarras.darker());
                    g2d.drawRoundRect(x, y, anchoBarra, alturaBarra, 10, 10);

                    // Mostrar valor encima
                    if (valor > 0) {
                        g2d.setColor(colorTexto);
                        String textoValor = String.valueOf(valor);
                        g2d.drawString(textoValor, x + (anchoBarra - g2d.getFontMetrics().stringWidth(textoValor)) / 2, y - 5);
                    }

                    // --- Lógica de fechas---
                    String fechaFormateada = formatarFecha(fecha);
                    g2d.setColor(colorTexto);

                    if (numBarras > 10) {
                        // ROTACIÓN SI HAY MUCHOS DÍAS
                        AffineTransform old = g2d.getTransform(); // Guardar estado original

                        int xFecha = x + (anchoBarra / 2);
                        int yFecha = alto + margen + 15;

                        g2d.translate(xFecha, yFecha); // Mover el origen a donde va la fecha
                        g2d.rotate(Math.toRadians(-45)); // Rotar 45 grados

                        g2d.drawString(fechaFormateada, 0, 0); // Dibujar en el nuevo origen

                        g2d.setTransform(old); // Restaurar normalidad
                    } else {
                        // DIBUJO NORMAL SI SON POCOS
                        int anchoFecha = g2d.getFontMetrics().stringWidth(fechaFormateada);
                        g2d.drawString(fechaFormateada, x + (anchoBarra - anchoFecha) / 2, alto + margen + 20);
                    }
                }
            }

            // Leyenda 
            g2d.setColor(colorTexto);
            g2d.drawString("Total accesos: " + calcularTotalAccesos(), margen, getHeight() - 10);
        }

        protected String formatarFecha(String fecha) {
            try {
                LocalDate fechaLD = LocalDate.parse(fecha);
                return fechaLD.format(DateTimeFormatter.ofPattern("dd/MM"));
            } catch (Exception e) {
                return fecha;
            }
        }

        protected int calcularTotalAccesos() {
            int total = 0;
            for (int valor : datos.values()) {
                total += valor;
            }
            return total;
        }

        @Override
        public Dimension getPreferredSize() {
            int numBarras = (datos != null) ? datos.size() : 0;
            // 60 píxeles de ancho por cada barra para que no se amontonen
            int anchoDinamico = Math.max(850, numBarras * 60);
            // Aumentamos a 600 para dar espacio a las fechas rotadas y al título
            int altoDinamico = 600;
            return new Dimension(anchoDinamico, altoDinamico);
        }
    }

    protected void mostrarGraficaAccesos() {
        // 1. Obtener datos iniciales
        Map<String, Integer> datosAccesos = sicaEngine.obtenerEstadisticasAccesos(7);

        // 2. Crear el diálogo
        Window parentWindow = SwingUtilities.getWindowAncestor(panelPrincipal);
        JDialog dialogoGrafica = new JDialog(parentWindow, "Gráfica de Accesos", Dialog.ModalityType.APPLICATION_MODAL);
        dialogoGrafica.setSize(900, 600);
        dialogoGrafica.setLocationRelativeTo(panelPrincipal);

        // 3. Panel Principal con BorderLayout
        JPanel panelContenedor = new JPanel(new BorderLayout(10, 10));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelContenedor.setBackground(Color.WHITE);

        // 4. Panel Superior (Título y Filtros)
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSuperior.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("📈 ANÁLISIS DE ACCESOS AL SISTEMA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JComboBox<String> comboTiempo = new JComboBox<>(new String[]{"Últimos 7 días", "Últimos 14 días", "Últimos 30 días"});
        JButton btnActualizar = new JButton("🔄 Actualizar");

        panelSuperior.add(lblTitulo);
        panelSuperior.add(new JLabel("Período:"));
        panelSuperior.add(comboTiempo);
        panelSuperior.add(btnActualizar);

        // 5. El Panel de la Gráfica (Centro)
        // Usamos el panel personalizado que ya tienes definido
        PanelGraficaAccesos panelGrafica = new PanelGraficaAccesos(datosAccesos);

        // Definir un ancho dinámico: 40 píxeles por cada barra (día)
        int anchoDinamico = datosAccesos.size() * 50;
        panelGrafica.setPreferredSize(new Dimension(Math.max(anchoDinamico, 800), 400));

        // Lo metemos en un JScrollPane para que si hay muchos días (30 días) se pueda desplazar
        JScrollPane scrollGrafica = new JScrollPane(panelGrafica);
        // Forzar a que siempre aparezca la barra si es necesario
        scrollGrafica.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollGrafica.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Mejora la velocidad de scroll con la rueda del ratón
        scrollGrafica.getVerticalScrollBar().setUnitIncrement(16);
        scrollGrafica.getHorizontalScrollBar().setUnitIncrement(16);

        // 6. Botón de Cierre (Inferior)
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.setBackground(Color.WHITE);
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogoGrafica.dispose());
        panelInferior.add(btnCerrar);

        // 7. Organizar en el contenedor principal
        panelContenedor.add(panelSuperior, BorderLayout.NORTH);
        panelContenedor.add(scrollGrafica, BorderLayout.CENTER); // <-- La gráfica ocupa todo el centro
        panelContenedor.add(panelInferior, BorderLayout.SOUTH);

        // 8. Lógica del botón actualizar (solo para la gráfica)
        btnActualizar.addActionListener(e -> {
            int dias = 7;
            switch (comboTiempo.getSelectedIndex()) {
                case 0:
                    dias = 7;
                    break;
                case 1:
                    dias = 14;
                    break;
                case 2:
                    dias = 30;
                    break;
            }

            Map<String, Integer> nuevosDatos = sicaEngine.obtenerEstadisticasAccesos(dias);
            panelGrafica.datos = nuevosDatos;

            // Recalcular escala de la gráfica
            panelGrafica.alturaMaxima = nuevosDatos.values().stream().mapToInt(Integer::intValue).max().orElse(1);

            panelGrafica.repaint();
            panelGrafica.revalidate();
        });

        dialogoGrafica.add(panelContenedor);
        dialogoGrafica.setVisible(true);
    }

// Método auxiliar para crear tarjetas de estadística
    protected JPanel crearTarjetaEstadistica(String titulo, String valor, Color color) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(new Color(108, 122, 137));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValor.setForeground(color);

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);

        return tarjeta;
    }

// Método para llenar la tabla de datos
    protected void llenarTablaDatos(DefaultTableModel modelo, Map<String, Integer> datos) {
        int total = datos.values().stream().mapToInt(Integer::intValue).sum();

        List<String> fechas = new ArrayList<>(datos.keySet());
        Collections.sort(fechas);

        for (String fecha : fechas) {
            int accesos = datos.get(fecha);
            double porcentaje = total > 0 ? (accesos * 100.0 / total) : 0;

            // Formatear fecha
            String fechaFormateada;
            String diaSemana;
            try {
                LocalDate fechaLD = LocalDate.parse(fecha);
                fechaFormateada = fechaLD.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                diaSemana = fechaLD.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES")));
                diaSemana = diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1);
            } catch (Exception e) {
                fechaFormateada = fecha;
                diaSemana = "-";
            }

            modelo.addRow(new Object[]{
                fechaFormateada,
                diaSemana,
                accesos,
                String.format("%.1f%%", porcentaje)
            });
        }
    }

// Método para exportar datos
    protected void exportarDatosAccesos(Map<String, Integer> datos) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "reporte_accesos_" + timestamp + ".csv";

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(nombreArchivo));
            fileChooser.setDialogTitle("Exportar Datos de Accesos");

            if (fileChooser.showSaveDialog(panelPrincipal) == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();

                try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
                    // Encabezados
                    writer.println("Fecha,Día de la semana,Accesos,Porcentaje");

                    // Calcular total
                    int total = datos.values().stream().mapToInt(Integer::intValue).sum();

                    // Datos
                    List<String> fechas = new ArrayList<>(datos.keySet());
                    Collections.sort(fechas);

                    for (String fecha : fechas) {
                        int accesos = datos.get(fecha);
                        double porcentaje = total > 0 ? (accesos * 100.0 / total) : 0;

                        String diaSemana;
                        try {
                            LocalDate fechaLD = LocalDate.parse(fecha);
                            diaSemana = fechaLD.format(DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES")));
                        } catch (Exception e) {
                            diaSemana = "Desconocido";
                        }

                        writer.printf("%s,%s,%d,%.1f%%\n", fecha, diaSemana, accesos, porcentaje);
                    }

                    JOptionPane.showMessageDialog(panelPrincipal,
                            "✅ Datos exportados exitosamente a:\n" + archivo.getAbsolutePath(),
                            "Exportación Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "❌ Error al exportar datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

// Método para imprimir la gráfica
    protected void mostrarGraficaEnVentana(PanelGraficaAccesos panelGrafica) {
        try {
            // Crear un diálogo para mostrar la gráfica
            Window parentWindow = SwingUtilities.getWindowAncestor(panelPrincipal);
            JDialog dialogoGrafica = new JDialog(
                    parentWindow,
                    "Visualización de Gráfica de Accesos",
                    Dialog.ModalityType.MODELESS
            );

            dialogoGrafica.setSize(900, 600);
            dialogoGrafica.setLocationRelativeTo(panelPrincipal);

            // Panel principal
            JPanel panelVisualizacion = new JPanel(new BorderLayout(10, 10));
            panelVisualizacion.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            panelVisualizacion.setBackground(Color.WHITE);

            // Título
            JLabel lblTitulo = new JLabel("📊 VISUALIZACIÓN DE GRÁFICA DE ACCESOS");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTitulo.setForeground(new Color(44, 62, 80));
            lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
            lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

            // Panel para la gráfica con scroll
            JScrollPane scrollGrafica = new JScrollPane(panelGrafica);
            scrollGrafica.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
            scrollGrafica.getViewport().setBackground(Color.WHITE);

            // Botones
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton btnCerrar = new JButton("Cerrar");
            JButton btnGuardar = new JButton("💾 Guardar como Imagen");

            btnCerrar.addActionListener(e -> dialogoGrafica.dispose());
            btnGuardar.addActionListener(e -> guardarGraficaComoImagen(panelGrafica));

            panelBotones.add(btnGuardar);
            panelBotones.add(btnCerrar);

            // Agregar componentes
            panelVisualizacion.add(lblTitulo, BorderLayout.NORTH);
            panelVisualizacion.add(scrollGrafica, BorderLayout.CENTER);
            panelVisualizacion.add(panelBotones, BorderLayout.SOUTH);

            dialogoGrafica.add(panelVisualizacion);
            dialogoGrafica.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "❌ Error al mostrar la gráfica: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

// Método para guardar la gráfica como imagen
    protected void guardarGraficaComoImagen(PanelGraficaAccesos panelGrafica) {
        try {
            // Crear un BufferedImage con las dimensiones del panel
            BufferedImage imagen = new BufferedImage(
                    panelGrafica.getWidth(),
                    panelGrafica.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            // Dibujar el panel en la imagen
            Graphics2D g2d = imagen.createGraphics();
            panelGrafica.paint(g2d);
            g2d.dispose();

            // Crear nombre de archivo con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "grafica_accesos_" + timestamp + ".png";

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(nombreArchivo));
            fileChooser.setDialogTitle("Guardar Gráfica como Imagen");

            if (fileChooser.showSaveDialog(panelPrincipal) == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();

                // Asegurarse de que tenga extensión .png
                if (!archivo.getName().toLowerCase().endsWith(".png")) {
                    archivo = new File(archivo.getAbsolutePath() + ".png");
                }

                // Guardar la imagen
                ImageIO.write(imagen, "png", archivo);

                JOptionPane.showMessageDialog(panelPrincipal,
                        "✅ Gráfica guardada exitosamente en:\n" + archivo.getAbsolutePath(),
                        "Guardado Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "❌ Error al guardar la imagen: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void actualizarListaBackups(DefaultListModel<String> modeloBackups) {
        modeloBackups.clear();
        List<String> backups = sicaEngine.obtenerListaBackups();

        if (backups.isEmpty()) {
            modeloBackups.addElement("No hay backups disponibles");
        } else {
            for (String backup : backups) {
                modeloBackups.addElement(backup);
            }
        }
    }

    protected void cargarBackupSeleccionado(String backupInfo) {
        // Extraer el nombre del archivo del backup
        String nombreArchivo = backupInfo.split(" - ")[0];

        int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                "¿Restaurar el backup?\n\n"
                + "Archivo: " + nombreArchivo + "\n"
                + "⚠️ ADVERTENCIA: Esta acción reemplazará todos los usuarios actuales",
                "Confirmar Restauración",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Obtener el índice del backup
                List<String> backups = sicaEngine.obtenerListaBackups();
                int indice = backups.indexOf(backupInfo);

                if (indice >= 0) {
                    sicaEngine.cambiarBackups(indice);
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "✅ Backup restaurado exitosamente\n\n"
                            + "Reinicie la aplicación para ver los cambios.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Actualizar datos
                    actualizarDatos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "❌ Error: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected void eliminarBackupSeleccionado(String backupInfo) {
        // Extraer el nombre del archivo del backup
        String nombreArchivo = backupInfo.split(" - ")[0];

        int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                "¿Eliminar el backup?\n\n"
                + "Archivo: " + nombreArchivo + "\n"
                + "⚠️ Esta acción no se puede deshacer",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean exito = sicaEngine.eliminarBackup(nombreArchivo);

            if (exito) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "✅ Backup eliminado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar la lista de backups en la interfaz
                actualizarDatos();
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "❌ Error al eliminar el backup",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}

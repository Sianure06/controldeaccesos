/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.mycompany.controldeaccesos.EncriptadorContrasena;
import com.mycompany.controldeaccesos.PreguntasSeguridad;
import com.mycompany.controldeaccesos.core.SicaEngine;
import com.mycompany.controldeaccesos.core.SicaEngine.ResultadoAutenticacion;
import com.mycompany.controldeaccesos.core.SicaEngine.ResultadoRegistro;
import com.mycompany.controldeaccesos.models.UsuarioModel;

public class VentanaPrincipal extends JFrame {

    private SicaEngine sicaEngine;
    private DefaultTableModel model;
    private javax.swing.JTable table;

    public VentanaPrincipal() {
        sicaEngine = SicaEngine.getInstancia();
        GestorIcono.aplicarIcono(this);
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("SICA - Sistema de Control de Acceso con Patrones de Diseño");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cargar ícono de la ventana
        cargarIconoVentana();

        // Crear panel principal con layout
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Panel superior con icono grande y título
        JPanel panelSuperior = crearPanelSuperior();
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // Panel central con fondo y login
        JPanel panelCentral = crearPanelCentral();
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con información
        JPanel panelInferior = crearPanelInferior();
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        // Cerrar conexión al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sicaEngine.desconectar();
            }
        });
    }

    private void cargarIconoVentana() {
        java.net.URL iconUrl = getClass().getResource("/icons/lock.png");
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            // Redimensionar para icono de ventana (más pequeño)
            Image iconImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            setIconImage(iconImage);
        } else {
            System.err.println("Icono no encontrado en classpath: /icons/lock.png");
        }
    }

    private JPanel crearPanelSuperior() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(250, 250, 252)); // Gris muy claro

        // Panel para el icono y título
        JPanel panelContenido = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelContenido.setBackground(new Color(44, 62, 80));

        // Cargar y mostrar icono grande
        JLabel lblIcono = crearIconoGrande();
        panelContenido.add(lblIcono);

        // Título del sistema
        JLabel lblTitulo = new JLabel("SISTEMA DE CONTROL DE ACCESO - SICA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelContenido.add(lblTitulo);

        panel.add(panelContenido, BorderLayout.CENTER);

        return panel;
    }

    private JLabel crearIconoGrande() {
        JLabel lblIcono = new JLabel();
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);

        java.net.URL iconUrl = getClass().getResource("/icons/lock.png");
        if (iconUrl != null) {
            try {
                // Cargar icono y redimensionar para mostrarlo grande
                ImageIcon originalIcon = new ImageIcon(iconUrl);
                Image iconImage = originalIcon.getImage()
                        .getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                ImageIcon iconGrande = new ImageIcon(iconImage);
                lblIcono.setIcon(iconGrande);

                // Tooltip
                lblIcono.setToolTipText("SICA - Sistema Integrado de Control de Acceso");

                // Efecto hover
                lblIcono.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        lblIcono.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        // Efecto de brillo (cambiar imagen)
                        ImageIcon iconBrillo = new ImageIcon(iconUrl);
                        Image iconBrilloImage = iconBrillo.getImage()
                                .getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                        lblIcono.setIcon(new ImageIcon(iconBrilloImage));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        lblIcono.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        lblIcono.setIcon(iconGrande);
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Al hacer clic en el icono
                        JOptionPane.showMessageDialog(VentanaPrincipal.this,
                                "SICA v2.0\n"
                                + "Sistema Integrado de Control de Acceso\n"
                                + "Arquitectura con Patrones de Diseño\n\n"
                                + "© 2026 - Todos los derechos reservados",
                                "Acerca de SICA",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            } catch (Exception e) {
                // Si hay error al cargar la imagen, mostrar emoji
                lblIcono.setText("🔐");
                lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 48));
                lblIcono.setForeground(new Color(52, 152, 219));
            }
        } else {
            // Si no encuentra el archivo, mostrar emoji
            lblIcono.setText("🔐");
            lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 48));
            lblIcono.setForeground(new Color(52, 152, 219));
            lblIcono.setToolTipText("Icono no encontrado - Usando emoji alternativo");
        }

        return lblIcono;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(250, 250, 252));

        JPanel panelLogin = crearPanelLogin();
        panel.add(panelLogin, new GridBagConstraints());

        return panel;
    }

    private JPanel crearPanelLogin() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(250, 250, 252)); // Gris muy claro
        // ... resto de tu código sin cambios ...
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(44, 62, 80, 120), 2, true),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // ========== ENCABEZADO DEL LOGIN ==========
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;

        // Icono pequeño dentro del panel de login
        JPanel panelIconoLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelIconoLogin.setOpaque(false);

        JLabel lblIconoLogin = crearIconoLoginPequeño();
        panelIconoLogin.add(lblIconoLogin);

        panel.add(panelIconoLogin, gbc);

        // Título del login
        gbc.gridy = 1;
        JLabel lblTituloLogin = new JLabel("INICIO DE SESIÓN", SwingConstants.CENTER);
        lblTituloLogin.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloLogin.setForeground(new Color(44, 62, 80));
        panel.add(lblTituloLogin, gbc);

        // Subtítulo
        gbc.gridy = 2;
        JLabel lblSubtituloLogin = new JLabel("Ingrese sus credenciales para acceder al sistema", SwingConstants.CENTER);
        lblSubtituloLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtituloLogin.setForeground(new Color(108, 122, 137));
        lblSubtituloLogin.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(lblSubtituloLogin, gbc);

        // ========== CAMPOS DEL FORMULARIO ==========
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Usuario
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsuario.setForeground(new Color(44, 62, 80));
        panel.add(lblUsuario, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JTextField usuarioField = new JTextField(20);
        usuarioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usuarioField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panel.add(usuarioField, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(new Color(44, 62, 80));
        panel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        // Botón para mostrar/ocultar contraseña
        JToggleButton btnMostrarPass = new JToggleButton(" 👁 ️");
        btnMostrarPass.setMargin(new Insets(2, 8, 2, 8));
        btnMostrarPass.setToolTipText("Mostrar/ocultar contraseña");
        btnMostrarPass.setFocusPainted(false);

        JPanel panelPassword = new JPanel(new BorderLayout());
        panelPassword.setOpaque(false);
        panelPassword.add(passwordField, BorderLayout.CENTER);
        panelPassword.add(btnMostrarPass, BorderLayout.EAST);

        panel.add(panelPassword, gbc);

        // ========== BOTONES ==========
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 5, 5);

        JButton btnLogin = crearBoton("Iniciar Sesión", new Color(46, 204, 113)); // Verde
        btnLogin.setForeground(new Color(44, 62, 80));
        panel.add(btnLogin, gbc);

        gbc.gridx = 2;
        JButton btnSalir = crearBoton("Salir", new Color(231, 76, 60)); // Rojo
        btnSalir.setForeground(new Color(44, 62, 80));
        panel.add(btnSalir, gbc);

        // ========== BOTONES ADICIONALES ==========
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 5, 5, 5);

        JButton btnRegistrar = crearBotonSecundario("Registrar Nuevo Usuario");
        panel.add(btnRegistrar, gbc);

        gbc.gridx = 2;
        JButton btnRecuperar = crearBotonSecundario("Recuperar Contraseña");
        panel.add(btnRecuperar, gbc);

        // ========== LISTENERS ==========
        // Mostrar/ocultar contraseña
        btnMostrarPass.addActionListener(e -> {
            if (btnMostrarPass.isSelected()) {
                passwordField.setEchoChar((char) 0);
                btnMostrarPass.setText(" 🔒 ");
                btnMostrarPass.setToolTipText("Ocultar contraseña");
            } else {
                passwordField.setEchoChar('•');
                btnMostrarPass.setText(" 👁 ️");
                btnMostrarPass.setToolTipText("Mostrar contraseña");
            }
        });

        // Enter en campo de usuario pasa a contraseña
        usuarioField.addActionListener(e -> passwordField.requestFocus());

        // Enter en campo de contraseña hace login
        passwordField.addActionListener(e -> btnLogin.doClick());

        // Acción Salir
        btnSalir.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    VentanaPrincipal.this,
                    "¿Está seguro que desea salir del sistema?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                sicaEngine.desconectar();
                System.exit(0);
            }
        });

        // Acción Login
        btnLogin.addActionListener(e -> {
            String usuario = usuarioField.getText().trim();
            String contrasena = new String(passwordField.getPassword());

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                mostrarMensajeError("Complete todos los campos");
                return;
            }

            ResultadoAutenticacion resultado = sicaEngine.autenticarUsuario(usuario, contrasena);

            if (resultado.isExito()) {
                UsuarioModel usuarioAutenticado = resultado.getUsuario();
                mostrarMensajeExito(resultado.getMensaje());

                // Crear ventana personalizada según tipo de usuario
                VentanaUsuarioInterface ventanaUsuario = FabricaVentanasUsuario.crearVentana(usuarioAutenticado);
                ventanaUsuario.cargarDatosUsuario(usuarioAutenticado);
                ventanaUsuario.configurarEventos();

                // Crear nueva ventana
                JFrame ventanaPersonalizada = new JFrame(ventanaUsuario.getTituloVentana());
                GestorIcono.aplicarIcono(ventanaPersonalizada);

                ventanaPersonalizada.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Cambiado
                ventanaPersonalizada.setContentPane(ventanaUsuario.getPanelPrincipal());
                ventanaPersonalizada.setSize(1200, 700);
                ventanaPersonalizada.setLocationRelativeTo(null);

                // Configurar cierre de ventana personalizada
                ventanaPersonalizada.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        cerrarSesionYVolver(ventanaPersonalizada);
                    }
                });

                // Configurar botón cerrar sesión
                configurarBotonCerrarSesion(ventanaPersonalizada, ventanaUsuario);

                // Mostrar ventana personalizada y ocultar login
                ventanaPersonalizada.setVisible(true);
                VentanaPrincipal.this.setVisible(false);

                // Limpiar campos del login
                usuarioField.setText("");
                passwordField.setText("");

            } else {
                mostrarMensajeError(resultado.getMensaje());
                passwordField.setText("");
                passwordField.requestFocus();
            }
        });

        // Otras acciones
        btnRegistrar.addActionListener(e -> mostrarVentanaRegistro());
        btnRecuperar.addActionListener(e -> mostrarVentanaRecuperacion());

        return panel;
    }

    private void cerrarSesionYVolver(JFrame ventanaPersonalizada) {
        int confirmacion = JOptionPane.showConfirmDialog(
                ventanaPersonalizada,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            ventanaPersonalizada.dispose();
            VentanaPrincipal.this.setVisible(true);

            // Mensaje opcional de confirmación
            JOptionPane.showMessageDialog(
                    VentanaPrincipal.this,
                    "Sesión cerrada correctamente",
                    "Cierre de sesión",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        // Si elige NO, no hacer nada - mantener la ventana personalizada abierta
    }

    private JLabel crearIconoLoginPequeño() {
        JLabel lblIcono = new JLabel();
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);

        java.net.URL iconUrl = getClass().getResource("/icons/lock.png");
        if (iconUrl != null) {
            try {
                ImageIcon originalIcon = new ImageIcon(iconUrl);
                Image iconImage = originalIcon.getImage()
                        .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                lblIcono.setIcon(new ImageIcon(iconImage));
            } catch (Exception e) {
                lblIcono.setText("🔐");
                lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 32));
                lblIcono.setForeground(new Color(52, 152, 219));
            }
        } else {
            lblIcono.setText("🔐");
            lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 32));
            lblIcono.setForeground(new Color(52, 152, 219));
        }

        return lblIcono;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
            }
        });

        return boton;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        boton.setForeground(new Color(52, 152, 219));
        boton.setBackground(new Color(236, 240, 241));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(225, 230, 235));
                boton.setForeground(new Color(41, 128, 185));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(236, 240, 241));
                boton.setForeground(new Color(52, 152, 219));
            }
        });

        return boton;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Información izquierda
        JLabel lblInfo = new JLabel("SICA v2.0 - Sistema Integrado de Control de Acceso");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(189, 195, 199));
        panel.add(lblInfo, BorderLayout.WEST);

        // Información derecha
        JLabel lblDerechos = new JLabel("© 2026 - Todos los derechos reservados");
        lblDerechos.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblDerechos.setForeground(new Color(149, 165, 166));
        lblDerechos.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblDerechos, BorderLayout.EAST);

        return panel;
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarVentanaRegistro() {
        JDialog dialogo = new JDialog(this, "Registro de Nuevo Usuario", true);
        dialogo.setSize(500, 550);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // Campos del formulario
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nombre de usuario:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JTextField txtUsuario = new JTextField(15);
        panel.add(txtUsuario, gbc);

        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JPasswordField txtPass = new JPasswordField(15);

        // Botón para mostrar/ocultar contraseña
        JToggleButton btnMostrarPass1 = new JToggleButton("👁️");
        btnMostrarPass1.setToolTipText("Mostrar/ocultar contraseña");

        JPanel panelPass1 = new JPanel(new GridBagLayout());
        panelPass1.setOpaque(false);
        GridBagConstraints gbcPass1 = new GridBagConstraints();
        gbcPass1.fill = GridBagConstraints.BOTH;
        gbcPass1.weightx = 1.0;

        gbcPass1.gridx = 0;
        gbcPass1.gridy = 0;
        panelPass1.add(txtPass, gbcPass1);
        gbcPass1.gridx = 1;
        gbcPass1.gridy = 0;
        gbcPass1.weightx = 0;
        gbcPass1.fill = GridBagConstraints.NONE;
        panelPass1.add(btnMostrarPass1, gbcPass1);

        panel.add(panelPass1, gbc);

        row++;

        // INDICADOR DE FORTALEZA
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        JPanel panelFortaleza = new JPanel(new GridBagLayout());
        GridBagConstraints gbcFort = new GridBagConstraints();

        JProgressBar barraFortaleza = new JProgressBar(0, 100);
        barraFortaleza.setStringPainted(true);
        barraFortaleza.setPreferredSize(new Dimension(300, 20));

        JLabel lblNivelFortaleza = new JLabel(" ");
        lblNivelFortaleza.setFont(new Font("Arial", Font.BOLD, 11));

        gbcFort.gridx = 0;
        gbcFort.gridy = 0;
        gbcFort.fill = GridBagConstraints.HORIZONTAL;
        gbcFort.weightx = 1.0;
        panelFortaleza.add(barraFortaleza, gbcFort);

        gbcFort.gridx = 1;
        gbcFort.gridy = 0;
        gbcFort.weightx = 0;
        panelFortaleza.add(lblNivelFortaleza, gbcFort);

        panel.add(panelFortaleza, gbc);

        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Confirmar contraseña:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JPasswordField txtPassConfirm = new JPasswordField(15);
        JToggleButton btnMostrarPass2 = new JToggleButton("👁️");
        btnMostrarPass2.setToolTipText("Mostrar/ocultar contraseña");

        JPanel panelPass2 = new JPanel(new GridBagLayout());
        panelPass2.setOpaque(false);
        GridBagConstraints gbcPass2 = new GridBagConstraints();
        gbcPass2.fill = GridBagConstraints.BOTH;
        gbcPass2.weightx = 1.0;

        gbcPass2.gridx = 0;
        gbcPass2.gridy = 0;
        panelPass2.add(txtPassConfirm, gbcPass2);
        gbcPass2.gridx = 1;
        gbcPass2.gridy = 0;
        gbcPass2.weightx = 0;
        gbcPass2.fill = GridBagConstraints.NONE;
        panelPass2.add(btnMostrarPass2, gbcPass2);

        panel.add(panelPass2, gbc);

        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JTextField txtNombre = new JTextField(15);
        panel.add(txtNombre, gbc);

        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JTextField txtEmail = new JTextField(15);
        panel.add(txtEmail, gbc);

        row++;

        // PREGUNTA SECRETA - ComboBox con preguntas predefinidas
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Pregunta de seguridad:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JComboBox<String> comboPreguntas = new JComboBox<>();
        comboPreguntas.addItem("-- Seleccione una pregunta --");
        for (String pregunta : PreguntasSeguridad.getPreguntasPredefinidas()) {
            comboPreguntas.addItem(pregunta);
        }
        comboPreguntas.addItem("-- Escribir pregunta personalizada --");
        panel.add(comboPreguntas, gbc);

        row++;

        // Campo para pregunta personalizada (inicialmente deshabilitado)
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Pregunta personalizada:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JTextField txtPreguntaPersonal = new JTextField(15);
        txtPreguntaPersonal.setEnabled(false);
        panel.add(txtPreguntaPersonal, gbc);

        row++;

        // RESPUESTA SECRETA
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Respuesta secreta:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JPasswordField txtRespuestaSecreta = new JPasswordField(15);

        // Botón para mostrar/ocultar respuesta secreta
        JToggleButton btnMostrarRespuesta = new JToggleButton("👁️");
        btnMostrarRespuesta.setToolTipText("Mostrar/ocultar respuesta");

        JPanel panelRespuesta = new JPanel(new GridBagLayout());
        panelRespuesta.setOpaque(false);
        GridBagConstraints gbcResp = new GridBagConstraints();
        gbcResp.fill = GridBagConstraints.BOTH;
        gbcResp.weightx = 1.0;

        gbcResp.gridx = 0;
        gbcResp.gridy = 0;
        panelRespuesta.add(txtRespuestaSecreta, gbcResp);
        gbcResp.gridx = 1;
        gbcResp.gridy = 0;
        gbcResp.weightx = 0;
        gbcResp.fill = GridBagConstraints.NONE;
        panelRespuesta.add(btnMostrarRespuesta, gbcResp);

        panel.add(panelRespuesta, gbc);

        row++;

        // Botones
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        JPanel panelBotones = new JPanel();
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        panel.add(panelBotones, gbc);

        // Listeners para mostrar/ocultar
        btnMostrarPass1.addActionListener(e -> {
            if (btnMostrarPass1.isSelected()) {
                txtPass.setEchoChar((char) 0);
                btnMostrarPass1.setText("🔒");
            } else {
                txtPass.setEchoChar('•');
                btnMostrarPass1.setText("👁️");
            }
        });

        btnMostrarPass2.addActionListener(e -> {
            if (btnMostrarPass2.isSelected()) {
                txtPassConfirm.setEchoChar((char) 0);
                btnMostrarPass2.setText("🔒");
            } else {
                txtPassConfirm.setEchoChar('•');
                btnMostrarPass2.setText("👁️");
            }
        });

        btnMostrarRespuesta.addActionListener(e -> {
            if (btnMostrarRespuesta.isSelected()) {
                txtRespuestaSecreta.setEchoChar((char) 0);
                btnMostrarRespuesta.setText("🔒");
            } else {
                txtRespuestaSecreta.setEchoChar('•');
                btnMostrarRespuesta.setText("👁️");
            }
        });

        // Listener para el combobox de preguntas
        comboPreguntas.addActionListener(e -> {
            String seleccion = (String) comboPreguntas.getSelectedItem();
            if ("-- Escribir pregunta personalizada --".equals(seleccion)) {
                txtPreguntaPersonal.setEnabled(true);
                txtPreguntaPersonal.setText("");
                txtPreguntaPersonal.requestFocus();
            } else if (!"-- Seleccione una pregunta --".equals(seleccion)) {
                txtPreguntaPersonal.setEnabled(false);
                txtPreguntaPersonal.setText(seleccion);
            }
        });

        // Listener para fortaleza de contraseña
        txtPass.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                actualizarFortaleza();
            }

            public void removeUpdate(DocumentEvent e) {
                actualizarFortaleza();
            }

            public void changedUpdate(DocumentEvent e) {
                actualizarFortaleza();
            }

            private void actualizarFortaleza() {
                String password = new String(txtPass.getPassword());
                int fortaleza = EncriptadorContrasena.calcularFortalezaContrasena(password);
                String nivel = EncriptadorContrasena.obtenerNivelFortaleza(password);
                Color color = EncriptadorContrasena.obtenerColorFortaleza(password);

                barraFortaleza.setValue(fortaleza);
                barraFortaleza.setForeground(color);
                barraFortaleza.setString(nivel + " (" + fortaleza + "%)");
                lblNivelFortaleza.setText(nivel);
                lblNivelFortaleza.setForeground(color);
            }
        });

        // Acción del botón Registrar
        btnRegistrar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String contrasena = new String(txtPass.getPassword());
            String contrasenaConfirm = new String(txtPassConfirm.getPassword());
            String nombreCompleto = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();

            // Determinar pregunta secreta
            String preguntaSecreta = "";
            if (txtPreguntaPersonal.isEnabled()) {
                preguntaSecreta = txtPreguntaPersonal.getText().trim();
            } else {
                preguntaSecreta = (String) comboPreguntas.getSelectedItem();
            }

            String respuestaSecreta = new String(txtRespuestaSecreta.getPassword());

            // Validaciones
            if (usuario.isEmpty() || contrasena.isEmpty() || nombreCompleto.isEmpty()
                    || email.isEmpty() || preguntaSecreta.isEmpty() || respuestaSecreta.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Complete todos los campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!contrasena.equals(contrasenaConfirm)) {
                JOptionPane.showMessageDialog(dialogo, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (preguntaSecreta.equals("-- Seleccione una pregunta --")) {
                JOptionPane.showMessageDialog(dialogo, "Seleccione una pregunta de seguridad", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar fortaleza de contraseña
            String erroresPassword = EncriptadorContrasena.validarFortalezaContrasena(contrasena);
            if (erroresPassword != null) {
                int fortaleza = EncriptadorContrasena.calcularFortalezaContrasena(contrasena);
                if (fortaleza < 60) {
                    int opcion = JOptionPane.showConfirmDialog(dialogo,
                            "La contraseña es débil. ¿Desea continuar de todos modos?\n\n" + erroresPassword,
                            "Contraseña débil", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (opcion != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }

            ResultadoRegistro resultado = sicaEngine.registrarUsuario(
                    usuario, contrasena, nombreCompleto, email, preguntaSecreta, respuestaSecreta
            );

            if (resultado.isExito()) {
                JOptionPane.showMessageDialog(dialogo,
                        resultado.getMensaje() + "\nRecuerde su pregunta secreta para recuperar la contraseña.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogo, resultado.getMensaje(),
                        "Error en registro", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void mostrarVentanaRecuperacion() {
        JDialog dialogo = new JDialog(this, "Recuperación de Contraseña", true);
        dialogo.setSize(500, 400);
        dialogo.setLocationRelativeTo(this);

        // Panel con pestañas para los diferentes pasos
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);

        // Variables compartidas entre pestañas
        String[] usuarioActual = new String[1];
        String[] codigoGenerado = new String[1];
        String[] respuestaVerificada = new String[1]; // NUEVO: para almacenar la respuesta verificada

        // Se crean los paneles sin agregar al tabbedPane
        JPanel panelPaso1 = crearPanelPaso1(dialogo, tabbedPane, usuarioActual);
        JPanel panelPaso2 = crearPanelPaso2(dialogo, tabbedPane, usuarioActual, codigoGenerado, respuestaVerificada); // Pasar respuestaVerificada
        JPanel panelPaso3 = crearPanelPaso3(dialogo, tabbedPane, usuarioActual, codigoGenerado);
        JPanel panelPaso4 = crearPanelPaso4(dialogo, tabbedPane, usuarioActual, respuestaVerificada); // Pasar respuestaVerificada

        // Aqui se agregan los paneles
        tabbedPane.addTab("1. Identificación", panelPaso1);
        tabbedPane.addTab("2. Pregunta Secreta", panelPaso2);
        tabbedPane.addTab("3. Código (Opcional)", panelPaso3);
        tabbedPane.addTab("4. Nueva Contraseña", panelPaso4);

        // Inicialmente solo la primera pestaña está habilitada
        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, false);
        }

        dialogo.add(tabbedPane);
        dialogo.setVisible(true);
    }

    private JPanel crearPanelPaso1(JDialog dialogo, JTabbedPane tabbedPane, String[] usuarioActual) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblInfo = new JLabel("<html><b>Paso 1:</b> Ingrese su nombre de usuario o email</html>");
        panel.add(lblInfo, gbc);

        gbc.gridy = 1;
        panel.add(new JLabel("Usuario o Email:"), gbc);

        gbc.gridy = 2;
        JTextField txtIdentificacion = new JTextField(20);
        panel.add(txtIdentificacion, gbc);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnContinuar = new JButton("Continuar");
        JButton btnCancelar = new JButton("Cancelar");

        btnContinuar.addActionListener(e -> {
            String identificacion = txtIdentificacion.getText().trim();

            if (identificacion.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Ingrese su usuario o email", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar si el usuario existe
            String username = identificacion;
            if (identificacion.contains("@")) {
                // Es un email, buscar usuario
                List<UsuarioModel> usuarios = sicaEngine.obtenerTodosUsuarios();
                for (UsuarioModel usuario : usuarios) {
                    if (usuario.getEmail().equals(identificacion)) {
                        username = usuario.getUsername();
                        break;
                    }
                }
            }

            if (!sicaEngine.usuarioExiste(username)) {
                JOptionPane.showMessageDialog(dialogo, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si tiene pregunta secreta configurada
            if (!sicaEngine.tienePreguntaSecreta(username)) {
                JOptionPane.showMessageDialog(dialogo,
                        "Este usuario no tiene pregunta secreta configurada.\n"
                        + "Contacte al administrador del sistema.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Guardar usuario y avanzar al siguiente paso
            usuarioActual[0] = username;
            dialogo.setTitle("Recuperación - Usuario: " + username);
            tabbedPane.setEnabledAt(1, true);
            tabbedPane.setSelectedIndex(1);

            // Cargar pregunta secreta en el paso 2
            cargarPreguntaSecreta(username, tabbedPane);
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        panelBotones.add(btnContinuar);
        panelBotones.add(btnCancelar);

        gbc.gridy = 3;
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JPanel crearPanelPaso2(JDialog dialogo, JTabbedPane tabbedPane, String[] usuarioActual,
            String[] codigoGenerado, String[] respuestaVerificada) { // Agregar respuestaVerificada
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblPregunta = new JLabel("Cargando pregunta secreta...");
        lblPregunta.setName("lblPregunta");
        panel.add(lblPregunta, gbc);

        gbc.gridy = 1;
        panel.add(new JLabel("Respuesta secreta:"), gbc);

        gbc.gridy = 2;
        JPasswordField txtRespuesta = new JPasswordField(20);
        txtRespuesta.setName("txtRespuesta");
        panel.add(txtRespuesta, gbc);

        JPanel panelBotones = new JPanel();
        JButton btnVerificar = new JButton("Verificar Respuesta");
        JButton btnAtras = new JButton("Atrás");
        JButton btnEnviarCodigo = new JButton("Enviar Código al Email");

        btnVerificar.addActionListener(e -> {
            String respuesta = new String(txtRespuesta.getPassword());
            String username = usuarioActual[0];

            if (respuesta.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Ingrese la respuesta secreta", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar respuesta secreta
            if (sicaEngine.verificarRespuestaSecreta(username, respuesta)) {
                // ALMACENAR LA RESPUESTA VERIFICADA
                respuestaVerificada[0] = respuesta;

                JOptionPane.showMessageDialog(dialogo,
                        "¡Respuesta correcta! Puede continuar para cambiar su contraseña.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Habilitar paso 4
                tabbedPane.setEnabledAt(3, true);

                // HABILITAR CAMPOS DEL PASO 4
                JPanel panelPaso4 = (JPanel) tabbedPane.getComponentAt(3);
                Runnable habilitarCampos = (Runnable) panelPaso4.getClientProperty("habilitarCampos");
                if (habilitarCampos != null) {
                    habilitarCampos.run();
                } else {
                    // Si no se encuentra el Runnable, habilitar directamente
                    Component[] components = panelPaso4.getComponents();
                    for (Component comp : components) {
                        if (comp instanceof JPasswordField) {
                            comp.setEnabled(true);
                        }
                    }
                }

                tabbedPane.setSelectedIndex(3);
            } else {
                JOptionPane.showMessageDialog(dialogo,
                        "Respuesta incorrecta. Intente nuevamente.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                txtRespuesta.setText("");
                txtRespuesta.requestFocus();
            }
        });

        btnEnviarCodigo.addActionListener(e -> {
            String username = usuarioActual[0];

            // Generar y mostrar código de recuperación
            String codigo = sicaEngine.generarCodigoRecuperacion(username);

            if (codigo != null) {
                codigoGenerado[0] = codigo;

                JOptionPane.showMessageDialog(dialogo,
                        "Código de verificación generado: " + codigo + "\n"
                        + "(En un sistema real, este código sería enviado a su email)\n"
                        + "Este código expira en 15 minutos.",
                        "Código de Verificación", JOptionPane.INFORMATION_MESSAGE);

                // Habilitar paso 3
                tabbedPane.setEnabledAt(2, true);
                tabbedPane.setSelectedIndex(2);
            } else {
                JOptionPane.showMessageDialog(dialogo,
                        "Error al generar código de verificación",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAtras.addActionListener(e -> tabbedPane.setSelectedIndex(0));

        panelBotones.add(btnVerificar);
        panelBotones.add(btnEnviarCodigo);
        panelBotones.add(btnAtras);

        gbc.gridy = 3;
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JPanel crearPanelPaso3(JDialog dialogo, JTabbedPane tabbedPane, String[] usuarioActual, String[] codigoGenerado) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblInfo = new JLabel("<html>Ingrese el código de verificación de 6 dígitos<br>"
                + "que se mostró en el paso anterior</html>");
        panel.add(lblInfo, gbc);

        gbc.gridy = 1;
        panel.add(new JLabel("Código (6 dígitos):"), gbc);

        gbc.gridy = 2;
        JTextField txtCodigo = new JTextField(10);
        txtCodigo.setName("txtCodigo");
        panel.add(txtCodigo, gbc);

        JPanel panelBotones = new JPanel();
        JButton btnVerificar = new JButton("Verificar Código");
        JButton btnReenviar = new JButton("Reenviar Código");
        JButton btnAtras = new JButton("Atrás");
        JButton btnSaltar = new JButton("Saltar");

        btnVerificar.addActionListener(e -> {
            String codigo = txtCodigo.getText().trim();
            String username = usuarioActual[0];

            if (codigo.isEmpty() || !codigo.matches("\\d{6}")) {
                JOptionPane.showMessageDialog(dialogo, "Ingrese un código válido de 6 dígitos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar código
            if (sicaEngine.restablecerContrasena(username, "temp", codigo)) {
                JOptionPane.showMessageDialog(dialogo,
                        "¡Código verificado correctamente!",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Habilitar paso 4
                tabbedPane.setEnabledAt(3, true);
                tabbedPane.setSelectedIndex(3);
            } else {
                JOptionPane.showMessageDialog(dialogo,
                        "Código incorrecto o expirado",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReenviar.addActionListener(e -> {
            String username = usuarioActual[0];
            String nuevoCodigo = sicaEngine.generarCodigoRecuperacion(username);

            if (nuevoCodigo != null) {
                codigoGenerado[0] = nuevoCodigo;
                JOptionPane.showMessageDialog(dialogo,
                        "Nuevo código generado: " + nuevoCodigo + "\n"
                        + "Este código expira en 15 minutos.",
                        "Código Reenviado", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnSaltar.addActionListener(e -> {
            // Saltar a la pregunta secreta
            tabbedPane.setSelectedIndex(1);
        });

        btnAtras.addActionListener(e -> tabbedPane.setSelectedIndex(1));

        panelBotones.add(btnVerificar);
        panelBotones.add(btnReenviar);
        panelBotones.add(btnSaltar);
        panelBotones.add(btnAtras);

        gbc.gridy = 3;
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JPanel crearPanelPaso4(JDialog dialogo, JTabbedPane tabbedPane, String[] usuarioActual,
            String[] respuestaVerificada) { // Agregar respuestaVerificada
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblInfo = new JLabel("<html><b>Paso final:</b> Establezca su nueva contraseña</html>");
        panel.add(lblInfo, gbc);

        gbc.gridy = 1;
        panel.add(new JLabel("Nueva Contraseña:"), gbc);

        gbc.gridy = 2;
        JPasswordField txtNuevaPass = new JPasswordField(15);
        txtNuevaPass.setName("txtNuevaPass");
        txtNuevaPass.setEnabled(true);
        panel.add(txtNuevaPass, gbc);

        // INDICADOR DE FORTALEZA
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel panelFortaleza = new JPanel(new GridBagLayout());
        GridBagConstraints gbcFort = new GridBagConstraints();

        JProgressBar barraFortaleza = new JProgressBar(0, 100);
        barraFortaleza.setStringPainted(true);
        barraFortaleza.setPreferredSize(new Dimension(250, 20));

        JLabel lblNivel = new JLabel(" ");
        lblNivel.setFont(new Font("Arial", Font.BOLD, 11));

        gbcFort.gridx = 0;
        gbcFort.gridy = 0;
        gbcFort.fill = GridBagConstraints.HORIZONTAL;
        gbcFort.weightx = 1.0;
        panelFortaleza.add(barraFortaleza, gbcFort);

        gbcFort.gridx = 1;
        gbcFort.gridy = 0;
        gbcFort.weightx = 0;
        panelFortaleza.add(lblNivel, gbcFort);

        panel.add(panelFortaleza, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Confirmar Contraseña:"), gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPasswordField txtConfirmarPass = new JPasswordField(15);
        txtConfirmarPass.setName("txtConfirmarPass");
        txtConfirmarPass.setEnabled(true);
        panel.add(txtConfirmarPass, gbc);

        // Listeners para fortaleza
        txtNuevaPass.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                actualizarFortaleza();
            }

            public void removeUpdate(DocumentEvent e) {
                actualizarFortaleza();
            }

            public void changedUpdate(DocumentEvent e) {
                actualizarFortaleza();
            }

            private void actualizarFortaleza() {
                String password = new String(txtNuevaPass.getPassword());
                int fortaleza = EncriptadorContrasena.calcularFortalezaContrasena(password);
                String nivel = EncriptadorContrasena.obtenerNivelFortaleza(password);
                Color color = EncriptadorContrasena.obtenerColorFortaleza(password);

                barraFortaleza.setValue(fortaleza);
                barraFortaleza.setForeground(color);
                barraFortaleza.setString(nivel + " (" + fortaleza + "%)");
                lblNivel.setText(nivel);
                lblNivel.setForeground(color);
            }
        });

        JPanel panelBotones = new JPanel();
        JButton btnFinalizar = new JButton("Restablecer Contraseña");
        JButton btnAtras = new JButton("Atrás");

        // Método público para habilitar/deshabilitar campos
        panel.putClientProperty("habilitarCampos", new Runnable() {
            @Override
            public void run() {
                txtNuevaPass.setEnabled(true);
                txtConfirmarPass.setEnabled(true);
                txtNuevaPass.requestFocus();
            }
        });

        btnFinalizar.addActionListener(e -> {
            String nuevaPass = new String(txtNuevaPass.getPassword());
            String confirmarPass = new String(txtConfirmarPass.getPassword());
            String username = usuarioActual[0];

            if (nuevaPass.isEmpty() || confirmarPass.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Complete ambos campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!nuevaPass.equals(confirmarPass)) {
                JOptionPane.showMessageDialog(dialogo, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar fortaleza de contraseña
            String erroresPassword = EncriptadorContrasena.validarFortalezaContrasena(nuevaPass);
            if (erroresPassword != null) {
                int fortaleza = EncriptadorContrasena.calcularFortalezaContrasena(nuevaPass);
                if (fortaleza < 60) {
                    int opcion = JOptionPane.showConfirmDialog(dialogo,
                            "La contraseña es débil. ¿Desea continuar de todos modos?\n\n" + erroresPassword,
                            "Contraseña débil", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (opcion != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }

            boolean restablecido = false;

            try {
                if (tabbedPane.isEnabledAt(2)) {
                    // Caso 1: Usando código de verificación
                    JPanel panelPaso3 = (JPanel) tabbedPane.getComponentAt(2);
                    String codigo = "";

                    // Buscar el campo de código
                    for (Component comp : panelPaso3.getComponents()) {
                        if (comp instanceof JTextField && "txtCodigo".equals(comp.getName())) {
                            codigo = ((JTextField) comp).getText().trim();
                            break;
                        }
                    }

                    if (!codigo.isEmpty()) {
                        restablecido = sicaEngine.restablecerContrasena(username, nuevaPass, codigo);
                    }
                } else {
                    // Caso 2: Usando respuesta secreta VERIFICADA (almacenada en el array)
                    if (respuestaVerificada[0] != null && !respuestaVerificada[0].isEmpty()) {
                        // Verificar primero que la respuesta es correcta (ya lo hicimos, pero por seguridad)
                        boolean respuestaCorrecta = sicaEngine.verificarRespuestaSecreta(username, respuestaVerificada[0]);

                        if (respuestaCorrecta) {
                            // Ahora cambiar la contraseña usando un código especial o método directo
                            // Necesitarás crear un método específico para esto
                            restablecido = sicaEngine.cambiarPasswordConRespuesta(username, nuevaPass, respuestaVerificada[0]);
                        } else {
                            JOptionPane.showMessageDialog(dialogo,
                                    "La respuesta secreta no es válida. Regrese al paso 2.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                if (restablecido) {
                    JOptionPane.showMessageDialog(dialogo,
                            "¡Contraseña restablecida exitosamente!\n"
                            + "Ahora puede iniciar sesión con su nueva contraseña.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dialogo.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialogo,
                            "Error al restablecer la contraseña. Verifique sus datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo,
                        "Error inesperado: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        btnAtras.addActionListener(e -> {
            // Volver al paso anterior disponible
            if (tabbedPane.isEnabledAt(2)) {
                tabbedPane.setSelectedIndex(2);
            } else {
                tabbedPane.setSelectedIndex(1);
            }
        });

        panelBotones.add(btnFinalizar);
        panelBotones.add(btnAtras);

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        return panel;
    }
    private void cargarPreguntaSecreta(String username, JTabbedPane tabbedPane) {
        String pregunta = sicaEngine.obtenerPreguntaSecreta(username);

        if (pregunta != null) {
            // Encontrar el label en el panel del paso 2
            JPanel panelPaso2 = (JPanel) tabbedPane.getComponentAt(1);

            for (Component comp : panelPaso2.getComponents()) {
                if (comp instanceof JLabel && "lblPregunta".equals(comp.getName())) {
                    ((JLabel) comp).setText("<html><b>Pregunta secreta:</b><br>" + pregunta + "</html>");
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Personalizar colores de JOptionPane
            UIManager.put("OptionPane.background", new Color(240, 245, 249));
            UIManager.put("Panel.background", new Color(240, 245, 249));

        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }

    private void configurarBotonCerrarSesion(JFrame ventanaPersonalizada, VentanaUsuarioInterface ventanaUsuario) {
        // Buscar botón de cerrar sesión en la ventana
        Component[] components = ventanaUsuario.getPanelPrincipal().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                buscarBotonCerrarSesion((JPanel) comp, ventanaPersonalizada);
            }
        }
    }

    private void buscarBotonCerrarSesion(JPanel panel, JFrame ventanaPersonalizada) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals("Cerrar Sesión") || btn.getText().equals("Salir")) {
                    // Reemplazar cualquier action listener existente
                    ActionListener[] listeners = btn.getActionListeners();
                    for (ActionListener listener : listeners) {
                        btn.removeActionListener(listener);
                    }

                    // Agregar el nuevo action listener
                    btn.addActionListener(e -> {
                        cerrarSesionYVolver(ventanaPersonalizada);
                    });
                }
            } else if (comp instanceof JPanel) {
                buscarBotonCerrarSesion((JPanel) comp, ventanaPersonalizada);
            }
        }
    }
}

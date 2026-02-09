package com.mycompany.controldeaccesos.ui;
import javax.swing.JOptionPane;
    // El auditor solo puede ver, pero no modificar nada

// El auditor solo puede ver, pero no modificar nada
public class VentanaAuditor extends VentanaAdministrador {

    public VentanaAuditor() {
        super();
        restringirPermisos();
    }

    private void restringirPermisos() {
        // Deshabilitar TODOS los botones de acción
        if (btnEditarUsuario != null) {
            btnEditarUsuario.setEnabled(false);
            btnEditarUsuario.setVisible(false);
        }

        if (btnEliminarUsuario != null) {
            btnEliminarUsuario.setEnabled(false);
            btnEliminarUsuario.setVisible(false);
        }

        // También deshabilitar el botón de actualizar si quieres
        if (btnActualizar != null) {
            btnActualizar.setEnabled(true); // Mantener activo para refrescar vista
        }
    }

    @Override
    public String getTituloVentana() {
        return "SICA v2.0 - Panel de Auditoría (Solo Lectura)";
    }

    @Override
    protected void editarUsuarioSeleccionado() {
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Auditor no tiene permisos para editar usuarios",
                "Acceso Restringido",
                JOptionPane.WARNING_MESSAGE);
    }

    @Override
    protected void eliminarUsuarioSeleccionado() {
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Auditor no tiene permisos para eliminar usuarios",
                "Acceso Restringido",
                JOptionPane.WARNING_MESSAGE);
    }

    @Override
    protected void cambiarPasswordUsuario(String username) {
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Auditor no tiene permisos para cambiar contraseñas",
                "Acceso Restringido",
                JOptionPane.WARNING_MESSAGE);
    }

    @Override
    protected void cambiarEstadoUsuario(String username, boolean estadoActual) {
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Auditor no tiene permisos para activar/desactivar usuarios",
                "Acceso Restringido",
                JOptionPane.WARNING_MESSAGE);
    }

    @Override
    protected void cambiarRolUsuario(String username, String rolActual) {
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Auditor no tiene permisos para cambiar roles",
                "Acceso Restringido",
                JOptionPane.WARNING_MESSAGE);
    }

    @Override
    protected void mostrarConfiguracionSistema() {
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Auditor no tiene acceso a la configuración del sistema.\n\n"
                + "Esta función está reservada para Administradores.",
                "Acceso Restringido",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    protected void mostrarPermisosRoles() {
        // Supervisor NO puede acceder a la gestión de permisos y roles
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Auditor no tiene acceso a la gestión de permisos y roles.\n\n"
                + "Esta función está reservada para Administradores.",
                "Acceso Restringido",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void mostrarBackupRestauracion() {
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Auditor no tiene permisos para realizar operaciones de backup.\n\n"
                + "Esta función está reservada para Administradores.",
                "Acceso Restringido",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void mostrarGestionUsuarios() {
        // Versión solo lectura
        JOptionPane.showMessageDialog(panelPrincipal,
                "👥 GESTIÓN DE USUARIOS (SOLO LECTURA)\n\n"
                + "Como Auditor, solo puede visualizar información de usuarios:\n\n"
                + "✅ PERMITIDO:\n"
                + "• Ver lista de usuarios\n"
                + "• Ver estados de usuarios\n"
                + "• Ver actividades\n"
                + "• Ver reportes de seguridad\n\n"
                + "❌ NO PERMITIDO:\n"
                + "• Modificar cualquier usuario\n"
                + "• Cambiar contraseñas\n"
                + "• Activar/desactivar usuarios\n"
                + "• Cambiar roles\n"
                + "• Eliminar usuarios",
                "Gestión de Usuarios - Solo Lectura",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

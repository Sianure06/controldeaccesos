package com.mycompany.controldeaccesos.ui;



  // El supervisor puede editar, pero no eliminar
import javax.swing.JOptionPane;

import com.mycompany.controldeaccesos.models.UsuarioModel;


// El supervisor puede editar, pero no eliminar
public class VentanaSupervisor extends VentanaAdministrador {

    public VentanaSupervisor() {
        super();
        restringirPermisos();
    }

    private void restringirPermisos() {
        // Deshabilitar solo el botón de eliminar usuario
        if (btnEliminarUsuario != null) {
            btnEliminarUsuario.setEnabled(false);
            btnEliminarUsuario.setVisible(false);
        }

        // Mantener activo el botón de editar (permite activar/desactivar y resetear contraseñas)
        if (btnEditarUsuario != null) {
            btnEditarUsuario.setEnabled(true);
            btnEditarUsuario.setVisible(true);
        }
    }

    @Override
    public String getTituloVentana() {
        return "SICA v2.0 - Panel de Supervisión";
    }

    @Override
    protected void cambiarRolUsuario(String username, String rolActual) {
        // Supervisor NO puede cambiar roles
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Supervisor no tiene permisos para cambiar roles de usuario\n\n"
                + "Solo los Administradores pueden modificar roles",
                "Acceso Restringido",
                JOptionPane.WARNING_MESSAGE);
    }

    @Override
    protected void mostrarConfiguracionSistema() {
        // Supervisor NO puede acceder a configuración del sistema
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Supervisor no tiene acceso a la configuración del sistema.\n\n"
                + "Esta función está reservada para Administradores.",
                "Acceso Restringido",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void mostrarPermisosRoles() {
        // Supervisor NO puede acceder a la gestión de permisos y roles
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Supervisor no tiene acceso a la gestión de permisos y roles.\n\n"
                + "Esta función está reservada para Administradores.",
                "Acceso Restringido",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    protected void mostrarBackupRestauracion() {
        // Supervisor NO puede hacer backup
        JOptionPane.showMessageDialog(panelPrincipal,
                "El rol de Supervisor no tiene permisos para realizar operaciones de backup.\n\n"
                + "Esta función está reservada para Administradores.",
                "Acceso Restringido",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void editarUsuarioSeleccionado() {
        // Versión simplificada para Supervisor
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
            // Supervisor solo puede: Activar/Desactivar y Resetear Contraseña
            String[] opciones = {
                "Cambiar Contraseña",
                usuario.isActivo() ? "Desactivar Usuario" : "Activar Usuario",
                usuario.estaBloqueado() ? "Desbloquear Usuario" : "Ver Estado",
                "Cancelar"
            };

            int opcion = JOptionPane.showOptionDialog(panelPrincipal,
                    "Usuario: " + username + "\n"
                    + "Estado actual: " + (usuario.isActivo() ? "Activo" : "Inactivo") + "\n"
                    + "Rol: " + usuario.getRol() + "\n"
                    + "Bloqueado: " + (usuario.estaBloqueado() ? "Sí" : "No") + "\n\n"
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
                case 1: // Activar/Desactivar
                    cambiarEstadoUsuario(username, usuario.isActivo());
                    break;
                case 2: // Desbloquear o ver estado
                    if (usuario.estaBloqueado()) {
                        sicaEngine.desbloquearUsuario(username);
                        actualizarDatos();
                        JOptionPane.showMessageDialog(panelPrincipal,
                                "Usuario desbloqueado exitosamente",
                                "Desbloqueo",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                // case 3 es Cancelar
            }
        }
    }
}

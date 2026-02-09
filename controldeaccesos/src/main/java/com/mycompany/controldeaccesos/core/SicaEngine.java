/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos.core;

import java.util.List;
import java.util.Map;

import com.mycompany.controldeaccesos.EncriptadorContrasena;
import com.mycompany.controldeaccesos.data.GestorBaseDatos;
import com.mycompany.controldeaccesos.models.UsuarioModel;

public class SicaEngine {

    private static SicaEngine instancia;
    private final GestorBaseDatos gestorBD;

    private SicaEngine() {
        this.gestorBD = GestorBaseDatos.getInstancia();
        this.gestorBD.conectar();
    }

    public static synchronized SicaEngine getInstancia() {
        if (instancia == null) {
            instancia = new SicaEngine();
        }
        return instancia;
    }

    public ResultadoAutenticacion autenticarUsuario(String username, String password) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return new ResultadoAutenticacion(false, null, "Usuario no puede estar vacío");
            }

            if (password == null || password.isEmpty()) {
                return new ResultadoAutenticacion(false, null, "Contraseña no puede estar vacía");
            }

            // Verificar si el usuario está bloqueado antes de intentar autenticar
            UsuarioModel u = gestorBD.getUsuario(username);
            if (u != null && u.estaBloqueado()) {
                return new ResultadoAutenticacion(false, null, "Usuario bloqueado temporalmente. Intente más tarde.");
            }

            boolean autenticado = gestorBD.autenticarUsuario(username, password);

            if (autenticado) {
                UsuarioModel usuario = gestorBD.getUsuario(username);
                return new ResultadoAutenticacion(true, usuario, "¡Autenticación exitosa!");
            } else {
                return new ResultadoAutenticacion(false, null, "Credenciales inválidas");
            }
        } catch (Exception e) {
            return new ResultadoAutenticacion(false, null, "Error en autenticación: " + e.getMessage());
        }
    }

    public ResultadoRegistro registrarUsuario(String username, String password,
            String nombreCompleto, String email,
            String preguntaSecreta, String respuestaSecreta) {
        try {
            if (gestorBD.usuarioExiste(username)) {
                return new ResultadoRegistro(false, "El usuario '" + username + "' ya existe");
            }

            // Verificar fortaleza de contraseña
            String erroresPassword = EncriptadorContrasena.validarFortalezaContrasena(password);
            if (erroresPassword != null) {
                int fortaleza = EncriptadorContrasena.calcularFortalezaContrasena(password);
                if (fortaleza < 60) {
                    return new ResultadoRegistro(false, "Contraseña débil:\n" + erroresPassword);
                }
            }

            boolean registrado = gestorBD.registrarUsuario(username, password, nombreCompleto,
                    email, preguntaSecreta, respuestaSecreta);

            if (registrado) {
                return new ResultadoRegistro(true, "Usuario registrado exitosamente");
            } else {
                return new ResultadoRegistro(false, "Error al registrar usuario");
            }
        } catch (IllegalArgumentException e) {
            return new ResultadoRegistro(false, e.getMessage());
        } catch (Exception e) {
            return new ResultadoRegistro(false, "Error en registro: " + e.getMessage());
        }
    }

    public boolean eliminarUsuario(String username) {
        return gestorBD.eliminarUsuario(username);
    }

    public boolean cambiarPassword(String username, String nuevaPassword) {
        return gestorBD.cambiarPassword(username, nuevaPassword);
    }

    public List<UsuarioModel> obtenerTodosUsuarios() {
        return gestorBD.obtenerTodosUsuarios();
    }

    public String obtenerPreguntaSecreta(String username) {
        return gestorBD.obtenerPreguntaSecreta(username);
    }

    public boolean verificarRespuestaSecreta(String username, String respuestaPlana) {
        try {
            UsuarioModel user = gestorBD.getUsuario(username);
            if (user == null || user.getRespuestaSecretaHash() == null) {
                return false;
            }

            // Normalizamos como se hizo al guardar (minúsculas y sin espacios)
            String respuestaNormalizada = respuestaPlana.trim().toLowerCase();

            // Usamos BCrypt para comparar el texto plano con el hash guardado
            return org.mindrot.jbcrypt.BCrypt.checkpw(respuestaNormalizada, user.getRespuestaSecretaHash());
        } catch (Exception e) {
            return false;
        }
    }

    public String generarCodigoRecuperacion(String username) {
        return gestorBD.generarCodigoRecuperacion(username);
    }

    public boolean restablecerContrasena(String username, String nuevaContrasena, String codigo) {
        return gestorBD.restablecerContrasena(username, nuevaContrasena, codigo);
    }

    public void crearBackup() {
        try {
            gestorBD.crearBackup();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear backup: " + e.getMessage());
        }
    }

    public void cambiarBackups(int numeroBackups) {
        try {
            gestorBD.cambiarBackups(numeroBackups);
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar backup: " + e.getMessage());
        }
    }

    public void desconectar() {
        gestorBD.desconectar();
    }

    // ========== CLASES INTERNAS ==========
    public static class ResultadoAutenticacion {

        private final boolean exito;
        private final UsuarioModel usuario;
        private final String mensaje;

        public ResultadoAutenticacion(boolean exito, UsuarioModel usuario, String mensaje) {
            this.exito = exito;
            this.usuario = usuario;
            this.mensaje = mensaje;
        }

        public boolean isExito() {
            return exito;
        }

        public UsuarioModel getUsuario() {
            return usuario;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    public static class ResultadoRegistro {

        private final boolean exito;
        private final String mensaje;

        public ResultadoRegistro(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }

        public boolean isExito() {
            return exito;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    public boolean usuarioExiste(String username) {
        return gestorBD.usuarioExiste(username);
    }

    public boolean tienePreguntaSecreta(String username) {
        return gestorBD.tienePreguntaSecreta(username);
    }

    public UsuarioModel obtenerUsuario(String username) {
        return gestorBD.getUsuario(username);
    }

    public int obtenerNumeroBackups() {
        return gestorBD.contarBackups();
    }

    public List<String> obtenerListaBackups() {
        return gestorBD.obtenerListaBackups();
    }

    public boolean eliminarBackup(String nombreArchivo) {
        return gestorBD.eliminarBackup(nombreArchivo);

    }
//para el sistema de intentos fallidos de sesion

    public int obtenerTotalIntentosFallidosHoy() {
        return gestorBD.obtenerTotalIntentosFallidosHoy();
    }

    public int obtenerUsuariosBloqueados() {
        return gestorBD.obtenerUsuariosBloqueados();
    }

    public List<UsuarioModel> obtenerUsuariosConIntentosFallidos() {
        return gestorBD.obtenerUsuariosConIntentosFallidos();
    }

    public boolean desbloquearUsuario(String username) {
        return gestorBD.desbloquearUsuario(username);
    }

    public boolean cambiarEstadoUsuario(String username, boolean activo) {
        return gestorBD.cambiarEstadoUsuario(username, activo);
    }

    public boolean cambiarPasswordConRespuesta(String username, String nuevaPassword, String respuestaSecreta) {
        try {
            // Verificar primero la respuesta secreta
            if (!verificarRespuestaSecreta(username, respuestaSecreta)) {
                return false;
            }

            // Si la respuesta es correcta, cambiar la contraseña
            return gestorBD.cambiarPassword(username, nuevaPassword);
        } catch (Exception e) {
            System.err.println("Error al cambiar contraseña con respuesta: " + e.getMessage());
            return false;
        }
    }
    public boolean cambiarRolUsuario(String username, String nuevoRol) {
        return gestorBD.cambiarRolUsuario(username, nuevoRol);
    }

    public boolean cambiarPreguntaSecreta(String username, String nuevaPregunta, String nuevaRespuesta) {
        try {
            UsuarioModel usuario = gestorBD.getUsuario(username);
            if (usuario == null) {
                return false;
            }
            if (nuevaPregunta == null || nuevaPregunta.trim().isEmpty()) {
                throw new IllegalArgumentException("La nueva pregunta secreta no puede estar vacía");
            }
            // Normaliza y encripta la respuesta secreta
            String respuestaNormalizada = nuevaRespuesta.trim().toLowerCase();
            String respuestaHash = EncriptadorContrasena.encriptarContrasena(respuestaNormalizada);

            usuario.setPreguntaSecreta(nuevaPregunta.trim());
            usuario.setRespuestaSecretaHash(respuestaHash);

            return gestorBD.actualizarUsuario(usuario);
        } catch (Exception e) {
            System.err.println("Error al cambiar pregunta secreta: " + e.getMessage());
            return false;
        }
    }

    public Map<String, Integer> obtenerEstadisticasAccesos(int ultimosDias) {
        return gestorBD.getEstadisticasAccesos(ultimosDias);
    }

    public int obtenerAccesosHoy() {
        return gestorBD.getAccesosHoy();
    }

}

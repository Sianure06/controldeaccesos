/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos.models;

import java.time.LocalDateTime;

public class UsuarioModel {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USUARIO = "USUARIO";
    public static final String ROLE_AUDITOR = "AUDITOR";
    public static final String ROLE_SUPERVISOR = "SUPERVISOR";
    private String username;
    private String passwordHash;
    private String email;
    private String nombreCompleto;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;
    private boolean activo;
    private String rol;
    private String preguntaSecreta;
    private String respuestaSecretaHash;
    private String codigoRecuperacion;
    private LocalDateTime expiracionCodigo;
    private int intentosFallidos;
    private LocalDateTime ultimoIntentoFallido;
    private boolean bloqueado;
    private LocalDateTime fechaDesbloqueo;

    public UsuarioModel(String username, String passwordHash, String email, 
                       String nombreCompleto, String rol) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.fechaRegistro = LocalDateTime.now();
        this.ultimoAcceso = LocalDateTime.now();
        this.activo = true;
        this.rol = rol;
        this.preguntaSecreta = "";
        this.respuestaSecretaHash = "";
        this.codigoRecuperacion = "";
        this.expiracionCodigo = LocalDateTime.now();
        this.intentosFallidos = 0;
        this.ultimoIntentoFallido = null;
        this.bloqueado = false;
        this.fechaDesbloqueo = null;
    }
    
    // Getters y setters
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getEmail() { return email; }
    public String getNombreCompleto() { return nombreCompleto; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public boolean isActivo() { return activo; }
    public String getPreguntaSecreta() { return preguntaSecreta; }
    public String getRespuestaSecretaHash() { return respuestaSecretaHash; }
    public String getCodigoRecuperacion() { return codigoRecuperacion; }
    public LocalDateTime getExpiracionCodigo() { return expiracionCodigo; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public LocalDateTime getUltimoIntentoFallido() { return ultimoIntentoFallido; }
    public boolean isBloqueado() { return bloqueado; }
    public LocalDateTime getFechaDesbloqueo() { return fechaDesbloqueo; }
    
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public void setPreguntaSecreta(String preguntaSecreta) { this.preguntaSecreta = preguntaSecreta; }
    public void setRespuestaSecretaHash(String respuestaSecretaHash) { this.respuestaSecretaHash = respuestaSecretaHash; }
    public void setCodigoRecuperacion(String codigoRecuperacion) { this.codigoRecuperacion = codigoRecuperacion; }
    public void setExpiracionCodigo(LocalDateTime expiracionCodigo) { this.expiracionCodigo = expiracionCodigo; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setFechaRegistro(LocalDateTime parse) {this.fechaRegistro = fechaRegistro; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }
    public void setUltimoIntentoFallido(LocalDateTime ultimoIntentoFallido) { this.ultimoIntentoFallido = ultimoIntentoFallido; }
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }
    public void setFechaDesbloqueo(LocalDateTime fechaDesbloqueo) { this.fechaDesbloqueo = fechaDesbloqueo; }
    
    public boolean esAdmin() {
        return ROLE_ADMIN.equalsIgnoreCase(this.rol);
    }

    public boolean esAuditor() {
        return ROLE_AUDITOR.equalsIgnoreCase(this.rol);
    }

    public boolean esSupervisor() {
        return ROLE_SUPERVISOR.equalsIgnoreCase(this.rol);
    }
    
    public boolean tienePreguntaSecreta() {
        return preguntaSecreta != null && !preguntaSecreta.trim().isEmpty() &&
               respuestaSecretaHash != null && !respuestaSecretaHash.isEmpty();
    }
    
    // Método para incrementar intentos fallidos
    public void incrementarIntentosFallidos() {
        this.intentosFallidos++;
        this.ultimoIntentoFallido = LocalDateTime.now();
    
    // Bloquear después de 3 intentos fallidos
        if (this.intentosFallidos >= 3) {
            this.bloqueado = true;
            this.fechaDesbloqueo = LocalDateTime.now().plusMinutes(15); // Bloqueo por 15 minutos
        }
    }

// Método para resetear intentos fallidos (al iniciar sesión exitosamente)
    public void resetearIntentosFallidos() {
        this.intentosFallidos = 0;
        this.ultimoIntentoFallido = null;
        this.bloqueado = false;
        this.fechaDesbloqueo = null;
    }

// Método para verificar si el usuario está bloqueado
    public boolean estaBloqueado() {
        if (bloqueado && fechaDesbloqueo != null) {
            // Si ya pasó el tiempo de bloqueo, desbloquear automáticamente
            if (LocalDateTime.now().isAfter(fechaDesbloqueo)) {
                resetearIntentosFallidos();
                return false;
            }
            return true;
        }
        return false;
    }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
}
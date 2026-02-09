/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos;

import java.security.SecureRandom;
import java.util.Base64;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase para manejar el encriptado y verificación de contraseñas usando el
 * algoritmo BCrypt.
 */
public class EncriptadorContrasena {

    // Cost factor - determina el trabajo necesario para hashear
    // Un número más alto hace más lento el hashing (más seguro)
    private static final int COST_FACTOR = 12;

    /**
     * Encripta una contraseña usando BCrypt
     *
     * @param contrasenaPlana Contraseña en texto plano
     * @return Contraseña encriptada (hash)
     */
    public static String encriptarContrasena(String contrasenaPlana) {
        if (contrasenaPlana == null || contrasenaPlana.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        // Generar salt automáticamente con el cost factor especificado
        String salt = BCrypt.gensalt(COST_FACTOR);

        // Hashear la contraseña con el salt
        return BCrypt.hashpw(contrasenaPlana, salt);
    }

    /**
     * Encripta una contraseña con un salt específico
     *
     * @param contrasenaPlana Contraseña en texto plano
     * @param salt Salt específico
     * @return Contraseña encriptada
     */
    public static String encriptarContrasenaConSalt(String contrasenaPlana, String salt) {
        if (contrasenaPlana == null || salt == null) {
            throw new IllegalArgumentException("Contraseña y salt no pueden ser nulos");
        }
        return BCrypt.hashpw(contrasenaPlana, salt);
    }

    /**
     * Verifica si una contraseña en texto plano coincide con el hash almacenado
     *
     * @param contrasenaPlana Contraseña en texto plano a verificar
     * @param hashAlmacenado Hash almacenado en la base de datos
     * @return true si coinciden, false si no
     */
    public static boolean verificarContrasena(String contrasenaPlana, String hashAlmacenado) {
        if (contrasenaPlana == null || hashAlmacenado == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(contrasenaPlana, hashAlmacenado);
        } catch (IllegalArgumentException e) {
            // El hash no tiene formato válido
            System.err.println("Error al verificar contraseña: " + e.getMessage());
            return false;
        }
    }

    /**
     * Genera un salt personalizado
     *
     * @return Salt generado
     */
    public static String generarSalt() {
        return BCrypt.gensalt(COST_FACTOR);
    }

    /**
     * Genera un token seguro para recuperación de contraseña
     *
     * @return Token seguro
     */
    public static String generarTokenSeguro() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256 bits
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Valida la fortaleza de una contraseña
     *
     * @param contrasena Contraseña a validar
     * @return Mensaje con errores o null si es válida
     */
    public static String validarFortalezaContrasena(String contrasena) {
        StringBuilder errores = new StringBuilder();

        if (contrasena == null || contrasena.length() < 8) {
            errores.append("• La contraseña debe tener al menos 8 caracteres\n");
        }

        if (!contrasena.matches(".*[A-Z].*")) {
            errores.append("• Debe contener al menos una letra mayúscula\n");
        }

        if (!contrasena.matches(".*[a-z].*")) {
            errores.append("• Debe contener al menos una letra minúscula\n");
        }

        if (!contrasena.matches(".*\\d.*")) {
            errores.append("• Debe contener al menos un número\n");
        }

        if (!contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            errores.append("• Debe contener al menos un carácter especial\n");
        }

        return errores.length() > 0 ? errores.toString() : null;
    }

    /**
     * Calcula el nivel de fortaleza de una contraseña (0-100)
     *
     * @param contrasena Contraseña a evaluar
     * @return Puntuación de fortaleza (0-100)
     */
    public static int calcularFortalezaContrasena(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            return 0;
        }

        int fortaleza = 0;

        // Longitud (máximo 40 puntos)
        int longitud = contrasena.length();
        if (longitud >= 8) {
            fortaleza += 20;
        }
        if (longitud >= 12) {
            fortaleza += 10;
        }
        if (longitud >= 16) {
            fortaleza += 10;
        }

        // Diferentes tipos de caracteres
        if (contrasena.matches(".*[A-Z].*")) {
            fortaleza += 15; // Mayúsculas

        }
        if (contrasena.matches(".*[a-z].*")) {
            fortaleza += 15; // Minúsculas

        }
        if (contrasena.matches(".*\\d.*")) {
            fortaleza += 15;   // Números

        }
        if (contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            fortaleza += 15; // Caracteres especiales
        }

        // Bonus por combinaciones
        if (contrasena.matches(".*[A-Z].*") && contrasena.matches(".*[a-z].*")) {
            fortaleza += 5;
        }
        if (contrasena.matches(".*\\d.*") && contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            fortaleza += 5;
        }

        return Math.min(fortaleza, 100); // Máximo 100
    }

    /**
     * Obtiene el nivel de fortaleza como texto
     *
     * @param contrasena Contraseña a evaluar
     * @return Nivel de fortaleza (Débil, Media, Fuerte, Muy Fuerte)
     */
    public static String obtenerNivelFortaleza(String contrasena) {
        int fortaleza = calcularFortalezaContrasena(contrasena);

        if (fortaleza < 40) {
            return "Débil";
        } else if (fortaleza < 60) {
            return "Media";
        } else if (fortaleza < 80) {
            return "Fuerte";
        } else {
            return "Muy Fuerte";
        }
    }

    /**
     * Obtiene el color correspondiente al nivel de fortaleza
     *
     * @param contrasena Contraseña a evaluar
     * @return Color (rojo, naranja, amarillo, verde)
     */
    public static java.awt.Color obtenerColorFortaleza(String contrasena) {
        int fortaleza = calcularFortalezaContrasena(contrasena);

        if (fortaleza < 40) {
            return new java.awt.Color(255, 0, 0); // Rojo
        } else if (fortaleza < 60) {
            return new java.awt.Color(255, 165, 0); // Naranja
        } else if (fortaleza < 80) {
            return new java.awt.Color(255, 255, 0); // Amarillo
        } else {
            return new java.awt.Color(0, 255, 0); // Verde
        }
    }

    /**
     * Obtiene información sobre el hash (para debugging)
     *
     * @param hash Hash a analizar
     * @return Información del hash
     */
    public static String obtenerInfoHash(String hash) {
        if (hash == null || !hash.startsWith("$2a$")) {
            return "Hash no válido";
        }

        try {
            String[] partes = hash.split("\\$");
            if (partes.length >= 4) {
                String version = partes[1];
                String cost = partes[2];
                String salt = partes[3].substring(0, 22);

                return String.format("Versión: %s, Coste: %s, Salt: %s...",
                        version, cost, salt);
            }
        } catch (Exception e) {
            // Ignorar errores de formato
        }

        return "No se pudo analizar el hash";
    }

}

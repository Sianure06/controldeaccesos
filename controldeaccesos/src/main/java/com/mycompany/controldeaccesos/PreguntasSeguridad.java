/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos;

import java.util.Arrays;
import java.util.List;

/**
 * Clase con preguntas de seguridad predefinidas
 */
public class PreguntasSeguridad {
    
    // Lista de preguntas de seguridad comunes
    public static final List<String> PREGUNTAS_PREDEFINIDAS = Arrays.asList(
        "¿Cuál es el nombre de tu mascota favorita?",
        "¿Cuál es el nombre de tu mejor amigo de la infancia?",
        "¿Cuál es el nombre de tu primera escuela?",
        "¿Cuál es el apellido de soltera de tu madre?",
        "¿En qué ciudad naciste?",
        "¿Cuál es tu color favorito?",
        "¿Cuál es tu comida favorita?",
        "¿Cuál es el modelo de tu primer auto?",
        "¿Cuál es el nombre de tu profesor favorito?",
        "¿Cuál es tu libro favorito?",
        "¿Cuál es el nombre de tu equipo deportivo favorito?",
        "¿En qué año te graduaste de la secundaria?",
        "¿Cuál es el nombre de tu primo/hermano mayor?",
        "¿Cuál es tu estación del año favorita?",
        "¿Cuál es tu película favorita?"
    );
    
    /**
     * Obtener todas las preguntas predefinidas
     */
    public static List<String> getPreguntasPredefinidas() {
        return PREGUNTAS_PREDEFINIDAS;
    }
    
    /**
     * Obtener sugerencias para respuestas seguras
     */
    public static String getSugerenciasRespuesta() {
        return """
               <html>
               <b>Sugerencias para una respuesta segura:</b><br>
               • Use una respuesta que solo usted conozca<br>
               • No use información pública o fácil de adivinar<br>
               • Combine palabras (ej: 'PerroAzul2023')<br>
               • No use la misma respuesta en diferentes sitios<br>
               • La respuesta debe tener al menos 3 caracteres
               </html>
               """;
    }
}

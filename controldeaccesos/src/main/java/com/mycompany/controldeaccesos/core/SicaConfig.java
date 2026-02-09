/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos.core;

public class SicaConfig {
    private static SicaConfig instancia;
    private String nombreSistema = "SICA v2.0";
    private int tiempoSesionMinutos = 30;


    private SicaConfig() {}
    
    public static synchronized SicaConfig getInstancia() {
        if (instancia == null) {
            instancia = new SicaConfig();
        }
        return instancia;
    }
    
    // Getters y Setters
    public String getNombreSistema() { return nombreSistema; }
    public void setNombreSistema(String nombre) { this.nombreSistema = nombre; }
    public int getTiempoSesionMinutos() { return tiempoSesionMinutos; }
    public void setTiempoSesionMinutos(int minutos) { this.tiempoSesionMinutos = minutos; }
}
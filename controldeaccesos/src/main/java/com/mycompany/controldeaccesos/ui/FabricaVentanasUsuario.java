/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos.ui;

import com.mycompany.controldeaccesos.models.UsuarioModel;

/**
 *
 * @author Carlos
 */
public class  FabricaVentanasUsuario {
        public static VentanaUsuarioInterface crearVentana(UsuarioModel usuario) {
            if (usuario == null) {
                throw new IllegalArgumentException("Usuario no puede ser nulo");
            }
            
            if (usuario.esAdmin()) {
                return new VentanaAdministrador();
            } else if (usuario.esSupervisor()) {
                return new VentanaSupervisor();
            } else if (usuario.esAuditor()) {
                return new VentanaAuditor();
            }else {
                return new VentanaUsuarioNormal();
            }
        }
    }

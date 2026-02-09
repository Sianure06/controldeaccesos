/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controldeaccesos.ui;

import com.mycompany.controldeaccesos.models.UsuarioModel;
import javax.swing.JPanel;

public interface VentanaUsuarioInterface {
    JPanel getPanelPrincipal();
    String getTituloVentana();
    void cargarDatosUsuario(UsuarioModel usuario);
    void configurarEventos();
}

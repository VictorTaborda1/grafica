/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import java.io.Serializable;


public class Cliente extends Persona implements Serializable {
    private static final long serialVersionUID = 1L;
    public Cliente(String nombre, String email) {
        super(nombre, email);
    }

    @Override
    public void mostrarInfo() {
        System.out.println("👤 Cliente  " + nombre + " - Correo: " + email);
    }

    
}
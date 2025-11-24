/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import java.io.Serializable;


public class Empleado extends Persona  implements Serializable{
    private static final long serialVersionUID = 1L;
    private String departamento;

    public Empleado(String nombre, String email, String departamento) {
        super(nombre, email);
        this.departamento = departamento;
    }

    public String getDepartamento() {
        return departamento;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("👨‍💼 Empleado: " + nombre + " - Departamento: " + departamento);
    }
}

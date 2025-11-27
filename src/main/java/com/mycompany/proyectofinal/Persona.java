/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

public abstract class Persona {
    protected String nombre;
    protected String email;

    public Persona(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }
    public String getTipo() {
    return this.getClass().getSimpleName(); // devuelve "Cliente" o "Empleado"
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    public abstract void mostrarInfo();
}

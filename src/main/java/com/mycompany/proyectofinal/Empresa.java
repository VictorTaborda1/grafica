/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import java.io.Serializable;
import java.util.ArrayList;

public class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre = "TechNova S.A.";
    private String sector = "Tecnología y Desarrollo de Software";
    private String direccion = "Calle 45 #22-10, Medellín";
    private ArrayList<Departamento> departamentos;

    public Empresa() {
        departamentos = new ArrayList<>();
        departamentos.add(new Departamento("Sistemas"));
        departamentos.add(new Departamento("Desarrollo"));
        departamentos.add(new Departamento("Redes"));
        departamentos.add(new Departamento("Soporte"));
        departamentos.add(new Departamento("Contabilidad"));
        departamentos.add(new Departamento("Recursos Humanos"));
    }

    public ArrayList<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void mostrarInfo() {
        System.out.println("🏢 Empresa: " + nombre);
        System.out.println("💼 Sector: " + sector);
        System.out.println("📍 Dirección: " + direccion);
        System.out.println("Departamentos activos:");
        for (Departamento d : departamentos) {
            d.mostrarInfo();
        }
    }
}


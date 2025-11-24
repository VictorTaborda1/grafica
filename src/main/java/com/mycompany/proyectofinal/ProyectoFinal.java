/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectofinal;

/**
 *
 * @author victo
 */
public class ProyectoFinal {
    
public static void main(String[] args) {
       // SistemaTickets sistema = new SistemaTickets();
      //  sistema.menu();
    
        SistemaTickets sistema = new SistemaTickets();

        java.awt.EventQueue.invokeLater(() -> {
            new Ventana(sistema).setVisible(true);
        });
    }

    
}

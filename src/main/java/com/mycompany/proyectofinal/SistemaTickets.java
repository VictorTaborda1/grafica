/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import java.util.*;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.Document;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class SistemaTickets implements Gestionable {
    private ArrayList<Ticket> tickets = new ArrayList<>();// composicion//
     private Empresa empresa = new Empresa();
    private Scanner sc = new Scanner(System.in);

    public SistemaTickets() {
        // Si más adelante necesitas inicializar algo extra, lo haces aquí
        System.out.println("Sistema de tickets iniciado correctamente");
    }   
    public Ticket buscarTicketPorId(int id) {
    for (Ticket t : tickets) {  // 'tickets' es la lista de tickets
        if (t.getId() == id) {
            return t;
        }
    }
    return null; // Si no encuentra el ticket, devuelve null
    }
    


    @Override
    public void asignarTecnico() {
    Scanner sc = new Scanner(System.in);
    System.out.print("Ingrese el ID del ticket al que desea asignar un técnico: ");
    int id = sc.nextInt();
    sc.nextLine();

    Ticket ticket = buscarTicketPorId(id);
    if (ticket == null) {
        System.out.println("⚠️ Ticket no encontrado.");
        return;}

    System.out.print("Nombre del técnico: ");
    String nombre = sc.nextLine();
    System.out.print("Correo del técnico: ");
    String email = sc.nextLine();
    System.out.print("Especialidad del técnico: ");
    String especialidad = sc.nextLine();

    Tecnico tecnico = new Tecnico(nombre, email, especialidad);
    ticket.setTecnicoAsignado(tecnico);

    System.out.println("✅ Técnico asignado correctamente al ticket " + id);}
    
    
    public ArrayList<Ticket> getTickets() {
    return tickets;
        }


    @Override
    public void cerrarTicket() {                                    //se cierra el ticket
    Scanner sc = new Scanner(System.in);
    System.out.print("Ingrese el ID del ticket que desea cerrar: ");
    int id = sc.nextInt();
    sc.nextLine();

    Ticket ticket = buscarTicketPorId(id);
    if (ticket == null) {
        System.out.println("⚠️ Ticket no encontrado.");
        return;
    }
    
    if (ticket.getTecnicoAsignado() == null) {
        System.out.println("⚠️ No se puede cerrar el ticket sin asignar un técnico.");
        return;
    }
    
    // Mostrar información importante del ticket
    System.out.println("\n=== Información del ticket ===");
    System.out.println("Descripción del problema: " + ticket.getDescripcion());

    System.out.print("Ingrese la solución del ticket: ");
    String solucion = sc.nextLine();
    System.out.print("Ingrese el costo del servicio: ");
    double costo = sc.nextDouble();
    sc.nextLine();
    ticket.setSolucion(solucion);
    ticket.setEstado("Cerrado");
    ticket.setCosto(costo);
    System.out.println("✅ Ticket " + id + " cerrado con éxito.");
    System.out.print("¿Desea generar el PDF actualizado de este ticket? (s/n): ");
    String resp = sc.nextLine();
    if (resp.equalsIgnoreCase("s")) {
        ticket.generarPDF();
    }
}
private void registrarIncidencia() {
    System.out.println("\n¿Quién reporta la falla?");
    System.out.println("1. Cliente externo");
    System.out.println("2. Empleado interno");
    System.out.print("Seleccione una opción: ");
    int tipo = sc.nextInt(); sc.nextLine();

    Persona persona = null;
    if (tipo == 1) {
        System.out.print("Nombre del cliente: ");
        String nombre = sc.nextLine();
        System.out.print("Correo del cliente: ");
        String email = sc.nextLine();
        persona = new Cliente(nombre, email);
    } else if (tipo == 2) {
        System.out.print("Nombre del empleado: ");
        String nombre = sc.nextLine();
        System.out.print("Correo del empleado: ");
        String email = sc.nextLine();
        System.out.print("Departamento del empleado: ");
        String dep = sc.nextLine();
        persona = new Empleado(nombre, email, dep);
    }

    System.out.print("Descripción del problema: ");
    String descripcion = sc.nextLine();
    System.out.print("Prioridad (Alta, Media, Baja): ");
    String prioridad = sc.nextLine();

    System.out.println("Seleccione el departamento donde ocurrió la falla:");
    for (int i = 0; i < empresa.getDepartamentos().size(); i++) {
        System.out.println((i + 1) + ". " + empresa.getDepartamentos().get(i).getNombre());
    }
    int depIndex = sc.nextInt(); sc.nextLine();
    Departamento dep = empresa.getDepartamentos().get(depIndex - 1);

    Ticket nuevo = new Ticket(descripcion, prioridad, persona, dep);
    tickets.add(nuevo);
    System.out.println("✅ Ticket creado exitosamente con ID: " + nuevo.getId());
    System.out.print("¿Desea generar el PDF del ticket? (s/n): ");
    String respuesta = sc.nextLine();
    if (respuesta.equalsIgnoreCase("s")) {
        nuevo.generarPDF();
    }
}

    // =============================
    // MÉTODOS DE PERSISTENCIA
    // =============================

    public void guardarArchivoTxt() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("tickets.txt"))) {
            for (Ticket t : tickets) {
                pw.println(t.getId() + ";" +
                           t.getDescripcion() + ";" +
                           t.getPrioridad() + ";" +
                           t.getEstado());
            }
            System.out.println("📄 Datos guardados en tickets.txt");
        } catch (IOException e) {
            System.out.println("⚠️ Error al guardar archivo: " + e.getMessage());
        }
    }

    public void generarHistorialClientes() {
    try {
        // Crear carpeta si no existe
        File carpeta = new File("ticketsPDF");
        if (!carpeta.exists()) carpeta.mkdirs();
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Crear PDF
        PdfWriter writer = new PdfWriter("ticketsPDF/Historial_Clientes.pdf");
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // Encabezado del documento
        doc.add(new Paragraph("HISTORIAL DE CLIENTES - TechNova Solutions")
        .setFont(bold)
        .setTextAlignment(TextAlignment.CENTER)
        .setFontSize(18));

        doc.add(new Paragraph("Generado: " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        doc.add(new Paragraph(" ")); // Espacio

        boolean hayTickets = false;

        // Recorrer tickets y mostrar por cliente
        for (Ticket t : tickets) {
            if (t.getReportante() != null) {
                hayTickets = true;
                doc.add(new Paragraph("Cliente: " + t.getReportante().getNombre() +
                " (" + t.getReportante().getEmail() + ")")
                .setFont(bold));


                doc.add(new Paragraph("ID Ticket: " + t.getId()));
                doc.add(new Paragraph("Descripción: " + t.getDescripcion()));
                doc.add(new Paragraph("Prioridad: " + t.getPrioridad()));
                doc.add(new Paragraph("Estado: " + t.getEstado()));

                if (t.getSolucion() != null) {
                    doc.add(new Paragraph("Solución: " + t.getSolucion()));
                    doc.add(new Paragraph("Costo: $" + t.getCosto()));
                }

                if (t.getFechaCreacionFormato() != null) {doc.add(new Paragraph("Fecha de creación: "
                 + t.getFechaCreacionFormato()));
                }

                if (t.getFechaCierreFormato() != null) {doc.add(new Paragraph("Fecha de cierre: " 
                + t.getFechaCierreFormato()));
                }


                doc.add(new Paragraph("--------------------------------------------------"));
            }
        }

        if (!hayTickets) {
            doc.add(new Paragraph("⚠️ No hay tickets registrados para clientes."));
        }

        doc.close();
        System.out.println("📄 Historial generado correctamente: ticketsPDF/Historial_Clientes.pdf");

    } catch (Exception e) {
        System.out.println("❌ Error al generar historial: " + e.getMessage());
    }
}
    
    public void registrarIncidenciaGUI(Cliente cliente, String descripcion, String prioridad, Departamento depto) {
    Ticket t = new Ticket(descripcion, prioridad, cliente, depto);
    tickets.add(t);
}


    public void generarReporteAbiertos() {
        try {
            File directorio = new File("ticketsPDF");
            if (!directorio.exists()) {
                directorio.mkdirs(); // crea la carpeta si no existe
                }
            PdfWriter writer = new PdfWriter("ticketsPDF/Tickets_Abiertos.pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);
        // Fuente en negrita (iText 9)
                PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

                // Título con fuente negrita y tamaño 16
                Paragraph titulo = new Paragraph("=== Reporte de Tickets Abiertos ===")
                        .setFont(boldFont)
                        .setFontSize(16);
                doc.add(titulo);
                doc.add(new Paragraph(" ")); // línea en blanco
            for (Ticket t : tickets) {
                if (t.getEstado().equalsIgnoreCase("Abierto")) {
                    doc.add(new Paragraph("ID: " + t.getId() + " - " + t.getDescripcion() + " (" + t.getPrioridad() + ")"));
                    doc.add(new Paragraph("Fecha: " + t.getFechaCreacionFormato()));
                    doc.add(new Paragraph("--------------------------------------"));
                }
            }

            doc.close();
            System.out.println("📄 PDF de tickets abiertos generado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al generar reporte: " + e.getMessage());
        }
    }



    @Override
    public void listar() {
        if (tickets.isEmpty()) System.out.println("No hay tickets.");
        else tickets.forEach(System.out::println);}

    @Override
    public void guardarArchivo() {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("tickets.txt"))) {
            for (Ticket t : tickets) {
                bw.write("ID: " + t.getId()); bw.newLine();
                bw.write("Descripcion: " + t.getDescripcion()); bw.newLine();
                bw.write("Prioridad: " + t.getPrioridad()); bw.newLine();
                bw.write("Estado: " + t.getEstado()); bw.newLine();
                bw.write("Departamento: " + t.getDepartamento().getNombre()); bw.newLine();
                bw.write("Reportante: " + t.getReportante().getNombre()); bw.newLine();
                if (t.getTecnicoAsignado() != null)
                    bw.write("Tecnico: " + t.getTecnicoAsignado().getNombre() +
                             " (" + t.getTecnicoAsignado().getEspecialidad() + ")"); bw.newLine();
                if (t.getSolucion() != null)
                    bw.write("Solucion: " + t.getSolucion()); bw.newLine();
                bw.write("----------------------------------------"); bw.newLine();
            }
            System.out.println("✅ Tickets guardados correctamente en 'tickets.txt'");
        } catch (IOException e) {
            System.out.println("⚠️ Error al guardar: " + e.getMessage());
        }
    }



@Override
public void cargarArchivo() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tickets.txt"))) {
        @SuppressWarnings("unchecked")
        ArrayList<Ticket> lista = (ArrayList<Ticket>) ois.readObject();
        if (lista != null) {
            tickets = lista;
            // recalcular el siguiente id disponible
            int maxId = 0;
            for (Ticket t : tickets) {
                if (t.getId() > maxId) maxId = t.getId();
            }
            Ticket.setContador(maxId + 1);
        }
        System.out.println("Datos cargados correctamente. Siguiente ID: " + Ticket.getContador());
    } catch (FileNotFoundException fnf) {
        System.out.println("No se encontró el archivo 'tickets.dat'. Se inicia con lista vacía.");
    } catch (EOFException eof) {
        // archivo vacío, se ignora
        System.out.println("Archivo encontrado pero vacío. Se inicia con lista vacía.");
    } catch (ClassNotFoundException cnf) {
        System.out.println("Error: clase no encontrada al leer el archivo: " + cnf.getMessage());
        cnf.printStackTrace();
    } catch (InvalidClassException ice) {
        System.out.println("Error de serialización (serialVersionUID): " + ice.getMessage());
        ice.printStackTrace();
    } catch (IOException ioe) {
        System.out.println("Error de E/S al cargar archivo: " + ioe.getMessage());
        ioe.printStackTrace();
    } catch (Exception e) {
        System.out.println("Error inesperado al cargar archivo: " + e.getMessage());
        e.printStackTrace();
    }
}


    // Menú principal (fuera de la interfaz)
    public void menu() {
        int opcion = 0;
        do {
            System.out.println("\n=== SISTEMA DE TICKETS ===");
            System.out.println("1. Registrar nueva incidencia");
            System.out.println("2. Listar tickets");
            System.out.println("3. Guardar en archivo");
            System.out.println("4. Asignar técnico a un ticket");
            System.out.println("5. Cerrar ticket con solución");
            System.out.println("6. pdf tickets abiertos");
            System.out.println("7. historial clientes");
            System.out.println("8. salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1 -> registrarIncidencia();
                    case 2 -> listar();
                    case 3 -> guardarArchivo(); 
                    case 4 -> asignarTecnico();
                    case 5 -> cerrarTicket();
                    case 6 -> generarReporteAbiertos();
                    case 7 -> generarHistorialClientes();
                    case 8 -> System.out.println(" Saliendo..."); 

                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion !=8 );
    }
}


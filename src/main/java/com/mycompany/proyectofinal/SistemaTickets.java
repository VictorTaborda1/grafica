        /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import java.util.*;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.HorizontalAlignment;

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
     private Persona reportante;
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
    private ArrayList<Tecnico> tecnicos = new ArrayList<>();

public void agregarTecnico(Tecnico t) {
    tecnicos.add(t);
}

public ArrayList<Tecnico> getTecnicos() {
    return tecnicos;
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
         PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        
        
        String destino = "Historial_Clientes.pdf";

        PdfWriter writer = new PdfWriter(destino);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // === LOGO ===
        String rutaLogo = "src/main/imagenes/logo tech(1).png"; // AJUSTA TU RUTA
        ImageData data = ImageDataFactory.create(rutaLogo);
        Image logo = new Image(data).scaleToFit(80, 80);
        logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
        doc.add(logo);

        // === TITULO ===

        Paragraph titulo = new Paragraph("Historial de Clientes - TechNova Solutions")
                .setFont(bold)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);

        doc.add(titulo);
        doc.add(new Paragraph("\n"));

        // === TABLA ===
        // Tabla con dos columnas
float[] col = {150, 350};
Table tabla = new Table(UnitValue.createPercentArray(col));
tabla.setWidth(UnitValue.createPercentValue(100));
// ENCABEZADOS
tabla.addCell(new Cell().add(new Paragraph("Campo").setFont(bold)));
tabla.addCell(new Cell().add(new Paragraph("Valor").setFont(bold)));



// RECORRER TICKETS
for (Ticket t : this.getTickets()) {
    if (t.getReportante() != null) {

        tabla.addCell(new Cell().add(new Paragraph("Cliente")));
        tabla.addCell(new Cell().add(new Paragraph(t.getReportante().getNombre())));

        tabla.addCell(new Cell().add(new Paragraph("Email")));
        tabla.addCell(new Cell().add(new Paragraph(t.getReportante().getEmail())));

        tabla.addCell(new Cell().add(new Paragraph("ID_Ticket")));
        tabla.addCell(new Cell().add(new Paragraph(String.valueOf(t.getId()))));

        tabla.addCell(new Cell().add(new Paragraph("Descripción")));
        tabla.addCell(new Cell().add(new Paragraph(t.getDescripcion())));

        tabla.addCell(new Cell().add(new Paragraph("Estado")));
        tabla.addCell(new Cell().add(new Paragraph(t.getEstado())));

        tabla.addCell(new Cell().add(new Paragraph("Fecha_creación")));
        tabla.addCell(new Cell().add(new Paragraph(t.getFechaCreacionFormato())));
        
        // Fecha cierre
        tabla.addCell(new Cell().add(new Paragraph("Fecha cierre").setFont(normal)));
        tabla.addCell(new Cell().add(new Paragraph(t.getFechaCierreFormato()).setFont(normal)));
        
        tabla.addCell(new Cell().add(new Paragraph("Técnico").setFont(bold)));
        tabla.addCell(new Cell().add(new Paragraph( (t.getTecnicoAsignado() != null) 
            ? t.getTecnicoAsignado().getNombre() : "No asignado").setFont(normal)));
          
        tabla.addCell(new Cell().add(new Paragraph("Solución").setFont(bold)));
        // Celda Solución
        tabla.addCell(new Cell().add(new Paragraph( (t.getSolucion() != null) ? t.getSolucion() 
            : "Sin solución" ).setFont(normal)));
        tabla.addCell(new Cell().add(new Paragraph("Costo").setFont(bold)));
        tabla.addCell(new Cell().add(new Paragraph("$" + t.getCosto()).setFont(normal)));
        
    }
}



        doc.add(tabla);
        
         

        

        System.out.println("📄 Historial generado correctamente: ticketsPDF/Historial_Clientes.pdf");
        doc.close();

    } catch (Exception e) {
        System.out.println("❌ Error al generar historial: " + e.getMessage());
    }
}
    
    public Ticket registrarIncidenciaGUI(Persona persona, String descripcion, String prioridad, Departamento depto) {
    Ticket t = new Ticket(descripcion, prioridad, persona, depto);
    tickets.add(t);
    return t;}


    public void generarReporteAbiertos() {
        try {
            File outDir = new File("ticketsPDF");
        if (!outDir.exists()) outDir.mkdirs();

        String ruta = outDir.getPath() + File.separator + "Tickets_Abiertos.pdf";

        // crear PDF
        PdfWriter writer = new PdfWriter(ruta);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // fuentes
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // título
        Paragraph titulo = new Paragraph("REPORTE - TICKETS ABIERTOS")
                .setFont(boldFont)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER);
        doc.add(titulo);
        doc.add(new Paragraph(" "));

        // tabla: ID, Descripción, Prioridad, Fecha, Reportante, Técnico, Solución, Costo
        float[] widths = { 1f, 4f, 1.5f, 2f, 2.5f, 2.0f, 3f, 1.5f };
        Table tabla = new Table(UnitValue.createPercentArray(widths));
        tabla.setWidth(UnitValue.createPercentValue(100));

        // encabezados (negrita)
        tabla.addHeaderCell(new Cell().add(new Paragraph("ID").setFont(boldFont)));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Descripción").setFont(boldFont)));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Prioridad").setFont(boldFont)));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Fecha creación").setFont(boldFont)));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Reportante").setFont(boldFont)));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Técnico").setFont(boldFont)));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Solución").setFont(boldFont)));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Costo").setFont(boldFont)));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // recorrer tickets
        for (Ticket t : this.tickets) {
            if (!"Abierto".equalsIgnoreCase(t.getEstado())) continue; // solo abiertos

            // ID
            tabla.addCell(new Cell().add(new Paragraph(String.valueOf(t.getId())).setFont(normalFont)));

            // Descripción (limitar longitud visual si quieres)
            String desc = t.getDescripcion() == null ? "" : t.getDescripcion();
            tabla.addCell(new Cell().add(new Paragraph(desc).setFont(normalFont)));

            // Prioridad
            tabla.addCell(new Cell().add(new Paragraph(t.getPrioridad() == null ? "" : t.getPrioridad()).setFont(normalFont)));

            // Fecha creación (formateada)
            String fechaCre = t.getFechaCreacionFormato(); // ya tienes método en Ticket
            tabla.addCell(new Cell().add(new Paragraph(fechaCre).setFont(normalFont)));

            // Reportante (distinguir Cliente/Empleado)
            String rep = "-";
            if (t.getReportante() != null) {
                if (t.getReportante() instanceof Empleado) {
                    Empleado e = (Empleado) t.getReportante();
                    rep = "Empleado - " + e.getNombre() + " (" + (e.getDepartamento() != null ? e.getDepartamento() : "-") + ")";
                } else if (t.getReportante() instanceof Cliente) {
                    Cliente c = (Cliente) t.getReportante();
                    rep = "Cliente - " + c.getNombre() + " (" + (c.getEmail() != null ? c.getEmail() : "-") + ")";
                } else {
                    rep = t.getReportante().getNombre();
                }
            }
            tabla.addCell(new Cell().add(new Paragraph(rep).setFont(normalFont)));

            // Técnico
            String tec = t.getTecnicoAsignado() != null ? t.getTecnicoAsignado().getNombre() + " (" + t.getTecnicoAsignado().getEspecialidad() + ")" : "-";
            tabla.addCell(new Cell().add(new Paragraph(tec).setFont(normalFont)));

            // Solución (si no existe poner '-')
            String sol = (t.getSolucion() != null && !t.getSolucion().isEmpty()) ? t.getSolucion() : "-";
            tabla.addCell(new Cell().add(new Paragraph(sol).setFont(normalFont)));

            // Costo (formateado)
            String costoStr = String.format("$%.2f", t.getCosto());
            tabla.addCell(new Cell().add(new Paragraph(costoStr).setFont(normalFont)));
        }

        // si la tabla quedó vacía (sin filas) puedes añadir mensaje
        if (tabla.getNumberOfRows() <= 0) { // sólo encabezado
            doc.add(new Paragraph("No hay tickets abiertos actualmente.").setFont(normalFont));
        } else {
            doc.add(tabla);
        }

        doc.add(new Paragraph("\nGenerado automáticamente por TechNova Solutions").setFont(normalFont).setFontSize(9));
        doc.close();

        System.out.println("📄 PDF de tickets abiertos generado: " + ruta);

    } catch (Exception e) {
        System.out.println("❌ Error al generar reporte: " + e.getMessage());
        e.printStackTrace();
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



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;

import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;

public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int contador = 1;
    private int id;
    private String descripcion;
    private String estado;
    private String prioridad;
    private Persona reportante; // puede ser Empleado o Cliente
    private Departamento departamento;
    private Tecnico tecnicoAsignado;
    private String solucion;
    private double costo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCierre;


    public Ticket(String descripcion, String prioridad, Persona reportante, Departamento departamento) {
        this.id = contador++;
        this.descripcion = descripcion;
        this.estado = "Abierto";
        this.prioridad = prioridad;
        this.reportante = reportante;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaCierre = LocalDateTime.now();
        this.departamento = departamento;
        
    }
     public void cerrarTicket(String solucion, double costo, Tecnico tecnico) {
        this.solucion = solucion;
        this.costo = costo;
        this.tecnicoAsignado = tecnico;
        this.estado = "Cerrado";
        this.fechaCierre = LocalDateTime.now();
    }
      public void registrarSolucion(String solucion) {
        this.solucion = solucion;
        this.estado = "Cerrado";
    }

    public void asignarTecnico(Tecnico tecnico) {
        this.tecnicoAsignado = tecnico; // Agregación (el técnico existe independientemente)
    }
    public void mostrarInfo() {
        System.out.println("\n🎫 Ticket ID: " + id);
        System.out.println("📄 Descripción: " + descripcion);
        System.out.println("📊 Prioridad: " + prioridad);
        System.out.println("📌 Estado: " + estado);
        System.out.println("🏢 Departamento: " + departamento.getNombre());
        reportante.mostrarInfo();
        if (tecnicoAsignado != null)
            tecnicoAsignado.mostrarInfo();
        if (solucion != null)
            System.out.println("✅ Solución: " + solucion);
        System.out.println("----------------------------------");
    }


    // Getters y setters
    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public String getPrioridad() { return prioridad; }
    public Persona getReportante() { return reportante; }
    public Departamento getDepartamento() { return departamento; }
    public Tecnico getTecnicoAsignado() {return tecnicoAsignado;}
    public String getSolucion() {return solucion;}
    public double getCosto() {
        return costo;
    }

    public String getFechaCreacionFormato() {
        return fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    public String getFechaCierreFormato() {
            return (fechaCierre != null)
                    ? fechaCierre.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : "En progreso";
        }

    public void setSolucion(String solucion) {this.solucion = solucion;}
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }
    public void setTecnicoAsignado(Tecnico tecnicoAsignado) {this.tecnicoAsignado = tecnicoAsignado;}
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public void setCosto(double costo) {
        this.costo = costo;
    }
    public void setReportante(Persona reportante) {this.reportante = reportante;}

    
    
    public void setEstado(String estado) { 
    this.estado = estado; }
    public void setId(int id) {
        this.id = id;
        // Asegurar que contador siempre sea mayor que cualquier id existente
        if (id >= contador) {
            contador = id + 1;
        }
    }
public void generarPDF() {
    try {
        String nombreArchivo = "Ticket_" + id + "_" + System.currentTimeMillis() + ".pdf";
        PdfWriter writer = new PdfWriter(nombreArchivo);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        Style boldStyle = new Style().setFont(boldFont).setFontSize(16);
        // fuentes
    PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
    PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        try {
            ImageData imgData = ImageDataFactory.create("src/main/imagenes/logo tech(1).png"); 
            Image logo = new Image(imgData);
            logo.setWidth(100);
            logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(logo);
        } catch (Exception ex) {
            System.out.println("No se encontró el logo, continuando sin imagen...");
        }
        document.setTextAlignment(TextAlignment.CENTER);
        document.add(new Paragraph("=== Ticket #" + id + " ===").addStyle(boldStyle));
         // tabla con 2 columnas (anchos en puntos)
    float[] columnWidths = {150f, 350f};
    Table tabla = new Table(columnWidths);
    tabla.setWidth(UnitValue.createPointValue(500f)); // ancho total en puntos

    // fila: Descripción
    tabla.addCell(new Cell().add(new Paragraph("Descripción:").setFont(bold)));
    tabla.addCell(new Cell().add(new Paragraph(descripcion).setFont(normal)));

    // fila: Prioridad
    tabla.addCell(new Cell().add(new Paragraph("Prioridad:").setFont(bold)));
    tabla.addCell(new Cell().add(new Paragraph(prioridad).setFont(normal)));

    // fila: Estado
    tabla.addCell(new Cell().add(new Paragraph("Estado:").setFont(bold)));
    tabla.addCell(new Cell().add(new Paragraph(estado).setFont(normal)));

    // fila: Fecha creación
    tabla.addCell(new Cell().add(new Paragraph("Fecha de creación:").setFont(bold)));
    tabla.addCell(new Cell().add(new Paragraph(getFechaCreacionFormato()).setFont(normal)));

    // fila: Fecha cierre
    tabla.addCell(new Cell().add(new Paragraph("Fecha de cierre:").setFont(bold)));
    tabla.addCell(new Cell().add(new Paragraph(getFechaCierreFormato()).setFont(normal)));

        String tipoReportante;

if (reportante instanceof Empleado) {
    tipoReportante = "Empleado";
} else if (reportante instanceof Cliente) {
    tipoReportante = "Cliente";
} else {
    tipoReportante = "Reportante";
}

document.add(new Paragraph(tipoReportante + ": " + reportante.getNombre()));
        // fila: Técnico
    tabla.addCell(new Cell().add(new Paragraph("Técnico asignado:").setFont(bold)));
    String tec = tecnicoAsignado != null ? tecnicoAsignado.getNombre() + " (" + tecnicoAsignado.getEspecialidad() + ")" : "NINGUNO";
    tabla.addCell(new Cell().add(new Paragraph(tec).setFont(normal)));

    // fila: Solución
    tabla.addCell(new Cell().add(new Paragraph("Solución:").setFont(bold)));
    tabla.addCell(new Cell().add(new Paragraph(solucion != null ? solucion : "SIN SOLUCIÓN").setFont(normal)));

    // fila: Costo
    tabla.addCell(new Cell().add(new Paragraph("Costo:").setFont(bold)));
    tabla.addCell(new Cell().add(new Paragraph("$" + String.format("%.2f", costo)).setFont(normal)));

    // añadir tabla al documento
    document.add(tabla);

    document.add(new Paragraph("--------------------------------------------------"));
    document.add(new Paragraph("Generado automáticamente por el Sistema de Tickets TechNova Solutions").setFont(normal));

        document.close();
        System.out.println("📄 PDF generado correctamente: " + nombreArchivo);
    } catch (Exception e) {
        System.out.println("❌ Error al generar PDF: " + e.getMessage());
    }
}


    // Métodos para el contador estático
    public static int getContador() { return contador; }
    public static void setContador(int nuevo) {
        if (nuevo > contador) contador = nuevo;
    }

    @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("🎫 Ticket ID: ").append(id).append("\n");
    sb.append("📄 Descripción: ").append(descripcion).append("\n");
    sb.append("📊 Prioridad: ").append(prioridad).append("\n");
    sb.append("📌 Estado: ").append(estado).append("\n");
    if (departamento != null) sb.append("🏢 Departamento: ").append(departamento.getNombre()).append("\n");
    if (reportante != null) {
        sb.append("🧾 Reportante: ");
        // usa el tipo concreto para mostrar detalles
        if (reportante instanceof Empleado) {
            Empleado e = (Empleado) reportante;
            sb.append("Empleado - ").append(e.getNombre()).append(" (").append(e.getDepartamento()).append(")").append("\n");
        } else if (reportante instanceof Cliente) {
            Cliente c = (Cliente) reportante;
            sb.append("Cliente - ").append(c.getNombre()).append(" (").append(c.getEmail()).append(")").append("\n");
        } else {
            sb.append(reportante.getNombre()).append("\n");
        }
    }
    if (tecnicoAsignado != null) sb.append("🧑‍🔧 Técnico: ").append(tecnicoAsignado.getNombre())
                                       .append(" (").append(tecnicoAsignado.getEspecialidad()).append(")\n");
    if (solucion != null) sb.append("✅ Solución: ").append(solucion).append("\n");
    sb.append("----------------------------------");
    return sb.toString();
}
}

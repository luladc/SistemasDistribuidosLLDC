/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RemateUSFX;

/**
 *
 * @author LLDC
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jgroups.Address;

public class Subasta implements Serializable {
    private static final long serialVersionUID = 1L;

    private String articulo;
    private double mejorPuja;
    private String creador;
    private String ganadorProvisional;
    private long tiempoCierre;
    private boolean activa;
    private List<String> historialPujas; 

    public Subasta(String articulo, double precioBase, long tiempoCierre, String creador) {
        this.articulo = articulo;
        this.mejorPuja = precioBase;
        this.tiempoCierre = tiempoCierre;
        this.creador = creador;
        this.activa = true;
        this.historialPujas = new ArrayList<>();
    }

    // Getters y modificadores básicos
    public String getArticulo() { return articulo; }
    public double getMejorPuja() { return mejorPuja; }
    public void setMejorPuja(double mejorPuja, String postor) { 
        this.mejorPuja = mejorPuja; 
        this.ganadorProvisional = postor;
    }
    public String getCreador() { return creador; }
    public String getGanadorProvisional() { return ganadorProvisional; }
    public long getTiempoCierre() { return tiempoCierre; }
    public boolean isActiva() { return activa; }
    public void cerrar() { this.activa = false; }
    
    public void registrarEnHistorial(String registro) {
        historialPujas.add(registro);
    }
    public List<String> getHistorialPujas() { return historialPujas; }
}

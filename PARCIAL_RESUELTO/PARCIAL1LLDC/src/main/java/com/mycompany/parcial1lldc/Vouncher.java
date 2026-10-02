/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parcial1lldc;

import java.io.Serializable;

/**
 *
 * @author USUARIO
 */
public class Vouncher implements Serializable{
    boolean confirmado;
    String codigoCompra;
    String motivo;

    public Vouncher(boolean confirmado, String codigoCompra, String motivo, double montoUSD) {
        this.confirmado = confirmado;
        this.codigoCompra = codigoCompra;
        this.motivo = motivo;
        this.montoUSD = montoUSD;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getCodigoCompra() {
        return codigoCompra;
    }

    public String getMotivo() {
        return motivo;
    }

    public double getMontoUSD() {
        return montoUSD;
    }
    double montoUSD;
    
    public String comprarTour(int pasaporte, String codigoTour, int personas){
        System.out.println("");
    }
}

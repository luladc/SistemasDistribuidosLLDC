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
public class Pago implements Serializable {

    boolean aprobado;
    String codigoAutorizacion;
    String motivo;

    public Pago(boolean aprobado, String codigoAutorizacion, String motivo) {
        this.aprobado = aprobado;
        this.codigoAutorizacion = codigoAutorizacion;
        this.motivo = motivo;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    public String getMotivo() {
        return motivo;
    }
    
    
}

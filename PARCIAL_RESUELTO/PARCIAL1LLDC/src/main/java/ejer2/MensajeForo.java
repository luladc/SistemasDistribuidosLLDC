/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejer2;

import java.io.Serializable;

/**
 *
 * @author USUARIO
 */
public class MensajeForo implements Serializable {
    private static long serialVersionUID= 1L;
    
    TipoMensaje tipo;
    String puerta;
    int personas;

    public MensajeForo(TipoMensaje tipo, String puerta, int personas) {
        this.tipo = tipo;
        this.puerta = puerta;
        this.personas = personas;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public TipoMensaje getTipo() {
        return tipo;
    }

    public String getPuerta() {
        return puerta;
    }

    public int getPersonas() {
        return personas;
    }
    
            
}

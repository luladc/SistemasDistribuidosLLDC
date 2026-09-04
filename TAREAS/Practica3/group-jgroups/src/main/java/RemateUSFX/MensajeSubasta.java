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
import org.jgroups.Address;

public class MensajeSubasta implements Serializable {
    private static final long serialVersionUID = 1L;

    private TipoMensaje tipo;
    private String articulo;
    private double monto;
    private long tiempoCierre; 
    private String emisor;

    public MensajeSubasta(TipoMensaje tipo, String articulo, double monto, long tiempoCierre, String emisor) {
        this.tipo = tipo;
        this.articulo = articulo;
        this.monto = monto;
        this.tiempoCierre = tiempoCierre;
        this.emisor = emisor;
    }

    // Getters para acceder a los datos al recibir el mensaje
    public TipoMensaje getTipo() { return tipo; }
    public String getArticulo() { return articulo; }
    public double getMonto() { return monto; }
    public long getTiempoCierre() { return tiempoCierre; }
    public String getEmisor() { return emisor; }
}
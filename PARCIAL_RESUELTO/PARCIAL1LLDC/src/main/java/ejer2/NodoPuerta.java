/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejer2;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.Receiver;

/**
 *
 * @author USUARIO
 */
public class NodoPuerta implements Receiver {

    private JChannel canal;
    private String nombre;

    public NodoPuerta(String nombre) {
        this.nombre = nombre;
    }
    View vistaAnterior;

    @Override
    public void viewAccepted(View vista) {
        if (vistaAnterior != null) {
            Address[][] cambios = View.diff(vistaAnterior, vista);
            for (Address a : cambios[0]) {
                System.out.println("** entro: " + a);
            }
            for (Address a : cambios[1]) {
                System.out.println("** salio: " + a);
            }
        }
        vistaAnterior = vista;
        System.out.println("** vista " + vista.getViewId().getId()
                + " | coodirnador: " + vista.getCoord()
                + " | miembros: " + vista.getMembers());

    }

    private List<String> historial = new ArrayList<>();

    public void receive(Message msg) {
        String texto = msg.getSrc() + ": " + msg.getObject();
        synchronized (historial) {
        historial.add(texto);
        }
        
    }

    public void getState(OutputStream salida) {
          synchronized (historial) {
         Util.objectToStream(historial, new DataOutputStream(salida));
         
        }
          System.out.println("** estado enviado a mienro ");
    }

}

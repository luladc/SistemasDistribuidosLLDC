/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RemateUSFX;

/**
 *
 * @author LLDC
 */
import java.io.BufferedReader;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.jgroups.ObjectMessage;
import java.util.concurrent.TimeUnit;

public class RemateUSFX implements Receiver {

    private JChannel canal;
    private final String nombreParticipante;

    private final ConcurrentHashMap<String, Subasta> subastasActivas = new ConcurrentHashMap<>();

    private final ScheduledExecutorService planificador = Executors.newScheduledThreadPool(5);

    public RemateUSFX(String nombreParticipante) {
        this.nombreParticipante = nombreParticipante;
    }

    public void iniciar() throws Exception {
        canal = new JChannel(System.getProperty("config", "udp.xml"));
        canal.name(nombreParticipante);
        canal.setReceiver(this);
        canal.connect("RemateSIS258");

        canal.getState(null, 10000);

        leerTeclado();

        planificador.shutdown();
        canal.close();
    }

    private void leerTeclado() throws Exception {
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Comandos: /crear <art> <precio> <seg> | /pujar <art> <monto> | /subastas | /salir");
        String linea;

        while ((linea = teclado.readLine()) != null) {
            if (linea.equals("/salir")) {
                break;
            }
            String[] partes = linea.split(" ");

            if (linea.startsWith("/crear ") && partes.length == 4) {
                String articulo = partes[1];
                double precio = Double.parseDouble(partes[2]);
                long segundos = Long.parseLong(partes[3]);
                long tiempoCierreAbsoluto = System.currentTimeMillis() + (segundos * 1000); // Instante absoluto

                MensajeSubasta msg = new MensajeSubasta(TipoMensaje.NUEVA_SUBASTA, articulo, precio, tiempoCierreAbsoluto, canal.getAddress().toString());
                canal.send(new ObjectMessage(null, msg)); // Multicast a todos[cite: 1]

            } else if (linea.startsWith("/pujar ") && partes.length == 3) {
                String articulo = partes[1];
                double monto = Double.parseDouble(partes[2]);

                MensajeSubasta msg = new MensajeSubasta(TipoMensaje.INTENTO_PUJA, articulo, monto, 0, canal.getAddress().toString());
                Address coordinador = canal.getView().getCoord();
                canal.send(new ObjectMessage(coordinador, msg)); // Unicast SOLO al coordinador[cite: 1]
                System.out.println("-> Propuesta enviada al coordinador para validación.");

            } else if (linea.equals("/subastas")) {
                subastasActivas.values().forEach(s -> {
                    if (s.isActiva()) {
                        System.out.println("- " + s.getArticulo() + " | Mejor puja: " + s.getMejorPuja());
                    }
                });
            }
            else if (linea.startsWith("/estado ") && partes.length == 2) {
            String articulo = partes[1];
            Subasta s = subastasActivas.get(articulo);
            
            if (s != null) {
                System.out.println("--- Historial de: " + articulo + " ---");
                if (s.getHistorialPujas().isEmpty()) {
                    System.out.println("Sin pujas aún.");
                } else {
                    s.getHistorialPujas().forEach(System.out::println);
                }
            } else {
                System.out.println("La subasta '" + articulo + "' no existe.");
            } 
            }else if (linea.equals("/quien")) {
            System.out.println("Participantes conectados: " + canal.getView().getMembers());
            System.out.println("Coordinador actual: " + canal.getView().getCoord());
        }
            else if (linea.equals("/ganadas")) {
            System.out.println("--- Mis artículos ganados ---");
            double totalPagar = 0;
            boolean tieneGanadas = false;
            
            for (Subasta s : subastasActivas.values()) {
                if (!s.isActiva() && canal.getAddress().equals(s.getGanadorProvisional())) {
                    System.out.println("- " + s.getArticulo() + " (Por: " + s.getMejorPuja() + ")");
                    totalPagar += s.getMejorPuja();
                    tieneGanadas = true;
                }
            }
            
            if (!tieneGanadas) {
                System.out.println("No has ganado ninguna subasta.");
            } else {
                System.out.println("TOTAL A PAGAR: " + totalPagar);
            }
    }}}

    @Override
    public void viewAccepted(View vista) {
        Address coordinador = vista.getCoord();
        System.out.println("** Nueva vista instalada. Coordinador actual: " + coordinador);

        if (canal.getAddress().equals(coordinador)) {
            subastasActivas.values().forEach(s -> {
                if (s.isActiva()) {
                    programarCierre(s.getArticulo(), s.getTiempoCierre());
                }
            });
            System.out.println("-> He asumido como coordinador. Temporizadores recalculados.");
        }
    }

    private void programarCierre(String articulo, long tiempoCierreAbsoluto) {
        long retardoMillis = tiempoCierreAbsoluto - System.currentTimeMillis();
        if (retardoMillis < 0) {
            retardoMillis = 0; // Si ya pasó, se ejecuta de inmediato
        }
        planificador.schedule(() -> {
            Subasta s = subastasActivas.get(articulo);
            if (s != null && s.isActiva()) {
                // El coordinador oficial envía el mensaje de CIERRE a todo el grupo
                MensajeSubasta msgCierre = new MensajeSubasta(TipoMensaje.CIERRE, articulo, s.getMejorPuja(), 0, canal.getAddress().toString());
                try {
                    canal.send(new ObjectMessage(null, msgCierre));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, retardoMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public void receive(Message msg) {
        if (!(msg.getObject() instanceof MensajeSubasta)) {
            return;
        }
        MensajeSubasta peticion = msg.getObject();
        String art = peticion.getArticulo();

        switch (peticion.getTipo()) {
            case NUEVA_SUBASTA:
                Subasta nueva = new Subasta(art, peticion.getMonto(), peticion.getTiempoCierre(), peticion.getEmisor());
                subastasActivas.put(art, nueva);
                System.out.println("\n*** NUEVA SUBASTA: " + art + " por " + peticion.getMonto());

                if (canal.getAddress().equals(canal.getView().getCoord())) {
                    programarCierre(art, peticion.getTiempoCierre());
                }
                break;

            case INTENTO_PUJA:
                // ESTO SOLO LO EJECUTA EL COORDINADOR[cite: 1]
                Subasta subastaValidar = subastasActivas.get(art);
                if (subastaValidar != null && subastaValidar.isActiva() && peticion.getMonto() > subastaValidar.getMejorPuja()) {
                    // Validación superada, el coordinador difunde la puja aceptada a todos[cite: 1]
                    MensajeSubasta aceptada = new MensajeSubasta(TipoMensaje.PUJA_ACEPTADA, art, peticion.getMonto(), 0, peticion.getEmisor());
                    try {
                        canal.send(new ObjectMessage(null, aceptada)); // Multicast de la decisión final[cite: 1]
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case PUJA_ACEPTADA:
                // TODOS los nodos actualizan su estado local con la decisión del coordinador[cite: 1]
                Subasta s = subastasActivas.get(art);
                if (s != null) {
                    s.setMejorPuja(peticion.getMonto(), peticion.getEmisor());
                    s.registrarEnHistorial(peticion.getEmisor() + " pujó " + peticion.getMonto());
                    System.out.println("\n*** PUJA ACEPTADA en " + art + ": " + peticion.getMonto() + " de " + peticion.getEmisor());
                }
                break;
            case CIERRE:
                Subasta subastaCerrada = subastasActivas.get(art);
                if (subastaCerrada != null && subastaCerrada.isActiva()) {
                    subastaCerrada.cerrar(); // Pasa a estado "cerrada" en todos los nodos[cite: 1]
                    System.out.println("\n*** SUBASTA CERRADA: " + art + " ***");
                    if (subastaCerrada.getGanadorProvisional() != null) {
                        System.out.println("Ganador: " + subastaCerrada.getGanadorProvisional() + " | Monto final: " + subastaCerrada.getMejorPuja());
                    } else {
                        System.out.println("Ningún participante pujó. Subasta desierta.");
                    }
                }
                break;
        }
    }

    @Override
    public void getState(OutputStream salida) throws Exception {
        Util.objectToStream(subastasActivas, new DataOutputStream(salida));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setState(InputStream entrada) throws Exception {
        ConcurrentHashMap<String, Subasta> recibido
                = (ConcurrentHashMap<String, Subasta>) Util.objectFromStream(new DataInputStream(entrada));
        subastasActivas.clear();
        subastasActivas.putAll(recibido);
        System.out.println("** Estado sincronizado: " + subastasActivas.size() + " subastas activas.");
    }

    public static void main(String[] args) throws Exception {
        String nombre = args.length > 0 ? args[0] : "Participante-" + (int) (Math.random() * 100);
        new RemateUSFX(nombre).iniciar();
    }
}

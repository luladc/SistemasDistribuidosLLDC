package grupo7;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/** Nodo 2: cuenta palabras y determina la paridad de caracteres. */
public class Nodo2 {
    private static final int PUERTO_NODO_2 = 7002;
    private static final int PUERTO_NODO_3 = 7003;
    private static final int TAMANO_BUFFER = 65507;

    public static void main(String[] args) {


        System.out.println("Nodo 2 escuchando en el puerto " + PUERTO_NODO_2 + "...");
        try (DatagramSocket socket = new DatagramSocket(PUERTO_NODO_2)) {
            byte[] buffer = new byte[TAMANO_BUFFER];
            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
            socket.receive(peticion);

            Protocolo.Mensaje mensaje = Protocolo.decodificar(
                    new String(peticion.getData(), 0, peticion.getLength(), StandardCharsets.UTF_8));
            int palabras = contarPalabras(mensaje.texto());
            String paridad = mensaje.caracteres() % 2 == 0 ? "par" : "impar";
            String procesado = Protocolo.codificar(mensaje.texto(), mensaje.caracteres(), palabras, paridad);

            byte[] datos = procesado.getBytes(StandardCharsets.UTF_8);
            DatagramPacket salida = new DatagramPacket(datos, datos.length,
                    InetAddress.getByName("172.20.10.3"), PUERTO_NODO_3);
            socket.send(salida);
            System.out.println("Mensaje procesado y enviado al Nodo 3.");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error en el Nodo 2: " + e.getMessage());
        }
    }

    private static int contarPalabras(String texto) {
        String limpio = texto.trim();
        return limpio.isEmpty() ? 0 : limpio.split("\\s+").length;
    }
}

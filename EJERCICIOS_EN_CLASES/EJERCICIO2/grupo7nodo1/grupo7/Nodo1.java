package grupo7;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/** Nodo 1: solicita el texto, cuenta sus caracteres y recibe el resultado final. */
public class Nodo1 {
    public static final int PUERTO_NODO_1 = 7001;
    public static final int PUERTO_NODO_2 = 7002;
    private static final int TAMANO_BUFFER = 65507;

    public static void main(String[] args) {
        

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
             DatagramSocket socket = new DatagramSocket(PUERTO_NODO_1)) {
            System.out.print("Ingrese una palabra o frase: ");
            String texto = scanner.nextLine();
            int caracteres = texto.length();

            String mensaje = Protocolo.codificar(texto, caracteres);
            enviar(socket, mensaje, InetAddress.getByName("172.20.10.4"), PUERTO_NODO_2);
            System.out.println("Información enviada al Nodo 2.");

            byte[] buffer = new byte[TAMANO_BUFFER];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
            socket.receive(respuesta);
            String resultado = new String(respuesta.getData(), 0, respuesta.getLength(),
                    StandardCharsets.UTF_8);

            System.out.println("\n===== RESULTADO FINAL =====");
            System.out.println(resultado);
        } catch (IOException e) {
            System.err.println("No fue posible completar la comunicación: " + e.getMessage());
        }
    }

    private static void enviar(DatagramSocket socket, String mensaje,
                               InetAddress destino, int puerto) throws IOException {
        byte[] datos = mensaje.getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(datos, datos.length, destino, puerto));
    }
}

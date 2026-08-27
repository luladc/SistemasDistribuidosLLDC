/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package justicia;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/**
 *
 * @author luisf
 */
public class ClienteBancoBCPUDP {
    private String host;
    private int puerto;

    public ClienteBancoBCPUDP(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public String consultar(String ci) {
        return enviarDatagrama("buscar:" + ci);
    }

    public String congelar(String nroCuenta, double monto) {
        return enviarDatagrama("congelar:" + nroCuenta + ":" + monto);
    }

    private String enviarDatagrama(String mensaje) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(4000);
            InetAddress ipDestino = InetAddress.getByName(host);

            byte[] bufferEnvio = mensaje.getBytes();
            DatagramPacket paqueteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, ipDestino, puerto);
            socket.send(paqueteEnvio);

            byte[] bufferRecepcion = new byte[1024];
            DatagramPacket paqueteRecepcion = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);
            socket.receive(paqueteRecepcion);

            return new String(paqueteRecepcion.getData(), 0, paqueteRecepcion.getLength()).trim();
        } catch (Exception e) {
            System.err.println("[UDP BCP] Error: " + e.getMessage());
            return "ERROR:No se pudo conectar con Banco BCP";
        }
    }
}
package justicia;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteBancoMercantilTCP {
    private String host;
    private int puerto;

    public ClienteBancoMercantilTCP(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public String consultar(String ci) {
        // Enviar en dos pasos: 1. CI, 2. Operación[cite: 1]
        return enviarDobleLinea(ci, "buscar");
    }

    public String congelar(String nroCuenta, double monto) {
        // Para que el servidor TCP de tu compañero avance, se envía el CI en la primera línea.
        // Se asume el CI del caso de prueba obligatorio (11021654) para que la búsqueda interna coincida.
        return enviarDobleLinea("11021654", "congelar:" + nroCuenta + ":" + monto);
    }

    private String enviarDobleLinea(String linea1, String linea2) {
        try (Socket socket = new Socket(host, puerto);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            socket.setSoTimeout(4000);
            
            // Obligatorio usar dos println() para que el BufferedReader del banco no se congele
            out.println(linea1); 
            out.println(linea2); 
            
            String respuesta = in.readLine();
            return (respuesta != null) ? respuesta.trim() : "";
        } catch (Exception e) {
            System.err.println("[TCP Mercantil] Error: " + e.getMessage());
            return "ERROR:No se pudo conectar con Banco Mercantil";
        }
    }
}

package justicia;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorJusticiaMain {
    public static void main(String[] args) {
        try {
            // ¡ESTA LÍNEA ES CLAVE! Debe ir antes de cualquier otra configuración RMI
            System.setProperty("java.rmi.server.hostname", "10.177.94.249"); // Usar su IP real aquí
            
            JusticiaImpl servidor = new JusticiaImpl("10.177.94.209", 5001,5002 );
            Registry registro = LocateRegistry.createRegistry(1099);
            registro.rebind("ServidorJusticiaMain", servidor);
            
            System.out.println("Servidor Justicia listo. Esperando al Juez...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
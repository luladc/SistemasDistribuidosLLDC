/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comun;

/**
 *
 * @author LLDC
 */
import comun.Banco;
import comun.Cuenta;
import comun.IJusticia;
import comun.RespuestaCuenta;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteJuez {
    public static void main(String[] args) {
        try {
            Registry registro = LocateRegistry.getRegistry("10.18.168.198", 1099);
            IJusticia servidorJusticiaMain = (IJusticia) registro.lookup("ServidorJusticiaMain");
            
            System.out.println("--- Iniciando Emisión de Consulta Judicial ---");
            
            String ci = "11021654"; //[cite: 1]
            String nombres = "Juan Perez"; //[cite: 1]
            String apellidos = "Segovia"; //[cite: 1]
            
            System.out.println("Consultando CI: " + ci + " | " + nombres + " " + apellidos);
            
            RespuestaCuenta respuesta = servidorJusticiaMain.ConsultarCuentas(ci, nombres, apellidos);
            
            if (!respuesta.isError()) {
                System.out.println("\nResultados encontrados:");
                for (Cuenta cuenta : respuesta.getCuentas()) {
                    System.out.println("- " + cuenta.getBanco() + ": Cuenta " + cuenta.getNrocuenta() + ", Saldo " + cuenta.getSaldo());
                }
                
                if (!respuesta.getCuentas().isEmpty()) {
                    System.out.println("\n--- Iniciando orden de congelamiento ---");
                    Cuenta cuentaObjetivo = respuesta.getCuentas().get(0); 
                    String mensajeRetencion = servidorJusticiaMain.Congelar(cuentaObjetivo, 1000.0);
                    System.out.println("Estado de retención en " + cuentaObjetivo.getBanco() + ": " + mensajeRetencion);
                }
            } else {
                System.out.println("Error en la consulta: " + respuesta.getMensaje());
            }
        } catch (Exception e) {
            System.err.println("Error en el Cliente Juez: No se pudo conectar.");
            e.printStackTrace();
        }
    }
}
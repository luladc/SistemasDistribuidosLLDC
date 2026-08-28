/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;

/**
 *
 * @author andradeaaron39
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class BancoBCP {
    private static final int PUERTO = 5002;
    private static final Map<String, String> cuentas = new HashMap<>();

    public static void main(String[] args) {
        cargarDatos();
        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
            System.out.println("Banco BCP UDP iniciado en puerto " + PUERTO + "...");
            byte[] buffer = new byte[2000];

            while (true) {
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                socket.receive(peticion);

                // SE AGREGA .trim() PARA LIMPIAR LA BASURA DEL BUFFER
                String mensaje = new String(peticion.getData(), 0, peticion.getLength()).trim();
                System.out.println("Mensaje UDP recibido: " + mensaje);

                String respuesta = procesar(mensaje);
                byte[] datosRespuesta = respuesta.getBytes();

                DatagramPacket paqueteRespuesta = new DatagramPacket(
                        datosRespuesta, datosRespuesta.length, 
                        peticion.getAddress(), peticion.getPort());
                socket.send(paqueteRespuesta);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void cargarDatos() {
        cuentas.put("11021654", "6576-6500"); //[cite: 1]
        cuentas.put("22222222", "8888-2000:9999-4000");
    }

    private static String procesar(String mensaje) {
        // El formato esperado aquí es "operacion:ci" (ej. "buscar:11021654")[cite: 1]
        String[] partes = mensaje.split(":");
        if (partes.length < 2) return "Formato incorrecto";

        String operacion = partes[0];
        String ci = partes[1];

        if ("buscar".equalsIgnoreCase(operacion)) {
            return cuentas.getOrDefault(ci, "");
        }
        
        if ("congelar".equalsIgnoreCase(operacion)) {
            if (partes.length < 4) return "Datos incompletos";
            String cuenta = partes[2];
            double monto = Double.parseDouble(partes[3]);
            return "Congelamiento BCP realizado. Cuenta: " + cuenta + " Monto: " + monto;
        }
        return "Operacion no valida";
    }
}
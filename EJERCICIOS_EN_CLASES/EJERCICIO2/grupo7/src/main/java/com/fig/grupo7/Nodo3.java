/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fig.grupo7;

/**
 *
 * @author LLDC
 */


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/** Nodo 3: convierte a mayúsculas, cuenta vocales y crea el resumen. */
public class Nodo3 {
    private static final int PUERTO_NODO_3 = 7003;
    private static final int PUERTO_NODO_1 = 7001;
    private static final int TAMANO_BUFFER = 65507;

    public static void main(String[] args) {

        System.out.println("Nodo 3 escuchando en el puerto " + PUERTO_NODO_3 + "...");
        try (DatagramSocket socket = new DatagramSocket(PUERTO_NODO_3)) {
            byte[] buffer = new byte[TAMANO_BUFFER];
            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
            socket.receive(peticion);

            Protocolo.Mensaje mensaje = Protocolo.decodificar(
                    new String(peticion.getData(), 0, peticion.getLength(), StandardCharsets.UTF_8));
            String mayusculas = mensaje.texto().toUpperCase(Locale.ROOT);
            int vocales = contarVocales(mensaje.texto());
            String resumen = "Texto original: " + mensaje.texto()
                    + "\nTexto en mayúsculas: " + mayusculas
                    + "\nCantidad de caracteres: " + mensaje.caracteres()
                    + "\nCantidad de palabras: " + mensaje.palabras()
                    + "\nLa cantidad de caracteres es: " + mensaje.paridad()
                    + "\nCantidad de vocales: " + vocales;

            byte[] datos = resumen.getBytes(StandardCharsets.UTF_8);
            DatagramPacket respuesta = new DatagramPacket(datos, datos.length,
                    InetAddress.getByName("172.20.10.2"), PUERTO_NODO_1);
            socket.send(respuesta);
            System.out.println("Resumen enviado nuevamente al Nodo 1.");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error en el Nodo 3: " + e.getMessage());
        }
    }

    private static int contarVocales(String texto) {
        int total = 0;
        for (char caracter : texto.toLowerCase(Locale.ROOT).toCharArray()) {
            if ("aeiouáéíóúü".indexOf(caracter) >= 0) {
                total++;
            }
        }
        return total;
    }
}
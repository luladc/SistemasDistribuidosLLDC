/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parcial1lldc;

import static com.mycompany.parcial1lldc.Migracion.procesar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author USUARIO
 */
public class Antifraude {
    public static void main(String[] args) {
        int puerto = 6789;
        try {
            DatagramSocket socketUDP = new DatagramSocket(puerto);
            byte [] bufer = new byte[1000];
            System.out.println("ANTIFRAUDE ECUHANDO POR EL PUERTO " + puerto);
            while (true) {
                DatagramPacket peticion = new DatagramPacket (bufer, bufer.length);
                socketUDP.receive(peticion);
                String cadena = new String (peticion.getData(), 0, peticion.getLength());
                System.out.println("ANTIFRAUDE RECIBIO: " +  cadena);
                String respuesta = procesar(cadena);
                byte [] mensaje = respuesta.getBytes();
                DatagramPacket envio=new DatagramPacket (mensaje, mensaje.length, peticion.getAddress(), peticion.getPort());
                socketUDP.send(envio);
                
            }
        } catch (SocketException ex) {
            System.out.println("socxket " + ex.getMessage());
        }   catch (IOException ex) {
            System.out.println("io. " + ex.getMessage());
        }
    }
    
    public static String procesar(String cadena){
    String [] partes = cadena.split(":");
    
    }
}

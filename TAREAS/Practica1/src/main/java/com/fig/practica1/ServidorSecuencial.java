/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fig.practica1;

import java.io.*;
import java.net.*;

/**
 *
 * @author LLDC
 */
public class ServidorSecuencial {

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(5000);
        System.out.println("Servidor escuchando en el puerto 5000...");
    while (true) {
    System.out.println("accept() ... esperando un cliente");
    Socket cliente = servidor.accept(); 
    System.out.println("Conectado: " + cliente.getInetAddress());
    
    try {
        BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
        String linea;
        
        while ((linea = in.readLine()) != null) { 
            System.out.println("Recibido: " + linea);
            out.println("ECO: " + linea);
        }
    } catch (IOException e) {
        System.out.println("El cliente se desconectó abruptamente: " + e.getMessage());
    } finally {
        cliente.close(); // Se asegura de cerrar el socket y vuelve a hacer accept()
    }
}
    }
}

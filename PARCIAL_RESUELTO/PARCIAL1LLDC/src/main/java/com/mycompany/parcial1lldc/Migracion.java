/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parcial1lldc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author USUARIO
 */
public class Migracion {

    public static void main(String[] args) throws IOException {
        int puerto = 5001;
        try {
            ServerSocket servidor = new ServerSocket(puerto);
            System.out.println("MIGRACION ECUHANDO POR EL PUERTO " + puerto);
            while (true) {
                Socket cliente = servidor.accept();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintStream salida = new PrintStream(cliente.getOutputStream());
                String recibido = entrada.readLine();
                System.out.println("MIGRACION RECIBIOP: " + recibido);
                salida.println(procesar(recibido));
                cliente.close();
            }
        } catch (IOException ex) {
            System.out.println("ERRO MIGRA. " + ex.getMessage());
        }

    }

    public static String procesar(String cadena) {

    }
}

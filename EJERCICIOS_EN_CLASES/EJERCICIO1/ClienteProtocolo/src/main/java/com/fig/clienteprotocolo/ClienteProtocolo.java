/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.fig.clienteprotocolo;

/**
 *
 * @author LLDC
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteProtocolo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String ipServidor = "localhost"; //para hacer la prueba con otra compu tenemos que poner la ip del servidor
        int puerto = 5002;

        System.out.println("--- CLIENTE POR PROTOCOLO ---");
        
        while (true) { 
            System.out.println("\nEscriba la operacion (ej. sum-5-3) o 'sal' para terminar:");
            String mensaje = scanner.nextLine();
            
            if (mensaje.equalsIgnoreCase("sal")) {
                break;
            }

            try (Socket socket = new Socket(ipServidor, puerto);
                 PrintStream toServer = new PrintStream(socket.getOutputStream());
                 BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                toServer.println(mensaje);
                
                String respuesta = fromServer.readLine();
                System.out.println("Respuesta del servidor: " + respuesta);

            } catch (IOException e) {
                System.out.println("No se pudo conectar al servidor: " + e.getMessage());
            }
        }
        scanner.close();
    }
}

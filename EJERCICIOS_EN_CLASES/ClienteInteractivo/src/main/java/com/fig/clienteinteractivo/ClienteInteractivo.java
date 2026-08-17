/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.fig.clienteinteractivo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
/**
 *
 * @author LLDC
 */
public class ClienteInteractivo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String ipServidor = "localhost"; //para hacer la prueba con otra compu tenemos que poner la ip del servidor
        int puerto = 5002;

        System.out.println("--- CLIENTE INTERACTIVO ---");

        while (true) { 
            System.out.println("\n¿Desea realizar una nueva operación? (s/n)");
            String opcion = scanner.nextLine();
            if (opcion.equalsIgnoreCase("n")) {
                break;
            }

            try (Socket socket = new Socket(ipServidor, puerto); PrintStream toServer = new PrintStream(socket.getOutputStream()); BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                System.out.print("Ingrese el primer numero: ");
                toServer.println(scanner.nextLine());

                System.out.println("Servidor: " + fromServer.readLine());
                System.out.print("Respuesta (segundo numero): ");
                toServer.println(scanner.nextLine());

                System.out.println("Servidor: " + fromServer.readLine());
                System.out.print("Respuesta (operacion): ");
                toServer.println(scanner.nextLine());

                System.out.println("Resultado Final del Servidor: " + fromServer.readLine());

            } catch (IOException e) {
                System.out.println("No se pudo conectar al servidor: " + e.getMessage());
            }
        }
        scanner.close();
    }

}

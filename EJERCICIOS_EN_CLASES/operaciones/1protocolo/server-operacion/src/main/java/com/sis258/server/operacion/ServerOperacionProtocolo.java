/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sis258.server.operacion;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author Dell
 */
public class ServerOperacionProtocolo {

    public static void main(String[] args) {
        int port = 5002;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Se inicio el servidor con éxito en el puerto " + port);
            
            while (true) {
                Socket client = server.accept(); 
                System.out.println("Cliente se conecto");
                
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintStream toClient = new PrintStream(client.getOutputStream());
                
                String recibido = fromClient.readLine();
                System.out.println("El cliente envio el mensaje:" + recibido);
                
                String respuesta = procesarSolicitud(recibido);
                toClient.println(respuesta);
                
                client.close();
            }
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
    }
    
    public static String procesarSolicitud(String cadena )
    {
        try {
            String[] partes = cadena.split("-");
            String operacion = partes[0].toLowerCase();
            int num1 = Integer.parseInt(partes[1]);
            int num2 = Integer.parseInt(partes[2]);
            
            switch (operacion) {
                case "sum": return String.valueOf(num1 + num2);
                case "res": return String.valueOf(num1 - num2);
                case "mul": return String.valueOf(num1 * num2);
                case "div": return String.valueOf(num1 / num2);
                default: return "Operación no reconocida";
            }
        } catch (Exception e) {
            return "Error de protocolo. Use el formato: operacion,num1,num2";
        }
    }
}

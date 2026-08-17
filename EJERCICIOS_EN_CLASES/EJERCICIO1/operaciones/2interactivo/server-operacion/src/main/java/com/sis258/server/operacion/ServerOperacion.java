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
public class ServerOperacion {
    public static void main(String[] args) {
        int port = 5002;
        
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Se inicio el servidor interactivo con éxito");
            
            while (true) {
                Socket client = server.accept(); 
                System.out.println("Cliente se conecto");
                
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintStream toClient = new PrintStream(client.getOutputStream());
                
                // 1. Recibe el primer numero
                String recibido = fromClient.readLine();
                int numero1 = Integer.parseInt(recibido);
                
                // 2. Pide el segundo numero
                toClient.println("introduzca el segundo numero");
                String recibido2 = fromClient.readLine();
                int numero2 = Integer.parseInt(recibido2);
                
                // 3. Pide la operacion
                toClient.println("1.suma 2.resta 3.multiplicacion 4.division .introduzca la operacion");
                String recibido3 = fromClient.readLine().toLowerCase();
                
                int resultado = 0;
                switch (recibido3) {
                    case "suma": case "1": resultado = numero1 + numero2; break;
                    case "resta": case "2": resultado = numero1 - numero2; break;
                    case "multiplicacion": case "3": resultado = numero1 * numero2; break;
                    case "division": case "4": 
                        if(numero2 != 0) resultado = numero1 / numero2; 
                        break;
                }
                
                // 4. Envia resultado
                toClient.println(String.valueOf(resultado));
                client.close();
            }
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
    }
}

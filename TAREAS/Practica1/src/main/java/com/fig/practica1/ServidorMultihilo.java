/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fig.practica1;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author LLDC
 */
public class ServidorMultihilo {

    public static void main(String[] args) throws IOException {
        int puerto = args.length > 0 ? Integer.parseInt(args[0]) : 5000;
        int hilos = args.length > 1 ? Integer.parseInt(args[1]) : 2;
        ServerSocket servidor = new ServerSocket(puerto);
        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        System.out.println("Servidor multihilo en el puerto " + puerto);
        int contador = 0;
        while (true) {
            Socket cliente = servidor.accept(); 
            contador++;
            System.out.println("Conexion #" + contador + " desde "
                    + cliente.getInetAddress().getHostAddress());
            pool.execute(new Manejador(cliente, contador)); 
        }
    }
    public static final ConcurrentMap<String, Sala> SALAS = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, Manejador> USUARIOS = new ConcurrentHashMap<>();
    public static final AtomicInteger contadorHistorico = new AtomicInteger(0);
}

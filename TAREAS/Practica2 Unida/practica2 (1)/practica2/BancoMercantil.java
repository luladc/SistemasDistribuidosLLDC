/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;

/**
 *
 * @author andradeaaron39
 */
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class BancoMercantil {
    private static final int PUERTO = 5001;
    private static final Map<String, String> cuentas = new HashMap<>();

    public static void main(String[] args) {
        cargarDatos();
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Banco Mercantil TCP iniciado en puerto " + PUERTO + "...");
            while (true) {
                Socket cliente = servidor.accept();
                atenderCliente(cliente);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void cargarDatos() {
        cuentas.put("11021654", "1515-5100"); //[cite: 1]
        cuentas.put("11111111", "2222-3000:3333-4500");
    }

    private static void atenderCliente(Socket cliente) {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true)) {

            // Lee una sola línea con todo el comando (ej: "11021654:buscar")
            String peticion = entrada.readLine();
            if (peticion == null) return;
            
            String[] datos = peticion.split(":");
            String ci = datos[0];
            // Si no envía operación, asume "buscar" por defecto
            String operacion = datos.length > 1 ? datos[1] : "buscar"; 

            System.out.println("Peticion TCP recibida -> CI: " + ci + " | Operación: " + operacion);

            if ("buscar".equalsIgnoreCase(operacion)) {
                String resultado = cuentas.getOrDefault(ci, "");
                salida.println(resultado);
            } else if ("congelar".equalsIgnoreCase(operacion) && datos.length >= 4) {
                String cuenta = datos[2];
                double monto = Double.parseDouble(datos[3]);
                salida.println(congelar(ci, cuenta, monto));
            } else {
                salida.println("Operacion no valida");
            }
        } catch (Exception e) {
            System.out.println("Error atendiendo cliente TCP: " + e.getMessage());
        }
    }

    private static String congelar(String ci, String cuenta, double monto) {
        String datos = cuentas.get(ci);
        if (datos == null) return "Cuenta no encontrada";

        String[] cuentasSeparadas = datos.split(":");
        for (String cuentaSaldo : cuentasSeparadas) {
            String[] partes = cuentaSaldo.split("-");
            if (partes[0].equals(cuenta)) {
                double saldo = Double.parseDouble(partes[1]);
                if (monto > saldo) return "Monto superior al saldo";
                return "Congelamiento realizado en Mercantil. Cuenta: " + cuenta + " Monto: " + monto;
            }
        }
        return "Cuenta no encontrada";
    }
}
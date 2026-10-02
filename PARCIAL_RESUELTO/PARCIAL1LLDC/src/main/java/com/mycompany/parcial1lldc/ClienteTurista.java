/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.parcial1lldc;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author USUARIO
 */
public class ClienteTurista {
    public static void main (String [] args){
    try{
    Registry registro= LocateRegistry.getRegistry("localhost", 1100);
    ITurista turista= (ITurista) registro.lookup("Turista");
    
    Scanner sc= new Scanner (System.in);
    System.out.print("CI:");
    
    } catch (Exception ex){
    System.out.println("Erros en el cliente");
}
}

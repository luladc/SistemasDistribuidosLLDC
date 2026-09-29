/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.parcial1lldc;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author USUARIO
 */
public interface ITurista extends Remote{
    Vouncher comprarTour(int pasaporte, String codigoTour, int personas) throws RemoteException;
    
}

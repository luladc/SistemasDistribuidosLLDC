/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author luisf
 */package comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IJusticia extends Remote {
    RespuestaCuenta ConsultarCuentas(String ci, String nombres, String apellidos) throws RemoteException;
    String Congelar(Cuenta cuenta, Double monto) throws RemoteException;
}

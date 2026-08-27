package comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IJusticia extends Remote {
    RespuestaCuenta ConsultarCuentas(String ci, String nombres, String apellidos) throws RemoteException;
    String Congelar(Cuenta cuenta, Double monto) throws RemoteException;
}
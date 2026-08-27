/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package justicia;
import comun.Banco;
import comun.Cuenta;
import comun.IJusticia;
import comun.RespuestaCuenta;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
/**
 *
 * @author luisf
 */
public class JusticiaImpl extends UnicastRemoteObject implements IJusticia {
    private ClienteBancoMercantilTCP clienteMercantil;
    private ClienteBancoBCPUDP clienteBCP;

    public JusticiaImpl(String ipBancos, int puertoMercantilTCP, int puertoBCPUDP) throws RemoteException {
    super();
    this.clienteMercantil = new ClienteBancoMercantilTCP(ipBancos, puertoMercantilTCP);
    this.clienteBCP = new ClienteBancoBCPUDP(ipBancos, puertoBCPUDP);
}

    @Override
    public RespuestaCuenta ConsultarCuentas(String ci, String nombres, String apellidos) throws RemoteException {
        System.out.println("\n[RMI] Peticion del Juez para CI: " + ci + " (" + nombres + " " + apellidos + ")");
        ArrayList<Cuenta> cuentasEncontradas = new ArrayList<>();
        StringBuilder logs = new StringBuilder();

        // 1. Consultar Banco Mercantil (TCP)
        String respMercantil = clienteMercantil.consultar(ci);
        if (respMercantil.startsWith("ERROR")) {
            logs.append("[Mercantil: ").append(respMercantil).append("] ");
        } else {
            procesarCadenaCuentas(Banco.MERCANTIL, respMercantil, ci, nombres, apellidos, cuentasEncontradas);
        }

        // 2. Consultar Banco BCP (UDP)
        String respBCP = clienteBCP.consultar(ci);
        if (respBCP.startsWith("ERROR")) {
            logs.append("[BCP: ").append(respBCP).append("] ");
        } else {
            procesarCadenaCuentas(Banco.BCP, respBCP, ci, nombres, apellidos, cuentasEncontradas);
        }

        // 3. Empaquetar resultado
        RespuestaCuenta respuesta = new RespuestaCuenta();
        respuesta.setCuentas(cuentasEncontradas);

        if (cuentasEncontradas.isEmpty() && logs.length() > 0) {
            respuesta.setError(true);
            respuesta.setMensaje("Fallo al contactar bancos: " + logs.toString());
        } else if (cuentasEncontradas.isEmpty()) {
            respuesta.setError(false);
            respuesta.setMensaje("No se hallaron cuentas para el CI: " + ci);
        } else {
            respuesta.setError(false);
            respuesta.setMensaje("Operacion exitosa. " + logs.toString());
        }

        return respuesta;
    }

    @Override
    public  String Congelar(Cuenta cuenta, Double monto) throws RemoteException {
        if (cuenta == null || cuenta.getBanco() == null) {
            return "ERROR: Datos de cuenta incompletos.";
        }

        System.out.println("[RMI] Orden de congelar " + monto + " Bs en cuenta " + cuenta.getNrocuenta() + " (" + cuenta.getBanco() + ")");

        if (cuenta.getBanco() == Banco.MERCANTIL) {
            return clienteMercantil.congelar(cuenta.getNrocuenta(), monto);
        } else if (cuenta.getBanco() == Banco.BCP) {
            return clienteBCP.congelar(cuenta.getNrocuenta(), monto);
        }

        return "ERROR: Banco no reconocido.";
    }

    private void procesarCadenaCuentas(Banco banco, String datos, String ci, String nombres, String apellidos, ArrayList<Cuenta> lista) {
        if (datos == null || datos.trim().isEmpty()) {
            return;
        }

        String[] pares = datos.split(":");
        for (String par : pares) {
            String[] sub = par.split("-");
            if (sub.length == 2) {
                try {
                    String nro = sub[0].trim();
                    Double saldo = Double.parseDouble(sub[1].trim());
                    lista.add(new Cuenta(banco, nro, ci, nombres, apellidos, saldo));
                } catch (NumberFormatException e) {
                    System.err.println("Formato numerico invalido en dato: " + par);
                }
            }
        }
    }
}
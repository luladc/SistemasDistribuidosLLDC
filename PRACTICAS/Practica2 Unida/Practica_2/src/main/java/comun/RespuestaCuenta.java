package comun;

import java.io.Serializable;
import java.util.ArrayList;

public class RespuestaCuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean error; 
    private String mensaje; 
    private ArrayList<Cuenta> cuentas; 

    public RespuestaCuenta() {
        this.cuentas = new ArrayList<>();
        this.error = false;
        this.mensaje = "";
    }

    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public ArrayList<Cuenta> getCuentas() { return cuentas; }
    public void setCuentas(ArrayList<Cuenta> cuentas) { this.cuentas = cuentas; }
}
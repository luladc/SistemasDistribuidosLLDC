/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comun;

/**
 *
 * @author LLDC
 */
import java.io.Serializable;

public class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L; // Recomendado para RMI
    
    private Banco banco; // Enumerado (MERCANTIL, BCP)
    private String nrocuenta; //
    private String ci; //
    private String nombres; //[cite: 1]
    private String apellidos; //[cite: 1]
    private Double saldo; //[cite: 1]

    // Constructor
    public Cuenta(Banco banco, String nrocuenta, String ci, String nombres, String apellidos, Double saldo) {
        this.banco = banco;
        this.nrocuenta = nrocuenta;
        this.ci = ci;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.saldo = saldo;
    }

    // Aquí deben generar los Getters y Setters para cada atributo
    public Banco getBanco() { return banco; }
    public void setBanco(Banco banco) { this.banco = banco; }

    public String getNrocuenta() {
        return nrocuenta;
    }

    public String getCi() {
        return ci;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setNrocuenta(String nrocuenta) {
        this.nrocuenta = nrocuenta;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    
}

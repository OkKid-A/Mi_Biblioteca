package com.cunoc.mi_biblioteca.Usuarios.Cliente;

import com.cunoc.mi_biblioteca.Usuarios.Tipo;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;

public class Cliente extends Usuario {

    private double saldo;
    private boolean suspendido;
    private boolean subscrito;
    private boolean valido;
    private int prestAct;
    private int incidencias;
    public Cliente(String username, String nombre, Tipo tipo, String correo, int id) {
        super(username, nombre, tipo, correo, id);
        this.saldo = 0;
        this.subscrito = false;
        this.subscrito = false;
    }
    public Cliente(String username, String nombre, Tipo tipo, String correo, int id, int saldo, boolean subscrito, boolean suspendido) {
        super(username, nombre, tipo, correo, id);
        this.saldo = saldo;
        this.subscrito= subscrito;
        this.suspendido = suspendido;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean isSuspendido() {
        return suspendido;
    }

    public void setSuspendido(boolean suspendido) {
        this.suspendido = suspendido;
    }

    public String getSubscribed(){
        String subs;
        if (this.subscrito){
            subs = "Premium";
        } else {
            subs = "No subscrito";
        }
        return subs;
    }

    public String getSuspendido() {
        String susp;
        if (this.suspendido){
            susp = "Activo";
        } else {
            susp = "Suspendido";
        }
        return susp;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public boolean isSubscrito() {
        return subscrito;
    }

    public void setSubscrito(boolean subscrito) {
        this.subscrito = subscrito;
    }

    public int getPrestAct() {
        return prestAct;
    }

    public void setPrestAct(int prestAct) {
        this.prestAct = prestAct;
    }

    public int getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(int incidencias) {
        this.incidencias = incidencias;
    }
}

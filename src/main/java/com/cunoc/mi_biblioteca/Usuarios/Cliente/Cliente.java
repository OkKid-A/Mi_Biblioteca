package com.cunoc.mi_biblioteca.Usuarios.Cliente;

import com.cunoc.mi_biblioteca.Usuarios.Tipo;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;

public class Cliente extends Usuario {

    private double saldo;
    private boolean suspendido;
    private boolean subscrito;
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

    public String getSuspendido() {
        String susp;
        if (this.suspendido){
            susp = "Activo";
        } else {
            susp = "Suspendido";
        }
        return susp;
    }

    public boolean isSubscrito() {
        return subscrito;
    }

    public void setSubscrito(boolean subscrito) {
        this.subscrito = subscrito;
    }
}
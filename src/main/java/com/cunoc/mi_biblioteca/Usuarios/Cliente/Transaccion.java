package com.cunoc.mi_biblioteca.Usuarios.Cliente;

import java.util.Date;

public class Transaccion {

    private Date fecha;
    private double valor;
    private String estado;

    public Transaccion(Date fecha, double valor) {
        this.fecha = fecha;
        this.valor = valor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getEstado() {
        if (this.valor>0){
            return "Ingreso";
        } else if (this.valor<0){
            return "Debito";
        }
        return null;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

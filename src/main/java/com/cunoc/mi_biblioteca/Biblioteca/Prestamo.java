package com.cunoc.mi_biblioteca.Biblioteca;

import com.cunoc.mi_biblioteca.Envios.TipoEntrega;

import java.sql.Date;

public class Prestamo {

    private int id_prestamo;
    private int cliente_id;
    private int isbn;
    private int biblio_origen;
    private int dias_reservados;
    private Date fecha_creacion;
    private int recepcionista_id;
    private TipoEntrega tipo_entrega;
    private int transportista_id;
    private EstadoPrestamo estado;

    public Prestamo(int id_prestamo, int cliente_id, int isbn, int biblio_origen, int dias_reservados, Date fecha_creacion,
                    int recepcionista_id, TipoEntrega tipo_entrega, EstadoPrestamo estado) {
        this.id_prestamo = id_prestamo;
        this.cliente_id = cliente_id;
        this.isbn = isbn;
        this.biblio_origen = biblio_origen;
        this.dias_reservados = dias_reservados;
        this.fecha_creacion = fecha_creacion;
        this.recepcionista_id = recepcionista_id;
        this.tipo_entrega = tipo_entrega;
        this.estado = estado;
    }

    public Prestamo(int id_prestamo, int cliente_id, int isbn, int biblio_origen, int dias_reservados, Date fecha_creacion,
                    TipoEntrega tipo_entrega, int transportista_id, EstadoPrestamo estado) {
        this.id_prestamo = id_prestamo;
        this.cliente_id = cliente_id;
        this.isbn = isbn;
        this.biblio_origen = biblio_origen;
        this.dias_reservados = dias_reservados;
        this.fecha_creacion = fecha_creacion;
        this.tipo_entrega = tipo_entrega;
        this.transportista_id = transportista_id;
        this.estado = estado;
    }

    public int getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public int getBiblio_origen() {
        return biblio_origen;
    }

    public void setBiblio_origen(int biblio_origen) {
        this.biblio_origen = biblio_origen;
    }

    public int getDias_reservados() {
        return dias_reservados;
    }

    public void setDias_reservados(int dias_reservados) {
        this.dias_reservados = dias_reservados;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getRecepcionista_id() {
        return recepcionista_id;
    }

    public void setRecepcionista_id(int recepcionista_id) {
        this.recepcionista_id = recepcionista_id;
    }

    public TipoEntrega getTipo_entrega() {
        return tipo_entrega;
    }

    public void setTipo_entrega(TipoEntrega tipo_entrega) {
        this.tipo_entrega = tipo_entrega;
    }

    public int getTransportista_id() {
        return transportista_id;
    }

    public void setTransportista_id(int transportista_id) {
        this.transportista_id = transportista_id;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }
}

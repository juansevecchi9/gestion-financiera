/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionfinanciera;

import java.time.LocalDate;

public abstract class Movimiento {
    // 🔒 Encapsulamiento: atributos privados
    private int id;
    private String descripcion;
    private double monto;
    private LocalDate fecha;

    // 🧱 Constructor
    public Movimiento(int id, String descripcion, double monto, LocalDate fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
    }

    // 🧩 Métodos getter y setter (Encapsulamiento)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    // 🔷 Abstracción: método abstracto que las subclases deberán implementar
    public abstract double calcularMontoFinal();

    // 🔁 Polimorfismo: método que puede sobreescribirse
    public void mostrarInfo() {
        System.out.println("Movimiento ID: " + id);
        System.out.println("Descripcion: " + descripcion);
        System.out.println("Monto: $" + monto);
        System.out.println("Fecha: " + fecha);
    }
}

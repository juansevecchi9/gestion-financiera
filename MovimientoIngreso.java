package com.gestionfinanciera;

import java.time.LocalDate;

public class MovimientoIngreso extends Movimiento {

    private double bonificacion; // por ejemplo, un bono adicional o premio

    // Constructor que llama al de la clase padre (super)
    public MovimientoIngreso(int id, String descripcion, double monto, LocalDate fecha, double bonificacion) {
        super(id, descripcion, monto, fecha);
        this.bonificacion = bonificacion;
    }

    public double getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(double bonificacion) {
        this.bonificacion = bonificacion;
    }

    // 🔁 Polimorfismo: redefinimos el método abstracto
    @Override
    public double calcularMontoFinal() {
        return getMonto() + bonificacion;
    }

    // 🔁 Polimorfismo: sobreescribimos el método mostrarInfo()
    @Override
    public void mostrarInfo() {
        System.out.println("=== Movimiento de Ingreso ===");
        super.mostrarInfo(); // llama al método de la clase padre
        System.out.println("Bonificación: $" + bonificacion);
        System.out.println("Monto final: $" + calcularMontoFinal());
        System.out.println("-----------------------------");
    }
}

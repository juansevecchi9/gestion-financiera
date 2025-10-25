package com.gestionfinanciera;

import java.time.LocalDate;

public class MovimientoEgreso extends Movimiento {

    private double impuesto; // Por ejemplo, retención o impuesto aplicado al egreso

    // Constructor que llama al de la clase padre
    public MovimientoEgreso(int id, String descripcion, double monto, LocalDate fecha, double impuesto) {
        super(id, descripcion, monto, fecha);
        this.impuesto = impuesto;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    // 🔁 Polimorfismo: redefinimos el método abstracto
    @Override
    public double calcularMontoFinal() {
        return getMonto() + impuesto; // el monto final incluye el impuesto adicional
    }

    // 🔁 Polimorfismo: sobreescribimos mostrarInfo()
    @Override
    public void mostrarInfo() {
        System.out.println("=== Movimiento de Egreso ===");
        super.mostrarInfo();
        System.out.println("Impuesto aplicado: $" + impuesto);
        System.out.println("Monto final con impuestos: $" + calcularMontoFinal());
        System.out.println("----------------------------");
    }
}

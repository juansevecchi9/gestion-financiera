package com.gestionfinanciera;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorFinanciero {

    private List<Movimiento> movimientos;

    public GestorFinanciero() {
        movimientos = new ArrayList<>();
    }

    // Agregar movimiento (Ingreso o Egreso)
    public void agregarMovimiento(Movimiento movimiento) {
        movimientos.add(movimiento);
        System.out.println("✅ Movimiento agregado correctamente.");
    }

    // Listar todos los movimientos
    public void listarMovimientos() {
        if (movimientos.isEmpty()) {
            System.out.println("⚠️ No hay movimientos registrados.");
            return;
        }

        System.out.println("\n=== Listado de Movimientos ===");
        for (Movimiento mov : movimientos) {
            mov.mostrarInfo();
        }
    }

    // Calcular saldo total (suma de ingresos - egresos)
    public double calcularSaldoTotal() {
        double saldo = 0.0;

        for (Movimiento mov : movimientos) {
            if (mov instanceof MovimientoIngreso) {
                saldo += mov.calcularMontoFinal();
            } else if (mov instanceof MovimientoEgreso) {
                saldo -= mov.calcularMontoFinal();
            }
        }

        return saldo;
    }

    // Mostrar resumen general
    public void mostrarResumen() {
        System.out.println("\n=== Resumen Financiero ===");
        System.out.println("Cantidad total de movimientos: " + movimientos.size());
        System.out.println("Saldo actual: $" + calcularSaldoTotal());
    }

    // Crear algunos datos de ejemplo para pruebas
    public void inicializarDatosDemo() {
        agregarMovimiento(new MovimientoIngreso(1, "Venta equipo", 1200.00, LocalDate.now(), 100));
        agregarMovimiento(new MovimientoEgreso(2, "Compra de insumos", 800.00, LocalDate.now(), 80));
        agregarMovimiento(new MovimientoIngreso(3, "Servicio técnico", 500.00, LocalDate.now(), 50));
    }
}

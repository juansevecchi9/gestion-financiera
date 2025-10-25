package com.gestionfinanciera;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class MenuPrincipal {

    public static void main(String[] args) throws Exception {

        ConexionBD.getConexion(); // ✅ abre conexión al iniciar

        // Forzar UTF-8 y formato local español
        System.setProperty("file.encoding", "UTF-8");
        Locale.setDefault(new Locale("es", "ES"));
        Scanner sc = new Scanner(System.in, "UTF-8");

        GestorFinanciero gestor = new GestorFinanciero();
        boolean salir = false;

        // Datos iniciales
        gestor.inicializarDatosDemo();

        while (!salir) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Registrar Ingreso");
            System.out.println("2. Registrar Egreso");
            System.out.println("3. Listar Movimientos");
            System.out.println("4. Mostrar Resumen Financiero");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                int opcion = sc.nextInt();
                sc.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1 -> registrarMovimiento(sc, gestor, true);
                    case 2 -> registrarMovimiento(sc, gestor, false);
                    case 3 -> gestor.listarMovimientos();
                    case 4 -> {
                        gestor.mostrarResumen();
                        MovimientoDAO.mostrarResumenFinanciero(); // ✅ muestra resumen también desde la base
                    }
                    case 0 -> {
                        salir = true;
                        System.out.println("👋 Saliendo del sistema. ¡Hasta luego!");
                    }
                    default -> System.out.println("⚠️ Opción no valida. Intente nuevamente.");
                }

            } catch (InputMismatchException e) {
                System.out.println("❌ Error: debe ingresar un número valido.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("❌ Error inesperado: " + e.getMessage());
            }
        }

        sc.close();
        ConexionBD.cerrarConexion(); // ✅ cerrar conexión antes de terminar
    }

    private static void registrarMovimiento(Scanner sc, GestorFinanciero gestor, boolean esIngreso) {
        System.out.print("Descripcion: ");
        String desc = sc.nextLine();

        System.out.print("Monto: ");
        double monto = sc.nextDouble();
        sc.nextLine(); // limpiar buffer

        System.out.print("Moneda (ARS / USD / EUR): ");
        String moneda = sc.nextLine().toUpperCase();

        double ajusteCambio = 1.0; // Simulación: luego tomaremos tipo de cambio real de una API

        // Simular ajuste por tipo de cambio (a futuro se reemplazará con conexión a fuente externa)
        if (moneda.equals("USD")) ajusteCambio = 1000; // ejemplo fijo
        if (moneda.equals("EUR")) ajusteCambio = 1100;

        double montoFinal = monto * ajusteCambio;

        if (esIngreso) {
            System.out.print("Bonificación: ");
            double bonificacion = sc.nextDouble();
            MovimientoIngreso ingreso = new MovimientoIngreso(
                    (int) (Math.random() * 1000),
                    desc, montoFinal, LocalDate.now(), bonificacion
            );
            gestor.agregarMovimiento(ingreso);

            // ✅ Guardar también en la base de datos
            MovimientoDAO.insertarMovimiento(ingreso);

        } else {
            System.out.print("Impuesto: ");
            double impuesto = sc.nextDouble();
            MovimientoEgreso egreso = new MovimientoEgreso(
                    (int) (Math.random() * 1000),
                    desc, montoFinal, LocalDate.now(), impuesto
            );
            gestor.agregarMovimiento(egreso);

            // ✅ Guardar también en la base de datos
            MovimientoDAO.insertarMovimiento(egreso);
        }

        System.out.println("✅ Movimiento registrado en " + moneda + " (convertido automaticamente a valor base).");
    }
}

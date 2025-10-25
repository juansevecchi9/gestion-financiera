package com.gestionfinanciera;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO que se adapta dinámicamente a las clases de movimiento sin requerir cambios
 * en su estructura interna. Usa reflexión para acceder a los atributos reales.
 */
public class MovimientoDAO {

    // ✅ Inserta un movimiento en la base de datos (Ingreso o Egreso)
    public static void insertarMovimiento(Movimiento movimiento) {
        String tipo = (movimiento instanceof MovimientoIngreso) ? "ingreso" : "egreso";

        String sql = "INSERT INTO MOVIMIENTO (tipo, monto, cod_moneda, fecha, descripcion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // ⚙️ Obtención flexible de atributos según cómo estén definidos
            Field montoField = obtenerCampo(movimiento, "monto", "importe", "valor");
            Field monedaField = obtenerCampo(movimiento, "moneda", "cod_moneda", "tipoMoneda");
            Field fechaField = obtenerCampo(movimiento, "fecha", "fechaMovimiento", "fechaOperacion");
            Field descripcionField = obtenerCampo(movimiento, "descripcion", "detalle", "desc");

            double monto = (double) montoField.get(movimiento);
            String moneda = (String) monedaField.get(movimiento);
            java.time.LocalDate fecha = (java.time.LocalDate) fechaField.get(movimiento);
            String descripcion = (String) descripcionField.get(movimiento);

            // ✅ Cargar parámetros SQL
            ps.setString(1, tipo);
            ps.setDouble(2, monto);
            ps.setString(3, moneda);
            ps.setDate(4, java.sql.Date.valueOf(fecha));
            ps.setString(5, descripcion);

            ps.executeUpdate();
            System.out.println("✅ Movimiento registrado correctamente en la base de datos.");

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar movimiento: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️ Error al acceder a los atributos del movimiento: " + e.getMessage());
        }
    }

    // ✅ Muestra el resumen financiero actual desde la base de datos
    public static void mostrarResumenFinanciero() {
        String sql = """
                SELECT cod_moneda,
                       SUM(monto * IF(tipo = 'ingreso', 1, -1)) AS flujo_total
                FROM MOVIMIENTO
                GROUP BY cod_moneda;
                """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n💰 Resumen financiero actual:");
            while (rs.next()) {
                String moneda = rs.getString("cod_moneda");
                double total = rs.getDouble("flujo_total");
                System.out.println(moneda + " → " + total);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al consultar resumen: " + e.getMessage());
        }
    }

    // 🔍 Método auxiliar: busca un campo por varios posibles nombres en clase o superclase
    private static Field obtenerCampo(Object obj, String... posiblesNombres) throws NoSuchFieldException {
        Class<?> clase = obj.getClass();
        while (clase != null) {
            for (String nombre : posiblesNombres) {
                try {
                    Field campo = clase.getDeclaredField(nombre);
                    campo.setAccessible(true);
                    return campo;
                } catch (NoSuchFieldException ignored) {
                    // Sigue buscando en otros nombres o superclases
                }
            }
            clase = clase.getSuperclass();
        }
        throw new NoSuchFieldException(posiblesNombres[0]);
    }
}

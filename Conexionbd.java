import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Conexionbd {

    public static void main(String[] args) {
       
        System.out.println("🟡 Iniciando prueba de conexión...");

        // Datos de conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/gestion_financiera";
        String user = "root";          // tu usuario de MySQL
        String password = "952000";          // tu contraseña (vacía si usás XAMPP)

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Conexión exitosa a la base de datos.");

            // Consulta SQL para validar el flujo de caja total por moneda
            String sql = """
                SELECT cod_moneda,
                       SUM(monto * IF(tipo = 'ingreso', 1, -1)) AS flujo_total
                FROM MOVIMIENTO
                GROUP BY cod_moneda;
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("💰 Flujo total por moneda:");
            while (rs.next()) {
                String moneda = rs.getString("cod_moneda");
                double flujo = rs.getDouble("flujo_total");
                System.out.println(moneda + " → " + flujo);
            }

        } catch (Exception e) {
            System.out.println("❌ Error en la conexión o en la ejecución: " + e.getMessage());
        }
    }
}

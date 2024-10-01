package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MiServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {  
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Configuración de la conexión
        String url = "jdbc:sqlserver://LAPTOP-JJJSBNLM;encrypt=true;database=PruebaDB;integratedSecurity=true;trustServerCertificate=true";

        // Obtener los parámetros del formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String comentarios = request.getParameter("comentarios");

        try {
            // Cargar el driver de SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Intentar establecer la conexión
            try (Connection connection = DriverManager.getConnection(url)) {
                // Preparar la sentencia SQL para insertar los datos
                String sql = "INSERT INTO Formulario (Nombre, Apellido, Email, Telefono, Comentarios) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, nombre);
                    statement.setString(2, apellido);
                    statement.setString(3, email);
                    statement.setString(4, telefono);
                    statement.setString(5, comentarios);

                    // Ejecutar la inserción
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        out.println("<html><body>");
                        out.println("<h1>Registro exitoso</h1>");
                        out.println("<p>Se han guardado los datos correctamente.</p>");
                        out.println("</body></html>");
                    }
                }
            }
        } catch (Exception e) {
            out.println("<html><body>");
            out.println("<h1>Error</h1>");
            out.println("<p>No se pudo guardar los datos.</p>");
            out.println("<p>Detalles: " + e.getMessage() + "</p>");
            out.println("</body></html>");
            e.printStackTrace(); // Para depuración
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para procesar el formulario y guardar datos en la base de datos.";
    }
}
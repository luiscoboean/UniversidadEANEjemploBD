
package universidad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.naming.spi.DirStateFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author luisc
 */
public class Universidad {

    // Operaciones con la BD
    private static Connection con = null;
    
    public static void conectarBD() {
        try {
            con = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\luisc\\Documents\\Universidad.accdb");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Imposible conectarse a la BD");
            e.printStackTrace();
        }
    }
    
    public static boolean insertarEstudianteEnLaBD(String nombre, String carrera, int sem, double prom) {
        try {
            String sql = "INSERT INTO Estudiante VALUES(0, ?, ?, ?, ?)";
            PreparedStatement st = con.prepareStatement(sql);
            
            st.setString(1, nombre);
            st.setString(2, carrera);
            st.setInt(3, sem);
            st.setDouble(4, prom);
            
            st.executeUpdate();
            return true;
            
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los estudiantes");
            e.printStackTrace();
        }
        return false;
    }    
    
    public static int obtenerIDDelUltimoEstudiante() {
        try {
            String sql = "SELECT MAX(ID)  FROM Estudiante";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                int ultimoID = rs.getInt(1); // Obtenemos la unica respuesta
                return ultimoID;
            }            
            
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error grave: " + e.getMessage());
        }
        return -1;
    }
    
    public static void mostrarInformacionDeUnEstudiante(String nombre) {
        try {
            String sql = "SELECT * FROM Estudiante WHERE NombreCompleto LIKE ?";
            PreparedStatement st = con.prepareStatement(sql);
            
            st.setString(1, "%" + nombre + "%");
            
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Información del estudiante " + nombre + 
                        "\nID = " + rs.getInt("ID") + "\n" +
                        "Nombres: " + rs.getString("NombreCompleto") + "\n" + 
                        "Carrera: " + rs.getString("Carrera") + "\n" +
                        "Semestre: " + rs.getInt("Semestre") + "\n" + 
                        "Promedio: " + rs.getDouble("Promedio"));
            }
            else {
                JOptionPane.showMessageDialog(null, "El estudiante con nombre " + nombre +
                        " no existe en la BD");
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error grave: " + e.getMessage());
        }
    }
    
    public static ArrayList<String[]> obtenerTodosLosEstudiantes() {
        try {
            Statement st = con.createStatement();
            String sql = "SELECT * FROM ESTUDIANTE ORDER BY NombreCompleto";
            
            ResultSet rs = st.executeQuery(sql);
            
            ArrayList<String[]> listaEstuds = new ArrayList<>();
            
            while (rs.next()) {
                String[] e = new String[5];
                e[0] = rs.getString(1);
                e[1] = rs.getString(2);
                e[2] = rs.getString(3);
                e[3] = rs.getString(4);
                e[4] = rs.getString(5);
                
                listaEstuds.add(e);
            }
            
            rs.close();
            return listaEstuds;
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return null;
    }
}

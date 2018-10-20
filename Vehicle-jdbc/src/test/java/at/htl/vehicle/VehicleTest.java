package at.htl.vehicle;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class VehicleTest {
    public static final String DRIVER_STRING = "org.apache.derby.jdbc.ClientDriver";
    public static final String CONNECTION_STRING = "jdbc:derby://localhost:1527/db";
    public static final String USER = "app";
    public static final String PASSWORD = "app";
    private static Connection conn;

    @BeforeClass
    public static void initJdbc(){
        //verbindung zu db herstellen
        try {
            Class.forName(DRIVER_STRING);
            conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Verbindung zur Datenbank nicht möglich\n" + e.getMessage() + "\n");
            System.exit(1);
        }

        //erstellen der tabelle vehicle
        try {
            Statement stmt = conn.createStatement();
            /*String sql = "CREATE TABLE vehicle ("
                    + "id INT CONSTRAINT vehicle_pk PRIMARY KEY"
                    + " GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "brand VARCHAR(255) NOT NULL,"
                    + "type VARCHAR(255) NOT NULL"
                    + ")";*/
            String sql = "CREATE TABLE vehicle ("
                    + "id INT CONSTRAINT vehicle_pk PRIMARY KEY,"
                    + "brand VARCHAR(255) NOT NULL,"
                    + "type VARCHAR(255) NOT NULL"
                    + ")";
            stmt.execute(sql);
        } catch (SQLException e) {
            // e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    @AfterClass
    public static void teardownJdbc() {
        // tabelle vehicle löschen
        try {
            conn.createStatement().execute("DROP TABLE vehicle");
            System.out.println("Tabelle vehicle gelöscht");
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Tabelle vehicle konnte nicht gelöscht werden:\n" + e.getMessage());
        }

        // connection schließen
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Good bye");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dml(){
        //daten einfügen
        int countInserts = 0;
        try {
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vehicle (id, brand, type) VALUES (1, 'Opel', 'Commodore')";
            countInserts += stat.executeUpdate(sql);
            sql = "INSERT INTO vehicle (id, brand, type) VALUES (2, 'Opel', 'Kapitän')";
            countInserts += stat.executeUpdate(sql);
            sql = "INSERT INTO vehicle (id, brand, type) VALUES (3, 'Opel', 'Kadett')";
            countInserts += stat.executeUpdate(sql);
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
        assertThat(countInserts, is(3));
    }

}

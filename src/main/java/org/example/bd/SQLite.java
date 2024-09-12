package org.example.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite {

    public Connection connect() throws SQLException {
        String url = "jdbc:sqlite:test.db";
        return DriverManager.getConnection(url);
    }

    public void createNewTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS services (\n"
                + " id integer PRIMARY KEY,\n"
                + " serviceName text NOT NULL,\n"
                + " serviceBalance text NOT NULL\n"
                + ");";

        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
        conn.close();
    }

    public void insert(String serviceId, String serviceName, String serviceBalance) throws SQLException {
        String sql = "INSERT INTO services(id, serviceName, serviceBalance) VALUES(?,?,?)";
        Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, serviceId);
        pstmt.setString(2, serviceName);
        pstmt.setString(3, serviceBalance);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    public void selectAll() throws SQLException {
        String sql = "SELECT id, serviceName, serviceBalance FROM services";
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("id");
            String serviceName = rs.getString("serviceName");
            String serviceBalance = rs.getString("serviceBalance");

            System.out.println("ID: " + id);
            System.out.println("Service Name: " + serviceName);
            System.out.println("Service Balance: " + serviceBalance);
            System.out.println();
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    public static void main(String[] args) throws SQLException {
        SQLite db = new SQLite();

        // db.createNewTable();

        // db.insert("1", "Visa", "$5500");
        // db.insert("2", "Mastercard", "$3350");
        // db.insert("3", "American Express", "$300");
        // db.insert("4", "PayPal", "$1670");
        // db.insert("5", "Discover", "$2100");

        db.selectAll();
    }
}
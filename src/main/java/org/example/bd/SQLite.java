package org.example.bd;

import org.example.data.data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite {

    public Connection connect() throws SQLException {
        String url = data.bd;
        return DriverManager.getConnection(url);
    }

    public void createNewTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS services (\n"
                + " id integer PRIMARY KEY,\n"
                + " serviceName text NOT NULL,\n"
                + " serviceBalance text NOT NULL,\n"
                + " status text\n"
                + ");";

        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
        conn.close();
    }

    public void insert(String serviceId, String serviceName, String serviceBalance, String status) throws SQLException {
        String sql = "INSERT OR IGNORE INTO services(id, serviceName, serviceBalance, status) VALUES(?,?,?,?)";
        Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, serviceId);
        pstmt.setString(2, serviceName);
        pstmt.setString(3, serviceBalance);
        pstmt.setString(4, status);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    public void updateServiceStatus(String serviceId, String status) throws SQLException {
        String sql = "UPDATE services SET status = ? WHERE id = ?";
        Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, status);
        pstmt.setString(2, serviceId);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    public void selectAll() throws SQLException {
        String sql = "SELECT id, serviceName, serviceBalance, status FROM services";
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("id");
            String serviceName = rs.getString("serviceName");
            String serviceBalance = rs.getString("serviceBalance");
            String status = rs.getString("status");

            System.out.println("ID: " + id);
            System.out.println("Service Name: " + serviceName);
            System.out.println("Service Balance: " + serviceBalance);
            System.out.println("Status: " + status);
            System.out.println();
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    public void selectDataFromTable(String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("id");
            String serviceName = rs.getString("serviceName");
            String serviceBalance = rs.getString("serviceBalance");
            String status = rs.getString("status");

            System.out.println("ID: " + id);
            System.out.println("Service Name: " + serviceName);
            System.out.println("Service Balance: " + serviceBalance);
            System.out.println("Status: " + status);
            System.out.println();
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM services";
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        conn.close();
    }

    public void dropTable() throws SQLException {
        String sql = "DROP TABLE IF EXISTS services";
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        conn.close();
    }

    public ServiceData getServiceDataById(int id) throws SQLException {
        String sql = "SELECT serviceName, serviceBalance, status FROM services WHERE id = ?";
        Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        ServiceData serviceData = null;
        if (rs.next()) {
            String serviceName = rs.getString("serviceName");
            String serviceBalance = rs.getString("serviceBalance");
            String serviceStatus = rs.getString("status");
            serviceData = new ServiceData(serviceName, serviceBalance, serviceStatus);
        }

        rs.close();
        pstmt.close();
        conn.close();
        return serviceData;
    }

    public static class ServiceData {
        public final String serviceName;
        public final String serviceBalance;
        public final String serviceStatus;

        public ServiceData(String serviceName, String serviceBalance, String serviceStatus) {
            this.serviceName = serviceName;
            this.serviceBalance = serviceBalance;
            this.serviceStatus = serviceStatus;
        }
    }

    public static void main(String[] args) throws SQLException {
        SQLite db = new SQLite();

        // db.dropTable();
        // db.createNewTable();
        db.selectAll();
    }
}
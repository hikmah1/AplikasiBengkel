/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplikasibengkel;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author LENOVO
 */
public class koneksi {
    public Connection bukaKoneksi(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/bengkel","root","");
            System.out.println("Koneksi Berhasil");
            return conn;
        } catch (Exception e) {
             System.out.println("Koneksi Gagal");
            return conn=null;
        }
    }
    
  
}

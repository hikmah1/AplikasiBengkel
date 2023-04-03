/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplikasibengkel;

import java.awt.Color;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author LENOVO
 */
public class TransaksiBengkel extends javax.swing.JDialog {

    /**
     * Creates new form TransaksiBengkel
     */
    koneksi obj;
    String kd_sp = "";
    public TransaksiBengkel(java.awt.Frame parent, boolean modal) {
        super(parent,modal);
        this.getContentPane().setBackground(Color.decode("#48D1CC"));
        initComponents();
           setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-getWidth())/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height-getHeight())/2);
        obj = new koneksi();
        kd_sp="";
        autoNumber();
        tampilNamaSpareparts();
        inisialisasi();
    }
    private void tambahData(){
        int discount = 0;
        int jml_bayar = 0;
        
        if (rb5.isSelected()) {
            discount = 5;
        }else if(rb10.isSelected()){
            discount = 10;
        }else if(rb15.isSelected()){
            discount = 15;
        }
        
        jml_bayar = Integer.parseInt(tharga.getText())*Integer.parseInt(tJumlah.getText());
        jml_bayar = jml_bayar - (jml_bayar*discount/100);
       
        
        try {
            Connection con = obj.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "insert into isi values('"+tNoservis.getText()+"',"
                    + "'"+kd_sp+"',"
                    + "'"+tJumlah.getText()+"',"
                    + "'"+discount+"',"
                    + "'"+jml_bayar+"')";
            int sukses = st.executeUpdate(sql);
                if (sukses > 0 ) {
                   JOptionPane.showMessageDialog(rootPane, "Data Berhasil di Tambahkan!!");
            } else {
                   JOptionPane.showMessageDialog(rootPane, "Data Tidak Berhasil di Tambahkan!!"); 
            }
                con.close();
            st.close();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this, e);
        }
        inisialisasi();
    }
    
    private void autoNumber(){
        String noServices = "SER000";
        int i = 0;
        try {
            Connection con  = obj.bukaKoneksi();
            Statement  st  = con.createStatement();
            String sql = "select no_services from isi";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                noServices = rs.getString("no_services");
            }
            noServices = noServices.substring(3);
            i = Integer.parseInt(noServices)+1;
            noServices = "00"+i;
            noServices = "SER"+noServices.substring(noServices.length()-3);
            tNoservis.setText(noServices);
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(this, e);
        }
    }
    
    private void tampilNamaSpareparts(){
        try {
            Connection con = obj.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "select nm_sp from spareparts order by nm_sp";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                cmbSparepart.addItem(rs.getString("nm_sp"));
            }
            con.close();
            st.close(); 
        } catch (SQLException e) {
             JOptionPane.showConfirmDialog(this, e);
        }
    }
    
    private void detailSpareparts(){
        
        try {
            Connection con = obj.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "select * from spareparts where nm_sp='"+cmbSparepart.getSelectedItem()+"'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                tharga.setText(rs.getString("harga"));
                kd_sp = rs.getString("kd_sp");
            }
            con.close();
            st.close();
        } catch (SQLException e) {
             JOptionPane.showConfirmDialog(this, e);
        }
    }
    
    private void inisialisasi(){
        btnTambah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnUbah.setEnabled(false);
    }
    private void cariData(){
        try {
            Connection con = obj.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "select * from isi where no_services='"+tNoservis.getText()+"' ";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                tJumlah.setText(rs.getString("jml_item"));
                if (rs.getString("discount").equals("5")) {
                    rb5.setSelected(true);
                }
                if (rs.getString("discount").equals("10")) {
                    rb10.setSelected(true);
                }
                if (rs.getString("discount").equals("15")) {
                    rb15.setSelected(true);
                }
                inisialisasi();
                btnUbah.setEnabled(true);
                btnHapus.setEnabled(true);
            }else{
                inisialisasi();
                btnTambah.setEnabled(true);
                tJumlah.setText("");
                rb5.setSelected(true);
            }
            con.close();
            st.close();
            
        } catch (SQLException e) {
             JOptionPane.showConfirmDialog(this, e);
        }
    }
    
    private void ubahData(){
       int discount = 0;
        int jml_bayar = 0;
        
        if (rb5.isSelected()) {
            discount = 5;
        }else if(rb10.isSelected()){
            discount = 10;
        }else if(rb15.isSelected()){
            discount = 15;
        }
        
        jml_bayar = Integer.parseInt(tharga.getText())*Integer.parseInt(tJumlah.getText());
        jml_bayar = jml_bayar - (jml_bayar*discount/100);
        
        try {
            Connection con = obj.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "update isi set no_services='"+tNoservis.getText()+"',"
                    + "kd_sp='"+kd_sp+"',"
                    + "jml_item='"+tJumlah.getText()+"',"
                    + "discount='"+discount+"',"
                    + "jml_bayar='"+jml_bayar+"'"
                    + "where no_services='"+tNoservis.getText()+"'";
            int sukses = st.executeUpdate(sql);
                if (sukses > 0 ) {
                   JOptionPane.showMessageDialog(rootPane, "Data Berhasil di Ubah!!");
            } else {
                   JOptionPane.showMessageDialog(rootPane, "Data Tidak Berhasil di Ubah!!"); 
            }
            con.close();
            st.close();
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(this, e);
        }
    }
    private void bersih(){
        tJumlah.setText("");
        rb5.setSelected(true);
    }
    
    private void hapus(){
           try {
            Connection con = obj.bukaKoneksi();
            Statement st = con.createStatement();
            String sql = "delete from isi where no_services='"+tNoservis.getText()+"'"
                    + "and kd_sp='"+kd_sp+"'";
            int sukses = st.executeUpdate(sql);
               if (sukses >0 ) {
                   JOptionPane.showMessageDialog(rootPane, "Data Berhasil di Hapus");
               } else {
                   JOptionPane.showMessageDialog(rootPane, "Data Tidak Berhasil di Hapus");
               }
            con.close();
            st.close();
        } catch (SQLException e) {
             JOptionPane.showConfirmDialog(this, e);
        }
    }
       
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        tNoservis = new javax.swing.JTextField();
        btnBaru = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cmbSparepart = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        tharga = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tJumlah = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rb5 = new javax.swing.JRadioButton();
        rb10 = new javax.swing.JRadioButton();
        rb15 = new javax.swing.JRadioButton();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        btnCari = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("No. Services");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(30, 160, 90, 14);

        tNoservis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tNoservisActionPerformed(evt);
            }
        });
        getContentPane().add(tNoservis);
        tNoservis.setBounds(140, 160, 220, 30);

        btnBaru.setBackground(new java.awt.Color(153, 153, 153));
        btnBaru.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnBaru.setText("Buat Baru");
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });
        getContentPane().add(btnBaru);
        btnBaru.setBounds(530, 160, 102, 23);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Spare Parts");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(30, 240, 110, 15);

        cmbSparepart.setBackground(new java.awt.Color(255, 153, 153));
        cmbSparepart.setEditable(true);
        cmbSparepart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSparepartActionPerformed(evt);
            }
        });
        getContentPane().add(cmbSparepart);
        cmbSparepart.setBounds(130, 240, 420, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Harga");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(30, 280, 110, 15);

        tharga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thargaActionPerformed(evt);
            }
        });
        getContentPane().add(tharga);
        tharga.setBounds(130, 280, 260, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Jumlah");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(30, 320, 110, 20);
        getContentPane().add(tJumlah);
        tJumlah.setBounds(130, 320, 260, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Discount");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(30, 360, 70, 15);

        buttonGroup1.add(rb5);
        rb5.setText("5%");
        rb5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb5ActionPerformed(evt);
            }
        });
        getContentPane().add(rb5);
        rb5.setBounds(130, 360, 43, 23);

        buttonGroup1.add(rb10);
        rb10.setText("10%");
        rb10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb10ActionPerformed(evt);
            }
        });
        getContentPane().add(rb10);
        rb10.setBounds(190, 360, 49, 23);

        buttonGroup1.add(rb15);
        rb15.setText("15%");
        rb15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb15ActionPerformed(evt);
            }
        });
        getContentPane().add(rb15);
        rb15.setBounds(260, 360, 49, 23);

        btnTambah.setBackground(new java.awt.Color(255, 255, 255));
        btnTambah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        getContentPane().add(btnTambah);
        btnTambah.setBounds(60, 400, 80, 23);

        btnUbah.setBackground(new java.awt.Color(255, 255, 255));
        btnUbah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        getContentPane().add(btnUbah);
        btnUbah.setBounds(150, 400, 95, 23);

        btnHapus.setBackground(new java.awt.Color(255, 255, 255));
        btnHapus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        getContentPane().add(btnHapus);
        btnHapus.setBounds(260, 400, 96, 23);

        btnKeluar.setBackground(new java.awt.Color(255, 0, 51));
        btnKeluar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        getContentPane().add(btnKeluar);
        btnKeluar.setBounds(510, 430, 100, 23);

        btnCari.setBackground(new java.awt.Color(153, 153, 153));
        btnCari.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        getContentPane().add(btnCari);
        btnCari.setBounds(370, 160, 140, 23);

        btnPrint.setBackground(new java.awt.Color(0, 102, 255));
        btnPrint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        getContentPane().add(btnPrint);
        btnPrint.setBounds(410, 430, 95, 23);

        jLabel7.setBackground(new java.awt.Color(204, 204, 204));
        jLabel7.setFont(new java.awt.Font("Tekton Pro Ext", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("ASHIAAAP BENGKEL");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(160, 30, 449, 50);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField1);
        jTextField1.setBounds(140, 200, 260, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nama Pelanggan");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(30, 200, 120, 15);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplikasibengkel/MAGE-removebg-preview.png"))); // NOI18N
        jLabel10.setText("jLabel10");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(-80, 0, 230, 140);

        jLabel8.setText("______________________________________________________________________________________________________________");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(0, 130, 670, 14);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Jl.Praktikum Alpro No.8, Kec. Informatika, UIN Malang");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(160, 80, 430, 14);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rb15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb15ActionPerformed

    private void rb5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb5ActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        tambahData();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruActionPerformed
        // TODO add your handling code here:
        autoNumber();
        bersih();
        cariData();
         btnTambah.setEnabled(true);
        
    }//GEN-LAST:event_btnBaruActionPerformed

    private void thargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_thargaActionPerformed

    private void cmbSparepartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSparepartActionPerformed
        // TODO add your handling code here:
        cariData();
        detailSpareparts();
    }//GEN-LAST:event_cmbSparepartActionPerformed

    private void tNoservisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tNoservisActionPerformed
        // TODO add your handling code here:
        cariData();
    }//GEN-LAST:event_tNoservisActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        // TODO add your handling code here:
        ubahData();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        hapus();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        cariData();
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void rb10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb10ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransaksiBengkel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransaksiBengkel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransaksiBengkel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransaksiBengkel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               
                  TransaksiBengkel dialog = new TransaksiBengkel(new java.awt.Frame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaru;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbSparepart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JRadioButton rb10;
    private javax.swing.JRadioButton rb15;
    private javax.swing.JRadioButton rb5;
    private javax.swing.JTextField tJumlah;
    private javax.swing.JTextField tNoservis;
    private javax.swing.JTextField tharga;
    // End of variables declaration//GEN-END:variables
}

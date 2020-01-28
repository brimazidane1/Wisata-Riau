/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.javafx.image.impl.IntArgb;
import com.sun.prism.PresentableState;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TreeMap;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FB
 */
public class frmBook extends javax.swing.JFrame implements data{

    private DefaultTableModel model;

    /**
     * Creates new form FrmJual
     */
    public frmBook() {
        initComponents();
        combo_wisata();
        auto_key();
        txtNofa.disable();
        txtWisata.disable();
        TxtTotal.disable();
        TxtSubTotal.disable();
        TxtKembali.disable();

        //variabel pembantu di tutup
        TxtDateTime.hide();
        

        model = new DefaultTableModel();
        TblDetail.setModel(model);
        model.addColumn("ID Booking");
        model.addColumn("Nama Wisata");
        model.addColumn("Nama Pemesan");
        model.addColumn("Jumlah Tiket");
        model.addColumn("Tanggal");
        model.addColumn("Total");
        
        TblDetail.getColumnModel().getColumn(0).setMinWidth(0);
        TblDetail.getColumnModel().getColumn(0).setMaxWidth(0);
        TblDetail.getColumnModel().getColumn(0).setWidth(0);

        loadData();
        Date date = new Date();
        JdateJual.setDate(date);
    }

    public void Batal() {
        //Proses mengahapus data dari tabel 
        
        try {
            Connection c = koneksi.getConnection();
            
            String sql = "DELETE FROM book WHERE id_booking='"+this.txtNofa.getText()+"' AND tanggal='"+this.TxtDateTime.getText()+"'";
            PreparedStatement p = (PreparedStatement) c.prepareStatement(sql);
            p.executeUpdate();
            p.close();
        } catch (SQLException e) {
            System.out.println("Terjadi Kesalahan");
        } finally {
            loadData();
            JOptionPane.showMessageDialog(this, "Deleted!");
        }
    }

    public void Cari_Kode() {
        int i = TblDetail.getSelectedRow();
        if (i == -1) {
            return;
        }
        String ID = (String) model.getValueAt(i, 2);
        TxtNama.setText(ID);
        String ID1 = (String) model.getValueAt(i, 4);
        TxtDateTime.setText(ID1);
    }

    public void ShowData() {
        try {
            Connection c = koneksi.getConnection();
            String sql = "Select * from book b, list l WHERE b.nama_wisata = l.nama_wisata AND b.nama_pemesan='"+this.TxtNama+"'";
            Statement st = koneksi.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                this.txtNofa.setText(rs.getString("id_booking"));
                this.cmbWisata.setSelectedItem(rs.getString("nama_wisata"));
                this.TxtNama.setText(rs.getString("nama_pemesan"));
                this.TxtQty.setText(rs.getString("jumlah_tiket"));
                this.TxtDateTime.setText(rs.getString("tanggal"));
                this.TxtSubTotal.setText(rs.getString("total"));

            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public final void loadData() {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try {
            Connection c = koneksi.getConnection();
            Statement s = c.createStatement();
            String sql = "Select * from book b, list l WHERE b.nama_wisata=l.nama_wisata AND b.id_booking='" + this.txtNofa.getText() + "'";
            ResultSet r = s.executeQuery(sql);
            while (r.next()) {
                Object[] o = new Object[6];
                o[0] = r.getString("id_booking");
                o[1] = r.getString("nama_wisata");
                o[2] = r.getString("nama_pemesan");
                o[3] = r.getString("jumlah_tiket");
                o[4] = r.getString("tanggal");
                o[5] = r.getString("total");
                model.addRow(o);
            }
            r.close();
            s.close();
            //ShowData();  
        } catch (SQLException e) {
            System.out.println("Terjadi Kesalahan");
        }

        //menjumlahkan isi colom ke 4 dalam tabel
        int total = 0;
        for (int i = 0; i < TblDetail.getRowCount(); i++) {
            int amount = Integer.parseInt((String) TblDetail.getValueAt(i, 5));
            total += amount;
        }
        TxtTotal.setText("" + total);
    }

    public void AutoSum() {
        int a, b, c;
        a = Integer.parseInt(txtWisata.getText());
        b = Integer.parseInt(TxtQty.getText());
        c = a * b;
        TxtSubTotal.setText("" + c);
    }

    public void HitungKembali() {
        int d, e, f;
        d = Integer.parseInt(TxtTotal.getText());
        e = Integer.parseInt(TxtCash.getText());
        f = e - d;
        TxtKembali.setText("" + f);
    }

    public void auto_key() {
        try {
            java.util.Date tgl = new java.util.Date();
            java.text.SimpleDateFormat kal = new java.text.SimpleDateFormat("yyMMdd");
            java.text.SimpleDateFormat tanggal = new java.text.SimpleDateFormat("yyyyMMdd");
            Connection c = koneksi.getConnection();
            String sql = "select max(id_booking) from book WHERE tanggal =" + tanggal.format(tgl);
            Statement st = koneksi.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Long a = rs.getLong(1); //mengambil nilai tertinggi  
                if (a == 0) {
                    this.txtNofa.setText(kal.format(tgl) + "0000" + (a + 1));
                } else {
                    this.txtNofa.setText("" + (a + 1));
                }
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalaahan");
        }
    }

    public void Selesai() {
        String jual_id = this.txtNofa.getText();
        String jual_wisata = this.cmbWisata.getSelectedItem().toString();
        String jual_total = this.TxtTotal.getText();
        String jual_cash = this.TxtCash.getText();
        String jual_kembali = this.TxtKembali.getText();

        //Date date = new Date();
        //JdateJual.setDate(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date tanggal = new Date();
        tanggal = JdateJual.getDate();
        String jual_tgl = dateFormat.format(tanggal);

        try {
            Connection c = koneksi.getConnection();
            String sql = "Insert into data_book (id_booking,tanggal,nama_wisata,total,cash,kembali) values (?,?,?,?,?,?)";
            PreparedStatement p = (PreparedStatement) c.prepareStatement(sql);
            p.setString(1, jual_id);
            p.setString(2, jual_tgl);
            p.setString(3, jual_wisata);
            p.setString(4, jual_total);
            p.setString(5, jual_cash);
            p.setString(6, jual_kembali);
            p.executeUpdate();
            p.close();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            //loadData();
            JOptionPane.showMessageDialog(this, "Pembelian Berhasil!");
        }

        auto_key();
        loadData();
    }

    public void TambahDetail() {
        Date HariSekarang = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String jual_nofa = this.txtNofa.getText();
        String jual_wisata = this.cmbWisata.getSelectedItem().toString();
        String jual_nama = this.TxtNama.getText();
        String jual_qty = this.TxtQty.getText();
        String DateTime = ft.format(HariSekarang);
        String jual_subtotal = this.TxtSubTotal.getText();
        

        try {
            Connection c = koneksi.getConnection();
            String sql = "Insert into book (id_booking,nama_wisata,nama_pemesan,jumlah_tiket,tanggal,total) values (?,?,?,?,?,?)";
            PreparedStatement p = (PreparedStatement) c.prepareStatement(sql);
            p.setString(1, jual_nofa);
            p.setString(2, jual_wisata);
            p.setString(3, jual_nama);
            p.setString(4, jual_qty);
            p.setString(5, DateTime);
            p.setString(6, jual_subtotal);
            p.executeUpdate();
            p.close();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            loadData();
            JOptionPane.showMessageDialog(this,"Data Telah Tersimpan");  
        }
    }

    public void cari_id() {
    try {
            Connection c = koneksi.getConnection();
            String sql_t = "select id_wisata from list where nama_wisata='" + cmbWisata.getSelectedItem() + "'";
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql_t);                              // yang anda ingin kan

            while (rs.next()) {
                this.txtWisata.setText(rs.getString("id_wisata"));
            }
            rs.close();
            st.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void cari_nama() {
        try {
            Connection c = koneksi.getConnection();
            String sql_t = "select harga from list where nama_wisata='" + cmbWisata.getSelectedItem() + "'";
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql_t);                              // yang anda ingin kan

            while (rs.next()) {
                this.txtWisata.setText(rs.getString("harga"));
            }
            rs.close();
            st.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void bersihkan() {
        TxtNama.setText("");
        TxtQty.setText("");
        TxtCash.setText("");
        TxtSubTotal.setText("");
        TxtKembali.setText("");
    }

    public void combo_wisata() {
        try {
            Connection c = koneksi.getConnection();
            Statement st = c.createStatement();
            String sql_tc = "select id_wisata, nama_wisata from list order by id_wisata asc";
            ResultSet rs = st.executeQuery(sql_tc);

            while (rs.next()) {
                String nama = rs.getString("nama_wisata");
                cmbWisata.addItem(nama);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        TxtSubTotal = new javax.swing.JTextField();
        txtNofa = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblDetail = new javax.swing.JTable();
        BtnSimpan = new javax.swing.JButton();
        BtnBatal = new javax.swing.JButton();
        TxtQty = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        TxtNama = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        BtnAdd = new javax.swing.JButton();
        TxtCash = new javax.swing.JTextField();
        cmbWisata = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        TxtTotal = new javax.swing.JTextField();
        JdateJual = new com.toedter.calendar.JDateChooser();
        TxtKembali = new javax.swing.JTextField();
        panelAtas = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        Lnama = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtWisata = new javax.swing.JTextField();
        TxtDateTime = new javax.swing.JTextField();
        btnCheck = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel2.setText("Id Booking");

        TxtSubTotal.setName(""); // NOI18N
        TxtSubTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtSubTotalActionPerformed(evt);
            }
        });
        TxtSubTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtSubTotalKeyPressed(evt);
            }
        });

        txtNofa.setName(""); // NOI18N
        txtNofa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNofaActionPerformed(evt);
            }
        });
        txtNofa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNofaKeyPressed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel13.setText("Kembali");

        TblDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Barang", "Nama  Barang", "Harga", "Qty", "Sub Total", "jual_time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TblDetail.getTableHeader().setReorderingAllowed(false);
        TblDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblDetailMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TblDetail);
        if (TblDetail.getColumnModel().getColumnCount() > 0) {
            TblDetail.getColumnModel().getColumn(0).setMinWidth(0);
            TblDetail.getColumnModel().getColumn(0).setPreferredWidth(0);
            TblDetail.getColumnModel().getColumn(0).setMaxWidth(0);
            TblDetail.getColumnModel().getColumn(1).setResizable(false);
            TblDetail.getColumnModel().getColumn(2).setResizable(false);
            TblDetail.getColumnModel().getColumn(3).setResizable(false);
            TblDetail.getColumnModel().getColumn(4).setResizable(false);
            TblDetail.getColumnModel().getColumn(5).setMinWidth(0);
            TblDetail.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        BtnSimpan.setText("Buy");
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });

        BtnBatal.setText("Cancel");
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });

        TxtQty.setName(""); // NOI18N
        TxtQty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TxtQtyMouseClicked(evt);
            }
        });
        TxtQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtQtyActionPerformed(evt);
            }
        });
        TxtQty.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                TxtQtyPropertyChange(evt);
            }
        });
        TxtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtQtyKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel9.setText("Jumlah");

        TxtNama.setName(""); // NOI18N
        TxtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtNamaActionPerformed(evt);
            }
        });
        TxtNama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtNamaKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel7.setText("Nama Pemesan");

        BtnAdd.setText("Add");
        BtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAddActionPerformed(evt);
            }
        });

        TxtCash.setName(""); // NOI18N
        TxtCash.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtCashKeyPressed(evt);
            }
        });

        cmbWisata.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbWisataMouseClicked(evt);
            }
        });
        cmbWisata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbWisataActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel8.setText("Harga/tiket");

        jLabel6.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel6.setText("Total Pembelian (Rp)");

        jLabel10.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel10.setText("Total");

        jLabel3.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel3.setText("Tanggal");

        jLabel11.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel11.setText("Cash");

        TxtTotal.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        TxtTotal.setName(""); // NOI18N
        TxtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtTotalActionPerformed(evt);
            }
        });
        TxtTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TxtTotalKeyPressed(evt);
            }
        });

        JdateJual.setDateFormatString("yyyy-MM-dd");

        TxtKembali.setName(""); // NOI18N

        panelAtas.setBackground(new java.awt.Color(3, 169, 244));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Book Your Ticket");

        Lnama.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        Lnama.setForeground(new java.awt.Color(255, 255, 255));
        Lnama.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lnama.setText("WISATA RIAU");

        javax.swing.GroupLayout panelAtasLayout = new javax.swing.GroupLayout(panelAtas);
        panelAtas.setLayout(panelAtasLayout);
        panelAtasLayout.setHorizontalGroup(
            panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAtasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAtasLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(322, 322, 322))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAtasLayout.createSequentialGroup()
                        .addComponent(Lnama, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(228, 228, 228))))
        );
        panelAtasLayout.setVerticalGroup(
            panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAtasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(Lnama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel15.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel15.setText("Wisata");

        txtWisata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWisataActionPerformed(evt);
            }
        });

        btnCheck.setText("Check Here!");
        btnCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelAtas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(TxtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9)
                                            .addComponent(TxtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addComponent(TxtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(14, 14, 14)
                                        .addComponent(BtnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BtnBatal))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(153, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel11)
                                .addGap(11, 11, 11)
                                .addComponent(TxtCash, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(TxtDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(140, 140, 140))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(13, 13, 13)
                                .addComponent(TxtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(BtnSimpan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBack)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCheck)
                                .addGap(52, 52, 52))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(6, 6, 6))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNofa)
                                    .addComponent(cmbWisata, 0, 142, Short.MAX_VALUE))
                                .addGap(8, 8, 8))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                                .addComponent(JdateJual, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtWisata, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TxtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(26, 26, 26))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(panelAtas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtNofa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbWisata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtWisata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(JdateJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(BtnAdd)
                                .addComponent(BtnBatal)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGap(0, 0, 0)
                                .addComponent(TxtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, 0)
                                .addComponent(TxtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(TxtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TxtCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TxtDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(TxtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(BtnSimpan)
                        .addComponent(btnCheck)
                        .addComponent(btnBack)))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNofaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNofaKeyPressed

    }//GEN-LAST:event_txtNofaKeyPressed

    private void txtNofaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNofaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNofaActionPerformed

    private void TxtCashKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtCashKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
        HitungKembali();
        }
    }//GEN-LAST:event_TxtCashKeyPressed

    private void TxtNamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtNamaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtNamaKeyPressed

    private void TxtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtNamaActionPerformed

    private void BtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAddActionPerformed
       TambahDetail();
       loadData();
       bersihkan();
       

    }//GEN-LAST:event_BtnAddActionPerformed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        // TODO add your handling code here:
        Selesai();
        bersihkan();
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        Batal();
        bersihkan();
    }//GEN-LAST:event_BtnBatalActionPerformed

    private void TxtSubTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtSubTotalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtSubTotalKeyPressed

    private void TxtSubTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtSubTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtSubTotalActionPerformed

    private void cmbWisataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbWisataActionPerformed
        // TODO add your handling code here:
        cari_nama();
        
    }//GEN-LAST:event_cmbWisataActionPerformed

    private void cmbWisataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbWisataMouseClicked
        // TODO add your handling code here:
         cari_nama();
    }//GEN-LAST:event_cmbWisataMouseClicked

    private void TxtTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtTotalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtTotalKeyPressed

    private void TxtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtTotalActionPerformed

    private void TxtQtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtQtyKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
        AutoSum();
        }
    }//GEN-LAST:event_TxtQtyKeyPressed

    private void TxtQtyPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_TxtQtyPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtQtyPropertyChange

    private void TxtQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtQtyActionPerformed

    private void TxtQtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TxtQtyMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_TxtQtyMouseClicked

    private void TblDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblDetailMouseClicked
        this.Cari_Kode();
        this.ShowData();

    }//GEN-LAST:event_TblDetailMouseClicked

    private void txtWisataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWisataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWisataActionPerformed

    private void btnCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckActionPerformed
        // TODO add your handling code here:
        dispose();
        frmBookList a = new frmBookList();
        a.setVisible(true);
    }//GEN-LAST:event_btnCheckActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        dispose();
        list a = new list();
        a.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

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
            java.util.logging.Logger.getLogger(frmBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAdd;
    private javax.swing.JButton BtnBatal;
    private javax.swing.JButton BtnSimpan;
    private com.toedter.calendar.JDateChooser JdateJual;
    private javax.swing.JLabel Lnama;
    private javax.swing.JTable TblDetail;
    private javax.swing.JTextField TxtCash;
    private javax.swing.JTextField TxtDateTime;
    private javax.swing.JTextField TxtKembali;
    private javax.swing.JTextField TxtNama;
    private javax.swing.JTextField TxtQty;
    private javax.swing.JTextField TxtSubTotal;
    private javax.swing.JTextField TxtTotal;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCheck;
    private javax.swing.JComboBox<String> cmbWisata;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelAtas;
    private javax.swing.JTextField txtNofa;
    private javax.swing.JTextField txtWisata;
    // End of variables declaration//GEN-END:variables
}

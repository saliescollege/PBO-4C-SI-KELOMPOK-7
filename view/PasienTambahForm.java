package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.PasienController;
import PBO_4C_SI_KELOMPOK_7.model.Pasien;
import PBO_4C_SI_KELOMPOK_7.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PasienTambahForm extends BaseFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private String username;
    private PasienList pasienListCaller; // Renamed to clarify it's the calling instance

    // Field untuk Step 1: Data Pasien
    private JTextField tfNama, tfAlamat, tfTelepon, tfDiagnosa, tfHistopatologi;
    private JTextField tfTanggalLahir;
    private JComboBox<String> cbKelamin;
    
    // Field untuk Step 2: Pemeriksaan Fisik dan Penunjang
    private JTextField tfTekananDarah, tfSuhuTubuh, tfDenyutNadi, tfBeratBadan, tfHB, tfLeukosit, tfTrombosit, tfFungsiHati, tfFungsiGinjal;
    
    // Field untuk Step 3: Rencana Terapi
    private JTextField tfJenisKemoterapi, tfDosis, tfSiklus, tfPremedikasi, tfAksesVena;
    private JComboBox<String> cbDokter;
    private Map<String, Integer> dokterMap;

    private static final int PREFERRED_FIELD_WIDTH = 500;

    // IMPORTANT CHANGE: Constructor now accepts a PasienList object
    public PasienTambahForm(String username, PasienList pasienListCaller) {
        super("Tambah Pasien", username);
        this.username = username;
        this.pasienListCaller = pasienListCaller; // Store the reference to the calling PasienList

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createStep1Panel(), "Step1");
        mainPanel.add(createStep2Panel(), "Step2");
        mainPanel.add(createStep3Panel(), "Step3");

        add(mainPanel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    PasienTambahForm.this,
                    "Apakah Anda yakin ingin keluar? Perubahan yang belum disimpan akan hilang.",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    pasienListCaller.setVisible(true); // Show the original PasienList again
                    dispose();
                }
            }
        });

        setVisible(true);
    }

    private JPanel createStep1Panel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        panel.setBorder(BorderFactory.createTitledBorder("Data Pasien"));

        // Nama Lengkap
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nama Lengkap"), gbc);
        gbc.gridx = 1;
        tfNama = new JTextField();
        tfNama.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfNama.getPreferredSize().height));
        panel.add(tfNama, gbc);

        // Alamat
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Alamat"), gbc);
        gbc.gridx = 1;
        tfAlamat = new JTextField();
        tfAlamat.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfAlamat.getPreferredSize().height));
        panel.add(tfAlamat, gbc);

        // No. Telepon
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("No. Telepon"), gbc);
        gbc.gridx = 1;
        tfTelepon = new JTextField();
        tfTelepon.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfTelepon.getPreferredSize().height));
        panel.add(tfTelepon, gbc);

        // Tanggal Lahir
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Tanggal Lahir"), gbc);
        gbc.gridx = 1;
        tfTanggalLahir = new JTextField();
        tfTanggalLahir.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfTanggalLahir.getPreferredSize().height));
        tfTanggalLahir.setToolTipText("Format: YYYY-MM-DD");
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        datePanel.add(tfTanggalLahir);
        JLabel dateHint = new JLabel("<html><font color='gray'>e.g., 2000-01-31</font></html>");
        datePanel.add(dateHint);
        panel.add(datePanel, gbc);

        // Jenis Kelamin (Dropdown)
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Jenis Kelamin"), gbc);
        gbc.gridx = 1;
        String[] kelaminOptions = {"L", "P"};
        cbKelamin = new JComboBox<>(kelaminOptions);
        cbKelamin.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, cbKelamin.getPreferredSize().height));
        panel.add(cbKelamin, gbc);

        // Diagnosa
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Diagnosa"), gbc);
        gbc.gridx = 1;
        tfDiagnosa = new JTextField();
        tfDiagnosa.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfDiagnosa.getPreferredSize().height));
        panel.add(tfDiagnosa, gbc);

        // Tipe Histopatologi
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Tipe Histopatologi"), gbc);
        gbc.gridx = 1;
        tfHistopatologi = new JTextField();
        tfHistopatologi.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfHistopatologi.getPreferredSize().height));
        panel.add(tfHistopatologi, gbc);

        // Navigation Buttons
        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton nextBtn = new JButton("Selanjutnya");
        nextBtn.setPreferredSize(new Dimension(120, 30));
        nextBtn.addActionListener(e -> cardLayout.show(mainPanel, "Step2"));
        navButtonPanel.add(nextBtn);

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(navButtonPanel, gbc);

        return panel;
    }

    private JPanel createStep2Panel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        panel.setBorder(BorderFactory.createTitledBorder("Pemeriksaan Fisik dan Penunjang"));

        // Tekanan Darah
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tekanan Darah"), gbc);
        gbc.gridx = 1;
        tfTekananDarah = new JTextField();
        tfTekananDarah.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfTekananDarah.getPreferredSize().height));
        tfTekananDarah.setToolTipText("e.g., 120/80");
        JPanel tdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tdPanel.add(tfTekananDarah);
        JLabel tdHint = new JLabel("<html><font color='gray'>mmHg</font></html>");
        tdPanel.add(tdHint);
        panel.add(tdPanel, gbc);

        // Suhu Tubuh
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Suhu Tubuh"), gbc);
        gbc.gridx = 1;
        tfSuhuTubuh = new JTextField();
        tfSuhuTubuh.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfSuhuTubuh.getPreferredSize().height));
        tfSuhuTubuh.setToolTipText("e.g., 36.5");
        JPanel suhuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        suhuPanel.add(tfSuhuTubuh);
        JLabel suhuHint = new JLabel("<html><font color='gray'>°C</font></html>");
        suhuPanel.add(suhuHint);
        panel.add(suhuPanel, gbc);

        // Denyut Nadi
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Denyut Nadi"), gbc);
        gbc.gridx = 1;
        tfDenyutNadi = new JTextField();
        tfDenyutNadi.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfDenyutNadi.getPreferredSize().height));
        tfDenyutNadi.setToolTipText("e.g., 75");
        JPanel nadiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nadiPanel.add(tfDenyutNadi);
        JLabel nadiHint = new JLabel("<html><font color='gray'>bpm</font></html>");
        nadiPanel.add(nadiHint);
        panel.add(nadiPanel, gbc);

        // Berat Badan
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Berat Badan"), gbc);
        gbc.gridx = 1;
        tfBeratBadan = new JTextField();
        tfBeratBadan.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfBeratBadan.getPreferredSize().height));
        tfBeratBadan.setToolTipText("e.g., 60.5");
        JPanel bbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bbPanel.add(tfBeratBadan);
        JLabel bbHint = new JLabel("<html><font color='gray'>kg</font></html>");
        bbPanel.add(bbHint);
        panel.add(bbPanel, gbc);

        // HB (Hemoglobin)
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("HB (Hemoglobin)"), gbc);
        gbc.gridx = 1;
        tfHB = new JTextField();
        tfHB.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfHB.getPreferredSize().height));
        tfHB.setToolTipText("e.g., 14.0");
        JPanel hbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hbPanel.add(tfHB);
        JLabel hbHint = new JLabel("<html><font color='gray'>g/dL</font></html>");
        hbPanel.add(hbHint);
        panel.add(hbPanel, gbc);

        // Leukosit
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Leukosit"), gbc);
        gbc.gridx = 1;
        tfLeukosit = new JTextField();
        tfLeukosit.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfLeukosit.getPreferredSize().height));
        tfLeukosit.setToolTipText("e.g., 7500");
        JPanel lePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        lePanel.add(tfLeukosit);
        JLabel leHint = new JLabel("<html><font color='gray'>/µL</font></html>");
        lePanel.add(leHint);
        panel.add(lePanel, gbc);

        // Trombosit
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Trombosit"), gbc);
        gbc.gridx = 1;
        tfTrombosit = new JTextField();
        tfTrombosit.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfTrombosit.getPreferredSize().height));
        tfTrombosit.setToolTipText("e.g., 250000");
        JPanel trPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        trPanel.add(tfTrombosit);
        JLabel trHint = new JLabel("<html><font color='gray'>/µL</font></html>");
        trPanel.add(trHint);
        panel.add(trPanel, gbc);

        // Fungsi Hati (SGOT/SGPT)
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Fungsi Hati"), gbc);
        gbc.gridx = 1;
        tfFungsiHati = new JTextField();
        tfFungsiHati.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfFungsiHati.getPreferredSize().height));
        tfFungsiHati.setToolTipText("e.g., SGOT 25, SGPT 30");
        JPanel fhPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fhPanel.add(tfFungsiHati);
        JLabel fhHint = new JLabel("<html><font color='gray'>(SGOT/SGPT)</font></html>");
        fhPanel.add(fhHint);
        panel.add(fhPanel, gbc);

        // Fungsi Ginjal (Ureum/Kreatinin)
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(new JLabel("Fungsi Ginjal"), gbc);
        gbc.gridx = 1;
        tfFungsiGinjal = new JTextField();
        tfFungsiGinjal.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfFungsiGinjal.getPreferredSize().height));
        tfFungsiGinjal.setToolTipText("e.g., Ureum 20, Kreatinin 0.8");
        JPanel fgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fgPanel.add(tfFungsiGinjal);
        JLabel fgHint = new JLabel("<html><font color='gray'>(Ureum/Kreatinin)</font></html>");
        fgPanel.add(fgHint);
        panel.add(fgPanel, gbc);

        // Navigation Buttons
        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backBtn = new JButton("Kembali");
        backBtn.setPreferredSize(new Dimension(120, 30));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Step1"));
        navButtonPanel.add(backBtn);

        JButton nextBtn = new JButton("Selanjutnya");
        nextBtn.setPreferredSize(new Dimension(120, 30));
        nextBtn.addActionListener(e -> cardLayout.show(mainPanel, "Step3"));
        navButtonPanel.add(nextBtn);

        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(navButtonPanel, gbc);

        return panel;
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        panel.setBorder(BorderFactory.createTitledBorder("Rencana Terapi"));

        // Jenis Kemoterapi
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Jenis Kemoterapi"), gbc);
        gbc.gridx = 1;
        tfJenisKemoterapi = new JTextField();
        tfJenisKemoterapi.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfJenisKemoterapi.getPreferredSize().height));
        panel.add(tfJenisKemoterapi, gbc);

        // Dosis
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Dosis"), gbc);
        gbc.gridx = 1;
        tfDosis = new JTextField();
        tfDosis.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfDosis.getPreferredSize().height));
        panel.add(tfDosis, gbc);

        // Siklus
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Siklus"), gbc);
        gbc.gridx = 1;
        tfSiklus = new JTextField();
        tfSiklus.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfSiklus.getPreferredSize().height));
        panel.add(tfSiklus, gbc);

        // Premedikasi
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Premedikasi"), gbc);
        gbc.gridx = 1;
        tfPremedikasi = new JTextField();
        tfPremedikasi.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfPremedikasi.getPreferredSize().height));
        panel.add(tfPremedikasi, gbc);

        // Akses Vena
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Akses Vena"), gbc);
        gbc.gridx = 1;
        tfAksesVena = new JTextField();
        tfAksesVena.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, tfAksesVena.getPreferredSize().height));
        panel.add(tfAksesVena, gbc);

        // Dokter Penanggung Jawab (Dropdown from DB)
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Dokter Penanggung Jawab"), gbc);
        gbc.gridx = 1;
        cbDokter = new JComboBox<>();
        cbDokter.setPreferredSize(new Dimension(PREFERRED_FIELD_WIDTH, cbDokter.getPreferredSize().height));
        populateDokterComboBox();
        panel.add(cbDokter, gbc);

        // Navigation Buttons
        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backBtn = new JButton("Kembali");
        backBtn.setPreferredSize(new Dimension(120, 30));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Step2"));
        navButtonPanel.add(backBtn);
        
        JButton submitBtn = new JButton("Simpan");
        submitBtn.setPreferredSize(new Dimension(120, 30));
        submitBtn.addActionListener(e -> simpanPasien());
        navButtonPanel.add(submitBtn);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(navButtonPanel, gbc);

        return panel;
    }

    private void populateDokterComboBox() {
        dokterMap = new HashMap<>();
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT dokter_id, nama, spesialisasi FROM dokter")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("dokter_id");
                String nama = rs.getString("nama");
                String spesialisasi = rs.getString("spesialisasi");
                String display = String.format("%d - %s - %s", id, nama, spesialisasi);
                cbDokter.addItem(display);
                dokterMap.put(display, id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading dokter data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void simpanPasien() {
        try {
            LocalDate tanggalLahir;
            try {
                tanggalLahir = LocalDate.parse(tfTanggalLahir.getText().trim());
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Format tanggal lahir tidak valid. Gunakan YYYY-MM-DD.");
                cardLayout.show(mainPanel, "Step1");
                return;
            }

            String selectedDokterDisplay = (String) cbDokter.getSelectedItem();
            if (selectedDokterDisplay == null || selectedDokterDisplay.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih dokter penanggung jawab.");
                cardLayout.show(mainPanel, "Step3");
                return;
            }
            int dokterId = dokterMap.get(selectedDokterDisplay);

            Pasien pasien = new Pasien();
            pasien.setNama(tfNama.getText().trim());
            pasien.setAlamat(tfAlamat.getText().trim());
            pasien.setTelepon(tfTelepon.getText().trim());
            pasien.setTanggalLahir(tanggalLahir);
            pasien.setKelamin((String) cbKelamin.getSelectedItem());
            pasien.setDiagnosa(tfDiagnosa.getText().trim());
            pasien.setHistopatologi(tfHistopatologi.getText().trim());

            pasien.setTekananDarah(tfTekananDarah.getText().trim());
            pasien.setSuhuTubuh(tfSuhuTubuh.getText().trim());
            pasien.setDenyutNadi(tfDenyutNadi.getText().trim());
            pasien.setBeratBadan(tfBeratBadan.getText().trim());
            pasien.setHb(tfHB.getText().trim());
            pasien.setLeukosit(tfLeukosit.getText().trim());
            pasien.setTrombosit(tfTrombosit.getText().trim());
            pasien.setFungsiHati(tfFungsiHati.getText().trim());
            pasien.setFungsiGinjal(tfFungsiGinjal.getText().trim());

            pasien.setJenisKemoterapi(tfJenisKemoterapi.getText().trim());
            pasien.setDosis(tfDosis.getText().trim());
            pasien.setSiklus(tfSiklus.getText().trim());
            pasien.setPremedikasi(tfPremedikasi.getText().trim());
            pasien.setAksesVena(tfAksesVena.getText().trim());
            pasien.setDokterId(dokterId);

            PasienController.tambahPasien(pasien);

            JOptionPane.showMessageDialog(this, "Pasien berhasil ditambahkan!");
            // IMPORTANT: Call loadPasien on the *calling* PasienList instance
            pasienListCaller.loadPasien();
            pasienListCaller.setVisible(true); // Make the original PasienList visible again
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan pasien: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
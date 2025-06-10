package view;

import controller.PasienController;
import model.Pasien;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PasienTambahForm extends BaseFrame { // <-- Mengubah dari JFrame menjadi BaseFrame

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    // Field untuk Step 1: Data Pasien
    private JTextField tfNama, tfAlamat, tfTelepon, tfTanggalLahir, tfKelamin, tfDiagnosa, tfHistopatologi;
    // Field untuk Step 2: Pemeriksaan Fisik dan Penunjang
    private JTextField tfTekananDarah, tfSuhuTubuh, tfDenyutNadi, tfBeratBadan, tfHB, tfLeukosit, tfTrombosit, tfFungsiHati, tfFungsiGinjal;
    // Field untuk Step 3: Rencana Terapi
    private JTextField tfJenisKemoterapi, tfDosis, tfSiklus, tfPremedikasi, tfAksesVena, tfDokterId;

    public PasienTambahForm(String username) {
        super("Tambah Pasien", username); // <-- Panggil konstruktor BaseFrame

        // Hapus kode navbar yang sebelumnya ada di sini karena sudah ditangani oleh BaseFrame.

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createStep1Panel(), "Step1");
        mainPanel.add(createStep2Panel(), "Step2");
        mainPanel.add(createStep3Panel(), "Step3");

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createStep1Panel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Data Pasien"));

        tfNama = new JTextField();
        tfAlamat = new JTextField();
        tfTelepon = new JTextField();
        tfTanggalLahir = new JTextField("2000-01-01");  // Format default
        tfKelamin = new JTextField();
        tfDiagnosa = new JTextField();
        tfHistopatologi = new JTextField();

        panel.add(new JLabel("Nama Lengkap"));
        panel.add(tfNama);
        panel.add(new JLabel("Alamat"));
        panel.add(tfAlamat);
        panel.add(new JLabel("No. Telepon"));
        panel.add(tfTelepon);
        panel.add(new JLabel("Tanggal Lahir (yyyy-mm-dd)"));
        panel.add(tfTanggalLahir);
        panel.add(new JLabel("Jenis Kelamin"));
        panel.add(tfKelamin);
        panel.add(new JLabel("Diagnosa"));
        panel.add(tfDiagnosa);
        panel.add(new JLabel("Tipe Histopatologi"));
        panel.add(tfHistopatologi);

        JButton nextBtn = new JButton("Selanjutnya");
        nextBtn.addActionListener(e -> cardLayout.show(mainPanel, "Step2"));
        panel.add(nextBtn);
        panel.add(new JLabel()); // Placeholder agar layout tetap rapi

        return panel;
    }

    private JPanel createStep2Panel() {
        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Pemeriksaan Fisik dan Penunjang"));

        tfTekananDarah = new JTextField();
        tfSuhuTubuh = new JTextField();
        tfDenyutNadi = new JTextField();
        tfBeratBadan = new JTextField();
        tfHB = new JTextField();
        tfLeukosit = new JTextField();
        tfTrombosit = new JTextField();
        tfFungsiHati = new JTextField();
        tfFungsiGinjal = new JTextField();

        panel.add(new JLabel("Tekanan Darah"));
        panel.add(tfTekananDarah);
        panel.add(new JLabel("Suhu Tubuh"));
        panel.add(tfSuhuTubuh);
        panel.add(new JLabel("Denyut Nadi"));
        panel.add(tfDenyutNadi);
        panel.add(new JLabel("Berat Badan"));
        panel.add(tfBeratBadan);
        panel.add(new JLabel("HB (Hemoglobin)"));
        panel.add(tfHB);
        panel.add(new JLabel("Leukosit"));
        panel.add(tfLeukosit);
        panel.add(new JLabel("Trombosit"));
        panel.add(tfTrombosit);
        panel.add(new JLabel("Fungsi Hati (SGOT/SGPT)"));
        panel.add(tfFungsiHati);
        panel.add(new JLabel("Fungsi Ginjal (Ureum/Kreatinin)"));
        panel.add(tfFungsiGinjal);

        JButton nextBtn = new JButton("Selanjutnya");
        nextBtn.addActionListener(e -> cardLayout.show(mainPanel, "Step3"));
        panel.add(nextBtn);
        panel.add(new JLabel());

        return panel;
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Rencana Terapi"));

        tfJenisKemoterapi = new JTextField();
        tfDosis = new JTextField();
        tfSiklus = new JTextField();
        tfPremedikasi = new JTextField();
        tfAksesVena = new JTextField();
        tfDokterId = new JTextField();

        panel.add(new JLabel("Jenis Kemoterapi"));
        panel.add(tfJenisKemoterapi);
        panel.add(new JLabel("Dosis"));
        panel.add(tfDosis);
        panel.add(new JLabel("Siklus"));
        panel.add(tfSiklus);
        panel.add(new JLabel("Premedikasi"));
        panel.add(tfPremedikasi);
        panel.add(new JLabel("Akses Vena"));
        panel.add(tfAksesVena);
        panel.add(new JLabel("Dokter Penanggung Jawab (ID)"));
        panel.add(tfDokterId);

        JButton submitBtn = new JButton("Simpan");
        submitBtn.addActionListener(e -> simpanPasien());
        panel.add(submitBtn);
        panel.add(new JLabel());

        return panel;
    }

    private void simpanPasien() {
        try {
            LocalDate tanggalLahir;
            try {
                tanggalLahir = LocalDate.parse(tfTanggalLahir.getText().trim());
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Format tanggal lahir tidak valid. Gunakan yyyy-mm-dd.");
                cardLayout.show(mainPanel, "Step1");
                return;
            }

            int dokterId;
            try {
                dokterId = Integer.parseInt(tfDokterId.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID Dokter harus berupa angka.");
                cardLayout.show(mainPanel, "Step3");
                return;
            }

            Pasien pasien = new Pasien();
            pasien.setNama(tfNama.getText().trim());
            pasien.setAlamat(tfAlamat.getText().trim());
            pasien.setTelepon(tfTelepon.getText().trim());
            pasien.setTanggalLahir(tanggalLahir);
            pasien.setKelamin(tfKelamin.getText().trim());
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
            new PasienList(username).setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan pasien: " + ex.getMessage());
        }
    }
}
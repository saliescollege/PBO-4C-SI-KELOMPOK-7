package view;

import controller.PasienController;
import model.Pasien;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class PasienTambahForm extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Step 1 - Data Pasien
    private JTextField tfNama, tfAlamat, tfTelepon, tfTanggalLahir, tfKelamin, tfDiagnosa, tfHistopatologi;

    // Step 2 - Pemeriksaan Fisik
    private JTextField tfTekananDarah, tfSuhuTubuh, tfDenyutNadi, tfBeratBadan, tfHB, tfLeukosit, tfTrombosit, tfFungsiHati, tfFungsiGinjal;

    // Step 3 - Rencana Terapi
    private JTextField tfJenisKemoterapi, tfDosis, tfSiklus, tfPremedikasi, tfAksesVena, tfDokterId;

    public PasienTambahForm() {
        setTitle("Tambah Pasien");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(step1Panel(), "Step1");
        mainPanel.add(step2Panel(), "Step2");
        mainPanel.add(step3Panel(), "Step3");

        add(mainPanel);
        cardLayout.show(mainPanel, "Step1");
    }

    private JPanel step1Panel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Data Pasien"));

        tfNama = new JTextField();
        tfAlamat = new JTextField();
        tfTelepon = new JTextField();
        tfTanggalLahir = new JTextField("2000-01-01");  // format yyyy-mm-dd
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

        JButton nextBtn = new JButton("Next");
        nextBtn.addActionListener(e -> cardLayout.show(mainPanel, "Step2"));
        panel.add(nextBtn);
        panel.add(new JLabel());

        return panel;
    }

    private JPanel step2Panel() {
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

        JButton nextBtn = new JButton("Next");
        nextBtn.addActionListener(e -> cardLayout.show(mainPanel, "Step3"));
        panel.add(nextBtn);
        panel.add(new JLabel());

        return panel;
    }

    private JPanel step3Panel() {
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

        JButton submitBtn = new JButton("Submit");
        submitBtn.addActionListener(e -> simpanPasien());
        panel.add(submitBtn);
        panel.add(new JLabel());

        return panel;
    }

    private void simpanPasien() {
        try {
            Pasien pasien = new Pasien();
            pasien.setNama(tfNama.getText());
            pasien.setAlamat(tfAlamat.getText());
            pasien.setTelepon(tfTelepon.getText());
            pasien.setTanggalLahir(LocalDate.parse(tfTanggalLahir.getText()));
            pasien.setKelamin(tfKelamin.getText());
            pasien.setDiagnosa(tfDiagnosa.getText());
            pasien.setHistopatologi(tfHistopatologi.getText());

            pasien.setTekananDarah(tfTekananDarah.getText());
            pasien.setSuhuTubuh(tfSuhuTubuh.getText());
            pasien.setDenyutNadi(tfDenyutNadi.getText());
            pasien.setBeratBadan(tfBeratBadan.getText());
            pasien.setHb(tfHB.getText());
            pasien.setLeukosit(tfLeukosit.getText());
            pasien.setTrombosit(tfTrombosit.getText());
            pasien.setFungsiHati(tfFungsiHati.getText());
            pasien.setFungsiGinjal(tfFungsiGinjal.getText());

            pasien.setJenisKemoterapi(tfJenisKemoterapi.getText());
            pasien.setDosis(tfDosis.getText());
            pasien.setSiklus(tfSiklus.getText());
            pasien.setPremedikasi(tfPremedikasi.getText());
            pasien.setAksesVena(tfAksesVena.getText());
            pasien.setDokterId(Integer.parseInt(tfDokterId.getText()));

            PasienController.tambahPasien(pasien);
            JOptionPane.showMessageDialog(this, "Data pasien berhasil disimpan.");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data pasien.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PasienTambahForm().setVisible(true));
    }
}

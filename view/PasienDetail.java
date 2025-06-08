package view;

import controller.PasienController;
import model.Pasien;

import javax.swing.*;
import java.awt.*;

public class PasienDetail extends JFrame {

    private JLabel lblNama, lblAlamat, lblTelepon, lblTanggalLahir, lblKelamin, lblDiagnosa, lblHistopatologi, lblDokterId;

    public PasienDetail(int pasienId) {
        setTitle("Detail Pasien");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Pasien pasien = PasienController.getPasienById(pasienId);
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Data pasien tidak ditemukan.");
            dispose();
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createTitledBorder("Informasi Pasien"));

        lblNama = new JLabel("Nama Lengkap: " + pasien.getNama());
        lblAlamat = new JLabel("Alamat: " + pasien.getAlamat());
        lblTelepon = new JLabel("No. Telepon: " + pasien.getTelepon());
        lblTanggalLahir = new JLabel("Tanggal Lahir: " + pasien.getTanggalLahir());
        lblKelamin = new JLabel("Jenis Kelamin: " + pasien.getKelamin());
        lblDiagnosa = new JLabel("Diagnosa: " + pasien.getDiagnosa());
        lblHistopatologi = new JLabel("Tipe Histopatologi: " + pasien.getHistopatologi());
        lblDokterId = new JLabel("Dokter Penanggung Jawab (ID): " + pasien.getDokterId());

        panel.add(lblNama);
        panel.add(lblAlamat);
        panel.add(lblTelepon);
        panel.add(lblTanggalLahir);
        panel.add(lblKelamin);
        panel.add(lblDiagnosa);
        panel.add(lblHistopatologi);
        panel.add(lblDokterId);

        JButton btnClose = new JButton("Tutup");
        btnClose.addActionListener(e -> dispose());

        add(panel, BorderLayout.CENTER);
        add(btnClose, BorderLayout.SOUTH);
    }
}

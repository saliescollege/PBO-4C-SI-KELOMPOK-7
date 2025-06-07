package view;
import javax.swing.*;
import java.awt.*;

public class PasienTambahForm extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public PasienTambahForm() {
        setTitle("Pasien Baru");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        DataPasienPanel dataPasienPanel = new DataPasienPanel();
        PeriksaFisikPanel periksaFisikPanel = new PeriksaFisikPanel();
        RencanaTerapiPanel rencanaTerapiPanel = new RencanaTerapiPanel();
        HasilPanel hasilPanel = new HasilPanel(); // Tambahkan panel hasil

        cardPanel.add(dataPasienPanel, "DataPasien");
        cardPanel.add(periksaFisikPanel, "PeriksaFisik");
        cardPanel.add(rencanaTerapiPanel, "RencanaTerapi");
        cardPanel.add(hasilPanel, "Hasil"); // Tambahkan ke cardPanel

        dataPasienPanel.nextButton.addActionListener(e -> cardLayout.show(cardPanel, "PeriksaFisik"));
        periksaFisikPanel.nextButton.addActionListener(e -> cardLayout.show(cardPanel, "RencanaTerapi"));
        rencanaTerapiPanel.submitButton.addActionListener(e -> {
            // Ambil data dari semua panel
            StringBuilder hasil = new StringBuilder();
            hasil.append("=== Data Pasien ===\n");
            hasil.append("Nama: ").append(dataPasienPanel.namaField.getText()).append("\n");
            hasil.append("Alamat: ").append(dataPasienPanel.alamatArea.getText()).append("\n");
            hasil.append("No. Telepon: ").append(dataPasienPanel.teleponField.getText()).append("\n");
            hasil.append("Tanggal Lahir: ").append(dataPasienPanel.tglLahirField.getText()).append("\n");
            hasil.append("Jenis Kelamin: ").append(dataPasienPanel.kelaminBox.getSelectedItem()).append("\n");
            hasil.append("Diagnosa: ").append(dataPasienPanel.diagnosaArea.getText()).append("\n");
            hasil.append("Tipe Histopatologi: ").append(dataPasienPanel.histopatologiArea.getText()).append("\n\n");

            hasil.append("=== Pemeriksaan Fisik ===\n");
            hasil.append("Tekanan Darah: ").append(periksaFisikPanel.tekananDarah.getText()).append("\n");
            hasil.append("Suhu Tubuh: ").append(periksaFisikPanel.suhuTubuh.getText()).append("\n");
            hasil.append("Denyut Nadi: ").append(periksaFisikPanel.nadi.getText()).append("\n");
            hasil.append("Berat Badan: ").append(periksaFisikPanel.beratBadan.getText()).append("\n");
            hasil.append("HB: ").append(periksaFisikPanel.hb.getText()).append("\n");
            hasil.append("Leukosit: ").append(periksaFisikPanel.leukosit.getText()).append("\n");
            hasil.append("Trombosit: ").append(periksaFisikPanel.trombosit.getText()).append("\n");
            hasil.append("Fungsi Hati: ").append(periksaFisikPanel.fungsiHati.getText()).append("\n");
            hasil.append("Fungsi Ginjal: ").append(periksaFisikPanel.fungsiGinjal.getText()).append("\n\n");

            hasil.append("=== Rencana Terapi ===\n");
            hasil.append("Jenis Kemoterapi: ").append(rencanaTerapiPanel.jenisKemoterapi.getText()).append("\n");
            hasil.append("Dosis: ").append(rencanaTerapiPanel.dosis.getText()).append("\n");
            hasil.append("Siklus: ").append(rencanaTerapiPanel.siklus.getText()).append("\n");
            hasil.append("Premedikasi: ").append(rencanaTerapiPanel.premedikasi.getText()).append("\n");
            hasil.append("Akses Vena: ").append(rencanaTerapiPanel.aksesVena.getText()).append("\n");
            hasil.append("Dokter Penanggung Jawab: ").append(rencanaTerapiPanel.dokterPenanggungJawab.getText()).append("\n");

            hasilPanel.setHasil(hasil.toString());
            cardLayout.show(cardPanel, "Hasil");
        });

        hasilPanel.selesaiButton.addActionListener(e -> {
            // Kembali ke awal atau tutup form
            cardLayout.show(cardPanel, "DataPasien");
        });

        add(cardPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new PasienTambahForm();
    }
}

class DataPasienPanel extends JPanel {
    JTextField namaField, teleponField;
    JTextArea alamatArea, diagnosaArea, histopatologiArea;
    JFormattedTextField tglLahirField;
    JComboBox<String> kelaminBox;
    JButton nextButton;

    public DataPasienPanel() {
        setLayout(new GridLayout(8, 2));
        add(new JLabel("Nama Lengkap:"));
        namaField = new JTextField();
        add(namaField);

        add(new JLabel("Alamat:"));
        alamatArea = new JTextArea(2, 15);
        add(new JScrollPane(alamatArea));

        add(new JLabel("No. Telepon:"));
        teleponField = new JTextField();
        add(teleponField);

        add(new JLabel("Tanggal Lahir (yyyy-MM-dd):"));
        tglLahirField = new JFormattedTextField(new java.text.SimpleDateFormat("yyyy-MM-dd"));
        add(tglLahirField);

        add(new JLabel("Jenis Kelamin:"));
        kelaminBox = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        add(kelaminBox);

        add(new JLabel("Diagnosa:"));
        diagnosaArea = new JTextArea(2, 15);
        add(new JScrollPane(diagnosaArea));

        add(new JLabel("Tipe Histopatologi:"));
        histopatologiArea = new JTextArea(2, 15);
        add(new JScrollPane(histopatologiArea));

        nextButton = new JButton("Next");
        add(new JLabel()); // spacer
        add(nextButton);
    }
}

class PeriksaFisikPanel extends JPanel {
    JTextField tekananDarah, suhuTubuh, nadi, beratBadan, hb, leukosit, trombosit, fungsiHati, fungsiGinjal;
    JButton nextButton;

    public PeriksaFisikPanel() {
        setLayout(new GridLayout(10, 2));
        add(new JLabel("Tekanan Darah:"));
        tekananDarah = new JTextField();
        add(tekananDarah);

        add(new JLabel("Suhu Tubuh:"));
        suhuTubuh = new JTextField();
        add(suhuTubuh);

        add(new JLabel("Denyut Nadi:"));
        nadi = new JTextField();
        add(nadi);

        add(new JLabel("Berat Badan:"));
        beratBadan = new JTextField();
        add(beratBadan);

        add(new JLabel("HB (Hemoglobin):"));
        hb = new JTextField();
        add(hb);

        add(new JLabel("Leukosit:"));
        leukosit = new JTextField();
        add(leukosit);

        add(new JLabel("Trombosit:"));
        trombosit = new JTextField();
        add(trombosit);

        add(new JLabel("Fungsi Hati (SGOT/SGPT):"));
        fungsiHati = new JTextField();
        add(fungsiHati);

        add(new JLabel("Fungsi Ginjal (Ureum/Kreatinin):"));
        fungsiGinjal = new JTextField();
        add(fungsiGinjal);

        nextButton = new JButton("Next");
        add(new JLabel()); // spacer
        add(nextButton);
    }
}

class RencanaTerapiPanel extends JPanel {
    JTextField jenisKemoterapi, dosis, siklus, premedikasi, aksesVena, dokterPenanggungJawab;
    JButton submitButton;

    public RencanaTerapiPanel() {
        setLayout(new GridLayout(7, 2));
        add(new JLabel("Jenis Kemoterapi:"));
        jenisKemoterapi = new JTextField();
        add(jenisKemoterapi);

        add(new JLabel("Dosis:"));
        dosis = new JTextField();
        add(dosis);

        add(new JLabel("Siklus:"));
        siklus = new JTextField();
        add(siklus);

        add(new JLabel("Premedikasi:"));
        premedikasi = new JTextField();
        add(premedikasi);

        add(new JLabel("Akses Vena:"));
        aksesVena = new JTextField();
        add(aksesVena);

        add(new JLabel("Dokter Penanggung Jawab:"));
        dokterPenanggungJawab = new JTextField();
        add(dokterPenanggungJawab);

        submitButton = new JButton("Submit");
        add(new JLabel()); // spacer
        add(submitButton);
    }
}

// Tambahkan class HasilPanel
class HasilPanel extends JPanel {
    JTextArea hasilArea;
    JButton selesaiButton;

    public HasilPanel() {
        setLayout(new BorderLayout());
        hasilArea = new JTextArea();
        hasilArea.setEditable(false);
        add(new JScrollPane(hasilArea), BorderLayout.CENTER);
        selesaiButton = new JButton("Selesai");
        add(selesaiButton, BorderLayout.SOUTH);
    }

    public void setHasil(String hasil) {
        hasilArea.setText(hasil);
    }
}
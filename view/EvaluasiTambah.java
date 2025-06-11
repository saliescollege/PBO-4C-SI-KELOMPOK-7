package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.EvaluasiController;
import javax.swing.*;
import java.awt.*;

public class EvaluasiTambah extends JDialog {
    private JTextArea kondisiField, efekSampingField, catatanField;
    private JButton simpanButton;
    private int pasienId;

    // Konstruktor untuk parent berupa Frame
    public EvaluasiTambah(Frame parent, int pasienId, String pasienNama) {
        super(parent, "Form Tambah Evaluasi", true);
        this.pasienId = pasienId;
        initUI(pasienNama);
    }

    // Konstruktor baru untuk parent berupa Dialog
    public EvaluasiTambah(Dialog parent, int pasienId, String pasienNama) {
        super(parent, "Form Tambah Evaluasi", true);
        this.pasienId = pasienId;
        initUI(pasienNama);
    }

    // Metode private untuk setup UI yang sama
    private void initUI(String pasienNama) {
        setSize(500, 550);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Evaluasi Sesi untuk Pasien:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        JLabel pasienLabel = new JLabel(pasienNama, SwingConstants.CENTER);
        pasienLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        gbc.gridy++;
        formPanel.add(pasienLabel, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Kondisi Pasca-Terapi:"), gbc);
        
        gbc.gridx = 1;
        kondisiField = new JTextArea(4, 20);
        kondisiField.setLineWrap(true);
        kondisiField.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(kondisiField), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Efek Samping:"), gbc);

        gbc.gridx = 1;
        efekSampingField = new JTextArea(4, 20);
        efekSampingField.setLineWrap(true);
        efekSampingField.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(efekSampingField), gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Catatan Tambahan:"), gbc);

        gbc.gridx = 1;
        catatanField = new JTextArea(4, 20);
        catatanField.setLineWrap(true);
        catatanField.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(catatanField), gbc);

        add(formPanel, BorderLayout.CENTER);

        simpanButton = new JButton("Simpan Evaluasi");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        buttonPanel.add(simpanButton);
        add(buttonPanel, BorderLayout.SOUTH);

        simpanButton.addActionListener(e -> simpanEvaluasi());
    }

    private void simpanEvaluasi() {
        String kondisi = kondisiField.getText().trim();
        String efek = efekSampingField.getText().trim();
        String catatan = catatanField.getText().trim();

        if (kondisi.isEmpty() || efek.isEmpty() || catatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = EvaluasiController.tambahEvaluasi(this.pasienId, kondisi, efek, catatan);

        if (success) {
            JOptionPane.showMessageDialog(this, "Data evaluasi berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data.\nPastikan pasien memiliki riwayat jadwal terapi.", "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }
}
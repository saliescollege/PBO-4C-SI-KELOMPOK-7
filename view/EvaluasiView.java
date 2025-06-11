// PBO_4C_SI_KELOMPOK_7/view/EvaluasiView.java
package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.EvaluasiController;
import PBO_4C_SI_KELOMPOK_7.model.Evaluasi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class EvaluasiView extends JDialog {
    private JTable evaluasiTable;
    private DefaultTableModel tableModel;
    private int pasienId;
    private String pasienNama;
    private String username;

    public EvaluasiView(Frame parent, String username, int pasienId, String pasienNama) {
        super(parent, "Riwayat Evaluasi Pasien", true);
        this.pasienId = pasienId;
        this.pasienNama = pasienNama;
        this.username = username;

        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Riwayat Evaluasi untuk: " + pasienNama, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Tanggal", "Sesi Ke-", "Kondisi Pasca-Terapi", "Efek Samping", "Catatan Tambahan", "Dokter"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        evaluasiTable = new JTable(tableModel);
        evaluasiTable.setRowHeight(25);
        mainPanel.add(new JScrollPane(evaluasiTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton tambahButton = new JButton("Tambah Evaluasi Baru");
        JButton tutupButton = new JButton("Tutup");

        buttonPanel.add(tambahButton);
        buttonPanel.add(tutupButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        tambahButton.addActionListener(e -> {
            // Membuka dialog EvaluasiTambah, kemudian merefresh data
            EvaluasiTambah form = new EvaluasiTambah(this, pasienId, pasienNama);
            form.setVisible(true);
            // Setelah form ditutup, data akan di-refresh
            loadEvaluasiData();
        });

        tutupButton.addActionListener(e -> dispose());

        loadEvaluasiData();
    }

    private void loadEvaluasiData() {
        tableModel.setRowCount(0); // Clear table
        List<Evaluasi> evaluasiList = EvaluasiController.getEvaluasiByPasienId(pasienId);

        if (evaluasiList.isEmpty()) {
            tableModel.addRow(new Object[]{"-", "-", "Belum ada data evaluasi", "-", "-", "-"});
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            for (Evaluasi eval : evaluasiList) {
                tableModel.addRow(new Object[]{
                    sdf.format(eval.getTanggalEvaluasi()),
                    eval.getSesiKe(),
                    eval.getKondisiPostTerapi(),
                    eval.getEfekSamping(),
                    eval.getCatatan(),
                    eval.getNamaDokter()
                });
            }
        }
    }
}
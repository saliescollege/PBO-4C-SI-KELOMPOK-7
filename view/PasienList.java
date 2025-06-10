package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.PasienController;
import PBO_4C_SI_KELOMPOK_7.model.Pasien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.ExecutionException; // Required for SwingWorker

public class PasienList extends BaseFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private String currentUsername;

    public PasienList(String username) {
        super("Daftar Pasien", username);
        this.currentUsername = username;

        // Tabel
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama Lengkap", "No. Telepon", "Dokter Penanggung Jawab"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // agar sel tabel tidak bisa diedit langsung
            }
        };

        table = new JTable(tableModel);
        loadPasien(); // Initial load

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Pasien"));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Tombol bawah
        JButton btnDetail = new JButton("Lihat Detail");
        btnDetail.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int pasienId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                // Use SwingWorker for potentially long-running task of opening PasienDetail
                new OpenPasienDetailTask(pasienId, currentUsername).execute();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih pasien terlebih dahulu.");
            }
        });

        JButton btnTambah = new JButton("Tambah Pasien");
        btnTambah.addActionListener(e -> {
            new PasienTambahForm(currentUsername, this).setVisible(true); // Pass 'this'
            this.setVisible(false); // Hide this PasienList temporarily
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(btnTambah);
        bottomPanel.add(btnDetail);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void loadPasien() {
        List<Pasien> pasienList = PasienController.getAllPasien(); //
        tableModel.setRowCount(0); //

        if (!pasienList.isEmpty()) { //
            for (Pasien p : pasienList) { //
                tableModel.addRow(new Object[]{ //
                        p.getId(), //
                        p.getNama(), //
                        p.getTelepon(), //
                        p.getDosis() // This will now contain the dokter_nama
                });
            }
        }
    }

    // SwingWorker for opening PasienDetail to prevent UI freeze
    private class OpenPasienDetailTask extends SwingWorker<PasienDetail, Void> {
        private int pasienId;
        private String username;
        private JDialog loadingDialog; // Optional: for showing loading progress

        public OpenPasienDetailTask(int pasienId, String username) {
            this.pasienId = pasienId;
            this.username = username;
        }

        @Override
        protected void done() { // Correct method to override for SwingWorker completion
            // This method runs on the Event Dispatch Thread (EDT)
            if (loadingDialog != null) {
                loadingDialog.dispose(); // Always close loading dialog
            }
            try {
                PasienDetail pasienDetail = get(); // Get the result from doInBackground
                if (pasienDetail != null) {
                    pasienDetail.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(PasienList.this,
                                                "Detail pasien tidak dapat dimuat. Data mungkin tidak lengkap.",
                                                "Informasi",
                                                JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                JOptionPane.showMessageDialog(PasienList.this,
                                            "Proses memuat detail pasien dibatalkan.",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                // This exception wraps the actual exception thrown in doInBackground()
                Throwable cause = ex.getCause(); // Get the real cause of the exception
                System.err.println("Exception during PasienDetail creation or data loading: " + cause.getMessage());
                cause.printStackTrace();
                JOptionPane.showMessageDialog(PasienList.this,
                                            "Gagal membuka detail pasien: " + cause.getMessage(),
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) { // Catch any other unexpected exceptions
                JOptionPane.showMessageDialog(PasienList.this,
                                            "Terjadi kesalahan tidak terduga saat memuat detail pasien: " + ex.getMessage(),
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        @Override
        protected PasienDetail doInBackground() throws Exception {
            // This method runs on a background thread
            // Show a loading indicator on the EDT before starting the task
            SwingUtilities.invokeLater(() -> {
                loadingDialog = new JDialog(PasienList.this, "Memuat...", true);
                JLabel loadingLabel = new JLabel("Mohon tunggu, sedang memuat detail pasien...");
                loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
                loadingDialog.add(loadingLabel, BorderLayout.CENTER); // Use BorderLayout to center
                loadingDialog.setSize(300, 100);
                loadingDialog.setLocationRelativeTo(PasienList.this);
                loadingDialog.setUndecorated(true); // Remove window decorations
                loadingDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border

                // Add a window listener to dispose on close if user tries to manually close
                loadingDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        // Prevent premature closing, or add cancel logic
                        // For now, simply don't allow closing
                    }
                });
                // Make the dialog visible only after all its setup is complete
                loadingDialog.setVisible(true); 
            });

            // Perform the potentially long-running operation
            // The constructor of PasienDetail will fetch data from the DB
            // It might return null if data is not found, which is handled in done()
            return new PasienDetail(pasienId, username);
        }
    }
}
package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.DokterController;
import PBO_4C_SI_KELOMPOK_7.model.Dokter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel; // Import TableColumnModel
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DokterList extends BaseFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private String currentUsername;

    public DokterList(String username) {
        super("Daftar Dokter", username);
        this.currentUsername = username;

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama Dokter", "Spesialisasi", "Jadwal", "Detail"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        table = new JTable(tableModel);
        // Menyesuaikan tinggi baris untuk mengakomodasi jadwal multi-baris
        table.setRowHeight(90); // Menaikkan tinggi baris menjadi 90 piksel

        // Mendapatkan ColumnModel untuk mengatur lebar kolom
        TableColumnModel columnModel = table.getColumnModel();

        // Mengatur lebar preferred untuk kolom "ID" (kolom indeks 0)
        columnModel.getColumn(0).setPreferredWidth(40); // Mengatur lebar kolom ID menjadi 40 piksel
        columnModel.getColumn(0).setMaxWidth(60);   // Batasi lebar maksimum ID
        columnModel.getColumn(0).setMinWidth(30);   // Batasi lebar minimum ID


        // Custom Cell Renderer for "Jadwal" column to display multiline text
        table.getColumn("Jadwal").setCellRenderer(new MultiLineTableCellRenderer());
        
        // Button Renderer and Editor for "Detail" column
        table.getColumn("Detail").setCellRenderer(new ButtonRendererDokter());
        table.getColumn("Detail").setCellEditor(new ButtonEditorDokter(new JCheckBox(), this));

        loadDokter();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Dokter"));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void loadDokter() {
        List<Dokter> dokterList = DokterController.getAllDokter();
        tableModel.setRowCount(0);

        if (!dokterList.isEmpty()) {
            for (Dokter d : dokterList) {
                String jadwalHtml = "<html>";
                if (d.getJadwal().isEmpty()) {
                    jadwalHtml += "Tidak Ada Jadwal";
                } else {
                    for (String s : d.getJadwal()) {
                        jadwalHtml += s + "<br>";
                    }
                }
                jadwalHtml += "</html>";

                tableModel.addRow(new Object[]{
                        d.getId(),
                        d.getNama(),
                        d.getSpesialisasi(),
                        jadwalHtml,
                        "Lihat"
                });
            }
        }
    }
    
    public String getCurrentUsername() {
        return currentUsername;
    }
    
    class MultiLineTableCellRenderer extends JLabel implements TableCellRenderer {
        public MultiLineTableCellRenderer() {
            setOpaque(true);
            setVerticalAlignment(SwingConstants.TOP);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "" : value.toString());
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }
}

class ButtonRendererDokter extends JButton implements TableCellRenderer {
    public ButtonRendererDokter() {
        setText("Lihat");
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        return this;
    }
}

class ButtonEditorDokter extends DefaultCellEditor {
    protected JButton button;
    private boolean clicked;
    private JTable table;
    private DokterList parentFrame;

    public ButtonEditorDokter(JCheckBox checkBox, DokterList parentFrame) {
        super(checkBox);
        this.parentFrame = parentFrame;
        button = new JButton("Lihat");
        button.setOpaque(true);

        button.addActionListener(e -> {
            if (clicked && table != null) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int dokterId = (int) table.getValueAt(row, 0);
                    new DokterDetail(dokterId, parentFrame.getCurrentUsername()).setVisible(true);
                }
            }
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.table = table;
        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        clicked = false;
        return "Lihat";
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }
}
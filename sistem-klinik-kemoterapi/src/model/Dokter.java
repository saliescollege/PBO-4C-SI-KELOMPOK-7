package model;
import javax.swing.*;
import java.awt.*;

public class Dokter extends JFrame {

    public Dokter() {
        setTitle("Daftar Dokter");
        setSize(720, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(new Color(220, 240, 255));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(150, 200, 240));
        header.setPreferredSize(new Dimension(getWidth(), 50));

        JLabel iconLabel = new JLabel("\uD83D\uDC64");
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.add(iconLabel, BorderLayout.WEST);

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        menuPanel.setOpaque(false); 

        JLabel dokterLabel = new JLabel("Dokter");
        JLabel pasienLabel = new JLabel("Pasien");
        JLabel berandaLabel = new JLabel("Beranda");

        Font menuFont = new Font("SansSerif", Font.PLAIN, 14);
        dokterLabel.setFont(menuFont);
        pasienLabel.setFont(menuFont);
        berandaLabel.setFont(menuFont);

        menuPanel.add(dokterLabel);
        menuPanel.add(pasienLabel);
        menuPanel.add(berandaLabel);
        header.add(menuPanel, BorderLayout.EAST);

        JLabel judul = new JLabel("Daftar Dokter");
        judul.setFont(new Font("SansSerif", Font.BOLD, 24));
        judul.setHorizontalAlignment(JLabel.CENTER);
        judul.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(header, BorderLayout.NORTH);
        panelAtas.add(judul, BorderLayout.SOUTH);

        panelUtama.add(panelAtas, BorderLayout.NORTH);

        String[] kolom = {"Nama Dokter", "Penanganan", "Ruangan", "Jadwal", "Detail"};
        Object[][] data = {
                {"dr. Sinta Chaira", "Sp.B (K) Onkologi", "Ruang A1", "Senin, 09.30–11.30", "Lihat"},
                {"dr. Ahmad Yusuf", "Sp.PD", "Ruang B2", "Selasa, 13.00–15.00", "Lihat"},
                {"dr. Lina Hartati", "Sp.KJ", "Ruang C3", "Rabu, 08.00–10.00", "Lihat"},
                {"dr. Riko Santosa", "Sp.THT", "Ruang D1", "Kamis, 10.00–12.00", "Lihat"},
                {"dr. Maya Lestari", "Sp.A", "Ruang E5", "Jumat, 14.00–16.00", "Lihat"},
        };

        JTable table = new JTable(data, kolom) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        table.getColumn("Detail").setCellRenderer(new ButtonRenderer());
        table.getColumn("Detail").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        panelUtama.add(scrollPane, BorderLayout.CENTER);

        add(panelUtama);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dokter::new);
    }
}

class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setText("Lihat");
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private boolean clicked;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton("Lihat");
        button.setOpaque(true);

        button.addActionListener(e -> {
            if (clicked && table != null) {
                int row = table.getSelectedRow();
                String nama = table.getValueAt(row, 0).toString();
                String spesialis = table.getValueAt(row, 1).toString();
                String ruangan = table.getValueAt(row, 2).toString();
                String jadwal = table.getValueAt(row, 3).toString();

                new DetailDokter(nama, spesialis, ruangan, jadwal);
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.table = table;
        clicked = true;
        return button;
    }

    public Object getCellEditorValue() {
        clicked = false;
        return "Lihat";
    }

    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }
}

class DetailDokter extends JFrame {
    public DetailDokter(String nama, String spesialis, String ruangan, String jadwal) {
        setTitle("Detail Dokter");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nama: " + nama));
        panel.add(new JLabel("Spesialis: " + spesialis));
        panel.add(new JLabel("Ruangan: " + ruangan));
        panel.add(new JLabel("Jadwal: " + jadwal));
        panel.add(new JButton("Pasien yang Ditangani"));

        add(panel);
        setVisible(true);
    }
}

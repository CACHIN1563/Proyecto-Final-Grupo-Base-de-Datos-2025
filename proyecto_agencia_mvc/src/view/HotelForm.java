package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Arrays;

import model.hotel;
import dao.hoteldao;

public class HotelForm extends JFrame {

    private final JTextField txtNombre = new JTextField(18);
    private final JTextField txtDireccion = new JTextField(18);
    private final JTextField txtCiudad = new JTextField(18);
    private final JTextField txtTelefono = new JTextField(14);

    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnModificar = new JButton("Modificar");
    private final JButton btnEliminar = new JButton("Eliminar");

    private final JTextField txtBuscar = new JTextField(20);
    private final JTable tabla = new JTable();
    private final DefaultTableModel modelo = new DefaultTableModel(
        new Object[]{"ID", "Nombre", "Direccion", "Ciudad", "Telefono"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c) { return c==0 ? Long.class : String.class; }
    };
    private TableRowSorter<DefaultTableModel> sorter;

    private Long seleccionadoId = null;

    public HotelForm() {
        setTitle("Catalogo de Hoteles");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10,10));
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(true);
        form.setBackground(new Color(245,247,250));
        form.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(new Color(200,205,210), 1, true), "Datos del Hotel",
                        TitledBorder.LEADING, TitledBorder.TOP, form.getFont().deriveFont(Font.BOLD)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,8,6,8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        addRow(form, gc, 0, "Nombre:", txtNombre);
        addRow(form, gc, 1, "Direccion:", txtDireccion);
        addRow(form, gc, 2, "Ciudad:", txtCiudad);
        addRow(form, gc, 3, "Telefono:", txtTelefono);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        stylePrimary(btnGuardar);
        stylePrimary(btnModificar);
        stylePrimary(btnEliminar);
        botones.add(btnGuardar);
        botones.add(btnModificar);
        botones.add(btnEliminar);

        gc.gridx = 0; gc.gridy = 5; gc.gridwidth = 2;
        form.add(botones, gc);

        JPanel buscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buscar.add(new JLabel("Buscar:"));
        buscar.add(txtBuscar);

        tabla.setModel(modelo);
        tabla.setRowHeight(22);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        JScrollPane sp = new JScrollPane(tabla);
        sp.setPreferredSize(new Dimension(600, 260));

        content.add(buscar, BorderLayout.NORTH);
        content.add(sp, BorderLayout.CENTER);
        content.add(form, BorderLayout.EAST);

        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);

        configurarEventos();
        cargarTabla();

        pack();
        setResizable(false);
    }

    private void addRow(JPanel panel, GridBagConstraints gc, int y, String label, JComponent field) {
        gc.gridy = y;

        gc.gridx = 0; gc.weightx = 0;
        JLabel lb = new JLabel(label);
        lb.setForeground(new Color(60,66,72));
        panel.add(lb, gc);

        gc.gridx = 1; gc.weightx = 1;
        if (field instanceof JTextField) styleField((JTextField) field);
        panel.add(field, gc);
    }


    private void styleField(JTextField tf) {
        tf.setMargin(new Insets(4,8,4,8));
        tf.setBorder(new CompoundBorder(
                new LineBorder(new Color(205,210,215), 1, true),
                new EmptyBorder(2,6,2,6)
        ));
        tf.setBackground(Color.WHITE);
    }

    private void stylePrimary(JButton b) {
        b.setFocusPainted(false);
        b.setForeground(Color.BLACK);
        b.setBackground(new Color(224,224,224)); 
        b.setBorder(new EmptyBorder(8,14,8,14));

        b.addChangeListener(e -> {
            if (b.getModel().isRollover())
                b.setBackground(new Color(189,189,189)); 
            else
                b.setBackground(new Color(224,224,224));
        });
    }

    private void configurarEventos() {

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = tabla.getSelectedRow();
            if (row >= 0) {
                int idx = tabla.convertRowIndexToModel(row);
                seleccionadoId = (Long) modelo.getValueAt(idx, 0);
                txtNombre.setText(modelo.getValueAt(idx, 1).toString());
                txtDireccion.setText(modelo.getValueAt(idx, 2).toString());
                txtCiudad.setText(modelo.getValueAt(idx, 3).toString());
                txtTelefono.setText(modelo.getValueAt(idx, 4).toString());

                btnModificar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }
        });

        btnGuardar.addActionListener(e -> guardar());
        btnModificar.addActionListener(e -> {
            if (seleccionadoId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un hotel.");
                return;
            }
            guardar();
        });
        btnEliminar.addActionListener(e -> eliminar());

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });
    }

    private void filtrar() {
        String t = txtBuscar.getText().trim();
        if (t.isEmpty()) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.orFilter(Arrays.asList(
                RowFilter.regexFilter("(?i)" + Pattern.quote(t), 1),
                RowFilter.regexFilter("(?i)" + Pattern.quote(t), 2),
                RowFilter.regexFilter("(?i)" + Pattern.quote(t), 3),
                RowFilter.regexFilter("(?i)" + Pattern.quote(t), 4)
        )));
    }

    private void cargarTabla() {
        try {
            modelo.setRowCount(0);
            List<hotel> lista = new hoteldao().listar();
            for (hotel h : lista) {
                modelo.addRow(new Object[]{
                        h.getid_hotel(),
                        h.getnombre(),
                        h.getdireccion(),
                        h.getciudad(),
                        h.gettelefono(),
                });
            }
            limpiarForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void limpiarForm() {
        seleccionadoId = null;
        txtNombre.setText("");
        txtDireccion.setText("");
        txtCiudad.setText("");
        txtTelefono.setText("");
        tabla.clearSelection();
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void guardar() {
        try {
            hotel h = new hotel();
            h.setnombre(txtNombre.getText().trim());
            h.setdireccion(txtDireccion.getText().trim());
            h.setciudad(txtCiudad.getText().trim());
            h.settelefono(txtTelefono.getText().trim());

            hoteldao dao = new hoteldao();

            if (seleccionadoId == null) {
                dao.insert(h);
                JOptionPane.showMessageDialog(this, "Hotel creado.");
            } else {
                h.setid_hotel(seleccionadoId);
                dao.update(h);
                JOptionPane.showMessageDialog(this, "Hotel actualizado.");
            }

            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el hotel. " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionadoId == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un hotel.");
            return;
        }

        if (JOptionPane.showConfirmDialog(this,"¿Desea eliminar el hotel seleccionada?", "SI", JOptionPane.YES_NO_OPTION)
                != JOptionPane.YES_OPTION) return;

        try {
            new hoteldao().delete(seleccionadoId);
            JOptionPane.showMessageDialog(this, "Hotel eliminado.");
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el hotel: " + ex.getMessage());
        }
    }
}

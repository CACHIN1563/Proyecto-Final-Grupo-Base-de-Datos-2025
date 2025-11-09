package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;
import model.pais;
import dao.paisdao;

public class PaisForm extends JFrame {

    private final JTextField txtNombre = new JTextField(22);
    private final JButton btnGuardar   = new JButton("Guardar");
    private final JButton btnModificar = new JButton("Modificar");
    private final JButton btnEliminar  = new JButton("Eliminar");

    private final JTextField txtBuscar = new JTextField(20);
    private final JTable tabla         = new JTable();
    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c) { return c==0 ? Long.class : String.class; }
    };
    private TableRowSorter<DefaultTableModel> sorter;

    private Long seleccionadoId = null;

    public PaisForm() {
        setTitle("Catálogo de Pais");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10,10));
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(true);
        form.setBackground(new Color(245,247,250));
        form.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(new Color(200,205,210), 1, true), "Datos del pais",
                        TitledBorder.LEADING, TitledBorder.TOP, form.getFont().deriveFont(Font.BOLD)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,8,6,8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        addRow(form, gc, 0, "Nombre del pais:", txtNombre);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        stylePrimary(btnGuardar);
        stylePrimary(btnModificar);
        stylePrimary(btnEliminar);
        botones.add(btnGuardar);
        botones.add(btnModificar);
        botones.add(btnEliminar);

        gc.gridx = 0; gc.gridy = 1; gc.gridwidth = 2; gc.weightx=1;
        form.add(botones, gc);

        JPanel buscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buscar.add(new JLabel("Buscar:"));
        buscar.add(txtBuscar);

        tabla.setModel(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(22);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        JScrollPane sp = new JScrollPane(tabla);
        sp.setPreferredSize(new Dimension(420, 220));

        content.add(buscar, BorderLayout.NORTH); 
        content.add(sp,     BorderLayout.CENTER); 
        content.add(form,   BorderLayout.EAST); 

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
        if (field instanceof JTextField) {
        styleField((JTextField) field);
        }
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
            int viewRow = tabla.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = tabla.convertRowIndexToModel(viewRow);
                Object idObj = modelo.getValueAt(modelRow, 0);
                Object nomObj = modelo.getValueAt(modelRow, 1);
                seleccionadoId = (idObj instanceof Number) ? ((Number) idObj).longValue() : Long.valueOf(idObj.toString());
                txtNombre.setText(nomObj != null ? nomObj.toString() : "");
                btnModificar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }
        });

        btnGuardar.addActionListener(e -> guardar());

        btnModificar.addActionListener(e -> {
            if (seleccionadoId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un país para modificar.");
                return;
            }
            guardar();
        });

        btnEliminar.addActionListener(e -> eliminar());

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrar(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrar(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrar(); }
        });
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto), 1)); // columna 1 = Nombre
        }
    }

    private void cargarTabla() {
        try {
            modelo.setRowCount(0);
            List<pais> lista = new paisdao().listar();
            for (pais p : lista) {
                Long id = p.getid_pais();        
                String nombre = p.getnombre(); 
                modelo.addRow(new Object[]{ id, nombre });
            }
            limpiarForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + ex.getMessage());
        }
    }

    private void limpiarForm() {
        seleccionadoId = null;
        txtNombre.setText("");
        tabla.clearSelection();
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        txtNombre.requestFocus();
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del pais.");
            txtNombre.requestFocus();
            return;
        }
        try {
            paisdao dao = new paisdao();
            pais p = new pais();
            p.setnombre(nombre);  

            if (seleccionadoId == null) {
                dao.insert(p);
                JOptionPane.showMessageDialog(this, "Pais creado con exito.");
            } else {
                p.setid_pais(seleccionadoId); 
                dao.update(p);
                JOptionPane.showMessageDialog(this, "Pais actualizado con exito.");
            }
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionadoId == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un pais para eliminar.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar el pais seleccionado?", "Si", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        try {
            new paisdao().delete(seleccionadoId);
            JOptionPane.showMessageDialog(this, "País eliminado.");
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }
}

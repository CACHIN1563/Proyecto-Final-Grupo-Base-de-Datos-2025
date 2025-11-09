package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

import model.hotel;
import model.tipohabitacion;
import model.tarifahotel;
import dao.hoteldao;
import dao.tipohabitaciondao;
import dao.tarifahoteldao;

public class TarifaHotelForm extends JFrame {

    private final JComboBox<hotel> cbHotel = new JComboBox<>();
    private final JComboBox<tipohabitacion> cbTipoHab = new JComboBox<>();
    private final JComboBox<String> cbRegimen =
            new JComboBox<>(new String[]{"MEDIA_PENSION", "PENSION_COMPLETA"});
    private final JTextField txtPrecio = new JTextField(10);

    private final JButton btnGuardar   = new JButton("Guardar");
    private final JButton btnModificar = new JButton("Modificar");
    private final JButton btnEliminar  = new JButton("Eliminar");

    private final JTextField txtBuscar = new JTextField(20);
    private final JTable tabla         = new JTable();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Hotel", "Tipo Habitación", "Régimen", "Precio/Noche"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c) {
            return c == 0 ? Long.class : (c == 4 ? Double.class : String.class);
        }
    };

    private TableRowSorter<DefaultTableModel> sorter;
    private Long seleccionadoId = null;
    private List<tarifahotel> listaTarifas;

    public TarifaHotelForm() {
        setTitle("Catálogo de Tarifas de Hotel");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10,10));
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(true);
        form.setBackground(new Color(245,247,250));
        form.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(new Color(200,205,210), 1, true),
                        "Datos de Tarifa por Hotel",
                        TitledBorder.LEADING, TitledBorder.TOP,
                        form.getFont().deriveFont(Font.BOLD)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,8,6,8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        int y = 0;
        addRow(form, gc, y++, "Hotel:", cbHotel);
        addRow(form, gc, y++, "Tipo Habitación:", cbTipoHab);
        addRow(form, gc, y++, "Régimen:", cbRegimen);
        addRow(form, gc, y++, "Precio/Noche:", txtPrecio);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        stylePrimary(btnGuardar);
        stylePrimary(btnModificar);
        stylePrimary(btnEliminar);
        botones.add(btnGuardar);
        botones.add(btnModificar);
        botones.add(btnEliminar);

        gc.gridx = 0; gc.gridy = y; gc.gridwidth = 2; gc.weightx = 1;
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
        sp.setPreferredSize(new Dimension(620, 260));

        content.add(buscar, BorderLayout.NORTH);
        content.add(sp,     BorderLayout.CENTER);
        content.add(form,   BorderLayout.EAST);

        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);

        configurarEventos();
        cargarHoteles();
        cargarTipos();
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
        if (field instanceof JTextField tf) {
            styleField(tf);
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
            if (viewRow >= 0 && listaTarifas != null) {
                int modelRow = tabla.convertRowIndexToModel(viewRow);
                tarifahotel th = listaTarifas.get(modelRow);

                seleccionadoId = th.getid_tarifa();
                seleccionarHotel(th.getid_hotel());
                seleccionarTipoHab(th.getid_tipo_hab());
                cbRegimen.setSelectedItem(th.getregimen());
                txtPrecio.setText(th.getprecio_noche() != null ? th.getprecio_noche().toString() : "");

                btnModificar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }
        });

        btnGuardar.addActionListener(e -> guardar());

        btnModificar.addActionListener(e -> {
            if (seleccionadoId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una tarifa para modificar.");
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

    private void cargarHoteles() {
        try {
            cbHotel.removeAllItems();
            hoteldao dao = new hoteldao();
            List<hotel> lista = dao.listar();
            for (hotel h : lista) {
                cbHotel.addItem(h);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando hoteles: " + ex.getMessage());
        }
    }

    private void cargarTipos() {
        try {
            cbTipoHab.removeAllItems();
            tipohabitaciondao dao = new tipohabitaciondao();
            List<tipohabitacion> lista = dao.listar();
            for (tipohabitacion t : lista) {
                cbTipoHab.addItem(t);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando tipos de habitación: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            modelo.setRowCount(0);
            tarifahoteldao dao = new tarifahoteldao();
            listaTarifas = dao.listar();

            for (tarifahotel th : listaTarifas) {
                modelo.addRow(new Object[]{
                        th.getid_tarifa(),
                        th.getNombreHotel(),
                        th.getTipoHabitacion(),
                        th.getregimen(),
                        th.getprecio_noche()
                });
            }
            limpiarForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + ex.getMessage());
        }
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(
                    "(?i)" + Pattern.quote(texto), 1, 2, 3
            ));
        }
    }

    private void limpiarForm() {
        seleccionadoId = null;
        txtPrecio.setText("");
        cbRegimen.setSelectedIndex(0);
        if (cbHotel.getItemCount() > 0) cbHotel.setSelectedIndex(0);
        if (cbTipoHab.getItemCount() > 0) cbTipoHab.setSelectedIndex(0);
        tabla.clearSelection();
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        txtPrecio.requestFocus();
    }

    private void guardar() {
        hotel h = (hotel) cbHotel.getSelectedItem();
        tipohabitacion t = (tipohabitacion) cbTipoHab.getSelectedItem();
        String regimen = (String) cbRegimen.getSelectedItem();
        String precioTxt = txtPrecio.getText().trim();

        if (h == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un hotel.");
            return;
        }
        if (t == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de habitación.");
            return;
        }
        if (precioTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el precio por noche.");
            txtPrecio.requestFocus();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioTxt);
            if (precio <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio inválido.");
            txtPrecio.requestFocus();
            return;
        }

        try {
            tarifahoteldao dao = new tarifahoteldao();
            tarifahotel th = new tarifahotel();

            th.setid_hotel(h.getid_hotel());
            th.setid_tipo_hab(t.getid_tipo_hab());
            th.setregimen(regimen);
            th.setprecio_noche(precio);

            if (seleccionadoId == null) {
                dao.insert(th);
                JOptionPane.showMessageDialog(this, "Tarifa creada con éxito.");
            } else {
                th.setid_tarifa(seleccionadoId);
                dao.update(th);
                JOptionPane.showMessageDialog(this, "Tarifa actualizada con éxito.");
            }

            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionadoId == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una tarifa para eliminar.");
            return;
        }

        int r = JOptionPane.showConfirmDialog(this,
                "¿Deseas eliminar la tarifa seleccionada?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (r != JOptionPane.YES_OPTION) return;

        try {
            new tarifahoteldao().delete(seleccionadoId);
            JOptionPane.showMessageDialog(this, "Tarifa eliminada.");
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void seleccionarHotel(Long idHotel) {
        if (idHotel == null) return;
        for (int i = 0; i < cbHotel.getItemCount(); i++) {
            hotel h = cbHotel.getItemAt(i);
            if (h.getid_hotel().equals(idHotel)) {
                cbHotel.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarTipoHab(Long idTipoHab) {
        if (idTipoHab == null) return;
        for (int i = 0; i < cbTipoHab.getItemCount(); i++) {
            tipohabitacion t = cbTipoHab.getItemAt(i);
            if (t.getid_tipo_hab().equals(idTipoHab)) {
                cbTipoHab.setSelectedIndex(i);
                break;
            }
        }
    }
}

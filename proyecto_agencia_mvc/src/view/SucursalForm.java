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
import model.pais;
import model.sucursal;
import dao.paisdao;
import dao.sucursaldao;

public class SucursalForm extends JFrame {

    private final JTextField txtDireccion = new JTextField(22);
    private final JTextField txtTelefono  = new JTextField(14);
    private final JComboBox<pais> cbPais  = new JComboBox<>();

    private final JButton btnGuardar   = new JButton("Guardar");
    private final JButton btnModificar = new JButton("Modificar");
    private final JButton btnEliminar  = new JButton("Eliminar");

    private final JTextField txtBuscar = new JTextField(20);
    private final JTable tabla = new JTable();
    private final DefaultTableModel modelo = new DefaultTableModel(
        new Object[]{"ID", "Dirección", "Teléfono", "Pais"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c) { return c==0 ? Long.class : String.class; }
    };
    private TableRowSorter<DefaultTableModel> sorter;

    private Long seleccionadoId = null;

    public SucursalForm() {
        setTitle("Catálogo de Sucursal");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10,10));
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(true);
        form.setBackground(new Color(245,247,250));
        form.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(new Color(200,205,210), 1, true), "Datos de la Sucursal",
                        TitledBorder.LEADING, TitledBorder.TOP, form.getFont().deriveFont(Font.BOLD)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,8,6,8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        
        
        int y = 0;
        addRow(form, gc, y++, "Dirección:", txtDireccion);
        addRow(form, gc, y++, "Teléfono:",  txtTelefono);
        addRow(form, gc, y++, "País:",      cbPais);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        stylePrimary(btnGuardar);
        stylePrimary(btnModificar);
        stylePrimary(btnEliminar);
        botones.add(btnGuardar);
        botones.add(btnModificar);
        botones.add(btnEliminar);

        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 2; gc.weightx=1;
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
        sp.setPreferredSize(new Dimension(520, 260));

        content.add(buscar, BorderLayout.NORTH);
        content.add(sp,     BorderLayout.CENTER);
        content.add(form,   BorderLayout.EAST);

        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);

        configurarEventos();
        cargarPaises();
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
    	    int viewRow = tabla.getSelectedRow();
    	    if (viewRow >= 0 && listaSucursales != null) {
    	        int modelRow = tabla.convertRowIndexToModel(viewRow);
    	        sucursal s = listaSucursales.get(modelRow);

    	        seleccionadoId = s.getid_sucursal();
    	        txtDireccion.setText(s.getdireccion());
    	        txtTelefono.setText(s.gettelefono());

    	        seleccionarPaisEnCombo(s.getid_pais()); // 👈 aquí fijamos el país correcto

    	        btnModificar.setEnabled(true);
    	        btnEliminar.setEnabled(true);
    	    }
    	});
    	
        btnGuardar.addActionListener(e -> guardar());

        btnModificar.addActionListener(e -> {
            if (seleccionadoId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una sucursal para modificar.");
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

    private void seleccionarPaisEnCombo(long idPais) {
        for (int i = 0; i < cbPais.getItemCount(); i++) {
            pais p = (pais) cbPais.getItemAt(i);
            if (p.getid_pais() == idPais) {   
                cbPais.setSelectedIndex(i);
                break;
            }
        }
    }

    private void cargarPaises() {
        try {
            cbPais.removeAllItems();
            List<pais> lista = new paisdao().listar();
            for (pais p : lista) {
                cbPais.addItem(p);
            }
            if (cbPais.getItemCount() > 0) {
                cbPais.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando países: " + ex.getMessage());
        }
    }
    
    private void filtrar() {
        String t = txtBuscar.getText().trim();
        if (t.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            RowFilter<DefaultTableModel,Object> rf = RowFilter.orFilter(Arrays.asList(
                RowFilter.regexFilter("(?i)" + Pattern.quote(t), 1),
                RowFilter.regexFilter("(?i)" + Pattern.quote(t), 2),
                RowFilter.regexFilter("(?i)" + Pattern.quote(t), 3)
            ));
            sorter.setRowFilter(rf);
        }
    }

    private List<sucursal> listaSucursales;
    
    private void cargarTabla() {
        try {
            modelo.setRowCount(0);
            sucursaldao dao = new sucursaldao();
            listaSucursales = dao.listar(); 

            for (sucursal s : listaSucursales) {
                Long id   = s.getid_sucursal();
                String dir  = s.getdireccion();
                String tel  = s.gettelefono();
                String pais = s.getNombrePais(); 
                modelo.addRow(new Object[]{ id, dir, tel, pais });
            }

            limpiarForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + ex.getMessage());
        }
    }

    private void limpiarForm() {
        seleccionadoId = null;
        txtDireccion.setText("");
        txtTelefono.setText("");
        tabla.clearSelection();
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void guardar() {
        String direccion = txtDireccion.getText().trim();
        String telefono  = txtTelefono.getText().trim();

        if (direccion.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese la dirección."); return; }

        pais paisSeleccionado = (pais) cbPais.getSelectedItem();
        if (paisSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un país.");
            return;
        }

        
        
        try {
            sucursaldao dao = new sucursaldao();
            sucursal s = new sucursal();
            s.setdireccion(direccion);
            s.settelefono(telefono);
            s.setid_pais(paisSeleccionado.getid_pais());

            if (seleccionadoId == null) {
                dao.insert(s);
                JOptionPane.showMessageDialog(this, "Sucursal creada.");
            } else {
                s.setid_sucursal(seleccionadoId);
                dao.update(s);
                JOptionPane.showMessageDialog(this, "Sucursal actualizada.");
            }
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la sucursal." + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionadoId == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una sucursal para eliminar.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Desea eliminar la sucursal seleccionada?", "SI", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        try {
            new sucursaldao().delete(seleccionadoId);
            JOptionPane.showMessageDialog(this, "Sucursal eliminada.");
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar la sucursal." + ex.getMessage());
        }
    }
}

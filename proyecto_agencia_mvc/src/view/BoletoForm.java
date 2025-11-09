package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.usuario;
import model.vuelo;
import dao.ticketspdao;
import dao.vuelodao;
import dao.reservalistadodao;

public class BoletoForm extends JFrame {
    private final usuario user;

    private final JComboBox<Long> cbReserva = new JComboBox<>();
    private final JComboBox<String> cbClase = new JComboBox<>(new String[]{"TURISTA","PRIMERA"});
    private final JTextField txtVueloId = new JTextField(8);
    private final JTextField txtAsiento = new JTextField(8);
    private final JTextField txtPrecio = new JTextField(10);
    private final JButton btnAgregar = new JButton("Agregar Boleto");
    private final JButton btnModificar = new JButton("Modificar Boleto");
    private final JButton btnEliminar = new JButton("Eliminar Boleto");


    private JTable tablaVuelos;
    private DefaultTableModel modeloVuelos;
    private List<vuelo> listaVuelos = new ArrayList<>();

    public BoletoForm(usuario u) {
        this.user = u;
        setTitle("Boletos por Vuelos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(950, 500);
        setLocationRelativeTo(null);

        JPanel panelIzquierdo = crearPanelVuelos();
        JPanel panelDerecho = crearPanelFormulario();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho);
        splitPane.setDividerLocation(550);
        splitPane.setResizeWeight(0.6);

        add(splitPane, BorderLayout.CENTER);

        cargarCombos();
        cargarTablaVuelos();
        actualizarPrecio(); 
    }

    private JPanel crearPanelVuelos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(
                new TitledBorder("Listado de Vuelos"),
                new EmptyBorder(5, 5, 5, 5)));

        String[] columnas = {
            "No. Vuelo",
            "Fecha de Salida",
            "Pais Origen",
            "Pais Destino",
            "Totales",
            "Turista",
            "Primera"
        };

        modeloVuelos = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVuelos = new JTable(modeloVuelos);
        tablaVuelos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaVuelos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tablaVuelos.getSelectedRow() != -1) {
                    int fila = tablaVuelos.getSelectedRow();
                    Object val = tablaVuelos.getValueAt(fila, 0);
                    if (val != null) {
                        txtVueloId.setText(val.toString());
                        actualizarPrecio();
                    }
                }
            }
        });

        panel.add(new JScrollPane(tablaVuelos), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel f = new JPanel(new GridBagLayout());
        f.setBorder(new CompoundBorder(
                new TitledBorder("Reservación de Boletos"),
                new EmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        int y = 0;
        addRow(f, gc, y++, "Reserva:", cbReserva);
        addRow(f, gc, y++, "No. Vuelo:", txtVueloId);
        addRow(f, gc, y++, "Clase:", cbClase);
        addRow(f, gc, y++, "Asiento (opcional):", txtAsiento);
        addRow(f, gc, y++, "Precio:", txtPrecio);

        txtVueloId.setEditable(false);
        txtVueloId.setBackground(new Color(240, 240, 240));

        txtPrecio.setEditable(false);
        txtPrecio.setFocusable(false);
        txtPrecio.setBackground(new Color(240, 240, 240));

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnAgregar);

        JPanel container = new JPanel(new BorderLayout());
        container.add(f, BorderLayout.CENTER);
        container.add(south, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregar());
        cbClase.addActionListener(e -> actualizarPrecio());

        return container;
    }

    private void actualizarPrecio() {
        try {
            String idVueloTxt = txtVueloId.getText().trim();
            String clase = (String) cbClase.getSelectedItem();

            if (idVueloTxt.isEmpty() || clase == null) {
                txtPrecio.setText("");
                return;
            }

            long idVuelo = Long.parseLong(idVueloTxt);
            vuelo v = buscarVueloPorId(idVuelo);

            if (v == null) {
                txtPrecio.setText("");
                return;
            }

            double precio;
            if ("TURISTA".equals(clase)) {
                precio = v.getPrecioTurista();
            } else if ("PRIMERA".equals(clase)) {
                precio = v.getPrecioPrimera();
            } else {
                txtPrecio.setText("");
                return;
            }

            txtPrecio.setText(String.format("%.2f", precio));
        } catch (NumberFormatException ex) {
            txtPrecio.setText("");
        } catch (Exception ex) {
            txtPrecio.setText("");
        }
    }

    private vuelo buscarVueloPorId(long idVuelo) {
        if (listaVuelos == null) return null;
        for (vuelo v : listaVuelos) {
            if (v.getIdVuelo() == idVuelo) {
                return v;
            }
        }
        return null;
    }

    private void cargarCombos() {
        try {
            List<Long> ids = new reservalistadodao().listarIdsPorTurista(user.getIdTurista());
            cbReserva.removeAllItems();
            for (Long id : ids) {
                cbReserva.addItem(id);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando reservas: " + ex.getMessage());
        }
    }

    private void cargarTablaVuelos() {
        try {
            modeloVuelos.setRowCount(0);
            listaVuelos = new vuelodao().listar();

            for (vuelo v : listaVuelos) {
                Object[] fila = {
                    v.getIdVuelo(),
                    v.getFechaHoraSalida(),
                    v.getNombrePaisOrigen(),
                    v.getNombrePaisDestino(),
                    v.getPlazasTotales(),
                    v.getPlazasTurista(),
                    v.getPlazasPrimera()
                };
                modeloVuelos.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando vuelos: " + e.getMessage());
        }
    }

    private void agregar() {
        try {
            Long idReserva = (Long) cbReserva.getSelectedItem();
            String idVueloTxt = txtVueloId.getText().trim();
            String clase = (String) cbClase.getSelectedItem();
            String asiento = txtAsiento.getText().trim();

            if (idReserva == null || idVueloTxt.isEmpty() || clase == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una reserva, un vuelo y una clase.");
                return;
            }

            long idVuelo = Long.parseLong(idVueloTxt);
            vuelo v = buscarVueloPorId(idVuelo);

            if (v == null) {
                JOptionPane.showMessageDialog(this, "El vuelo seleccionado no existe.");
                return;
            }

            double precio;
            if ("TURISTA".equals(clase)) {
                precio = v.getPrecioTurista();
            } else if ("PRIMERA".equals(clase)) {
                precio = v.getPrecioPrimera();
            } else {
                JOptionPane.showMessageDialog(this, "Clase inválida.");
                return;
            }

            if (asiento.isEmpty()) {
                asiento = null;
            }

            new ticketspdao().agregarTicket(idReserva, idVuelo, clase, asiento, precio);

            JOptionPane.showMessageDialog(this, "Boleto agregado correctamente.");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID de vuelo debe ser numérico.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void addRow(JPanel p, GridBagConstraints gc, int y, String label, JComponent c) {
        gc.gridy = y;
        gc.gridx = 0;
        gc.weightx = 0;
        p.add(new JLabel(label), gc);

        gc.gridx = 1;
        gc.weightx = 1;
        p.add(c, gc);
    }
}


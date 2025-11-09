package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import model.usuario;
import model.hotel;
import model.tipohabitacion;
import dao.hospedajespdao;
import dao.reservalistadodao;
import dao.hoteldao;
import dao.tarifahoteldao;
import dao.tipohabitaciondao;

public class HospedajeForm extends JFrame {

    private final usuario user;

    private final JComboBox<Long> cbReserva = new JComboBox<>();
    private final JComboBox<hotel> cbHotel = new JComboBox<>();
    private final JTextField txtLlegada = new JTextField(10);
    private final JTextField txtSalida = new JTextField(10);
    private final JTextField txtHabitaciones = new JTextField(5);
    private final JTextField txtPrecioTotal = new JTextField(10);
    private final JComboBox<tipohabitacion> cbTipoHab = new JComboBox<>();
    private final JComboBox<String> cbRegimen = new JComboBox<>();
    private Double precioCalculado = null;

    private final JButton btnAgregar = new JButton("Agregar Hospedaje");

    public HospedajeForm(usuario u) {
        this.user = u;
        setTitle("Agregar Hospedaje a Reserva");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new CompoundBorder(
                new TitledBorder("Datos del Hospedaje"),
                new EmptyBorder(10,10,10,10)
        ));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,8,6,8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        int y = 0;
        addRow(form, gc, y++, "Reserva:", cbReserva);
        addRow(form, gc, y++, "Hotel:", cbHotel);
        addRow(form, gc, y++, "Tipo Habitación:", cbTipoHab);
        addRow(form, gc, y++, "Régimen:", cbRegimen);
        addRow(form, gc, y++, "Fecha llegada (yyyy-MM-dd):", txtLlegada);
        addRow(form, gc, y++, "Fecha salida (yyyy-MM-dd):", txtSalida);
        addRow(form, gc, y++, "Habitaciones:", txtHabitaciones);
        addRow(form, gc, y++, "Precio total:", txtPrecioTotal);

        txtPrecioTotal.setEditable(false);              

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        stylePrimary(btnAgregar);
        south.add(btnAgregar);

        add(form, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        cargarCombos();
        cargarCombosRegimen();
        cargarTiposHabitacion();
        configurarEventos();

        btnAgregar.addActionListener(e -> agregarHospedaje());

        pack();
        setResizable(false);
    }

    private void configurarEventos() {
        cbHotel.addActionListener(e -> calcularPrecioTotal());
        cbTipoHab.addActionListener(e -> calcularPrecioTotal());
        cbRegimen.addActionListener(e -> calcularPrecioTotal());

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calcularPrecioTotal(); }
            public void removeUpdate(DocumentEvent e) { calcularPrecioTotal(); }
            public void changedUpdate(DocumentEvent e) { calcularPrecioTotal(); }
        };

        txtLlegada.getDocument().addDocumentListener(dl);
        txtSalida.getDocument().addDocumentListener(dl);
        txtHabitaciones.getDocument().addDocumentListener(dl);
    }

    private void cargarCombos() {
        try {
            cbReserva.removeAllItems();
            for (Long id : new reservalistadodao().listarIdsPorTurista(user.getIdTurista())) {
                cbReserva.addItem(id);
            }

            cbHotel.removeAllItems();
            List<hotel> hoteles = new hoteldao().listar();
            for (hotel h : hoteles) cbHotel.addItem(h);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + ex.getMessage());
        }
    }
    
    private void cargarCombosRegimen() {
        try {
            cbRegimen.removeAllItems();

            Long idTurista = user.getIdTurista();
            Long idReserva = (Long) cbReserva.getSelectedItem();

            if (idTurista == null || idReserva == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una reserva antes de cargar el régimen.");
                return;
            }

            List<String> regimenes = new reservalistadodao().listarRegimenPorTurista(idTurista, idReserva);

            for (String reg : regimenes) {
                cbRegimen.addItem(reg);
            }

            if (cbRegimen.getItemCount() > 0)
                cbRegimen.setSelectedIndex(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando regímenes: " + ex.getMessage());
        }
    }



    private void cargarTiposHabitacion() {
        try {
            cbTipoHab.removeAllItems();
            List<tipohabitacion> lista = new tipohabitaciondao().listar();
            for (tipohabitacion t : lista) {
                cbTipoHab.addItem(t);
            }
            if (cbTipoHab.getItemCount() > 0)
                cbTipoHab.setSelectedIndex(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando tipos de habitación: " + ex.getMessage());
        }
    }

    private void calcularPrecioTotal() {
        try {
            hotel h = (hotel) cbHotel.getSelectedItem();
            tipohabitacion th = (tipohabitacion) cbTipoHab.getSelectedItem();
            String regimen = (String) cbRegimen.getSelectedItem();

            if (h == null || th == null || regimen == null) {
                txtPrecioTotal.setText("");
                return;
            }

            String iniTxt = txtLlegada.getText().trim();
            String finTxt = txtSalida.getText().trim();
            String habTxt = txtHabitaciones.getText().trim();

            if (iniTxt.isEmpty() || finTxt.isEmpty() || habTxt.isEmpty()) {
                txtPrecioTotal.setText("");
                return;
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate ini = LocalDate.parse(iniTxt, fmt);
            LocalDate fin = LocalDate.parse(finTxt, fmt);

            long noches = ChronoUnit.DAYS.between(ini, fin);
            if (noches <= 0) {
                txtPrecioTotal.setText("");
                return;
            }

            int habitaciones = Integer.parseInt(habTxt);
            if (habitaciones <= 0) {
                txtPrecioTotal.setText("");
                return;
            }

            tarifahoteldao tdao = new tarifahoteldao();
            Double precioNoche = tdao.buscarPrecio(
                    h.getid_hotel(),
                    th.getid_tipo_hab(),
                    regimen
            );

            if (precioNoche == null) {
                txtPrecioTotal.setText("");
                return;
            }

            double total = precioNoche * noches * habitaciones;
            precioCalculado = total;
            txtPrecioTotal.setText(String.format("%.2f", total));

        } catch (Exception ex) {
        	precioCalculado = null;
            txtPrecioTotal.setText("");
        }
    }

    
    private void agregarHospedaje() {
        try {
            Long idReserva = (Long) cbReserva.getSelectedItem();
            hotel h = (hotel) cbHotel.getSelectedItem();
            tipohabitacion tipoSel = (tipohabitacion) cbTipoHab.getSelectedItem();
            String regimen = (String) cbRegimen.getSelectedItem();
            if (idReserva == null || h == null) {
                JOptionPane.showMessageDialog(this, "Seleccione reserva y hotel.");
                return;
            }
            if (tipoSel == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un tipo de habitación.");
                return;
            }
            if (regimen == null || regimen.isBlank()) {
                JOptionPane.showMessageDialog(this, "Seleccione un régimen.");
                return;
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fLleg = new Date(sdf.parse(txtLlegada.getText().trim()).getTime());
            Date fSal = new Date(sdf.parse(txtSalida.getText().trim()).getTime());
            int habitaciones = Integer.parseInt(txtHabitaciones.getText().trim());
            
            if (habitaciones <= 0) {
                JOptionPane.showMessageDialog(this, "Debe de ingresar el numero de habitaciones");
                return;
            }

            if (precioCalculado == null) {
                JOptionPane.showMessageDialog(this,
                        "El precio no ha sido calculado");
                return;
            }

            Long idTipoHab = tipoSel.getid_tipo_hab();
            double precio = precioCalculado;

            new hospedajespdao().agregarHospedaje(idReserva, h.getid_hotel(), fLleg, fSal, habitaciones, precio, idTipoHab);
                  

            JOptionPane.showMessageDialog(this, "Hospedaje agregado correctamente.");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void addRow(JPanel p, GridBagConstraints gc, int y, String label, JComponent c) {
        gc.gridy = y;
        gc.gridx = 0; gc.weightx = 0;
        p.add(new JLabel(label), gc);
        gc.gridx = 1; gc.weightx = 1;
        p.add(c, gc);
    }

    private void stylePrimary(JButton b) {
        b.setFocusPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(30,136,229));
        b.setBorder(new EmptyBorder(8,14,8,14));
    }
}

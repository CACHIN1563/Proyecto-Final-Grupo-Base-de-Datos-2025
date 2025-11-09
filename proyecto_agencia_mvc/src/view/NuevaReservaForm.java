package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import dao.reservaspdao;
import dao.sucursaldao;
import model.sucursal;
import model.usuario;
import java.util.List;

public class NuevaReservaForm extends JFrame {
    private final usuario user;

    private final JComboBox<sucursal> cbSucursal = new JComboBox<>();
    private final JComboBox<String> cbRegimen = new JComboBox<>(new String[]{"MEDIA_PENSION","PENSION_COMPLETA"});
    private final JTextField txtObs = new JTextField(20);
    private final JButton btnCrear = new JButton("Crear Reserva");

    public NuevaReservaForm(usuario u) {
        this.user = u; 
        setTitle("Nueva Reserva");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel f = new JPanel(new GridBagLayout());
        f.setBorder(new CompoundBorder(new TitledBorder("Datos de Reserva"), new EmptyBorder(10,10,10,10)));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,8,6,8); gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx=1;

        int y=0;
        addRow(f,gc,y++,"Sucursal:", cbSucursal);
        addRow(f,gc,y++,"Régimen:", cbRegimen);
        addRow(f,gc,y++,"Observaciones:", txtObs);

        JPanel south=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnCrear);

        add(f,BorderLayout.CENTER);
        add(south,BorderLayout.SOUTH);
        pack();

        cargarSucursales();

        btnCrear.addActionListener(e -> crear());
    }

    private void cargarSucursales() {
        try {
            List<sucursal> lista = new sucursaldao().listar();
            for (sucursal s : lista) cbSucursal.addItem(s);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando sucursales: " + ex.getMessage());
        }
    }

    
    private void crear() {
        try {
            sucursal s = (sucursal) cbSucursal.getSelectedItem();
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una sucursal.");
                return;
            }

            Long idSucursal = s.getid_sucursal();
            Long idTurista = user.getIdTurista();
            String regimen = (String) cbRegimen.getSelectedItem();
            String obs = txtObs.getText().trim();

            Long idReserva = new reservaspdao().crearReserva(idSucursal, idTurista, regimen, obs);
            JOptionPane.showMessageDialog(this, "Reserva creada con exito. Numero: " + idReserva);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear reserva: " + ex.getMessage());
        }
    }

    private void addRow(JPanel p, GridBagConstraints gc, int y, String label, JComponent c){
        gc.gridy=y;
        gc.gridx=0; gc.weightx=0;
        p.add(new JLabel(label),gc);
        gc.gridx=1; gc.weightx=1;
        p.add(c,gc);
    }

}

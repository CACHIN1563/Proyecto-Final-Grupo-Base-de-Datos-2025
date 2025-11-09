package view;

import javax.swing.*;
import java.awt.*;
import model.usuario; 

public class AdminMenuView extends JFrame {

    private final usuario user;
    private final JButton btnPais     = new JButton("Catálogo de País");
    private final JButton btnHabitacion    = new JButton("Catálogo Tipo de Habitación");
    private final JButton btnSucursal = new JButton("Sucursal");
    private final JButton btnHotel    = new JButton("Hotel");
    private final JButton btnTarifaHotel    = new JButton("Tarifa del Hotel");
    private final JButton btnHabitacionHotel    = new JButton("Habitaciones por Hotel");
    private final JButton btnVuelo    = new JButton("Vuelo");

    public AdminMenuView(usuario u) {
        this.user = u;

        setTitle("Menú de Administración");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("Menú de Opciones para Administradores", SwingConstants.LEFT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        JPanel grid = new JPanel(new GridLayout(0, 3, 12, 12));
        grid.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));
        grid.setBackground(Color.WHITE);

        Color gris = new Color(224,224,224);
        Color grisHover = new Color(189,189,189);
        for (JButton b : new JButton[]{ btnPais, btnHotel, btnSucursal, btnVuelo, btnHabitacion, btnTarifaHotel,btnHabitacionHotel }) {
            b.setBackground(gris);
            b.setForeground(Color.BLACK);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
            b.addChangeListener(e -> {
                if (b.getModel().isRollover()) b.setBackground(grisHover);
                else b.setBackground(gris);
            });
            grid.add(b);
        }

        btnPais.addActionListener(e -> {
            PaisForm paisForm = new PaisForm();
            paisForm.setLocationRelativeTo(this); 
            paisForm.setVisible(true);
        });
        
        btnHabitacion.addActionListener(e -> {
        	TipoHabitacionForm  TipoHabitacionForm = new TipoHabitacionForm();
        	TipoHabitacionForm .setLocationRelativeTo(this); 
        	TipoHabitacionForm .setVisible(true);
        });

        btnTarifaHotel.addActionListener(e -> {
        	TarifaHotelForm TarifaHotelForm  = new TarifaHotelForm();
        	TarifaHotelForm.setLocationRelativeTo(this);
        	TarifaHotelForm.setVisible(true);
        });
        
        btnHabitacionHotel.addActionListener(e -> {
        	DisponibilidadHabitacionForm DisponibilidadHabitacionForm  = new DisponibilidadHabitacionForm();
        	DisponibilidadHabitacionForm.setLocationRelativeTo(this);
        	DisponibilidadHabitacionForm.setVisible(true);
        });
        
        btnHotel.addActionListener(e -> {
            HotelForm hotelForm = new HotelForm();
            hotelForm.setLocationRelativeTo(this);
            hotelForm.setVisible(true);
        });

        btnSucursal.addActionListener(e -> {
            SucursalForm sucursalForm = new SucursalForm();
            sucursalForm.setLocationRelativeTo(this);
            sucursalForm.setVisible(true);
        });

        btnVuelo.addActionListener(e -> {
            VueloForm vueloForm = new VueloForm();
            vueloForm.setLocationRelativeTo(this);
            vueloForm.setVisible(true);
        });

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);

        pack();
        setSize(780, 280);
    }
}

package view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import model.turista;
import dao.turistadao;

public class TuristaForm extends JFrame {
    private final JTextField n1=new JTextField(10), n2=new JTextField(10), n3=new JTextField(10);
    private final JTextField a1=new JTextField(10), a2=new JTextField(10);
    private final JTextField dir=new JTextField(18), tel1=new JTextField(10), tel2=new JTextField(10);
    private final JTextField email=new JTextField(16);
    private final JTextField idPais=new JTextField(6);

    private final JButton btnGuardar = new JButton("Guardar");
    private final Consumer<Long> onSaved; 

    public TuristaForm(Consumer<Long> onSaved){
        this.onSaved = onSaved;

        setTitle("Registro de Turista");
        setSize(420,280); setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7,2,6,6));

        add(new JLabel("Primer Nombre:")); add(n1);
        add(new JLabel("Segundo Nombre:")); add(n2);
        add(new JLabel("Tercer Nombre:")); add(n3);
        add(new JLabel("Primer Apellido:")); add(a1);
        add(new JLabel("Segundo Apellido:")); add(a2);
        add(new JLabel("Dirección:")); add(dir);
        add(new JLabel("Teléfono 1:")); add(tel1);
        add(new JLabel("Teléfono 2:")); add(tel2);
        add(new JLabel("Email:")); add(email);
        add(new JLabel("ID País:")); add(idPais);
        add(new JLabel()); add(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
    }

    private void guardar(){
        try{
            turista t = new turista();
            t.setNombre1(n1.getText().trim());
            t.setNombre2(n2.getText().trim());
            t.setNombre3(n3.getText().trim());
            t.setApellido1(a1.getText().trim());
            t.setApellido2(a2.getText().trim());
            t.setDireccion(dir.getText().trim());
            t.setTelefono1(tel1.getText().trim());
            t.setTelefono2(tel2.getText().trim());
            t.setEmail(email.getText().trim());
            t.setid_pais(Long.parseLong(idPais.getText().trim()));

            Long id = new turistadao().insertReturningId(t);
            JOptionPane.showMessageDialog(this,"Turista creado. ID: "+id);

            if(onSaved!=null) onSaved.accept(id); // llama al siguiente paso
            dispose();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());
        }
    }
}

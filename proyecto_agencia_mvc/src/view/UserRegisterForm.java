package view;

import javax.swing.*;
import java.awt.*;
import dao.usuariodao;

public class UserRegisterForm extends JFrame {
    private final Long idTurista;

    private final JTextField txtUser = new JTextField(16);
    private final JPasswordField txtPass = new JPasswordField(16);
    private final JPasswordField txtPass2 = new JPasswordField(16);
    private final JButton btnCrear = new JButton("Crear Usuario");

    public UserRegisterForm(Long idTurista){
        this.idTurista = idTurista;

        setTitle("Crear Usuario");
        setSize(360,220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4,2,6,6));

        add(new JLabel("Usuario:")); add(txtUser);
        add(new JLabel("Contraseña:")); add(txtPass);
        add(new JLabel("Confirmar:")); add(txtPass2);
        add(new JLabel()); add(btnCrear);

        btnCrear.addActionListener(e -> crear());
    }

    private void crear(){
        try{
            String user = txtUser.getText().trim();
            String p1 = new String(txtPass.getPassword());
            String p2 = new String(txtPass2.getPassword());
            if(user.isEmpty() || p1.isEmpty()){ JOptionPane.showMessageDialog(this,"Complete todos los campos"); return; }
            if(!p1.equals(p2)){ JOptionPane.showMessageDialog(this,"Las contraseñas no coinciden"); return; }

            new usuariodao().insertCliente(user, p1, idTurista);
            JOptionPane.showMessageDialog(this,"Usuario creado y vinculado al turista "+idTurista);
            dispose();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());
        }
    }
}

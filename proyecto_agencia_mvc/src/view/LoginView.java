package view;
import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class LoginView extends JFrame {
    private JTextField userField = new JTextField();
    private JPasswordField passField = new JPasswordField();
    private JButton btnLogin = new JButton("Ingresar");
    private JButton btnRegister = new JButton("Registrarse");
    private BiConsumer<String,String> onSubmit;
    private Runnable onRegister;

    public LoginView() {
        setTitle("Login");
        setSize(400,210);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4,2));
        add(new JLabel("Usuario:"));
        add(userField);
        add(new JLabel("Contraseña:"));
        add(passField);
        add(btnLogin);
        add(btnRegister);

        btnLogin.addActionListener(e -> {
            if(onSubmit!=null) onSubmit.accept(userField.getText(), new String(passField.getPassword()));
        });
        btnRegister.addActionListener(e -> {
            if(onRegister!=null) onRegister.run();
        });
    }
    public void onSubmit(BiConsumer<String,String> f){ this.onSubmit = f; }
    public void onRegister(Runnable r){ this.onRegister = r; }
}

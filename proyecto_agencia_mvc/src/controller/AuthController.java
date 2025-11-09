package controller;

import service.AuthService;
import view.LoginView;
import view.AdminMenuView;
import view.ClienteMenuView;
import javax.swing.*;
import model.usuario;

public class AuthController {
    public AuthController(LoginView v){
        v.onSubmit((user,pass)->{
            try{
                usuario u = new AuthService().authenticate(user,pass);
                if(u==null){ JOptionPane.showMessageDialog(v,"Credenciales inválidas"); return;}
                v.dispose();
                System.out.println("DEBUG -> role=" + u.getRole() + ", activo=" + u.getActivo());

                if (u.getRole() == 1 && u.getActivo() != null && "SI".equalsIgnoreCase(u.getActivo().trim())) {
                    new AdminMenuView(u).setVisible(true);
                } else {
                    new ClienteMenuView(u).setVisible(true);
                }

            }catch(Exception ex){ JOptionPane.showMessageDialog(v,ex.getMessage()); }
        });

        v.onRegister(() -> RegistroUserTurista.start(v));
    }
}

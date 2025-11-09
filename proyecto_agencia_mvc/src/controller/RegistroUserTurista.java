package controller;

import javax.swing.JFrame;
import view.TuristaForm;
import view.UserRegisterForm;

public class RegistroUserTurista {
    public static void start(JFrame parent){
        TuristaForm tf = new TuristaForm(idTuristaGenerado -> {
            UserRegisterForm uf = new UserRegisterForm(idTuristaGenerado);
            uf.setVisible(true);
        });
        tf.setVisible(true);
    }
}

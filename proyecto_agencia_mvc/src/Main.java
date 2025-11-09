import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import view.LoginView;
import controller.AuthController;

/*public class Main {
    public static void main(String[] args){
        new ClienteMenuView(null).setVisible(true);
    }
}*/

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                UIManager.put("Label.font", UIManager.getFont("Label.font").deriveFont(14f));
                UIManager.put("TextField.font", UIManager.getFont("TextField.font").deriveFont(14f));
                UIManager.put("Button.font", UIManager.getFont("Button.font").deriveFont(14f));
            } catch (Exception ignored) {}

            LoginView v = new LoginView();
            new AuthController(v);
            v.setVisible(true);
        });
    }
}

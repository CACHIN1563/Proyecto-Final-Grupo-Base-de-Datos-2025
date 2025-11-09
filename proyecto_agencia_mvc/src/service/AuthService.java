package service;
import dao.usuariodao;
import model.usuario;

public class AuthService {
	public usuario authenticate(String username, String password) throws Exception {
        return new usuariodao().login(username, password);
    }
}

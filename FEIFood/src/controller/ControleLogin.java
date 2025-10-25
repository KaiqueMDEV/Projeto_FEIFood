package controller;

import dao.Conexao;
import dao.ClienteDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Cliente;
import view.Login;
import view.Logado;

/**
 * Controla a lógica da tela de login.
 */
public class ControleLogin {

    private Login tela1; // A tela de login associada

    public ControleLogin(Login view) {
        this.tela1 = view;
    }
    
    
    public void loginUsuario() {
        Cliente usuario = new Cliente(null, tela1.getTxtUsuario().getText(),tela1.getTxtSenha().getText());
        Conexao conexao = new Conexao();
        try {
            Connection conn = conexao.getConnection();
            ClienteDAO dao = new ClienteDAO(conn);
            ResultSet res = dao.consultar(usuario);
            if(res.next()){
                JOptionPane.showMessageDialog(tela1, "Login Feito", "Aviso",
                                              JOptionPane.INFORMATION_MESSAGE);
                String nome = res.getString("nome");
                String username = res.getString("username");
                String senha = res.getString("senha");
                Logado tela2 = new Logado(new Cliente(nome, username, senha));
                tela2.setVisible(true);
                tela1.setVisible(false);
            }else{
                JOptionPane.showMessageDialog(tela1, "login nao efetuado", "erro", JOptionPane.ERROR_MESSAGE);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(tela1,"Erro de Conexao","Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
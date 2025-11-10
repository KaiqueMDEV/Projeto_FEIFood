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
 * Controller para a tela de Login.
 * Gerecia a autenticação do utilizador.
 */
public class ControleLogin {

    private Login view;

    /**
     * @param view A tela de Login (JFrame) que este controller irá gerir.
     */
    public ControleLogin(Login view) {
        this.view = view;
    }
    
    /**
     * Tenta autenticar o utilizador.
     * Chamado pelo botão "ENTRAR".
     */
    public void loginUsuario() {
        //Cria um cliente 'parcial' apenas com os dados de login
        Cliente usuario = new Cliente(0, null, view.getTxtUsuario().getText(), view.getTxtSenha().getText());
        
        Conexao conexao = new Conexao();
        Connection conn = null;
        try {
            conn = conexao.getConnection();
            ClienteDAO dao = new ClienteDAO(conn);
            ResultSet res = dao.consultar(usuario);
            
            if(res.next()){ //Se o DAO encontrou o utilizador
                JOptionPane.showMessageDialog(view, "Login Feito", "Aviso",
                                              JOptionPane.INFORMATION_MESSAGE);
                //Busca os dados completos do utilizador no ResultSet
                int id = res.getInt("id");
                String nome = res.getString("nome");
                String username = res.getString("username");
                String senha = res.getString("senha");
                
                //Cria o objeto Cliente completo (o "crachá")
                Cliente clienteLogado = new Cliente(id, nome, username, senha);
                
                //Navega para a tela Logado
                Logado telaLogado = new Logado(clienteLogado);
                telaLogado.setVisible(true);
                view.dispose(); //Fecha a tela de login
                
            }else{
                JOptionPane.showMessageDialog(view, "Utilizador ou senha inválidos", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(view,"Erro de Conexão com o Banco.","Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }finally {
            if (conn != null) { //Garante que a conexão seja fechada
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }
}
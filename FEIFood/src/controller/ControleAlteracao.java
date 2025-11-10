package controller;

import dao.ClienteDAO;
import dao.Conexao;
import model.Cliente;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import view.Alteracao;

/**
 * Controla a lógica da tela de alteração de senha.
 * @author Micro
 */
public class ControleAlteracao {
    private Alteracao view;
    private Cliente cliente;
    
    /**
     * Construtor que liga o controller à sua view e ao cliente.
     * @param view A instância da tela de Alteracao.
     * @param cliente O cliente logado que terá a senha alterada.
     */
    public ControleAlteracao(Alteracao view, Cliente cliente){
        this.view = view;
        this.cliente = cliente;
    }
    
    /**
     * Atualiza a senha do cliente no banco de dados.
     */
    public void atualizarSenha(){
       String novaSenha = view.getTxtSenhaNova().getText();
       
       this.cliente.setSenha(novaSenha); //Atualiza o objeto na memória
       
       Conexao conexao = new Conexao();
       Connection conn = null;
       try{
           conn = conexao.getConnection();
           ClienteDAO dao = new ClienteDAO(conn);
           
           dao.atualizar(this.cliente); //Envia o objeto cliente completo para o DAO
           
           JOptionPane.showMessageDialog(view,"Senha atualizada",
                   "Aviso", JOptionPane.INFORMATION_MESSAGE);
           
           view.dispose(); //Fecha a tela
           
       }catch(SQLException e){
           JOptionPane.showMessageDialog(view, "Falha de conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
           e.printStackTrace();
       }
       finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }
}
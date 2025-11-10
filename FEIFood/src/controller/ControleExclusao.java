/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ClienteDAO;
import dao.Conexao;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Cliente;
import view.Exclusao;


/**
 * Controla a lógica da tela de exclusão de conta.
 * @author Micro
 */
public class ControleExclusao {
    private Exclusao view;
    private Cliente cliente;
    
    /**
     * Construtor que liga o controller à sua view e ao cliente.
     * @param view A instância da tela de Exclusao.
     * @param cliente O cliente a ser excluído.
     */
    public ControleExclusao(Exclusao view, Cliente cliente){
        this.view = view;
        this.cliente = cliente;
    }
    
    /**
     * Remove o cliente do banco de dados após confirmação.
     */
    public void remover(){
        int option = JOptionPane.showConfirmDialog(view, "Deseja Realmente excluir o cadastro?",
                "Aviso", JOptionPane.YES_NO_OPTION);
        
        //CORRIGIDO: Verifica se o utilizador clicou em "SIM"
        if(option == JOptionPane.YES_OPTION){
            Conexao conexao = new Conexao();
            Connection conn = null;
            try{
                conn = conexao.getConnection();
                ClienteDAO dao = new ClienteDAO(conn);
                dao.remover(cliente);
                JOptionPane.showMessageDialog(view, "Usuário removido com sucesso!",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
                
                view.dispose(); //Fecha a tela de exclusão
                new view.Login().setVisible(true); //Devolve ao Login
                
            }catch(SQLException e){
                JOptionPane.showMessageDialog(view, "Falha de conexao!", "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        System.err.println("erro ao fechar conexão: " + e.getMessage());
                    }
                }
            }
        }
    }
}
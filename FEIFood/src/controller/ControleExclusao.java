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
 *
 * @author Micro
 */
public class ControleExclusao {
    private Exclusao tela5;
    private Cliente cliente;
    
    public ControleExclusao(Exclusao tela5, Cliente cliente){
        this.tela5 = tela5;
        this.cliente = cliente;
    }
    
    public void remover(){
        int option = JOptionPane.showConfirmDialog(tela5, "Deseja Realmente excluir o cadastro?",
                "Aviso", JOptionPane.YES_NO_OPTION);
        if(option == JOptionPane.YES_OPTION){
            Conexao conexao = new Conexao();
            try{
                Connection conn = conexao.getConnection();
                ClienteDAO dao = new ClienteDAO(conn);
                dao.remover(cliente);
                JOptionPane.showMessageDialog(tela5, "Usuário removido com sucesso!",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }catch(SQLException e){
                JOptionPane.showMessageDialog(tela5, "Falha de conexao!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    
}

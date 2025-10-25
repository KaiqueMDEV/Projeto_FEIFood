/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ClienteDAO;
import dao.Conexao;
import model.Cliente;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import view.Alteracao;

/**
 *
 * @author Micro
 */
public class ControleAlteracao {
    private Alteracao tela6;
    private Cliente cliente;
    
    public ControleAlteracao(Alteracao tela6, Cliente cliente){
        this.tela6 = tela6;
        this.cliente = cliente;
    }
    
    public void atualizar(){
       String usuario = cliente.getUsername();
       String novaSenha = tela6.getTxtSenhaNova().getText();
       Cliente cliente = new Cliente("",usuario, novaSenha);
       Conexao conexao = new Conexao();
       try{
           Connection conn = conexao.getConnection();
           ClienteDAO dao = new ClienteDAO(conn);
           dao.atualizar(cliente);
           JOptionPane.showMessageDialog(tela6,"Senha atualizada",
                   "Aviso", JOptionPane.INFORMATION_MESSAGE);
       }catch(SQLException e){
           JOptionPane.showMessageDialog(tela6, "Falha de conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
       }
    }
    
    
    
}

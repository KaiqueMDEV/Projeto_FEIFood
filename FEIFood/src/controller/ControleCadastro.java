/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.Conexao;
import dao.ClienteDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Cliente;
import view.Cadastro;

/**
 *
 * @author Micro
 */
public class ControleCadastro {
    private Cadastro tela3;
    
    public ControleCadastro(Cadastro tela3){
        this.tela3 = tela3;
    }
    
    public void salvarCliente(){
        String nome = tela3.getTxtNome().getText();
        String usuario = tela3.getTxtUsuario().getText();
        String senha = tela3.getTxtSenha().getText();
        Cliente cliente = new Cliente(nome, usuario, senha);
        
        Conexao conexao = new Conexao();
        try{
            Connection conn = conexao.getConnection();
            ClienteDAO dao = new  ClienteDAO(conn);
            dao.inserir(cliente);
            JOptionPane.showMessageDialog(tela3,"Usuário Cadastrado!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(tela3, "Usuário não Cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
}

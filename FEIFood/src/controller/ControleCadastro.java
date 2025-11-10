/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.Conexao;
import dao.ClienteDAO;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Cliente;
import view.Cadastro;

/**
 * Controla a lógica da tela de cadastro de novos clientes.
 * @author Micro
 */
public class ControleCadastro {
    private Cadastro view;
    
    /**
     * Construtor que liga o controller à sua view.
     * @param view A instância da tela de Cadastro.
     */
    public ControleCadastro(Cadastro view){
        this.view = view;
    }
    
    /**
     * Salva um novo cliente no banco de dados.
     */
    public void salvarCliente(){
        String nome = view.getTxtNome().getText();
        String usuario = view.getTxtUsuario().getText();
        String senha = view.getTxtSenha().getText();
        
        //Usa o construtor de cliente novo (ID 0)
        Cliente cliente = new Cliente(nome, usuario, senha);
        
        Connection conn = null;
        Conexao conexao = new Conexao();
        try{
            conn = conexao.getConnection();
            ClienteDAO dao = new ClienteDAO(conn);
            dao.inserir(cliente);
            JOptionPane.showMessageDialog(view,"Usuário Cadastrado!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            view.dispose(); //Fecha a tela de cadastro após o sucesso
            
        } catch(SQLException ex){
            //Verifica se é um erro de "usuário duplicado"
            if (ex.getSQLState().equals("23505")) { //Código de violação de constraint UNIQUE
                JOptionPane.showMessageDialog(view, "Este nome de usuário já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "Usuário não Cadastrado! Erro de BD.", "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } finally{
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
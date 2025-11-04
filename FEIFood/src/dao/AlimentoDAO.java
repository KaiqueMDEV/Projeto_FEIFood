/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import model.Alimento;

/**
 *
 * @author Micro
 */
public class AlimentoDAO {
    private Connection conn;

    public AlimentoDAO(Connection conn) {
        this.conn = conn;   
    }
    
    /**
     * Busca um alimento no banco de dados pelo nome.
     * @param nomeDoLanche O nome exato do lanche (ex: "X-Burguer")
     * @return um objeto Alimento preenchido, ou null se não encontrar
     * @throws SQLException 
     */
    public Alimento consultarPorNome(String nomeDoLanche) throws SQLException {
        
        //**IMPORTANTE**: Verifique se o nome da sua tabela e colunas estão corretos!
        String sql = "SELECT * FROM tbalimentos WHERE nome = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, nomeDoLanche); //Substitui o "?" pelo nome   
        ResultSet resultado = statement.executeQuery(); // Executa a busca        
        Alimento alimento = null;
        //Se encontrou algo no banco (resultado.next() é true)
        if (resultado.next()) {
            int id = resultado.getInt("id");
            String nome = resultado.getString("nome");
            String descricao = resultado.getString("descricao");
            double preco = resultado.getDouble("preco");
            alimento = new Alimento(id, nome, descricao, preco);
        }
        
        conn.close();
        return alimento;
    }
        public void pesquisarPorNome(String pesquisaLanche, DefaultListModel mod) throws SQLException {
        
        String sql = "SELECT nome FROM tbalimentos WHERE nome ILIKE ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);  
        statement.setString(1, pesquisaLanche + "%");
        ResultSet resultado = statement.executeQuery(); // Executa a busca   
        
        while (resultado.next()){
            mod.addElement(resultado.getString("nome"));
        }
    }
}


package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import model.Cliente;


public class ClienteDAO {

    private Connection conn; // Conexão com o banco de dados

    public ClienteDAO(Connection conn) {
        this.conn = conn;
    }


    public ResultSet consultar(Cliente usuario) throws SQLException {
        // O SQL busca por um registro que tenha o usuario E a senha informados
        String sql = "select * from tbalunos where usuario = ? and senha = ?";       
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getUsuario()); // Pega o usuário do objeto
        statement.setString(2, usuario.getSenha());   // Pega a senha do objeto    
        statement.execute();        
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }


    public void inserir(Cliente usuario) throws SQLException {
        // SQL para inserir os três campos
        String sql = "insert into tbalunos (nome, usuario, senha) values ('"
                + usuario.getNome() + " ', ' "
                + usuario.getUsuario() + " ',' "
                + usuario.getSenha() + " ')";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.execute();
        conn.close();
    }


    public void atualizar(Cliente usuario) throws SQLException {
        // SQL para atualizar a senha com base no nome de usuário
        String sql = "update tbalunos set senha = ? where usuario = ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getSenha());     // A nova senha
        statement.setString(2, usuario.getUsuario()); // O usuário a ser atualizado
        
        statement.execute();
        conn.close(); // Fecha a conexão
    }


    public void remover(Cliente usuario) throws SQLException {
        // SQL para deletar com base no nome de usuário
        String sql = "delete from tbalunos where usuario = ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getUsuario()); // O usuário a ser removido
        
        statement.execute();
        conn.close(); // Fecha a conexão
    }
}
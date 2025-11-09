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
        String sql = "select * from tbusuarios where username = ? and senha = ?";       
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getUsername()); // Pega o usuário do objeto
        statement.setString(2, usuario.getSenha());   // Pega a senha do objeto
        statement.execute();        
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }


    public void inserir(Cliente usuario) throws SQLException {
            String sql = "insert into tbusuarios (nome, username, senha) values (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getUsername());
            statement.setString(3, usuario.getSenha());
            statement.execute();
    }


    public void atualizar(Cliente usuario) throws SQLException {
        // SQL para atualizar a senha com base no nome de usuário
        String sql = "update tbusuarios set senha = ? where username= ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getSenha());     // A nova senha
        statement.setString(2, usuario.getUsername()); // O usuário a ser atualizado
        
        statement.execute();
    }


    public void remover(Cliente usuario) throws SQLException {
        // SQL para deletar com base no nome de usuário
        String sql = "delete from tbusuarios where username = ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getUsername()); // O usuário a ser removido
        
        statement.execute();
    }
}
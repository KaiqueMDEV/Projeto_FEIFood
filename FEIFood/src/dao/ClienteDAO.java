package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import model.Cliente;

/**
 * Data Access Object (DAO) para a entidade Cliente.
 */
public class ClienteDAO {

    private Connection conn; //A conexão é recebida do Controller

    /**
     * Construtor que recebe a conexão ativa.
     * @param conn A conexão SQL.
     */
    public ClienteDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Consulta um utilizador no banco usando username e senha.
     * @param usuario Objeto Cliente contendo o username e a senha para a consulta.
     * @return Um ResultSet com os dados do utilizador, se encontrado.
     * @throws SQLException
     */
    public ResultSet consultar(Cliente usuario) throws SQLException {
        String sql = "select * from tbusuarios where username = ? and senha = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getUsername());
        statement.setString(2, usuario.getSenha());
        statement.execute();        
        ResultSet resultado = statement.getResultSet();
        return resultado;
    }

    /**
     * Insere um novo cliente na tabela 'tbusuarios'.
     * @param usuario O objeto Cliente com nome, username e senha.
     * @throws SQLException
     */
    public void inserir(Cliente usuario) throws SQLException {
        String sql = "insert into tbusuarios (nome, username, senha) values (?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getNome());
        statement.setString(2, usuario.getUsername());
        statement.setString(3, usuario.getSenha());
        statement.execute();
    }

    /**
     * Atualiza a senha de um cliente existente, baseado no username.
     * @param usuario O objeto Cliente com a nova senha e o username.
     * @throws SQLException
     */
    public void atualizar(Cliente usuario) throws SQLException {
        String sql = "update tbusuarios set senha = ? where username = ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getSenha());
        statement.setString(2, usuario.getUsername());
        statement.execute();
    }

    /**
     * Remove um cliente do banco, baseado no username.
     * @param usuario O objeto Cliente a ser removido.
     * @throws SQLException
     */
    public void remover(Cliente usuario) throws SQLException {
        String sql = "delete from tbusuarios where username = ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, usuario.getUsername());
        statement.execute();
    }
}
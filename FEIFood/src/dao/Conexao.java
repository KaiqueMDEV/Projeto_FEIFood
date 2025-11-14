package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe de Conexão Singleton para o banco de dados PostgreSQL.
 * Gerecia a URL, utilizador e senha.
 */
public class Conexao {
    
    /**
     * Tenta estabelecer uma conexão com o banco de dados.
     * @return Um objeto Connection.
     * @throws SQLException Se a conexão falhar.
     */
    public Connection getConnection() throws SQLException{
        Connection conexao = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/feifood_kaique",
                "postgres",
                "fei"
        );
        return conexao;
    }
}
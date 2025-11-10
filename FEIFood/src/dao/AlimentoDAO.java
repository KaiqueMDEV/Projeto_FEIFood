package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import model.Alimento;
import model.Bebida;
import model.Comida;

/**
 * DAO para a entidade Alimento.
 */
public class AlimentoDAO {
    private Connection conn;

    /**
     * Construtor que recebe a conexão ativa.
     * @param conn A conexão SQL.
     */
    public AlimentoDAO(Connection conn) {
        this.conn = conn;   
    }
    
    /**
     * Consulta um alimento pelo nome exato.
     * @param nomeDoLanche O nome exato do produto.
     * @return Um objeto Alimento (Comida ou Bebida) ou null.
     * @throws SQLException
     */
    public Alimento consultarPorNome(String nomeDoLanche) throws SQLException {
        
        String sql = "SELECT * FROM tbalimentos WHERE nome = ?"; //Usa tbalimentos
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, nomeDoLanche); 
        ResultSet resultado = statement.executeQuery();      
        Alimento alimento = null;
        
        if (resultado.next()) {
            
            int id = resultado.getInt("id");
            String nome = resultado.getString("nome");
            String descricao = resultado.getString("descricao");
            double preco = resultado.getDouble("preco"); //Usa a coluna 'preco'
            String tipo = resultado.getString("tipo_alimento");
            
            //Lógica da Fábrica
            if ("COMIDA".equals(tipo)) {
                boolean veggie = resultado.getBoolean("veggie");
                alimento = new Comida(id, nome, descricao, preco, veggie);
            
            } else if ("BEBIDA".equals(tipo)) {
                boolean zerosugar = resultado.getBoolean("zero_sugar");
                double teor_alcoolico = resultado.getDouble("teor_alcoolico");
                alimento = new Bebida(id, nome, descricao, preco, zerosugar, teor_alcoolico);
            }
        }
        
        return alimento;
    }
        
    /**
     * Busca alimentos para a barra de pesquisa (com 'LIKE').
     * @param pesquisaLanche O texto inicial digitado pelo utilizador.
     * @param mod O ListModel da JList (View) a ser preenchido.
     * @throws SQLException
     */
    public void pesquisarPorNome(String pesquisaLanche, DefaultListModel mod) throws SQLException {
        
        String sql = "SELECT nome FROM tbalimentos WHERE nome ILIKE ?"; //ILIKE ignora maiúsculas
        
        PreparedStatement statement = conn.prepareStatement(sql);  
        statement.setString(1, pesquisaLanche + "%"); //O % é o wildcard do LIKE
        ResultSet resultado = statement.executeQuery(); 
        
        while (resultado.next()){
            mod.addElement(resultado.getString("nome"));
        }
    }
}
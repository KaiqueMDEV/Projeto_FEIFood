package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Alimento;
import model.Bebida;
import model.Comida;
import model.Cliente;
import model.Pedido;

/**
 * DAO para a entidade Pedido.
 * Gerecia as tabelas 'tbpedidos' e 'tbalimentos_pedido'.
 */
public class PedidoDAO {
    
    private Connection conn;

    /**
     * Construtor que recebe a conexão ativa.
     * @param conn A conexão SQL.
     */
    public PedidoDAO(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Consulta o histórico de pedidos de um cliente.
     * @param cliente O cliente logado.
     * @return Uma Lista de Pedidos (sem os itens preenchidos).
     * @throws SQLException
     */
    public List<Pedido> consultarPedidosPorCliente(Cliente cliente) throws SQLException {
        
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id, data_hora, valor_total, avaliacao FROM tbpedidos WHERE usuario_id = ? ORDER BY data_hora DESC";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, cliente.getId());
        
        ResultSet resultado = statement.executeQuery();
        
        while (resultado.next()) {
            int id = resultado.getInt("id");
            Timestamp dataHora = resultado.getTimestamp("data_hora");
            double valorTotal = resultado.getDouble("valor_total");
            int avaliacao = resultado.getInt("avaliacao");
            
            Pedido pedido = new Pedido(id, dataHora, valorTotal, avaliacao, cliente);
            pedidos.add(pedido);
        }
        
        return pedidos;
    }
    
    /**
     * Insere o "cabeçalho" do pedido na tabela 'tbpedidos'.
     * @param pedido O objeto Pedido (carrinho).
     * @return O ID (Primary Key) do pedido recém-criado.
     * @throws SQLException
     */
    public int inserirPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO tbpedidos (valor_total, avaliacao, usuario_id) VALUES (?, ?, ?) RETURNING id";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setDouble(1, pedido.getValorTotal());
        statement.setInt(2, 0); //0 = "Não avaliado"
        statement.setInt(3, pedido.getCliente().getId());
        
        ResultSet rs = statement.executeQuery();
        rs.next();
        
        return rs.getInt("id");
    }   
    
    /**
     * Insere os itens do carrinho na tabela 'tbalimentos_pedido'.
     * @param novoPedidoId O ID do pedido-pai.
     * @param itens O Map de Itens e Quantidades.
     * @throws SQLException
     */
    public void inserirItens(int novoPedidoId, Map<Alimento, Integer> itens) throws SQLException {
        String sql = "INSERT INTO tbalimentos_pedido (pedido_id, alimento_id, quantidade) VALUES (?, ?, ?)";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        
        for (Map.Entry<Alimento, Integer> item : itens.entrySet()) {
            statement.setInt(1, novoPedidoId);
            statement.setInt(2, item.getKey().getId());
    
            statement.setInt(3, item.getValue());
            statement.addBatch(); //Adiciona a consulta ao lote
        }
        
        statement.executeBatch(); //Executa todas as consultas de uma vez
    }
    
    /**
     * Consulta os itens de um pedido antigo e preenche o Map do objeto Pedido.
     * @param pedido O objeto Pedido (com ID > 0) a ser preenchido.
     * @throws SQLException
     */
    public void consultarItens(Pedido pedido) throws SQLException {
        String sql = "SELECT pa.quantidade, a.* " +
                     "FROM tbalimentos_pedido pa " +
                     "JOIN tbalimentos a ON pa.alimento_id = a.id " +
                     "WHERE pa.pedido_id = ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, pedido.getId());
        
        ResultSet resultado = statement.executeQuery();
        
        Map<Alimento, Integer> itens = pedido.getItens();
        itens.clear();
        
        while (resultado.next()) {
            int quantidade = resultado.getInt("quantidade");
            
            //Recria o objeto Alimento (Comida ou Bebida)
            int id = resultado.getInt("id");
            String nome = resultado.getString("nome");
            String descricao = resultado.getString("descricao");
            double preco = resultado.getDouble("preco"); //Corrigido de "valor" para "preco"
            String tipo = resultado.getString("tipo_alimento");
            
            Alimento alimento = null;
            if ("COMIDA".equals(tipo)) {
                boolean veggie = resultado.getBoolean("veggie");
                alimento = new Comida(id, nome, descricao, preco, veggie);
            } else if ("BEBIDA".equals(tipo)) {
                boolean zerosugar = resultado.getBoolean("zero_sugar");
                double teor_alcoolico = resultado.getDouble("teor_alcoolico");
                alimento = new Bebida(id, nome, descricao, preco, zerosugar, teor_alcoolico);
            }
            
            if (alimento != null) {
                itens.put(alimento, quantidade);
            }
        }
    }
    
    /**
     * Exclui um pedido completo (itens e cabeçalho) usando transação.
     * @param pedido O Pedido a ser excluído.
     * @throws SQLException
     */
    public void excluirPedido(Pedido pedido) throws SQLException {
        
        String sqlItens = "DELETE FROM tbalimentos_pedido WHERE pedido_id = ?";
        String sqlPedido = "DELETE FROM tbpedidos WHERE id = ?";
        
        try (PreparedStatement stmtItens = this.conn.prepareStatement(sqlItens);
             PreparedStatement stmtPedido = this.conn.prepareStatement(sqlPedido)) {
            
            this.conn.setAutoCommit(false); //Inicia transação manual
            
            stmtItens.setInt(1, pedido.getId());
            stmtItens.executeUpdate(); //1. Deleta os filhos
            
            stmtPedido.setInt(1, pedido.getId());
            stmtPedido.executeUpdate(); //2. Deleta o pai
            
            this.conn.commit(); //Confirma a transação
            
        } catch (SQLException e) {
            this.conn.rollback(); //Desfaz tudo se houver erro
            throw e;
        } finally {
            this.conn.setAutoCommit(true); //Devolve ao modo padrão
        }
    }
    
    /**
     * Salva a nota de avaliação na tabela 'tbpedidos'.
     * @param pedido O Pedido com o ID e a nova Avaliação.
     * @throws SQLException
     */
    public void salvarAvaliacao(Pedido pedido) throws SQLException {
        String sql = "UPDATE tbpedidos SET avaliacao = ? WHERE id = ?";
        
        try (PreparedStatement statement = this.conn.prepareStatement(sql)) {
            statement.setInt(1, pedido.getAvaliacao());
            statement.setInt(2, pedido.getId());
            statement.executeUpdate();
        }
    }
}
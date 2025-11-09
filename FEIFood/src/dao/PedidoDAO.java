/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import model.Cliente;
import model.Pedido;

/**
 *
 * @author Micro
 */
public class PedidoDAO {
    
    private Connection conn;

    public PedidoDAO(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Consulta o histórico de pedidos de um cliente específico.
     * @param cliente O cliente logado.
     * @return Uma lista de Pedidos (sem os itens, apenas o cabeçalho).
     * @throws SQLException 
     */
    public List<Pedido> consultarPedidosPorCliente(Cliente cliente) throws SQLException {
        
        List<Pedido> pedidos = new ArrayList<>();
        
        //Busca na tabela 'pedido' pelo 'usuario_id'
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
    public int inserirPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO tbpedidos (valor_total, avaliacao, usuario_id) VALUES (?, ?, ?) RETURNING id";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setDouble(1, pedido.getValorTotal());
        statement.setInt(2, 0); //0 = não avaliado
        statement.setInt(3, pedido.getCliente().getId()); //id do cliente
        
        ResultSet rs = statement.executeQuery();
        rs.next();
        
        return rs.getInt("id"); //retorna o novo ID gerado
    }   
    public void inserirItens(int novoPedidoId, Map<Alimento, Integer> itens) throws SQLException {
        String sql = "INSERT INTO tbalimentos_pedido (pedido_id, alimento_id, quantidade) VALUES (?, ?, ?)";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        
        for (Map.Entry<Alimento, Integer> item : itens.entrySet()) {
            statement.setInt(1, novoPedidoId);             //ID do pedido
            statement.setInt(2, item.getKey().getId());    //ID do Alimento
            statement.setInt(3, item.getValue());          //quantidade
            
            statement.addBatch(); //usa "lote" para otimizar 
        }
        
        //executa todas as inserções de itens de uma vez
        statement.executeBatch();
    }
    public void consultarItens(Pedido pedido) throws SQLException {
        // SQL que "junta" as tabelas pedido_alimento e tbalimentos
        // para buscar os produtos e as suas quantidades
        String sql = "SELECT pa.quantidade, a.* " +
                     "FROM tbalimentos_pedido pa " +
                     "JOIN tbalimentos a ON pa.alimento_id = a.id " +
                     "WHERE pa.pedido_id = ?";
        
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, pedido.getId()); // Usa o ID do pedido que já temos
        
        ResultSet resultado = statement.executeQuery();
        
        // Limpa o mapa (caso tenha lixo)
        Map<Alimento, Integer> itens = pedido.getItensDoPedido();
        itens.clear();
        
        // Loop sobre todos os itens encontrados no banco
        while (resultado.next()) {
            // Pega a quantidade da tabela 'pedido_alimento'
            int quantidade = resultado.getInt("quantidade");
            
            // Recria o objeto Alimento com os dados da tabela 'tbalimentos'
            int id = resultado.getInt("id");
            String nome = resultado.getString("nome");
            String descricao = resultado.getString("descricao");
            double preco = resultado.getDouble("preco");
            boolean zerosugar = resultado.getBoolean("zero_sugar");
            boolean veggie = resultado.getBoolean("veggie");
            double teor_alcoolico = resultado.getDouble("teor_alcoolico");
            
            boolean alcool = (teor_alcoolico > 0);
            
            Alimento alimento = new Alimento(id, nome, descricao, preco, zerosugar, veggie, alcool);
            
            // Adiciona o alimento e sua quantidade ao Map do pedido!
            itens.put(alimento, quantidade);
        }
    }
    public void excluirPedido(Pedido pedido) throws SQLException {
        
        // A transação é controlada AQUI, dentro do DAO.
        // O Controller não precisa se preocupar com isso.
        
        String sqlItens = "DELETE FROM tbalimentos_pedido WHERE pedido_id = ?";
        String sqlPedido = "DELETE FROM tbpedidos WHERE id = ?";
        
        // Usamos o try-with-resources para garantir que os 'PreparedStatement' fechem.
        // A conexão (this.conn) será fechada pelo Controller.
        try (PreparedStatement stmtItens = this.conn.prepareStatement(sqlItens);
             PreparedStatement stmtPedido = this.conn.prepareStatement(sqlPedido)) {
            
            // 1. Inicia a transação
            this.conn.setAutoCommit(false);
            
            // 2. Prepara a exclusão dos itens (tabela 'pedido_alimento')
            stmtItens.setInt(1, pedido.getId());
            stmtItens.executeUpdate(); // Executa o primeiro delete
            
            // 3. Prepara a exclusão do pedido (tabela 'pedido')
            stmtPedido.setInt(1, pedido.getId());
            stmtPedido.executeUpdate(); // Executa o segundo delete
            
            // 4. Se ambos funcionaram, commita (confirma) a transação
            this.conn.commit();
            
        } catch (SQLException e) {
            // 5. Se ALGO deu errado, faz o rollback (desfaz tudo)
            this.conn.rollback();
            throw e; // Lança o erro para o Controller saber que falhou
        } finally {
            // 6. Devolve a conexão ao modo normal
            this.conn.setAutoCommit(true);
        }
    }
    public void salvarAvaliacao(Pedido pedido) throws SQLException {
        // A sua tabela 'pedido' tem a coluna 'avaliacao'
        // (Não é na 'pedido_alimento', como você mencionou - o que é mais simples!)
        String sql = "UPDATE tbpedidos SET avaliacao = ? WHERE id = ?";
        
        try (PreparedStatement statement = this.conn.prepareStatement(sql)) {
            statement.setInt(1, pedido.getAvaliacao());
            statement.setInt(2, pedido.getId());
            statement.executeUpdate();
        }
        // A conexão será fechada pelo Controller
    }
}

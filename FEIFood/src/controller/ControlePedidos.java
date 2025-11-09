/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.Conexao;
import dao.PedidoDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import model.Cliente;
import model.Pedido;
import view.Logado;
import view.PedidoFinal;
import view.Pedidos;

/**
 *
 * @author Micro
 */
public class ControlePedidos {
    private Pedidos view;
    private Cliente cliente;
    private List<Pedido> pedidosCarregados; // Guarda a lista de pedidos

    public ControlePedidos(Pedidos view, Cliente cliente) {
        this.view = view;
        this.cliente = cliente;
    }
    
    /**
     * Chamado pelo construtor da View para encher a JList
     */
    public void carregarPedidos() {
        DefaultListModel<String> modelLista = new DefaultListModel<>();
        Connection conn = null;
        
        try {
            conn = new Conexao().getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            
            // 1. Busca os pedidos no banco
            this.pedidosCarregados = dao.consultarPedidosPorCliente(this.cliente);
            
            // 2. Formata cada pedido para uma string bonita
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
            
            if (pedidosCarregados.isEmpty()) {
                modelLista.addElement("Você ainda não fez nenhum pedido.");
            } else {
                for (Pedido p : pedidosCarregados) {
                String dataFormatada = sdf.format(p.getDataHora());
                
                // --- O SEU CÓDIGO (QUE ESTÁ PERFEITO) ---
                String textoAvaliacao; 
                int nota = p.getAvaliacao(); 
                
                if (nota > 0) {
                    String estrelas = "★".repeat(nota); 
                    textoAvaliacao = "| " + estrelas;
                } else {
                    textoAvaliacao = "| (Pendente)";
                }
                // --- FIM DO SEU CÓDIGO ---

                // --- A LINHA FINAL QUE USA A VARIÁVEL ---
                String linha = String.format("Pedido #%d | %s | R$ %.2f %s",
                                             p.getId(),
                                             dataFormatada,
                                             p.getValorTotal(),
                                             textoAvaliacao); // <-- E é usada aqui!
                
                modelLista.addElement(linha);
            }
            }
            
            // 3. Define o modelo na JList da View
            view.getjList1().setModel(modelLista);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erro ao buscar histórico de pedidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { /* Ignora */ }
            }
        }
    }
    
    /**
     * Chamado pelo clique duplo na JList.
     * @param selectedIndex O índice do item que o usuário clicou.
     */
    public void abrirPedido(int selectedIndex) {
        // Se a lista estiver vazia, não faz nada
        if (pedidosCarregados == null || pedidosCarregados.isEmpty()) {
            return; 
        }
        
        // 1. Pega o objeto Pedido correspondente ao índice
        Pedido pedidoSelecionado = this.pedidosCarregados.get(selectedIndex);
        
        // 2. Abre a tela PedidoFinal (reutilizando o construtor que já fizemos!)
        // A tela PedidoFinal agora será responsável por carregar os itens deste pedido.
        PedidoFinal telaFinal = new PedidoFinal(this.cliente, pedidoSelecionado);
        
        // 3. Controla a navegação
        telaFinal.setVisible(true);
        this.view.setVisible(false);
    }
    
    // (Lógica para o botão Menu)
    public void voltarMenu() {
        Logado logado = new Logado(this.cliente);
        logado.setVisible(true);
        this.view.setVisible(false);
    }
   
}

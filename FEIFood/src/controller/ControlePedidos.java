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
 * Controla a lógica da tela de histórico de Pedidos.
 * @author Micro
 */
public class ControlePedidos {
    private Pedidos view;
    private Cliente cliente;
    private List<Pedido> listaPedidos;

    /**
     * Construtor que liga o controller à sua view e ao cliente.
     * @param view A instância da tela Pedidos.
     * @param cliente O cliente logado.
     */
    public ControlePedidos(Pedidos view, Cliente cliente) {
        this.view = view;
        this.cliente = cliente;
        
        carregarPedidos(); //Carrega os pedidos assim que a tela é criada
    }
    
    /**
     * Busca o histórico de pedidos no banco e preenche a JList.
     */
    public void carregarPedidos() {
        DefaultListModel<String> modelLista = new DefaultListModel<>();
        Connection conn = null;
        
        try {
            conn = new Conexao().getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            
            this.listaPedidos = dao.consultarPedidosPorCliente(this.cliente); //CORRIGIDO (era consultarPedidosPorCliente)
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
            
            if (listaPedidos.isEmpty()) {
                modelLista.addElement("Você ainda não fez nenhum pedido.");
            } else {
                for (Pedido p : listaPedidos) {
                    String dataFormatada = sdf.format(p.getDataHora());
                    
                    String textoAvaliacao; 
                    int nota = p.getAvaliacao(); 
                    
                    if (nota > 0) {
                        String estrelas = "★".repeat(nota); 
                        textoAvaliacao = "| " + estrelas;
                    } else {
                        textoAvaliacao = "| (Pendente)";
                    }

                    String linha = String.format("Pedido #%d | %s | R$ %.2f %s",
                                                 p.getId(),
                                                 dataFormatada,
                                                 p.getValorTotal(),
                                                 textoAvaliacao);
                
                    modelLista.addElement(linha);
                }
            }
            
            view.getjList1().setModel(modelLista);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erro ao buscar histórico de pedidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    /**
     * Chamado pelo clique duplo na JList.
     * @param selectedIndex O índice do item que o utilizador clicou.
     */
    public void abrirPedido(int selectedIndex) {
        if (listaPedidos == null || listaPedidos.isEmpty() || selectedIndex < 0) {
            return; 
        }
        
        Pedido pedidoSelecionado = this.listaPedidos.get(selectedIndex);
        
        PedidoFinal telaFinal = new PedidoFinal(this.cliente, pedidoSelecionado);
        
        telaFinal.setVisible(true);
        this.view.dispose();
    }
    
    /**
     * Fecha a tela de Pedidos
     */
    public void voltarParaLogado() {
        this.view.dispose();
    }
}
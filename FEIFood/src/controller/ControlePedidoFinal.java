package controller;

import dao.Conexao;
import dao.PedidoDAO;
import java.awt.Menu;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map; // Importe o Map
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel; // Importe o DefaultListModel
import javax.swing.JOptionPane;
import model.Alimento; // Importe o Alimento
import model.Cliente;
import model.Pedido;
import view.Logado; 
import view.PedidoFinal;
import view.Pedidos;

public class ControlePedidoFinal {
    
    private PedidoFinal view;
    private Cliente cliente;
    private Pedido pedido;
    private ButtonGroup grupoAvaliacao;

    public ControlePedidoFinal(PedidoFinal view, Cliente cliente, Pedido pedido) {
        this.view = view;
        this.cliente = cliente;
        this.pedido = pedido;
        
        // O Controller agora chama o seu próprio método para configurar a tela.
        configurarBotoesAvaliacao();
        inicializarTela(); 
    }
    
    /**
     * Verifica se o pedido é NOVO (do carrinho) ou ANTIGO (do banco)
     * e configura a tela de acordo.
     */
    private void inicializarTela() {
        if (pedido.getId() == 0) {
            // FLUXO 1: NOVO (do carrinho)
            popularListaDeItens();
            view.getBtnFinalizar().setEnabled(true);
            view.mostrarPainelAvaliacao(false); // Esconde o painel de avaliação
            
        } else {
            // FLUXO 2: ANTIGO (do banco)
            view.getBtnFinalizar().setText("Pedido Concluído");
            view.getBtnFinalizar().setEnabled(false);
            
            carregarItensDoPedidoAntigo(); 
            
            if (pedido.getAvaliacao() > 0) {
                // Pedido JÁ AVALIADO
                view.mostrarPainelAvaliacao(true);
                view.mostrarAvaliacaoSalva(pedido.getAvaliacao());
            } else {
                // Pedido FINALIZADO, MAS NÃO AVALIADO
                view.mostrarPainelAvaliacao(true); 
            }
        }
    }
    
    public void avaliar(int nota) {
        
        int resposta = JOptionPane.showConfirmDialog(
            view, 
            "Tem certeza que deseja enviar a nota " + nota + "?\nVocê não poderá alterar depois.", 
            "Confirmar Avaliação", 
            JOptionPane.YES_NO_OPTION
        );

        if (resposta != JOptionPane.YES_OPTION) {
            // Se o usuário clicou "Não", o Controller limpa a seleção
            this.grupoAvaliacao.clearSelection(); 
            return;
        }
        
        this.pedido.setAvaliacao(nota);
        
        Connection conn = null;
        try {
            conn = new Conexao().getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            
            dao.salvarAvaliacao(this.pedido);
            
            JOptionPane.showMessageDialog(view, "Obrigado por avaliar!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // O Controller manda a View travar
            view.mostrarAvaliacaoSalva(nota); 
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar avaliação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) {  }
            }
        }
    }
    
    private void configurarBotoesAvaliacao() {
        this.grupoAvaliacao = new ButtonGroup();
        
        // Pega os botões da View e adiciona-os ao grupo lógico
        this.grupoAvaliacao.add(view.getBotaoAvaliar1());
        this.grupoAvaliacao.add(view.getBotaoAvaliar2());
        this.grupoAvaliacao.add(view.getBotaoAvaliar3());
        this.grupoAvaliacao.add(view.getBotaoAvaliar4());
        this.grupoAvaliacao.add(view.getBotaoAvaliar5());
        
        // (Opcional, mas recomendado) Define o "valor" de cada botão
        view.getBotaoAvaliar1().setActionCommand("1");
        view.getBotaoAvaliar2().setActionCommand("2");
        view.getBotaoAvaliar3().setActionCommand("3");
        view.getBotaoAvaliar4().setActionCommand("4");
        view.getBotaoAvaliar5().setActionCommand("5");
    }
    
    /**
     * MÉTODO NOVO (movido da View): Pega os itens do objeto 'pedido'
     * e os insere na JList da View.
     */
    private void popularListaDeItens() {
        DefaultListModel<String> modelLista = new DefaultListModel<>();
        
        // 2. Use o Map do Pedido para preencher o model
        for (Map.Entry<Alimento, Integer> item : pedido.getItensDoPedido().entrySet()) {
            Alimento alimento = item.getKey();
            Integer quantidade = item.getValue();
            
            String linha = String.format("%dx %s (R$ %.2f)", 
                                         quantidade, 
                                         alimento.getNome(), 
                                         alimento.getPreco());
            modelLista.addElement(linha);
        }
        
        modelLista.addElement("----------------------");
        modelLista.addElement(String.format("TOTAL: R$ %.2f", pedido.getValorTotal()));

        // 3. O Controller "empurra" o modelo pronto para a View
        view.getListaPedido().setModel(modelLista); 
    }
    
    
    /**
     * (Apenas para o Fluxo 2) Busca no DAO os itens do pedido antigo
     * e manda a View exibi-los.
     */
    private void carregarItensDoPedidoAntigo() {
        Connection conn = null;
        try {
            conn = new Conexao().getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            
            // 1. Chama o DAO para "encher" o objeto pedido (na memória)
            dao.consultarItens(this.pedido);
            
            // 2. Agora que o pedido está cheio, chama o método local para popular a JList
            popularListaDeItens(); 
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar itens do pedido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { /* Ignora */ }
            }
        }
    }

    /**
     * Chamado pelo botão "FINALIZAR".
     * Salva o carrinho (que está na memória) no banco de dados.
     */
    public void finalizarPedido() {
        
        // (Toda a sua lógica de transação que já estava correta vem aqui...)
        
        if (pedido.getId() > 0) {
            // ... (lógica de "pedido já finalizado") ...
            return;
        }
        
        Connection conn = null;
        try {
            conn = new Conexao().getConnection();
            conn.setAutoCommit(false); 
            PedidoDAO dao = new PedidoDAO(conn);
            
            int novoPedidoId = dao.inserirPedido(pedido);
            pedido.setId(novoPedidoId); 
            dao.inserirItens(novoPedidoId, pedido.getItensDoPedido());
            
            conn.commit(); // SUCESSO!
            
            view.getBtnFinalizar().setText("Pedido Finalizado!");
            view.getBtnFinalizar().setEnabled(false);
            
            JOptionPane.showMessageDialog(view, "Pedido #" + novoPedidoId + " finalizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            new Logado(this.cliente).setVisible(true);
            this.view.setVisible(false);
            
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException e2) { /* Ignora */ }
            JOptionPane.showMessageDialog(view, "Erro grave ao salvar o pedido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    public void excluirPedidoAtual() {
        // Se o pedido for 0, ele nem existe no banco ainda (é um carrinho)
        if (this.pedido.getId() == 0) {
            // Apenas "cancela" o carrinho e volta para o menu
            new Logado(this.cliente).setVisible(true); // Ou Logado, você decide
            this.view.setVisible(false);
            return;
        }
        
        // 1. Confirmação
        int resposta = JOptionPane.showConfirmDialog(
            view, 
            "Tem certeza que deseja excluir o Pedido #" + this.pedido.getId() + "?\nEsta ação não pode ser desfeita.", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );

        if (resposta != JOptionPane.YES_OPTION) {
            return; // Usuário cancelou
        }

        // 2. Tenta executar a exclusão
        Connection conn = null;
        try {
            conn = new Conexao().getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            
            // Chama o mesmo método do DAO
            dao.excluirPedido(this.pedido);
            
            // 3. Sucesso!
            JOptionPane.showMessageDialog(view, "Pedido #" + this.pedido.getId() + " excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // 4. Navega de VOLTA para a lista de pedidos
            // (A lista 'Pedidos' vai recarregar sozinha quando for aberta)
            new Pedidos(this.cliente).setVisible(true);
            this.view.setVisible(false);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao excluir o pedido: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { /* Ignora */ }
            }
        }
    }
    // (Métodos futuros para Excluir, Avaliar, etc.)
}
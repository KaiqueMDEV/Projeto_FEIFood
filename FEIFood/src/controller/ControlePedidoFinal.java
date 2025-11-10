package controller;

import dao.Conexao;
import dao.PedidoDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import model.Alimento;
import model.Cliente;
import model.Pedido;
import view.Logado; 
import view.PedidoFinal;
import view.Pedidos;

/**
 * Controla a lógica da tela de finalização e detalhe do pedido.
 * @author Micro
 */
public class ControlePedidoFinal {
    
    private PedidoFinal view;
    private Cliente cliente;
    private Pedido pedido;
    private ButtonGroup grupoAvaliacao;

    /**
     * Construtor que liga o controller à sua view, cliente e pedido.
     * @param view A instância da tela PedidoFinal.
     * @param cliente O cliente logado.
     * @param pedido O Pedido (pode ser um carrinho novo ou um pedido antigo).
     */
    public ControlePedidoFinal(PedidoFinal view, Cliente cliente, Pedido pedido) {
        this.view = view;
        this.cliente = cliente;
        this.pedido = pedido;
        
        configurarBotoesAvaliacao(); //Agrupa os JRadioButtons
        inicializarTela(); //Decide o que mostrar
    }
    
    /**
     * Verifica se o pedido é NOVO (carrinho) ou ANTIGO (do banco)
     * e configura a tela (botões, avaliação, etc.).
     */
    private void inicializarTela() {
        if (pedido.getId() == 0) { //Se ID = 0, é um carrinho novo
            popularListaItens();
            view.getBtnFinalizar().setEnabled(true);
            view.mostrarPainelAvaliacao(false); //Esconde avaliação
            
        } else { //Se ID > 0, é um pedido antigo
            view.getBtnFinalizar().setText("Pedido Concluído");
            view.getBtnFinalizar().setEnabled(false); //Tranca o botão
            
            carregarItensPedidoAntigo(); //Busca os itens do pedido no banco
            
            if (pedido.getAvaliacao() > 0) { //Se já foi avaliado
                view.mostrarPainelAvaliacao(true);
                view.mostrarAvaliacaoSalva(pedido.getAvaliacao()); //Mostra a nota e trava
            } else { //Se não foi avaliado
                view.mostrarPainelAvaliacao(true); //Mostra os botões para avaliar
            }
        }
    }
    
    /**
     * Chamado quando o utilizador clica num dos botões de avaliação (1-5).
     * @param nota A nota (1-5) que o utilizador selecionou.
     */
    public void avaliar(int nota) {
        
        int resposta = JOptionPane.showConfirmDialog(
            view, 
            "Tem certeza que deseja enviar a nota " + nota + "?\nVocê não poderá alterar depois.", 
            "Confirmar Avaliação", 
            JOptionPane.YES_NO_OPTION
        );

        if (resposta != JOptionPane.YES_OPTION) {
            this.grupoAvaliacao.clearSelection(); //Limpa a seleção se o utilizador clicar "Não"
            return;
        }
        
        this.pedido.setAvaliacao(nota); //Define a nota no objeto
        
        Connection conn = null;
        try {
            conn = new Conexao().getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            
            dao.salvarAvaliacao(this.pedido); //CORRIGIDO (era salvarAvaliacao)
            
            JOptionPane.showMessageDialog(view, "Obrigado por avaliar!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            view.mostrarAvaliacaoSalva(nota); //Trava os botões na View
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar avaliação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    //Configura a lógica do ButtonGroup para os JRadioButtons
    private void configurarBotoesAvaliacao() {
        this.grupoAvaliacao = new ButtonGroup();
        this.grupoAvaliacao.add(view.getBotaoAvaliar1());
        this.grupoAvaliacao.add(view.getBotaoAvaliar2());
        this.grupoAvaliacao.add(view.getBotaoAvaliar3());
        this.grupoAvaliacao.add(view.getBotaoAvaliar4());
        this.grupoAvaliacao.add(view.getBotaoAvaliar5());
        
        view.getBotaoAvaliar1().setActionCommand("1");
        view.getBotaoAvaliar2().setActionCommand("2");
        view.getBotaoAvaliar3().setActionCommand("3");
        view.getBotaoAvaliar4().setActionCommand("4");
        view.getBotaoAvaliar5().setActionCommand("5");
    }
    
    //Preenche a JList da View com os itens do objeto Pedido
    private void popularListaItens() {
        DefaultListModel<String> modelLista = new DefaultListModel<>();
        
        for (Map.Entry<Alimento, Integer> item : pedido.getItens().entrySet()) { //CORRIGIDO (era getItensDoPedido)
            Alimento alimento = item.getKey();
            Integer qtd = item.getValue(); //Abreviado
            
            String linha = String.format("%dx %s (R$ %.2f)", 
                                         qtd, 
                                         alimento.getNome(), 
                                         alimento.getPreco());
            modelLista.addElement(linha);
        }
        
        modelLista.addElement("----------------------");
        modelLista.addElement(String.format("TOTAL: R$ %.2f", pedido.getValorTotal()));

        view.getListaPedido().setModel(modelLista); 
    }
    
    //Busca os itens de um pedido antigo no banco
    private void carregarItensPedidoAntigo() {
        Connection conn = null;
        try {
            conn = new Conexao().getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            
            dao.consultarItens(this.pedido); //CORRIGIDO (era consultarItens)
            
            popularListaItens(); //Atualiza a JList
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar itens do pedido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Salva o carrinho (que está na memória) no banco de dados.
     */
    public void finalizarPedido() {
        
        if (pedido.getId() > 0) { //Se já tem ID, já foi finalizado
            JOptionPane.showMessageDialog(view, "Este pedido já foi finalizado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection conn = null;
        try {
            conn = new Conexao().getConnection();
            conn.setAutoCommit(false); //Inicia transação
            PedidoDAO dao = new PedidoDAO(conn);
            
            int novoPedidoId = dao.inserirPedido(pedido); //CORRIGIDO (era inserirPedido)
            pedido.setId(novoPedidoId); //Atualiza o ID no objeto
            dao.inserirItens(novoPedidoId, pedido.getItens()); //CORRIGIDO (era inserirItens e getItensDoPedido)
            
            conn.commit(); //Confirma a transação
            
            view.getBtnFinalizar().setText("Pedido Finalizado!");
            view.getBtnFinalizar().setEnabled(false);
            
            JOptionPane.showMessageDialog(view, "Pedido #" + novoPedidoId + " finalizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            new Logado(this.cliente).setVisible(true);
            this.view.dispose(); //Navegação corrigida
            
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException e2) { e.printStackTrace(); } //Desfaz tudo
            JOptionPane.showMessageDialog(view, "Erro grave ao salvar o pedido: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
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
    
    /**
     * Exclui o pedido atual (seja ele um carrinho ou um pedido do banco).
     */
    public void excluirPedido() {
        if (this.pedido.getId() == 0) { //Se é um carrinho (ID 0)
            new Logado(this.cliente).setVisible(true);
            this.view.dispose(); //Apenas fecha a tela
            return;
        }
        
        int resposta = JOptionPane.showConfirmDialog(
            view, 
            "Tem certeza que deseja excluir o Pedido #" + this.pedido.getId() + "?\nEsta ação não pode ser desfeita.", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        Connection conn = null;
        try {
            conn = new Conexao().getConnection();
            PedidoDAO dao = new PedidoDAO(conn);
            
            dao.excluirPedido(this.pedido); //CORRIGIDO (era excluirPedido)
            
            JOptionPane.showMessageDialog(view, "Pedido #" + this.pedido.getId() + " excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            new Pedidos(this.cliente).setVisible(true);
            this.view.dispose(); //Volta para a lista de pedidos
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao excluir o pedido: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}
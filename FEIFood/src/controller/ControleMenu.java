package controller;

import dao.AlimentoDAO;
import dao.Conexao;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import model.Alimento;
import model.Bebida;
import model.Cliente;
import model.Comida;
import model.Pedido;
import view.Cardapio;
import view.PedidoFinal;

/**
 * Controla a lógica da tela principal do Cardápio.
 * @author Micro
 */
public class ControleMenu {
    
    private Cardapio view;
    private Cliente cliente;
    private Pedido pedidoAtual;
    private Alimento itemEmDestaque;

    /**
     * Construtor que liga o controller à view e ao cliente.
     * @param view A instância da tela Cardapio.
     * @param cliente O cliente logado.
     */
    public ControleMenu(Cardapio view, Cliente cliente) {
        this.view = view;
        this.cliente = cliente;
        this.pedidoAtual = new Pedido(this.cliente); //Cria um novo carrinho vazio
    }
    
    /**
     * Busca os detalhes de um alimento no banco e atualiza o painel de destaque.
     * @param nomeAlimento O nome do item que o utilizador clicou.
     */
    public void exibirDetalhes(String nomeAlimento) {
        
        Alimento alimento = null;
        Connection conn = null;
        try {
            Conexao conexao = new Conexao();
            conn = conexao.getConnection();
            AlimentoDAO dao = new AlimentoDAO(conn);            
            alimento = dao.consultarPorNome(nomeAlimento); //Busca o Alimento (Comida ou Bebida)
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erro ao conectar ao banco de dados!\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        
        if (alimento != null) {
            view.getPainelTituloAlimento().setText(alimento.getNome());
            view.getPainelDescAlimento().setText(alimento.getDescricao());
            view.getPainelPrecoAlimento().setText("R$: " + String.valueOf(alimento.getPreco()));
            
            //Lógica dos ícones usando instanceof
            if (alimento instanceof Comida) {
                Comida comida = (Comida) alimento;
                view.getIconeVeggie().setVisible(comida.isVeggie());
                view.getIconeAlcool().setVisible(false);
                view.getIconeZero().setVisible(false);
                
            } else if (alimento instanceof Bebida) {
                Bebida bebida = (Bebida) alimento;
                view.getIconeVeggie().setVisible(false);
                view.getIconeAlcool().setVisible(bebida.getTeor_alcoolico() > 0);
                view.getIconeZero().setVisible(bebida.isZerosugar());
            }

            //Lógica de carregar a imagem de destaque local
            String caminhoImagem = "";
            caminhoImagem = switch (nomeAlimento) {
                //(O seu switch case completo com os caminhos das imagens)
                case "X-Burger" -> "/resources/xburger_destaque.png";
                case "X-Salada" -> "/resources/xsalada_destaque.png";
                case "X-Frango" -> "/resources/xfrango_destaque.png";
                case "X-Bacon Duplo" -> "/resources/xbacon_destaque.png";
                case "Pizza de Calabresa" -> "/resources/pcalabresa_destaque.png";
                case "Pizza Quatro Queijos" -> "/resources/p4queijos_destaque.png";
                case "Pizza de Frango com Catupiry" -> "/resources/fcatupiry_destaque.png";
                case "Pizza Portuguesa" -> "/resources/portuguesa_destaque.png";
                case "Pizza de Brócolis com Alho" -> "/resources/brocolis_destaque.png";
                case "Batata Frita Pequena" -> "/resources/batatafrita_destaque.png";
                case "Batata Frita Grande" -> "/resources/batatafritag_destaque.png";
                case "Coca-Cola" -> "/resources/cocacola_destaque.png";
                case "Coca-Cola Zero" -> "/resources/cocacolazero_destaque.png";
                case "Guaraná Antarctica" -> "/resources/guarana_destaque.png";
                case "Suco de Laranja Natural" -> "/resources/sucolaranja_destaque.png"; 
                case "Cerveja Long Neck" -> "/resources/cerveja_destaque.png";
                default -> "";
            }; 
            
            try {
                Icon imagemDestaque = new ImageIcon(getClass().getResource(caminhoImagem));
                view.getPainelDestaqueAlimento().setIcon(imagemDestaque);
                view.getPainelDestaqueAlimento().setText("");
            } catch (Exception e) {
                view.getPainelDestaqueAlimento().setIcon(null);
                view.getPainelDestaqueAlimento().setText("Imagem não encontrada");
                System.err.println("Erro ao carregar imagem: " + caminhoImagem);
            }
            
        } else {
            JOptionPane.showMessageDialog(view, "Alimento '" + nomeAlimento + "' não encontrado no cadastro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        this.itemEmDestaque = alimento; //Guarda o item na vitrine
    }
    
    /**
     * Atualiza a lista de pesquisa (JList) com base no que o utilizador digita.
     * @param mod O DefaultListModel da JList da View.
     */
    public void pesquisar(DefaultListModel mod){
        mod.removeAllElements(); //Limpa resultados anteriores
        
        String textoPesquisa = view.getBarraPesquisa().getText();
        
        if (textoPesquisa.equals("") || textoPesquisa.equals("Pesquisar...")) {
            view.getPopUpPesquisa().setVisible(false);
        }else{
            view.getPopUpPesquisa().show(view.getBarraPesquisa(), 0, view.getBarraPesquisa().getHeight());
            Connection conn = null;
            try{
                Conexao conexao = new Conexao();
                conn = conexao.getConnection();
                AlimentoDAO dao = new AlimentoDAO(conn); 
                dao.pesquisarPorNome(textoPesquisa, mod);
          
            }catch(Exception e){
                System.out.println(e.getMessage());
            }finally{
                if(conn != null){
                    try{
                        conn.close();
                    }catch(SQLException e){
                        System.err.println("Erro ao fechar conexao: "+ e.getMessage());
                    }
                }
            }
        }
    }
    
    /**
     * Adiciona o item atualmente em destaque ao carrinho (Pedido).
     */
    public void addItemAoPedido() {
        if (this.itemEmDestaque != null) {
            this.pedidoAtual.addItem(this.itemEmDestaque); //CORRIGIDO (era adicionarItemAoPedido)
            String nome = this.itemEmDestaque.getNome();
            JOptionPane.showMessageDialog(view, nome + " foi adicionado ao seu pedido!", "Item Adicionado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, clique em um item do cardápio primeiro.", "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Navega para a tela de finalização de pedido.
     */
    public void finalizarPedido() {
        if (this.pedidoAtual.getItens().isEmpty()) { //CORRIGIDO (era getItensDoPedido)
            JOptionPane.showMessageDialog(this.view, "Seu carrinho está vazio!", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            PedidoFinal telaFinal = new PedidoFinal(this.cliente, this.pedidoAtual);
            telaFinal.setVisible(true);
            this.view.dispose(); //Navegação corrigida
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import dao.AlimentoDAO;
import dao.Conexao;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.Alimento;
import model.Cliente;
import view.Menu;

/**
 *
 * @author Micro
 */
public class ControleMenu {
    
    private Menu view; // A tela que este cérebro controla
    private Cliente cliente; // O usuário que está logado

    // Construtor: Recebe a tela e o usuário logado
    public ControleMenu(Menu view, Cliente cliente) {
        this.view = view;
        this.cliente = cliente;
    }
    
    /**
     * O método principal: busca dados no banco e atualiza a tela
     * @param nomeDoLanche O nome do lanche que o usuário clicou (ex: "X-Burguer")
     */
    public void exibirDetalhes(String nomeDoLanche) {
        
        Alimento alimento = null;
        try {
            Conexao conexao = new Conexao();
            Connection conn = conexao.getConnection();
            AlimentoDAO dao = new AlimentoDAO(conn);            
            // Pede ao DAO para consultar o lanche pelo nome
            alimento = dao.consultarPorNome(nomeDoLanche);
            
        } catch (SQLException e) {
            e.printStackTrace(); // Mostra o erro real no console
            JOptionPane.showMessageDialog(view, "Erro ao conectar ao banco de dados!\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return; // Para a execução se deu erro
        }
        if (alimento != null) {
            
            // Atualiza os textos com os dados que vieram do banco
            view.getPainelTituloAlimento().setText(alimento.getNome());
            view.getPainelDescAlimento().setText(alimento.getDescricao());
            view.getPainelPrecoAlimento().setText("R$: " + String.valueOf(alimento.getPreco()));

            String caminhoImagem = "";
            
            caminhoImagem = switch (nomeDoLanche) {
                case "X-Burger" -> "/resources/xburger_destaque.png";
                case "X-Salada" -> "/resources/xsalada_destaque.png";
                case "X-Frango" -> "/resources/xfrango_destaque.png";
                case "X-Bacon Duplo" -> "/resources/xbacon_destaque.png";
                case "Pizza de Calabresa" -> "/resources/pcalabresa_destaque.png";
                case "Pizza Quatro Queijos" -> "/resources/p4queijos_destaque.png";
                default -> "";
            }; 
            
            try {
                Icon imagemDestaque = new ImageIcon(getClass().getResource(caminhoImagem));
                view.getPainelDestaqueAlimento().setIcon(imagemDestaque);
                view.getPainelDestaqueAlimento().setText(""); // Limpa o texto (caso tenha dado erro antes)
            } catch (Exception e) {
                // Se não achar o arquivo de imagem
                view.getPainelDestaqueAlimento().setIcon(null); // Remove a imagem anterior
                view.getPainelDestaqueAlimento().setText("Imagem não encontrada");
                System.err.println("Erro ao carregar imagem: " + caminhoImagem);
            }
            
        } else {
            JOptionPane.showMessageDialog(view, "Alimento '" + nomeDoLanche + "' não encontrado no cadastro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa um Pedido (carrinho de compras).
 * Guarda o cliente e um Mapa de Alimentos com as suas quantidades.
 * @author Micro
 */
public class Pedido {

    private Cliente cliente;
    private Map<Alimento, Integer> itens; //Mapeia um Alimento à sua quantidade
    
    private int id;
    private double valorTotal;
    private Timestamp dataHora;
    private int avaliacao;

    /**
     * Construtor para um NOVO carrinho de compras (em memória).
     * @param cliente O cliente que está logado.
     */
    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.itens = new HashMap<>(); //Usa o nome abreviado 'itens'
        this.valorTotal = 0.0;
        this.dataHora = null;
        this.avaliacao = 0; //0 = pendente
    }
    
    /**
     * Construtor para carregar um PEDIDO ANTIGO (do banco).
     * @param id O ID do pedido.
     * @param dataHora A data/hora da compra.
     * @param valorTotal O valor total já calculado.
     * @param avaliacao A nota (0-5) dada pelo cliente.
     * @param cliente O cliente dono do pedido.
     */
    public Pedido(int id, Timestamp dataHora, double valorTotal, int avaliacao, Cliente cliente) {
        this.id = id;
        this.dataHora = dataHora;
        this.valorTotal = valorTotal;
        this.avaliacao = avaliacao;
        this.cliente = cliente;
        this.itens = new HashMap<>(); //Usa o nome abreviado 'itens'
    }
    
    /**
     * Adiciona um item ao carrinho (Map).
     * Se o item já existe, apenas incrementa a quantidade (qtd).
     * Calcula o imposto se o item for uma Bebida.
     * @param alimento O Alimento (Comida ou Bebida) a ser adicionado.
     */
    public void addItem(Alimento alimento) {
        int qtdAtual = this.itens.getOrDefault(alimento, 0); //qtd
        this.itens.put(alimento, qtdAtual + 1);
        
        double precoBase = alimento.getPreco();
        double precoFinal = precoBase;
        
        //Lógica de Imposto
        if (alimento instanceof Bebida) {
            Bebida bebida = (Bebida) alimento;
            precoFinal += bebida.calcularImposto(precoBase); //Calcula imposto
        }
        
        this.valorTotal += precoFinal;
    }
    
    // --- Getters e Setters ---
    
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Retorna o mapa de itens do pedido.
     * @return Um Map onde a Chave é o Alimento e o Valor é a Quantidade (Integer).
     */
    public Map<Alimento, Integer> getItens() {
        return itens;
    }

    public double getValorTotal() {
        return valorTotal;
    }
    
    public int getId() {
        return id;
    }
    
    public Timestamp getDataHora() {
        return dataHora;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    /**
     * Define o ID deste pedido (usado após salvar no banco).
     * @param id O novo ID vindo do banco.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Define a nota da avaliação para este pedido.
     * @param avaliacao A nota de 1 a 5.
     */
    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }
}
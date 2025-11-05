/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Micro
 */
public class Pedido {

    private Cliente cliente; //dono do pedido
    
    //{Chave=Alimento, Valor=Quantidade}
    private Map<Alimento, Integer> itensDoPedido; 
    
    // (Também guardamos os dados da tabela 'pedido' do seu SQL)
    private int id;
    private double valorTotal;
    // ... (outros campos como data, avaliacao, etc. podem vir depois)

    /**
     * Construtor: Cria um novo carrinho de compras vazio para um cliente.
     * @param cliente O cliente que está logado.
     */
    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.itensDoPedido = new HashMap<>(); // Cria o mapa vazio
        this.valorTotal = 0.0;
    }
    
    /**
     * Adiciona um item ao carrinho (Map).
     * Se o item não existe, adiciona com quantidade 1.
     * Se o item JÁ existe, apenas incrementa a quantidade.
     * @param alimento O Alimento a ser adicionado.
     */
    public void adicionarItem(Alimento alimento) {
        // Usa o 'getOrDefault' para pegar a qtd atual (ou 0 se for novo)
        int quantidadeAtual = this.itensDoPedido.getOrDefault(alimento, 0);
        
        // Coloca o alimento no mapa com a quantidade + 1
        this.itensDoPedido.put(alimento, quantidadeAtual + 1);
        
        // Atualiza o valor total (isto é uma lógica simples, podemos melhorar depois)
        this.valorTotal += alimento.getPreco();
        
        // (Debug) Mostra no console o que está no carrinho
        System.out.println("Item adicionado: " + alimento.getNome());
        System.out.println("Itens no carrinho: " + this.itensDoPedido.size());
        System.out.println("Valor total: " + this.valorTotal);
    }
    
    // --- Getters e Setters ---
    
    public Cliente getCliente() {
        return cliente;
    }

    public Map<Alimento, Integer> getItensDoPedido() {
        return itensDoPedido;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}
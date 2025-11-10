/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import model.Cliente;
import view.Logado;

/**
 * Controla a tela principal "Logado".
 * O seu principal trabalho é guardar o objeto Cliente
 * que acabou de fazer login.
 * @author Micro
 */
public class ControleLogado {
    
    private Logado view;
    private Cliente cliente;
    
    /**
     * Construtor que liga o controller à sua view e ao cliente logado.
     * @param view A instância da tela Logado.
     * @param cliente O objeto Cliente que fez o login.
     */
    public ControleLogado(Logado view, Cliente cliente){
        this.view = view;
        this.cliente = cliente;
    }
    
    // (Este controller não precisa de métodos por agora,
    // pois as Views filhas (Perfil, Cardapio, etc.)
    // são chamadas diretamente pela View 'Logado'
    // e recebem o 'cliente' no seu construtor.)
}
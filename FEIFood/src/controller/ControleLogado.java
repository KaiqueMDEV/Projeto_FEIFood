/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import model.Cliente;
import view.Logado;

/**
 *
 * @author Micro
 * 
 * Controla a tela "Logado", repassando o objeto Usuario
 * para as telas de Alteração ou Exclusão.
 * Código corrigido e funcional.
 */
public class ControleLogado {
    private Logado tela4;
    private Cliente cliente;
    
    public ControleLogado(Logado tela4, Cliente cliente){
        this.tela4 = tela4;
        this.cliente = cliente;
    }
    
    
    public Cliente chamarAlteracao(){
        return cliente;
    }
    
    public Cliente chamarExclusao(){
        return cliente;
    }
   
    
}

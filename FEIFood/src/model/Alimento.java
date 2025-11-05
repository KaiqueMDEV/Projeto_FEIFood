                                                                    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Micro
 */
public class Alimento {
    
    private int id;
    private String nome;
    private String descricao;
    private String sabor;
    private double preco;
    private boolean zerosugar;
    private boolean veggie;
    private boolean alcool;
    

    public Alimento(int id, String nome, String descricao, double preco, boolean zerosugar, boolean veggie, boolean alcool) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.zerosugar = zerosugar;
        this.veggie = veggie;
        this.alcool = alcool;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public String getSabor() {
        return sabor;
    }

    public boolean isZerosugar() {
        return zerosugar;
    }

    public boolean isVeggie() {
        return veggie;
    }

    public boolean isAlcool() {
        return alcool;
    }
    
    
}

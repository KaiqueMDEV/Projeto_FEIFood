/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Micro
 */
public class Cliente {
    private String nome,username,senha;
    private int id;
    
    public Cliente(String nome, String username, String senha){   
        this.username = username;
        this.senha = senha;
    }
    
    public Cliente(int id, String nome, String username, String senha){
        this.username = username;
        this.senha = senha;
        this.id = id;
    }
    

    public String getNome() {
        return nome;
    }

    public String getUsername() {
        return username;
    }

    public String getSenha() {
        return senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public int getId(){
        return this.id;
    }
    
    
    
}

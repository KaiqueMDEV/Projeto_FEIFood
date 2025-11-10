package model;

/**
 * Representa o utilizador da aplicação.
 * Guarda os dados lidos ou a serem inseridos na tabela 'tbusuarios'.
 */
public class Cliente {
    private String nome,username,senha;
    private int id;
    
    /**
     * Construtor para novos clientes (usado pela tela de Cadastro).
     * @param nome O nome completo do utilizador.
     * @param username O login (único) do utilizador.
     * @param senha A senha do utilizador.
     */
    public Cliente(String nome, String username, String senha){   
        this.nome = nome;
        this.username = username;
        this.senha = senha;
        this.id = 0; //ID é 0 pois ainda não foi salvo no banco
    }
    
    /**
     * Construtor para clientes existentes (usado pelo Login).
     * @param id O ID do utilizador vindo do banco.
     * @param nome O nome completo do utilizador.
     * @param username O login do utilizador.
     * @param senha A senha do utilizador.
     */
    public Cliente(int id, String nome, String username, String senha){
        this.id = id;
        this.nome = nome; //Esta linha estava em falta e foi corrigida
        this.username = username;
        this.senha = senha;
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
    
    public int getId(){
        return this.id;
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
}
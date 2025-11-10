package model;

/**
 * Superclasse abstrata para todos os itens vendáveis (Comida e Bebida).
 * Define as propriedades comuns que vêm da tabela 'tbalimentos'.
 *
 */
public abstract class Alimento {
    
    private int id;
    private String nome;
    private String descricao;
    private double preco;

    /**
     * Construtor principal para um Alimento.
     * @param id O ID do item no banco.
     * @param nome O nome do item.
     * @param descricao A descrição do item.
     * @param preco O preço base (coluna 'preco' no BD).
     */
    public Alimento(int id, String nome, String descricao, double preco) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
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
}
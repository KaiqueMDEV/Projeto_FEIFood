package model;

/**
 * Subclasse de Alimento que representa uma Comida (Lanche, Pizza, etc.).
 * Adiciona propriedades específicas de comida.
 *
 */
public class Comida extends Alimento {
    
    private boolean veggie;

    /**
     * Cria uma nova instância de Comida.
     * @param id O ID do banco.
     * @param nome O nome do produto.
     * @param descricao A descrição.
     * @param preco O preço.
     * @param veggie Se é vegetariano (coluna 'veggie').
     */
    public Comida(int id, String nome, String descricao, double preco, boolean veggie) {
        super(id, nome, descricao, preco); //Chama o construtor da classe-mãe Alimento
        this.veggie = veggie;
    }

    /**
     * Verifica se este item de comida é vegetariano.
     * @return true se for vegetariano, false caso contrário.
     */
    public boolean isVeggie() {
        return veggie;
    }
}
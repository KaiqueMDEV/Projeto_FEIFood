package model;

/**
 * Subclasse de Alimento que representa uma Bebida.
 * Implementa a interface 'Imposto_Alcool' para calcular impostos.
 *
 */
public class Bebida extends Alimento implements Imposto_Alcool {
    
    private boolean zerosugar;
    private double teor_alcoolico;

    /**
     * Cria uma nova instância de Bebida.
     * @param id ID do banco.
     * @param nome Nome da bebida.
     * @param descricao Descrição.
     * @param preco Preço base.
     * @param zerosugar Se é zero açúcar (coluna 'zero_sugar').
     * @param teor_alcoolico O teor alcoólico (coluna 'teor_alcoolico').
     */
    public Bebida(int id, String nome, String descricao, double preco, 
                  boolean zerosugar, double teor_alcoolico) {
        
        super(id, nome, descricao, preco); //Chama o construtor da classe-mãe Alimento
        
        this.zerosugar = zerosugar;
        this.teor_alcoolico = teor_alcoolico;
    }

    
    public boolean isZerosugar() {
        return zerosugar;
    }

    public double getTeor_alcoolico() {
        return teor_alcoolico;
    }
    
    /**
     * Calcula o imposto para esta bebida com base no seu teor alcoólico.
     * @param precoBase O preço de custo do produto (vem do getPreco()).
     * @return O valor ADICIONAL do imposto.
     */
    @Override
    public double calcularImposto(double precoBase) {
        if (this.teor_alcoolico > 0) { //Se a bebida tiver álcool
            return precoBase * 0.10; //Regra de negócio: imposto de 10%
        } else {
            return 0.0; //Sem imposto para bebidas não-alcoólicas
        }
    }
}
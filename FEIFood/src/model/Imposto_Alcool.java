package model;

/**
 * Interface (contrato) que obriga uma classe a implementar
 * um método para calcular impostos sobre bebidas alcoólicas.
 *
 */
public interface Imposto_Alcool {
    
    /**
     * Calcula o valor do imposto com base no preço do item.
     * @param precoBase O preço de custo do produto.
     * @return O valor *adicional* do imposto (não o preço total).
     */
    public double calcularImposto(double precoBase);
    
}
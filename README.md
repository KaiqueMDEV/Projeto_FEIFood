# Projeto_FEIFood
Repositório voltado para o desenvolvimento do projeto FEIFood da disciplina de Arquitetura de Software &amp; Programação Orientada a Objetos


Aluno: Kaique Medeiros Moreira 
RA: 22.224.005-3

COMO USAR O APLICATIVO:
===========================================
-Insira o banco de dados na máquina local através do backup
-Execute o FEIFOOD.jar
-Insira um login válido ou cadastre um novo usuário
-Cardápio: complemente seu pedido clicando nas imagens ou pesquisando pelos itens, ao faze-lo, o item selecionado ficará em 
        destaque, clique em "adicionar ao pedido", quando estiver satisfeito clique em "Gerar Pedido", isso irá te redirecionar para a tela "pedidoFinal" onde você pode finalizar ou excluir o pedido. 
        Ao finalizar, você será redirecionado de volta ao menu.
-Pedidos: escolha um pedido do histórico de pedidos e dê um clique duplo para acessa-lo, isso irá te redirecionar para a tela "pedidoFinal" de um pedido já concluído, permitindo que você deixe uma avaliação ou
        exclua o mesmo. 
-Perfil: acesse para mudar informações do seu cadastro no banco de dados.
=====================================================================================================

***================RELATÓRIO====================***

Iniciei o desenvolvimento do meu projeto FEIFOOD seguindo o tutorial disponibilizado pela professora sobre lógica de login e cadastro ``exemplo_bd`` adaptando os nomes das tabelas para o contexto
do projeto, como alunos virando clientes por exemplo.

Após implementar a lógica e os menus de login e cadastro, comecei a implementar as views e seus devidos controles.
comecei pelo "Menu" que se tornou uma extensão ou modificação da lógica de alteração do `exemplo_bd`

Depois fui para o cardápio onde decidi fazer uma abordagem mais visual com ícones clicáveis que exibiriam o alimento em destaque para o usuários. me utilizei de <jLabels> e modifiquei suas propriedades "icon" para colocar miniaturas dos alimentos. Armazenei todas as imagens em miniatura e destaque na pasta resources, e para não pesar o projeto decidi não listar tantos itens no cardápio 

Para o cardápio funcionar, segui o mesmo padrão MVC do login. Criei o <ControleMenu> para ser o cérebro da tela e o <AlimentoDAO> para buscar os produtos no banco (tabela tbalimentos). Quando o utilizador clica num <jLabel> (ex: X-Burger), o <ControleMenu> é acionado, busca os dados no <AlimentoDAO> e usa-os para preencher o painel de destaque (título, descrição, preço e a imagem grande).

Neste ponto, percebi uma falha crítica no meu código, me dei conta que me concentrei demais em fazer o cardápio de forma mais visual e intuitiva e acabei esquecendo de seguir o diagrama de classes e o padrão estabelecido no projeto.

Então, fiz a maior refatoração do projeto para seguir o diagrama de classes.
-Transformei <Alimento.java> numa classe-mãe abstrata.
-Criei as subclasses <Comida.java> e <Bebida.java>, que herdam de <Alimento>.
-Criei a interface <Imposto_Alcool.java>, que a Bebida implementa.

O AlimentoDAO foi modificado para agir como uma "Fábrica": ele lê a coluna tipo_alimento do banco e decide se cria um objeto Comida ou Bebida. Isto limpou a lógica de imposto: o Pedido.java agora só verifica se (<alimento instanceof Bebida>) e chama o <bebida.calcularImposto()>.

implementei A barra de pesquisa que funciona "ao vivo". Usei um JPopupMenu com uma JList que é atualizada a cada tecla (keyReleased). O <AlimentoDAO> usa `ILIKE` ? para fazer a busca no banco. Também fiz o "Enter"
e o clique na lista chamarem o exibirDetalhes, mostrando o item pesquisado. 
Como optei por fazer um código mais visual e com menos itens, acabou gerando um pouco de ruído na barra de pesquisa, pois ainda é possível pesquisar por itens que não estão no cardápio, porém basta limpar o banco de dados e tudo funcionaria normalmente, e além disso, o código funciona de forma "dinamica" então ele não depende que o itens estejam necessariamente ilustrados no cardápio, porém da maneira que fiz seria inconsistente mante-los.

Criei a classe Pedido.java para ser o "carrinho de compras". A decisão chave aqui foi usar um Map<Alimento, Integer> para guardar os itens e as suas quantidades (qtd), o que facilitou muito a lógica do btnAdicionar:
-Finalização: Criei o <PedidoDAO> e o <ControlePedidoFinal>. O <controller> usa uma transação (<conn.setAutoCommit(false)>) para salvar o pedido em duas tabelas ``(tbpedidos e tbalimentos_pedido)`` de forma segura.
-Histórico: Criei a tela Pedidos.java. O ControlePedidos busca os pedidos antigos do cliente no PedidoDAO e exibe-os na lista.
-Avaliação: Na tela PedidoFinal, o controller verifica se o pedido já foi finalizado (pedido.getId() > 0). Se sim, ele mostra os JRadioButtons de avaliação. A nota é salva no banco usando um UPDATE na tabela
        tbpedidos. Na tela de histórico, a nota aparece como estrelas (usando "★".repeat(nota)).

**Correções Finais**
Por fim, fiz uma "limpeza" geral: corrigi vários bugs lógicos (como o Cliente.java não guardar o nome) e padronizei a gestão das conexões (conn.close() sempre no finally do controller) e a navegação (usando this.dispose() nas telas filhas para voltar ao Logado).
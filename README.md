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

==========## RELATÓRIO ##=====================

Iniciei o desenvolvimento do meu projeto FEIFOOD seguindo o tutorial disponibilizado pela professora sobre lógica de login e cadastro "exemplo_bd" adaptando os nomes das tabelas para o contexto
do projeto, como alunos virando clientes por exemplo.

Após implementar a lógica e os menus de login e cadastro, comecei a implementar as views e seus devidos controles.
comecei pelo "Menu" que se tornou uma extensão ou modificação da lógica de alteração do exemplo_bd

Depois fui para o cardápio onde decidi fazer uma abordagem mais visual com ícones clicáveis que exibiriam o alimento em destaque para o usuários. me utilizei de jLabels e modifiquei suas propriedades "icon" para colocar miniaturas dos alimentos. Armazenei todas as imagens em miniatura e destaque na pasta resources, e para não pesar o projeto decidi não listar tantos itens no cardápio 
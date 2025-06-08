# Ferramenta de Análise da Loteria

## Sobre

Esta ferramenta foi criada para facilitar a análise dos ciclos da Lotofácil, permitindo acompanhar de forma clara e prática os números já sorteados e os que ainda faltam para completar um ciclo.

A tabela de ciclos exibe as dezenas pendentes para encerramento de um ciclo e início de outro, considerando o período em que todas as dezenas são sorteadas ao menos uma vez, desde o primeiro até o concurso atual.

Nela, é possível visualizar o último ciclo completo e o ciclo em andamento. Você pode explorar todos os ciclos no período selecionado. As dezenas que ainda não foram sorteadas aparecem destacadas, indicando uma alta probabilidade de serem escolhidas nos próximos concursos.

## Funcionalidades

- [x] Gerar tabela de ciclos da Lotofácil;
- [x] Listar os últimos 5 concursos;
- [x] Listar concursos separadamente;
- [x] Listar quantidade que quiser de concursos ajustando o tamanho da lista e escolhendo entre qual concursos gostaria de buscar;
- [x] Baixar automaticamente a planilha da Lotofácil diretamente do site da Caixa;
- [x] Gerar jogos aleatórios para serem jogados;
- [x] Gerar jogos baseados nos números faltantes do ciclo;
- [ ] Exportar jogos gerados para um arquivo.

## Como usar

1. Clone o repositório:
   ```bash
   git clone https://github.com/gustavommartins/lotofacil-caixa.git
   ```
   Este comando irá clonar o repositório no seu computador para que você possa acessar os arquivos.


2. Navegue até o diretório do projeto:
   ```bash
   cd lotofacil-caixa
   ```

3. Compile o projeto utilizando o Maven:
   ```bash
   mvn clean package
   ```
   Este comando irá compilar o código e gerar um arquivo JAR na pasta `target`.


4. Execute a ferramenta via linha de comando:
   ```bash
   java -jar target/lotofacil-caixa-1.0-SNAPSHOT.jar
   ```
   Substitua `lotofacil-caixa-1.0-SNAPSHOT.jar` pelo nome exato do arquivo gerado na pasta `target`, se for diferente.

   Este comando executa o programa principal que gera a tabela de ciclos com base nos dados fornecidos.

## Fonte dos dados

Os dados utilizados para a análise são obtidos diretamente do site oficial da Caixa Econômica Federal. Acesse o site da Loterias Caixa para mais informações:
[https://loterias.caixa.gov.br](https://loterias.caixa.gov.br)

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests para adicionar funcionalidades ou melhorar o código existente.

## Licença

Este projeto está sob a licença MIT. Consulte o arquivo LICENSE para mais detalhes.

# Sudoku em Java

Aplicação de Sudoku em **Java 17** com interface gráfica Swing, modo de linha de comando, validação de jogadas e solver por backtracking. O projeto separa regras de domínio, carregamento de tabuleiros, renderização e interfaces para manter o código testável e fácil de evoluir.

## Funcionalidades

- Jogo completo em interface gráfica com Java Swing.
- Modo CLI para execução no terminal.
- Validação de linhas, colunas e blocos 3×3.
- Proteção das células originais do desafio.
- Verificação do estado e conclusão do tabuleiro.
- Resolução automática por algoritmo de backtracking.
- Carregamento e representação de diferentes desafios.
- Testes automatizados das regras do tabuleiro.

## Estrutura

```text
src/main/java/br/com/dio/sudoku/
├── model/    Estado do tabuleiro, células e resultado das jogadas
├── solver/   Algoritmo de resolução por backtracking
├── ui/       Interface gráfica Swing
├── cli/      Execução pelo terminal
├── io/       Carregamento dos desafios
├── view/     Renderização textual
└── demo/     Tabuleiros de demonstração

src/test/     Testes automatizados do domínio
```

## Tecnologias

- Java 17+
- Maven
- Java Swing
- JUnit 5

## Como executar

Pré-requisitos: **JDK 17+** e **Maven 3.8+**.

Clone o projeto e execute os testes:

```bash
git clone https://github.com/algomjo/sudoku-java.git
cd sudoku-java
mvn test
```

Para iniciar o modo CLI:

```bash
mvn -q -DskipTests exec:java -Dexec.mainClass="br.com.dio.sudoku.cli.MainCli"
```

Para iniciar a interface gráfica:

```bash
mvn -q -DskipTests exec:java -Dexec.mainClass="br.com.dio.sudoku.ui.SudokuFrame"
```

## Decisões de implementação

O domínio do tabuleiro fica isolado das interfaces. Assim, a mesma regra de validação atende tanto a versão gráfica quanto a CLI. O solver trabalha sobre o modelo existente e usa backtracking para testar valores válidos até concluir o desafio, sem misturar o algoritmo com código de apresentação.

---

Desenvolvido por [Alexandre Gomes de Araújo](https://github.com/algomjo).

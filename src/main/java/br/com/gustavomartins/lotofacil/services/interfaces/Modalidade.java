package br.com.gustavomartins.lotofacil.services.interfaces;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static java.lang.System.in;
import static java.lang.System.out;

public interface Modalidade {

    void funcionalidades();

    void leituraExcel();

    void geraJogosAleatorios(int quantidade);

    default void getUltimosSorteios(Map<Integer, List<Integer>> concursos) {
        out.println("Buscando os 5 últimos sorteios...");
        out.println("-".repeat(78));
        for(int i = 0; i <= 5; i++){
            out.println(StringUtils.rightPad(String.format("| Concurso: %s | %s ", concursos.size() - i, concursos.get(concursos.size() - i).toString()),77) + "|");
            out.println("-".repeat(78));
        }
    }

    default void getSorteioSelecionado(Map<Integer, List<Integer>> concursos){
        Scanner scanner = new Scanner(in);
        out.printf("Selecione um sorteio ele vai de %s até %s%n", 1, concursos.size());
        out.print("> ");
        String input = scanner.nextLine().trim();
        out.printf("Buscando pelo sorteio %s%n", input);
        try{
            List<Integer> concurso = concursos.get(Integer.valueOf(input));
            if(Objects.nonNull(concurso)){
                out.println(concurso);
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            out.println("Sorteio pesquisado não existe ou ainda não aconteceu!");
        } catch (NumberFormatException ex){
            out.println("Por favor digite apenas números!");
        } catch (Exception ex) {
            out.println("Não foi possível pesquisar pelo sorteio selecionado!");
        }
    };

    default void getAtualCiclo(int ciclo, HashSet<Integer> numerosNaoSorteados) {
        out.printf("Números que ainda faltam a ser sorteados no atual ciclo (%s) = %s%n", ciclo, numerosNaoSorteados);
    }

}

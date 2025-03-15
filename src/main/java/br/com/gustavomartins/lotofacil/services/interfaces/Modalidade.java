package br.com.gustavomartins.lotofacil.services.interfaces;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

public interface Modalidade {

    void funcionalidades();

    void leituraExcel();

    default void getUltimosSorteios(Map<Integer, List<Integer>> concursos) {
        out.println("Buscando os 5 últimos sorteios...");
        out.println("-".repeat(78));
        for(int i = 0; i <= 5; i++){
            out.println(StringUtils.rightPad(String.format("| Concurso: %s | %s ", concursos.size() - i, concursos.get(concursos.size() - i).toString()),77) + "|");
            out.println("-".repeat(78));
        }
    }

    default void getAtualCiclo(int ciclo, HashSet<Integer> numerosNaoSorteados) {
        out.printf("Números que ainda faltam a ser sorteados no atual ciclo (%s) = %s%n", ciclo, numerosNaoSorteados);
    }

}

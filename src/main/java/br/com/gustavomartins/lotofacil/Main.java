package br.com.gustavomartins.lotofacil;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static java.lang.System.in;
import static java.lang.System.out;

public class Main {

    private static final Map<Integer, List<Integer>> concursos = new TreeMap<>();
    private static final Map<Integer, Integer> ciclos = new TreeMap<>();
    private static final HashSet<Integer> numerosNaoSorteados = new HashSet<>(createSetNumerosNaoSorteados());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(in);

        out.println("Bem-vindo à CLI!");
        out.println("Digite 'sair' para encerrar o programa.");

        while (true) {
            out.print("> ");
            String input = scanner.nextLine().trim();

            if ("sair".equalsIgnoreCase(input)) {
                out.println("Encerrando...");
                break;
            }

            switch (input) {
                case "lotofacil":
                    leituraExcel(scanner);
                    break;
                case "Numeros faltantes":
                    getNumerosNaoSorteados();
                    break;
                case "ajuda":
                    out.println("Comandos disponíveis: lotofacil, ajuda, sair");
                    break;
                default:
                    out.println("Comando não reconhecido: " + input);
                    break;
            }
        }
        scanner.close();
    }

    private static void getNumerosNaoSorteados() {
        out.printf("Números que ainda faltam a ser sorteados no atual ciclo (%s) = %s%n", ciclos.size(), numerosNaoSorteados);
    }

    private static void leituraExcel(Scanner scanner) {
        if(!concursos.isEmpty()){
            out.println("Trazendo dados da memória...");
            out.println(concursos.get(concursos.size()));
            return;
        }

        String input = chamadaComando(scanner, "Qual o caminho para o arquivo?");

        try(FileInputStream file = new FileInputStream(input);
            Workbook workbook = new XSSFWorkbook(file)){
            Sheet sheet = workbook.getSheetAt(0);
            mapConcursos(sheet);
            leituraCiclos();

            getNumerosNaoSorteados();
        }   catch (IOException ex){
            out.print(ex.getMessage());
        }
    }

    private static void mapConcursos(Sheet sheet) {
        sheet.iterator().forEachRemaining(row -> {
            if(row.getRowNum() == 0) return;
            List<Integer> numerosSorteados = new ArrayList<>();
            row.cellIterator().forEachRemaining(cell -> {
                if(numerosSorteados.size() == 15) return;
                if(String.valueOf(cell.getColumnIndex()).matches("^(1[0-6]|[23456789])$")){
                    numerosSorteados.add((int) cell.getNumericCellValue());
                }
            });
            concursos.put(row.getRowNum(), numerosSorteados);
        });
    }

    private static void leituraCiclos() {
        concursos.forEach((concurso, lista) -> {
            lista.forEach(numerosNaoSorteados::remove);
            if (numerosNaoSorteados.isEmpty()) {
                numerosNaoSorteados.addAll(createSetNumerosNaoSorteados());
                ciclos.put(ciclos.size() + 1, concurso);
            }
        });
    }

    private static HashSet<Integer> createSetNumerosNaoSorteados() {
        return new HashSet<>(Arrays.asList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25
        ));
    }

    private static String chamadaComando(Scanner scanner, String textoDescricao) {
        out.println(textoDescricao);
        out.print("> ");
        return scanner.nextLine().trim();
    }
}
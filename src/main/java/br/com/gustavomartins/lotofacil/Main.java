package br.com.gustavomartins.lotofacil;

import br.com.gustavomartins.lotofacil.services.DownloadService;
import org.apache.commons.lang3.StringUtils;
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(in);

        out.println("Bem-vindo à CLI!");
        out.println("Digite apenas o número da modalidades:");
        out.println("1 - Lotofácil");
        out.println("Digite 'sair' para encerrar o programa.");

        while (true) {
            out.print("> ");
            String input = scanner.nextLine().trim();

            if ("sair".equalsIgnoreCase(input)) {
                out.println("Encerrando...");
                break;
            }

            switch (input.toLowerCase()) {
                case "1":
                    leituraExcel();
                    break;
                case "help":
                    out.println("Comandos disponíveis: 1 (Lotofácil), help, sair");
                    break;
                default:
                    out.println("Comando não reconhecido: " + input);
                    break;
            }
        }
        scanner.close();
    }

    private static void getUltimosSorteios() {
        out.println("Buscando os 4 últimos sorteios...");
        out.println("-".repeat(78));
        for(int i = 0; i < 4; i++){
            out.println(StringUtils.rightPad(String.format("| Concurso: %s | %s ", concursos.size() - i, concursos.get(concursos.size() - i).toString()),77) + "|");
            out.println("-".repeat(78));
        }
    }

    private static void getNumerosNaoSorteados() {
        out.printf("Números que ainda faltam a ser sorteados no atual ciclo (%s) = %s%n", ciclos.size() + 1, numerosNaoSorteados);
    }

    private static void leituraExcel() throws IOException {
        if(!concursos.isEmpty()){
            getNumerosNaoSorteados();
            return;
        }

        new DownloadService().downloadFile();

        String input = "./src/main/resources/loteria/lotofacil.xlsx";

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



}
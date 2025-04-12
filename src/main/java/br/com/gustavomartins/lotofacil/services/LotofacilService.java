package br.com.gustavomartins.lotofacil.services;

import br.com.gustavomartins.lotofacil.services.interfaces.Modalidade;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static java.lang.System.in;
import static java.lang.System.out;

public class LotofacilService implements Modalidade {

    private static final Map<Integer, List<Integer>> concursos = new TreeMap<>();
    private static final Map<Integer, Integer> ciclos = new TreeMap<>();
    private static final HashSet<Integer> numerosNaoSorteados = new HashSet<>(createSetNumerosNaoSorteados());

    @Override
    public void funcionalidades() {
        Scanner scanner = new Scanner(in);
        leituraExcel();

        out.println("Bem-vindo ao menu da Lotofácil!");
        out.println("Digite apenas o número da funcionalidade:");
        out.println("1 - Ciclo atual");
        out.println("2 - últimos 5 sorteios");
        out.println("3 - Selecione um sorteio em específico");
        out.println("Digite 'sair' para encerrar o programa.");

        while (true) {
            out.print("> ");
            String input = scanner.nextLine().trim();

            if ("sair".equalsIgnoreCase(input)) {
                out.println("Encerrando...");
                break;
            }

            switch (input.toLowerCase()) {
                case "1" -> getAtualCiclo(ciclos.size() + 1, numerosNaoSorteados);
                case "2" -> getUltimosSorteios(concursos);
                case "3" -> getSorteioSelecionado(concursos);
                case "help" -> out.println("Comandos disponíveis: 1, 2, help, sair");
                default -> out.println("Comando não reconhecido: " + input);
            }
        }
        scanner.close();
    }

    @Override
    public void leituraExcel(){
        if(!concursos.isEmpty()){
            return;
        }

        new DownloadService().downloadFile();

        String input = "./src/main/resources/loteria/lotofacil.xlsx";

        try(FileInputStream file = new FileInputStream(input);
            Workbook workbook = new XSSFWorkbook(file)){
            Sheet sheet = workbook.getSheetAt(0);
            mapConcursos(sheet);
            leituraCiclos();
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

package br.com.gustavomartins.lotofacil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static java.lang.System.in;
import static java.lang.System.out;

public class Main {
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

    private static void leituraExcel(Scanner scanner) {
        String input = chamadaComando(scanner, "Qual o caminho para o arquivo?");

        try(FileInputStream file = new FileInputStream(input);
            Workbook workbook = new XSSFWorkbook(file)){
            Sheet sheet = workbook.getSheetAt(0);
            Map<Integer, List<Integer>> concursos = new HashMap<>();
            sheet.removeRow(sheet.getRow(sheet.getFirstRowNum()));
            mapConcursos(sheet, concursos);
            //Mostra Ultimo concurso
            out.println(concursos.get(concursos.size()));
        }   catch (IOException ex){
            out.print(ex.getMessage());
        }

    }

    private static void mapConcursos(Sheet sheet, Map<Integer, List<Integer>> concursos) {
        for (Row row : sheet) {
            List<Integer> numerosSorteados = new ArrayList<>();
            sanitizaCelulas(row);
            for (Cell cell : row) {
                    numerosSorteados.add((int) cell.getNumericCellValue());
            }
            concursos.put(row.getRowNum(),numerosSorteados);
        }
    }

    private static void sanitizaCelulas(Row row) {
        for(int i = 0; i <= row.getLastCellNum(); i++){
            if(!String.valueOf(row.getCell(i).getColumnIndex()).matches("^(1[0-6]|[23456789])$")){
                row.removeCell(row.getCell(i));
            }
        }
    }

    private static String chamadaComando(Scanner scanner, String textoDescricao) {
        out.println(textoDescricao);
        out.print("> ");
        return scanner.nextLine().trim();
    }
}
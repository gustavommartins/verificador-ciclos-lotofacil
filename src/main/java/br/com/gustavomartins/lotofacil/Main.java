package br.com.gustavomartins.lotofacil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
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
                case "saudar":
                    out.println("Olá! Como posso ajudar?");
                    break;
                case "excel":
                    leituraExcel(scanner);
                    break;
                case "ajuda":
                    out.println("Comandos disponíveis: saudar, ajuda, sair");
                    break;
                default:
                    out.println("Comando não reconhecido: " + input);
                    break;
            }
        }
        scanner.close();
    }

    private static void leituraExcel(Scanner scanner) {
        out.println("Qual o caminho para o arquivo?");
        out.print("> ");
        String input = scanner.nextLine().trim();
        try(
                FileInputStream file = new FileInputStream(new File(input));
                Workbook workbook = new XSSFWorkbook(file);
        ){

            Sheet sheet = workbook.getSheetAt(0);
            Map<Integer, List<String>> data = new HashMap<>();
            Map<Integer, List<Cell>> concursos = new HashMap<>();
            int i = 0;
            sheet.removeRow(sheet.getRow(sheet.getFirstRowNum()));
            for (Row row : sheet) {
                data.put(i, new ArrayList<>());
                List<Cell> numerosSorteados = new ArrayList<>();
                for (Cell cell : row) {
                    if(String.valueOf(cell.getColumnIndex()).matches("^(1[0-6]|[23456789])$")){
                        numerosSorteados.add(cell);
                    }
                }
                concursos.put(row.getRowNum(),numerosSorteados);
                i++;
            }
            //Mostra Ultimo concurso
            out.println(concursos.get(concursos.size()));
        }   catch (IOException ex){
            out.print(ex.getMessage());
        }

    }
}
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
    private static final Set<Integer> numerosNaoSorteados = new HashSet<>(createSetNumerosNaoSorteados());
    private final Scanner scanner = new Scanner(in);

    @Override
    public void funcionalidades() {
        leituraExcel();
        exibirMenu();

        while (true) {
            out.print("> ");
            String input = scanner.nextLine().trim();

            if ("sair".equalsIgnoreCase(input)) {
                out.println("Encerrando...");
                break;
            }

            processarComando(input);
        }
        scanner.close();
    }

    private void exibirMenu() {
        out.println("Bem-vindo ao menu da Lotofácil!");
        out.println("Digite apenas o número da funcionalidade:");
        out.println("1 - Ciclo atual");
        out.println("2 - últimos 5 sorteios");
        out.println("3 - Selecione um sorteio em específico");
        out.println("4 - Seleciona sorteios fatiados por exemplo 4 ao 8");
        out.println("5 - Gera um jogo aleatório para a Lotofácil");
        out.println("Digite 'sair' para encerrar o programa.");
    }

    private void processarComando(String input) {
        switch (input.toLowerCase()) {
            case "1" -> getAtualCiclo(ciclos.size() + 1, numerosNaoSorteados);
            case "2" -> getUltimosSorteios(concursos);
            case "3" -> getSorteioSelecionado(concursos);
            case "4" -> getConcursosFatiados();
            case "5" -> solicitarQuantidadeJogos();
            case "help" -> out.println("Comandos disponíveis: 1, 2, 3, 4, 5, help, sair");
            default -> out.println("Comando não reconhecido: " + input);
        }
    }

    private void solicitarQuantidadeJogos() {
        out.println("Quantos jogos você gostaria de gerar?");
        out.print("> ");
        try {
            int quantidade = Integer.parseInt(scanner.nextLine().trim());
            geraJogosAleatorios(quantidade);
        } catch (NumberFormatException e) {
            out.println("Quantidade inválida.");
        }
    }

    @Override
    public void leituraExcel() {
        if (!concursos.isEmpty()) return;

        new DownloadService().downloadFile();
        String input = "./src/main/resources/loteria/lotofacil.xlsx";

        try (FileInputStream file = new FileInputStream(input);
             Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            mapConcursos(sheet);
            leituraCiclos();
        } catch (IOException ex) {
            out.print(ex.getMessage());
        }
    }

    @Override
    public void geraJogosAleatorios(int quantidade) {
        List<Set<Integer>> listaDeJogos = new ArrayList<>();
        while (listaDeJogos.size() < quantidade) {
            listaDeJogos.add(geraJogoAleatorio());
        }
        out.println("Os jogos aleatórios gerados são: ");
        listaDeJogos.forEach(out::println);
    }

    private Set<Integer> geraJogoAleatorio() {
        Random gerador = new Random();
        Set<Integer> jogoAleatorio = new HashSet<>();
        while (jogoAleatorio.size() < 15) {
            jogoAleatorio.add(gerador.nextInt(1, 26));
        }
        return jogoAleatorio;
    }

    private void getConcursosFatiados() {
        int concursoInicial = solicitarNumero("Qual seria o sorteio que gostaria de mapear inicialmente?");
        int concursoFinal = solicitarNumero("Até qual concurso você gostaria de visualizar?");
        fatiarConcursos(concursos, concursoInicial, concursoFinal);
    }

    private int solicitarNumero(String mensagem) {
        out.println(mensagem);
        out.print("> ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            out.println("Valor inválido. Tente novamente.");
            return solicitarNumero(mensagem);
        }
    }

    private static void mapConcursos(Sheet sheet) {
        sheet.iterator().forEachRemaining(row -> {
            if (row.getRowNum() == 0) return;
            List<Integer> numerosSorteados = new ArrayList<>();
            row.cellIterator().forEachRemaining(cell -> {
                if (numerosSorteados.size() == 15) return;
                if (cell.getColumnIndex() >= 2 && cell.getColumnIndex() <= 16) {
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

    private static Set<Integer> createSetNumerosNaoSorteados() {
        Set<Integer> numeros = new HashSet<>();
        for (int i = 1; i <= 25; i++) {
            numeros.add(i);
        }
        return numeros;
    }


}
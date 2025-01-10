package br.com.gustavomartins.lotofacil;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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

    private static void leituraExcel(Scanner scanner) throws IOException {
        if(!concursos.isEmpty()){
            out.println("Trazendo dados da memória...");
            out.println(concursos.get(concursos.size()));
            return;
        }

        downloadFile();

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

    private static void downloadFile() throws IOException {
        HttpURLConnection connection = null;
        try(BufferedReader br = new BufferedReader(new FileReader("./src/main/resources/application.yml"))) {

            Properties properties = new Properties();
            properties.load(br);

            String lotofacilUrl = properties.getProperty("lotofacilUrl");
            String filePath = "./src/main/resources/loteria/lotofacil.xlsx";
            File outputFile = new File(filePath);

            // Verifica e cria diretórios
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            URL url = new URI(lotofacilUrl).toURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); // Tempo de espera para conectar (10s)
            connection.setReadTimeout(10000); // Tempo de espera para leitura (10s)

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }

                    System.out.println("Arquivo baixado com sucesso: " + "lotofacil.xlsx");
                }
            } else {
                System.err.println("Erro ao baixar o arquivo. Código de resposta HTTP: " + responseCode);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
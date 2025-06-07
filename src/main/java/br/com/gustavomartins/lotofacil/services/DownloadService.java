package br.com.gustavomartins.lotofacil.services;

import br.com.gustavomartins.lotofacil.exceptions.DownloadeException;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.time.LocalDate;

public class DownloadService {

    public static final String LOTOFACIL_XLSX_PATH = "./src/main/resources/loteria/lotofacil.xlsx";
    private static final String LOTOFACIL_URL = "https://servicebus2.caixa.gov.br/portaldeloterias/api/resultados/download?modalidade=lotofacil";

    public void downloadFile() {
        HttpURLConnection connection = null;
        try {
            File outputFile = new File(LOTOFACIL_XLSX_PATH);

            if (arquivoAtualizadoHoje(outputFile)) return;

            criarDiretoriosSeNecessario(outputFile);

            URL url = new URI(LOTOFACIL_URL).toURL();
            connection = criarConexao(url);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                salvarArquivo(connection);
            } else {
                System.err.println("Erro ao baixar o arquivo. Código de resposta HTTP: " + connection.getResponseCode());
            }
        } catch (URISyntaxException | IOException e) {
            throw new DownloadeException(e);
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private static boolean arquivoAtualizadoHoje(File arquivo) throws IOException {
        return arquivo.exists() && dataDeModificacao(arquivo).isEqual(LocalDate.now());
    }

    private static LocalDate dataDeModificacao(File arquivo) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(arquivo.toPath(), BasicFileAttributes.class);
        return new Timestamp(attr.lastModifiedTime().toMillis()).toLocalDateTime().toLocalDate();
    }

    private static void criarDiretoriosSeNecessario(File arquivo) {
        File parent = arquivo.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
    }

    private static void salvarArquivo(HttpURLConnection connection) {
        try (InputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream out = new FileOutputStream(LOTOFACIL_XLSX_PATH)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            System.out.println("Arquivo baixado com sucesso: lotofacil.xlsx");
        } catch (IOException e) {
            throw new DownloadeException(e);
        }
    }

    private HttpURLConnection criarConexao(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        return conn;
    }
}

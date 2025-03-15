package br.com.gustavomartins.lotofacil.services;

import br.com.gustavomartins.lotofacil.exceptions.DownloadeException;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Properties;

public class DownloadService {

    public static final String SRC_MAIN_RESOURCES_LOTERIA_LOTOFACIL_XLSX = "./src/main/resources/loteria/lotofacil.xlsx";

    public void downloadFile() {
        HttpURLConnection connection = null;

        try {
            File outputFile = new File(SRC_MAIN_RESOURCES_LOTERIA_LOTOFACIL_XLSX);

            if (deveBaixarArquivo(outputFile)) return;

            deveCriarDiretorios(outputFile);

            URL url = new URI("https://servicebus2.caixa.gov.br/portaldeloterias/api/resultados/download?modalidade=lotofacil").toURL();

            connection = createConnection(url);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                download(connection);
            } else {
                System.err.println("Erro ao baixar o arquivo. Código de resposta HTTP: " + responseCode);
            }
        } catch (URISyntaxException | IOException e) {
            throw new DownloadeException(e);
        }  finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void deveCriarDiretorios(File outputFile) {
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    private static Properties loadProperties(BufferedReader br) throws IOException {
        Properties properties = new Properties();
        properties.load(br);
        return properties;
    }

    private static boolean deveBaixarArquivo(File outputFile) throws IOException {
        return outputFile.exists() && getDataDeCriacaoArquivo(outputFile).isEqual(LocalDate.now());
    }

    private static void download(HttpURLConnection connection) {
        try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(DownloadService.SRC_MAIN_RESOURCES_LOTERIA_LOTOFACIL_XLSX)) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("Arquivo baixado com sucesso: " + "lotofacil.xlsx");
        } catch (IOException e) {
            throw new DownloadeException(e);
        }
    }

    private static LocalDate getDataDeCriacaoArquivo(File outputFile) throws IOException {
        BasicFileAttributes fileAttributes = Files.readAttributes(outputFile.toPath(), BasicFileAttributes.class);
        return new Timestamp(fileAttributes.creationTime().toMillis()).toLocalDateTime().toLocalDate();
    }

    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        return connection;
    }

}

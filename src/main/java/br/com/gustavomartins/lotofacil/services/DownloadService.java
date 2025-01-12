package br.com.gustavomartins.lotofacil.services;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class DownloadService {

    public void downloadFile() throws IOException {
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
            connection = createConnection(url);

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
            throw new IOException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        return connection;
    }

}

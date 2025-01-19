package br.com.gustavomartins.lotofacil.exceptions;

public class DownloadeException extends RuntimeException{

    private static final String MENSAGEM = "Houve um problema ao baixar o arquivo, tente novamente mais tarde!";

    public DownloadeException(Throwable cause) {
        super(MENSAGEM, cause);
    }
}

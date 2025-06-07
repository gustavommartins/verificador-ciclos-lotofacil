package br.com.gustavomartins.lotofacil;

import br.com.gustavomartins.lotofacil.services.LotofacilService;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(in);
        exibirMenuPrincipal(false); // Exibe o menu principal ao iniciar
        while (true) {
            out.print("> ");
            String input = scanner.nextLine().trim();
            if (!processarComando(input)) {
                break;
            }
        }
    }

    private static void exibirMenuPrincipal(boolean retorno) {
        if (retorno) {
            out.println("Menu principal");
        } else {
            out.println("Bem-vindo à CLI!");
        }
        out.println("Digite apenas o número da modalidades:");
        out.println("1 - Lotofácil");
        out.println("Digite 'sair' para encerrar o programa.");
    }

    private static boolean processarComando(String input) {
        switch (input.toLowerCase()) {
            case "1" -> {
                new LotofacilService().funcionalidades();
                exibirMenuPrincipal(true); // Exibe o menu principal após retornar da Lotofácil
            }
            case "help" -> out.println("Comandos disponíveis: 1 (Lotofácil), help, sair");
            case "sair" -> {
                out.println("Encerrando...");
                return false;
            }
            default -> out.println("Comando não reconhecido: " + input);
        }
        return true;
    }
}
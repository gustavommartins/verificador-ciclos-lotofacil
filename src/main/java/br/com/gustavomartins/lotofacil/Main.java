package br.com.gustavomartins.lotofacil;

import br.com.gustavomartins.lotofacil.services.LotofacilService;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(in);

        out.println("Bem-vindo à CLI!");
        out.println("Digite apenas o número da modalidades:");
        out.println("1 - Lotofácil");
        out.println("Digite 'sair' para encerrar o programa.");

        while (true) {
            out.print("> ");
            String input = scanner.nextLine().trim();

            switch (input.toLowerCase()) {
                case "1":
                    new LotofacilService().funcionalidades();
                    break;
                case "help":
                    out.println("Comandos disponíveis: 1 (Lotofácil), help, sair");
                    break;
                case "sair":
                    out.println("Encerrando...");
                    return;
                default:
                    out.println("Comando não reconhecido: " + input);
                    break;
            }
        }
    }







}
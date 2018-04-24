package criptorede;

import Utils.PBKDF2Util;
import cripto.Algoritimo;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Algoritimo algoritimo = new Algoritimo();
        Integer opcaoMenu = 0;

        while (opcaoMenu != 7) {
            Integer iteracoes = 10000;

            System.out.println("Sistema Chaveiro - Criptografia de Redes");
            System.out.println("Digite uma opção:");
            System.out.println("1 - inserir chave");
            System.out.println("2 - consultar chave");
            System.out.println("3 - remover chave");
            System.out.println("4 - atualizar chave");
            System.out.println("5 - cifrar arquivo");
            System.out.println("6 - decifrar arquivo");
            System.out.println("7 - sair");
            opcaoMenu = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoMenu) {
                case 1:
                    System.out.println("Digite uma senha: ");
                    String senha = scanner.nextLine();
                    System.out.println("Senha digitada: " + senha);
                    String generateDerivedKey = PBKDF2Util.generateDerivedKey(senha, PBKDF2Util.getSalt(), iteracoes);
                    System.out.println("Chave derivada: " + generateDerivedKey);
                    algoritimo.adicionarChave(generateDerivedKey);
                    break;
                case 2:
                    Map<String, String> chaves = algoritimo.listar();
                    chaves.forEach((key, value) -> System.out.println("Chave: " + value));
                    break;
                case 3:
                    System.out.println("Digite a chave para remover: ");
                    String chaveToRemove = scanner.nextLine();
                    algoritimo.removerChave(chaveToRemove);
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                default:
                    break;
            }
        }
    }
}

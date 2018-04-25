package criptorede;

import Utils.ArquivoUtil;
import Utils.Utils;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.Security;
import java.util.Map;
import java.util.Scanner;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;

public class Main {

    private static Integer getNumeroDigitado(Scanner scanner) {
        Integer opcao = scanner.nextInt();
        scanner.nextLine();
        return opcao;
    }

    private static String criarChaveApartirSenha(Algoritimo algoritimo, Integer iteracoes, Scanner scanner) {
        System.out.println("Digite uma senha: ");
        String senha = scanner.nextLine();
        System.out.println("Senha digitada: " + senha);
        String generateDerivedKey = Utils.generateDerivedKey(senha, Utils.getSalt(), iteracoes);
        System.out.println("Chave derivada: " + generateDerivedKey);
        algoritimo.adicionarChave(generateDerivedKey);
        return generateDerivedKey;
    }

    public static void main(String[] args) throws UnsupportedEncodingException, InvalidKeyException, IOException, Exception {
        Security.addProvider(new BouncyCastleFipsProvider());

        Scanner scanner = new Scanner(System.in);
        Algoritimo algoritimo = new Algoritimo();
        ArquivoUtil arquivoUtil = new ArquivoUtil();
        Integer iteracoes = 10000;
        Integer opcaoMenu = 0;

        while (opcaoMenu != 7) {
            System.out.println("Sistema Chaveiro - Criptografia de Redes");
            System.out.println("Digite uma opção:");
            System.out.println("1 - inserir chave");
            System.out.println("2 - consultar chave");
            System.out.println("3 - remover chave");
            System.out.println("4 - atualizar chave");
            System.out.println("5 - cifrar arquivo");
            System.out.println("6 - decifrar arquivo");
            System.out.println("7 - sair");
            opcaoMenu = getNumeroDigitado(scanner);

            switch (opcaoMenu) {
                case 1:
                    criarChaveApartirSenha(algoritimo, iteracoes, scanner);
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
                    System.out.println("Digite o caminho do arquivo, para ser aberto: ");
                    String pathArquivo = scanner.nextLine();
                    File file = arquivoUtil.getFile(pathArquivo);
                    if (file != null) {
                        String plainText = arquivoUtil.getConteudoArquivo(file);
                        String chaveCifrada = criarChaveApartirSenha(algoritimo, iteracoes, scanner);
                        byte[] keyBytes = Hex.decodeHex(chaveCifrada.toCharArray());
                        Key key = new SecretKeySpec(keyBytes, "AES/GCM");
                        String nomeArquivoHmac = algoritimo.gerarHmac(arquivoUtil.getNomeArquivo(file), key);
                        System.out.println("Nome arquivo hmac: " + nomeArquivoHmac);
                        byte[] conteudoArquivoCifrado = algoritimo.cifrar(plainText, key);
                        char[] conteudoArquivoChar = Hex.encodeHex(conteudoArquivoCifrado);
                        String conteudoArquivoCriptogradado = new String(conteudoArquivoChar);
                        System.out.println(conteudoArquivoCriptogradado);
                    }
                    break;
                case 6:
                    break;
                default:
                    break;
            }
        }
    }
}

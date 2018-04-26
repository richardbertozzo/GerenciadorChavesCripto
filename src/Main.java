import Utils.Utils;

import java.io.File;
import java.security.Key;
import java.security.Security;
import java.util.List;
import java.util.Scanner;
import javax.crypto.spec.SecretKeySpec;

import algoritmo.Algoritimo;
import algoritmo.ManipuladorArquivos;
import model.ArquivoChave;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;

public class Main {

    private static Integer getNumeroDigitado(Scanner scanner) {
        Integer opcao = scanner.nextInt();
        scanner.nextLine();
        return opcao;
    }

    private static Key criarChaveApartirSenha(Integer iteracoes, Scanner scanner) {
        System.out.println("Digite uma senha: ");
        String senha = scanner.nextLine();
        return Utils.generateDerivedKey(senha, Utils.getSalt(), iteracoes);
    }

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleFipsProvider());

        Scanner scanner = new Scanner(System.in);
        Algoritimo algoritimo = new Algoritimo();
        ManipuladorArquivos manipuladorArquivos = new ManipuladorArquivos();
        Integer iteracoes = 10000;
        Integer opcaoMenu = 0;

        while (opcaoMenu != 5) {
            System.out.println("Sistema Chaveiro - Criptografia de Redes");
            System.out.println("Digite uma opção:");
            System.out.println("1 - cifrar arquivo");
            System.out.println("2 - decifrar arquivo");
            System.out.println("3 - consultar chaveiro");
            System.out.println("4 - remover do chaveiro");
            System.out.println("5 - sair");
            opcaoMenu = getNumeroDigitado(scanner);

            switch (opcaoMenu) {
                case 1:
                    System.out.println("Digite o caminho do arquivo, para ser aberto: ");
                    String pathArquivo = scanner.nextLine();
                    File file = manipuladorArquivos.getFile(pathArquivo);
                    if (file != null) {
                        Key key = criarChaveApartirSenha(iteracoes, scanner);
                        Key keyAesGcm = new SecretKeySpec(key.getEncoded(), "AES/GCM");
                        String chaveCifrada = Hex.encodeHexString(keyAesGcm.getEncoded());
                        System.out.println("Chave cifrada: " + chaveCifrada);

                        String nomeArquivoHmac = algoritimo.gerarHmac(manipuladorArquivos.getNomeArquivo(file), keyAesGcm);
                        System.out.println("Nome arquivo hmac: " + nomeArquivoHmac);

                        String plainText = manipuladorArquivos.getConteudoArquivo(file);
                        byte[] conteudoArquivoCifrado = algoritimo.cifrar(plainText, keyAesGcm);
                        String conteudoArquivoCriptogradado = new String(Hex.encodeHex(conteudoArquivoCifrado));
                        System.out.println(conteudoArquivoCriptogradado);

                        ArquivoChave arquivoChave = new ArquivoChave(nomeArquivoHmac, chaveCifrada);
                        algoritimo.adicionarArquivoChave(arquivoChave);
                        manipuladorArquivos.escreveArquivoChaveiro(arquivoChave);
                        manipuladorArquivos.escreveArquivoCifrado(conteudoArquivoCriptogradado, nomeArquivoHmac);
                    }
                    break;
                case 2:
                    List<ArquivoChave> arquivoChaves = algoritimo.listar();
                    System.out.println("Nome arquivo - Chave para cifrar arquivo");
                    for (int i = 0; i < arquivoChaves.size(); i++) {
                        System.out.println(i + " - " + arquivoChaves.get(i).toString());
                    }
                    Integer indexDigitado = getNumeroDigitado(scanner);
                    ArquivoChave arquivoChaveToDecifrar = arquivoChaves.get(indexDigitado);

                    Key key = new SecretKeySpec(arquivoChaveToDecifrar.getGcmChave().getBytes(), "AES/GCM");

                    File fileCifrado = manipuladorArquivos.getFilePorHmac(arquivoChaveToDecifrar.getHmacNomeArquivo());
                    String conteudoArquivoCifrado = manipuladorArquivos.getConteudoArquivo(fileCifrado);


                    String ivString = conteudoArquivoCifrado.substring(0, 32);
                    String conteudoArquivoCifradoSemIv = conteudoArquivoCifrado.substring(30, conteudoArquivoCifrado.length());
                    byte[] conteudoDecifradoBytes = algoritimo.decifrar(conteudoArquivoCifradoSemIv, key, ivString);
                    String conteudoDecifrado = new String(Hex.encodeHex(conteudoDecifradoBytes));
                    System.out.println(conteudoDecifrado);

                    break;
                case 3:
                    List<ArquivoChave> arquivoChavesList = algoritimo.listar();
                    System.out.println("Nome arquivo - Chave para cifrar arquivo");
                    for (int i = 0; i < arquivoChavesList.size(); i++) {
                        System.out.println(i + " - " + arquivoChavesList.get(i).toString());
                    }
                    break;
                case 4:
                    System.out.println("Digite a chave para remover: ");
                    Integer numeroDigitado = getNumeroDigitado(scanner);
                    algoritimo.removerChave(numeroDigitado);
                    break;
                default:
                    break;
            }
        }
    }
}

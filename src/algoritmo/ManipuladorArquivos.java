package algoritmo;

import model.ArquivoChave;

import java.io.*;

public class ManipuladorArquivos {

    private File fileChaveiro;
    private static String PATH_ARQUIVO = "src\\arquivos\\";
    private static String PATH_ARQUIVO_CHAVEIRO = PATH_ARQUIVO + "chaveiro";

    public ManipuladorArquivos() throws IOException {
        this.fileChaveiro = new File(PATH_ARQUIVO_CHAVEIRO);
        if (!fileChaveiro.exists()) {
            fileChaveiro.createNewFile();
        }
    }

    public void escreveArquivoCifrado(String conteudo, String nomeArquivo) throws IOException {
        String filepath = PATH_ARQUIVO + nomeArquivo;
        if (!(new File(filepath).exists())) {
            new File(filepath).createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write(conteudo);
        writer.close();
    }

    public File getFilePorHmac(String hmacArquivo) {
        String filePath = PATH_ARQUIVO + hmacArquivo;
        if (!filePath.isEmpty()) {
            return new File(filePath);
        }
        return null;
    }

    public File getFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            return new File(filePath);
        }
        return null;
    }

    public String getNomeArquivo(File file) {
        return file.getName();
    }

    public String getConteudoArquivo(File file) {
        String conteudo = "";
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while (reader.ready()) {
                    String linha = reader.readLine();
                    System.out.println(linha);
                    conteudo += linha;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conteudo;
    }

    public void escreveArquivoChaveiro(ArquivoChave arquivoChave) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_ARQUIVO_CHAVEIRO));
        writer.write(arquivoChave.toString());
        writer.close();
    }
}

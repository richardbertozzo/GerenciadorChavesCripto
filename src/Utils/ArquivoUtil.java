package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ArquivoUtil {

//    public void escreve(String filepath, String conteudo) throws IOException {
//        if (!(new File(filepath).exists())) {
//            new File(filepath).createNewFile();
//        }
//        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
//        writer.write(conteudo);
//        writer.close();
//    }
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
}

package criptorede;

import Utils.StringUtil;
import Utils.Utils;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class Algoritimo {

    private Map<String, String> chaves;
    private SecureRandom random;
    private File fileChaveiro;

    public Algoritimo() throws Exception {
        this.chaves = new HashMap<>();
        this.random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        this.fileChaveiro = new File("src\\arquivos\\chaveiro");
        if (!fileChaveiro.exists()) {
            fileChaveiro.createNewFile();
        }
    }

    public void adicionarChave(String chave) {
        this.chaves.put(chave, chave);
    }

    public void removerChave(String chaveToRemove) {
        this.chaves.remove(chaveToRemove);
    }

    public Map<String, String> listar() {
        return chaves;
    }

    public String gerarHmac(String nomeArquivo, Key key) throws InvalidKeyException, UnsupportedEncodingException {
        try {
            Mac mac = Mac.getInstance("HMacSHA256", "BCFIPS");
            Key macKey = new SecretKeySpec(key.getEncoded(), "HMacSHA256");
            mac.init(macKey);
            byte[] macNomeArquivo = mac.doFinal(StringUtil.toByteArray(nomeArquivo));
            char[] charMacNomeArquivo = Hex.encodeHex(macNomeArquivo);
            return new String(charMacNomeArquivo);
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(Algoritimo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public byte[] cifrar(String plainText, Key key) {
        try {
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            return this.encrypt(plainText, key, ivSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encrypt(String plainText, Key key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(StringUtil.toByteArray(plainText));
    }

    private static String decrypt(byte[] cipherText, String encryptionKey, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BCFIPS");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }
}

package cripto;

import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Algoritimo {
    
    private Map<String, String> chaves;

    public Algoritimo() {
        this.chaves = new HashMap<>();
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

    public static byte[] encrypt(String plainText, String encryptionKey, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static String decrypt(byte[] cipherText, String encryptionKey, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }
}

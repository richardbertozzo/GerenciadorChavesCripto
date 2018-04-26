package algoritmo;

import Utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Utils.Utils;
import model.ArquivoChave;
import org.apache.commons.codec.binary.Hex;

public class Algoritimo {

    private Cipher cipher;
    private List<ArquivoChave> arquivoChaves;
    private SecureRandom random;

    public Algoritimo() throws Exception {
        this.cipher = Cipher.getInstance("AES/GCM/NoPadding");
        this.arquivoChaves = new ArrayList<>();
        this.random = SecureRandom.getInstance("SHA1PRNG", "SUN");
    }

    public void adicionarArquivoChave(ArquivoChave arquivoChave) {
        this.arquivoChaves.add(arquivoChave);
    }

    public void removerChave(Integer index) {
        this.arquivoChaves.remove(arquivoChaves.get(index));
    }

    public List<ArquivoChave> listar() {
        return arquivoChaves;
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
            return this.encrypt(plainText, key, gerarIv());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decifrar(String cipherText, Key key, String ivString) {
        try {
            IvParameterSpec iv = new IvParameterSpec(ivString.getBytes());
            return this.decrypt(cipherText, key, iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private IvParameterSpec gerarIv() {
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private byte[] encrypt(String plainText, Key key, IvParameterSpec iv) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(plainText.getBytes());
    }

    private byte[] decrypt(String cipherText, Key key, IvParameterSpec iv) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherText.getBytes());
    }
}

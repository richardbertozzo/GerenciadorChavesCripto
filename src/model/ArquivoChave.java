package model;

public class ArquivoChave {
    private String hmacNomeArquivo;
    private String gcmChave;

    public ArquivoChave() {
    }

    public ArquivoChave(String hmacNomeArquivo, String gcmChave) {
        this.hmacNomeArquivo = hmacNomeArquivo;
        this.gcmChave = gcmChave;
    }

    public String getHmacNomeArquivo() {
        return hmacNomeArquivo;
    }

    public void setHmacNomeArquivo(String hmacNomeArquivo) {
        this.hmacNomeArquivo = hmacNomeArquivo;
    }

    public String getGcmChave() {
        return gcmChave;
    }

    public void setGcmChave(String gcmChave) {
        this.gcmChave = gcmChave;
    }

    @Override
    public String toString() {
        return getHmacNomeArquivo() + " " + getGcmChave();
    }
}

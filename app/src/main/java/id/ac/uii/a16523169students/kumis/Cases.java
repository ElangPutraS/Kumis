package id.ac.uii.a16523169students.kumis;

public class Cases {
    private int banyakGejala;
    private String ket;
    private String penyebab;
    private int idKasus;

    public Cases(int banyakGejala, String ket, String penyebab, int idKasus) {
        this.banyakGejala = banyakGejala;
        this.ket = ket;
        this.penyebab = penyebab;
        this.idKasus = idKasus;
    }

    public int getIdKasus() {
        return idKasus;
    }

    public void setIdKasus(int idKasus) {
        this.idKasus = idKasus;
    }

    public int getBanyakGejala() {
        return banyakGejala;
    }

    public void setBanyakGejala(int banyakGejala) {
        this.banyakGejala = banyakGejala;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public String getPenyebab() {
        return penyebab;
    }

    public void setPenyebab(String penyebab) {
        this.penyebab = penyebab;
    }
}

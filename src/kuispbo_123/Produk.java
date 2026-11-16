import java.io.Serializable;

// Abstract class + Encapsulation
public abstract class Produk implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kode;
    private String nama;
    private int stok;

    public Produk(String kode, String nama, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.stok = stok;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public int getStok() { return stok; }

    public void kurangiStok(int jumlah) throws StokTidakCukupException {
        if (jumlah > stok) {
            throw new StokTidakCukupException(
                "Stok " + kode + " (" + nama + ") tidak cukup. Sisa: " + stok + ", diminta: " + jumlah);
        }
        stok -= jumlah;
    }

    // Polymorphism: tiap jenis produk punya cara hitung harga berbeda
    public abstract double getHargaSatuan();
    public abstract double hitungTotal(int qty);
    public abstract String getKategori();

    @Override
    public String toString() {
        return kode + " - " + nama + " (stok: " + stok + ")";
    }
}

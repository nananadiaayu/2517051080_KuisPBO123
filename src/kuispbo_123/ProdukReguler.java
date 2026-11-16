public class ProdukReguler extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukReguler(String kode, String nama, int stok) { super(kode, nama, stok); }

    @Override public double getHargaSatuan() { return 150_000; }

    // Reguler: harga normal
    @Override public double hitungTotal(int qty) { return getHargaSatuan() * qty; }

    @Override public String getKategori() { return "Reguler"; }
}

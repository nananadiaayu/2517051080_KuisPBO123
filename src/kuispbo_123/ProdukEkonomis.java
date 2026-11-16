public class ProdukEkonomis extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukEkonomis(String kode, String nama, int stok) { super(kode, nama, stok); }

    @Override public double getHargaSatuan() { return 10_000; }

    // Ekonomis: diskon 5% bila beli >= 2
    @Override public double hitungTotal(int qty) {
        double total = getHargaSatuan() * qty;
        return qty >= 2 ? total * 0.95 : total;
    }

    @Override public String getKategori() { return "Ekonomis"; }
}

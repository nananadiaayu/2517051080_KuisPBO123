public class ProdukPremium extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukPremium(String kode, String nama, int stok) { super(kode, nama, stok); }

    @Override public double getHargaSatuan() { return 15_000_000; }

    // Premium: ditambah biaya garansi 5%
    @Override public double hitungTotal(int qty) { return getHargaSatuan() * qty * 1.05; }

    @Override public String getKategori() { return "Premium"; }
}

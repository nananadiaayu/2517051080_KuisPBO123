import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SistemToko implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaToko;
    private HashMap<String, Aktor> daftarPelanggan = new HashMap<>();
    private HashMap<String, Produk> daftarProduk = new HashMap<>();
    private ArrayList<Transaksi> riwayat = new ArrayList<>();

    public SistemToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public void tambahPelanggan(String id, String nama) {
        daftarPelanggan.put(id, new Pelanggan(id, nama));
    }

    // Jenis produk ditentukan dari kata kunci pada nama (Premium/Reguler/Ekonomis)
    public void tambahProduk(String kode, String nama, int stok) {
        Produk p;
        if (nama.contains("Premium")) {
            p = new ProdukPremium(kode, nama, stok);
        } else if (nama.contains("Reguler")) {
            p = new ProdukReguler(kode, nama, stok);
        } else {
            p = new ProdukEkonomis(kode, nama, stok);
        }
        daftarProduk.put(kode, p);
    }

    // ===== PEMBACAAN FILE (Varian 2: "=" , "," dan spasi) =====
    public void jalankanTransaksiDariFile(String path) throws Exception {
        StringBuilder isi = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                isi.append(baris).append(" ");
            }
        }

        // Pisah antar-record berdasarkan spasi
        String[] records = isi.toString().trim().split("\\s+");

        int nomor = 0;
        for (String record : records) {
            nomor++;
            if (record.isEmpty()) continue;
            try {
                prosesRecord(record);
            } catch (AktorTidakDitemukanException | ProdukTidakDitemukanException
                     | StokTidakCukupException | TransaksiTidakValidException e) {
                System.out.println("[GAGAL] Transaksi #" + nomor + ": " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("[GAGAL] Transaksi #" + nomor + ": JML bukan angka -> " + record);
            }
        }
    }

    private void prosesRecord(String record) throws AktorTidakDitemukanException,
            ProdukTidakDitemukanException, StokTidakCukupException, TransaksiTidakValidException {
        // "ID=ACT-01,KD=OBJ-A,JML=3," -> pisah dengan koma
        String[] bagian = record.split(",");
        String id = null, kd = null, jml = null;

        for (String b : bagian) {
            String[] kv = b.split("=");   // pisah dengan "="
            if (kv.length != 2) continue;
            switch (kv[0].trim()) {
                case "ID":  id = kv[1].trim();  break;
                case "KD":  kd = kv[1].trim();  break;
                case "JML": jml = kv[1].trim(); break;
            }
        }

        if (id == null || kd == null || jml == null) {
            throw new TransaksiTidakValidException("Format record tidak lengkap: " + record);
        }

        int qty = Integer.parseInt(jml);
        if (qty <= 0) {
            throw new TransaksiTidakValidException("JML harus > 0: " + record);
        }

        Aktor aktor = daftarPelanggan.get(id);
        if (aktor == null) {
            throw new AktorTidakDitemukanException("Pelanggan dengan ID " + id + " tidak ditemukan");
        }
        Produk produk = daftarProduk.get(kd);
        if (produk == null) {
            throw new ProdukTidakDitemukanException("Produk dengan kode " + kd + " tidak ditemukan");
        }

        produk.kurangiStok(qty); // bisa melempar StokTidakCukupException

        Transaksi t = new Transaksi((Pelanggan) aktor, produk, qty);
        riwayat.add(t);
        System.out.println("[OK] " + aktor.getNama() + " beli " + qty + " x " + produk.getNama()
                + " | Total: Rp" + String.format("%,.0f", t.getTotal()));
    }

    // ===== OUTPUT: serialisasi + laporan teks =====
    public void simpanLaporanAkhir() {
        // 1) Object Serialization -> backup_data.dat
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("backup_data.dat"))) {
            oos.writeObject(this);
            System.out.println("[INFO] Serialisasi berhasil -> backup_data.dat");
        } catch (IOException e) {
            System.out.println("[ERROR] Serialisasi gagal: " + e.getMessage());
        }

        // 2) Struk Riwayat Transaksi -> laporan.txt (for normal indeks 0..akhir, FileWriter)
        try (FileWriter fw = new FileWriter("laporan.txt")) {
            fw.write("=== STRUK RIWAYAT TRANSAKSI - " + namaToko + " ===\n");
            for (int i = 0; i < riwayat.size(); i++) {
                Transaksi t = riwayat.get(i);
                fw.write((i + 1) + ". Pelanggan: " + t.getPelanggan().getNama()
                        + " (" + t.getPelanggan().getId() + ")"
                        + " | Produk: " + t.getProduk().getNama()
                        + " (" + t.getProduk().getKode() + ")"
                        + " | Qty: " + t.getQty()
                        + " | Total: Rp" + String.format("%,.0f", t.getTotal()) + "\n");
            }
            fw.write("=== Total transaksi berhasil: " + riwayat.size() + " ===\n");
            System.out.println("[INFO] Laporan berhasil ditulis -> laporan.txt");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menulis laporan: " + e.getMessage());
        }
    }
}

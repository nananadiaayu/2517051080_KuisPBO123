// Custom Exception
public class ProdukTidakDitemukanException extends Exception {
    private static final long serialVersionUID = 1L;

    public ProdukTidakDitemukanException(String pesan) {
        super(pesan);
    }
}

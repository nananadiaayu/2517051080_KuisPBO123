// Custom Exception
public class TransaksiTidakValidException extends Exception {
    private static final long serialVersionUID = 1L;

    public TransaksiTidakValidException(String pesan) {
        super(pesan);
    }
}
